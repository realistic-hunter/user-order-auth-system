package com.liushipin.userorderauthsystem;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/integration-schema.sql")
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StringRedisTemplate redisTemplate;

    @SuppressWarnings("unchecked")
    private final ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
    private final Set<String> redisKeys = ConcurrentHashMap.newKeySet();

    @BeforeEach
    void setUpRedis() {
        redisKeys.clear();
        reset(redisTemplate, valueOperations);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.hasKey(anyString())).thenAnswer(invocation -> redisKeys.contains(invocation.getArgument(0)));
        when(valueOperations.get(anyString())).thenReturn(null);
        doAnswer(invocation -> {
            redisKeys.add(invocation.getArgument(0));
            return null;
        }).when(valueOperations).set(anyString(), anyString(), ArgumentMatchers.any(Duration.class));
    }

    @Test
    void shouldRejectProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/orders/my"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    void shouldLoginCreateOrderAndReadCurrentUsersOrders() throws Exception {
        String token = login("zhangsan", "123456");

        mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 88.50,
                                  "remark": "integration-order"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        mockMvc.perform(get("/orders/my")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[*].userId", hasItem(2)))
                .andExpect(jsonPath("$.data[*].remark", hasItem("integration-order")));
    }

    @Test
    void shouldDenyCustomerAccessToAdminOrderList() throws Exception {
        String token = login("zhangsan", "123456");

        mockMvc.perform(get("/orders")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(403));
    }

    @Test
    void shouldCreateOrderAndLetAdminManageItAcrossFullLifecycle() throws Exception {
        String customerToken = login("zhangsan", "123456");
        String adminToken = login("lisi", "123456");
        String remark = "admin-lifecycle-order";

        createOrder(customerToken, "128.66", remark);

        Long orderId = findOrderIdByRemark(adminToken, remark);

        mockMvc.perform(get("/orders")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[*].id", hasItem(orderId.intValue())))
                .andExpect(jsonPath("$.data[*].remark", hasItem(remark)));

        mockMvc.perform(get("/orders/page")
                        .param("pageNum", "1")
                        .param("pageSize", "10")
                        .param("status", "0")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.records[*].id", hasItem(orderId.intValue())))
                .andExpect(jsonPath("$.data.records[*].status", hasItem(0)));

        mockMvc.perform(put("/orders/{id}/status", orderId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": 2
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        mockMvc.perform(get("/orders/page")
                        .param("pageNum", "1")
                        .param("pageSize", "10")
                        .param("status", "2")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.records[*].id", hasItem(orderId.intValue())))
                .andExpect(jsonPath("$.data.records[*].status", hasItem(2)));

        mockMvc.perform(delete("/orders/{id}", orderId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        mockMvc.perform(get("/orders")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[*].id", not(hasItem(orderId.intValue()))));
    }

    @Test
    void shouldRejectTokenAfterLogout() throws Exception {
        String token = login("lisi", "123456");

        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        mockMvc.perform(get("/orders/my")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401));
    }

    private String login(String username, String password) throws Exception {
        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "%s",
                                  "password": "%s"
                                }
                                """.formatted(username, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode root = objectMapper.readTree(response);
        return root.path("data").path("token").asText();
    }

    private void createOrder(String token, String amount, String remark) throws Exception {
        mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": %s,
                                  "remark": "%s"
                                }
                                """.formatted(amount, remark)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    private Long findOrderIdByRemark(String adminToken, String remark) throws Exception {
        String response = mockMvc.perform(get("/orders")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        for (JsonNode order : objectMapper.readTree(response).path("data")) {
            if (remark.equals(order.path("remark").asText())) {
                return order.path("id").asLong();
            }
        }
        throw new AssertionError("Order not found by remark: " + remark);
    }
}
