package com.liushipin.userorderauthsystem.service;

import com.liushipin.userorderauthsystem.exception.BusinessException;
import com.liushipin.userorderauthsystem.mapper.PermissionMapper;
import com.liushipin.userorderauthsystem.service.impl.PermissionServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PermissionServiceImplTest {

    private final PermissionMapper permissionMapper = mock(PermissionMapper.class);
    private final PermissionCacheService permissionCacheService = mock(PermissionCacheService.class);
    private final PermissionServiceImpl permissionService =
            new PermissionServiceImpl(permissionMapper, permissionCacheService);

    @Test
    void shouldUseCachedPermissionsWithoutQueryingDatabase() {
        when(permissionCacheService.get(1L)).thenReturn(Optional.of(List.of("order:list")));

        permissionService.checkPermission(1L, "order:list", "forbidden");

        verify(permissionMapper, never()).findPermissionCodesByUserId(1L);
    }

    @Test
    void shouldQueryDatabaseAndPopulateCacheOnCacheMiss() {
        when(permissionCacheService.get(1L)).thenReturn(Optional.empty());
        when(permissionMapper.findPermissionCodesByUserId(1L))
                .thenReturn(List.of("order:list", "order:update"));

        permissionService.checkPermission(1L, "order:update", "forbidden");

        verify(permissionMapper).findPermissionCodesByUserId(1L);
        verify(permissionCacheService).put(1L, List.of("order:list", "order:update"));
    }

    @Test
    void shouldRejectUserWithoutRequiredPermission() {
        when(permissionCacheService.get(1L)).thenReturn(Optional.of(List.of("order:list")));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> permissionService.checkPermission(1L, "order:delete", "forbidden")
        );

        assertEquals(403, exception.getCode());
    }
}
