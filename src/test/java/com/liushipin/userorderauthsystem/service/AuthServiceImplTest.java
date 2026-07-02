package com.liushipin.userorderauthsystem.service;

import com.liushipin.userorderauthsystem.dto.LoginDTO;
import com.liushipin.userorderauthsystem.entity.User;
import com.liushipin.userorderauthsystem.exception.BusinessException;
import com.liushipin.userorderauthsystem.security.JwtService;
import com.liushipin.userorderauthsystem.security.LoginUser;
import com.liushipin.userorderauthsystem.service.impl.AuthServiceImpl;
import com.liushipin.userorderauthsystem.vo.LoginVO;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final JwtService jwtService = mock(JwtService.class);
    private final LoginAttemptService loginAttemptService = mock(LoginAttemptService.class);
    private final TokenBlacklistService tokenBlacklistService = mock(TokenBlacklistService.class);
    private final AuthServiceImpl authService =
            new AuthServiceImpl(authenticationManager, jwtService, loginAttemptService, tokenBlacklistService);

    @Test
    void shouldRejectBlockedLoginBeforeAuthenticating() {
        LoginDTO dto = login("lisi", "123456");
        when(loginAttemptService.isBlocked("lisi")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(dto));

        assertEquals(429, exception.getCode());
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void shouldRecordFailureWhenAuthenticationFails() {
        LoginDTO dto = login("unknown", "wrong");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad credentials"));

        assertThrows(BusinessException.class, () -> authService.login(dto));

        verify(loginAttemptService).recordFailure("unknown");
    }

    @Test
    void shouldClearFailuresAfterSuccessfulLogin() {
        LoginDTO dto = login("lisi", "123456");
        LoginUser loginUser = loginUser(1L, "lisi", "$2a$10$encoded");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(loginUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtService.generateToken(1L, "lisi")).thenReturn("jwt-token");

        LoginVO result = authService.login(dto);

        assertEquals(1L, result.getUserId());
        assertEquals("jwt-token", result.getToken());
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

    private LoginUser loginUser(Long userId, String username, String password) {
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setPassword(password);
        return new LoginUser(user);
    }
}
