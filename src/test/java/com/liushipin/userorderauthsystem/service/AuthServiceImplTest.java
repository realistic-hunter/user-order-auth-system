package com.liushipin.userorderauthsystem.service;

import com.liushipin.userorderauthsystem.dto.LoginDTO;
import com.liushipin.userorderauthsystem.entity.User;
import com.liushipin.userorderauthsystem.exception.BusinessException;
import com.liushipin.userorderauthsystem.mapper.UserMapper;
import com.liushipin.userorderauthsystem.service.impl.AuthServiceImpl;
import com.liushipin.userorderauthsystem.util.PasswordUtil;
import com.liushipin.userorderauthsystem.vo.LoginVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    private final UserMapper userMapper = mock(UserMapper.class);
    private final LoginAttemptService loginAttemptService = mock(LoginAttemptService.class);
    private final TokenBlacklistService tokenBlacklistService = mock(TokenBlacklistService.class);
    private final AuthServiceImpl authService =
            new AuthServiceImpl(userMapper, loginAttemptService, tokenBlacklistService);

    @Test
    void shouldRejectBlockedLoginBeforeQueryingDatabase() {
        LoginDTO dto = login("lisi", "123456");
        when(loginAttemptService.isBlocked("lisi")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(dto));

        assertEquals(429, exception.getCode());
        verify(userMapper, never()).findByUsername("lisi");
    }

    @Test
    void shouldRecordFailureWhenUserDoesNotExist() {
        LoginDTO dto = login("unknown", "wrong");
        when(userMapper.findByUsername("unknown")).thenReturn(null);

        assertThrows(BusinessException.class, () -> authService.login(dto));

        verify(loginAttemptService).recordFailure("unknown");
    }

    @Test
    void shouldClearFailuresAfterSuccessfulLogin() {
        LoginDTO dto = login("lisi", "123456");
        User user = new User();
        user.setId(1L);
        user.setUsername("lisi");
        user.setPassword(PasswordUtil.encode("123456"));
        when(userMapper.findByUsername("lisi")).thenReturn(user);

        LoginVO result = authService.login(dto);

        assertEquals(1L, result.getUserId());
        assertNotNull(result.getToken());
        verify(loginAttemptService).clear("lisi");
    }

    @Test
    void shouldBlacklistTokenOnLogout() {
        authService.logout("token");

        verify(tokenBlacklistService).blacklist("token");
    }

    private LoginDTO login(String username, String password) {
        LoginDTO dto = new LoginDTO();
        dto.setUsername(username);
        dto.setPassword(password);
        return dto;
    }
}
