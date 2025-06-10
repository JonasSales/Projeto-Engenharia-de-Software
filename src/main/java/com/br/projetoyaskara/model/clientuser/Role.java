package com.br.projetoyaskara.model.clientuser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;



@Getter
@RequiredArgsConstructor
public enum Role {
    USER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    Permission.ADMIN_CREATE,
                    Permission.ADMIN_READ,
                    Permission.ADMIN_DELETE,
                    Permission.ADMIN_PUT,
                    Permission.ORGANIZATION_CREATE,
                    Permission.ORGANIZATION_READ,
                    Permission.ORGANIZATION_DELETE,
                    Permission.ORGANIZATION_PUT
            )
    ),

    ORGANIZATION(
            Set.of(
            Permission.ORGANIZATION_CREATE,
            Permission.ORGANIZATION_READ,
            Permission.ORGANIZATION_DELETE,
            Permission.ORGANIZATION_PUT
    )
    );


    private final Set<Permission> permissionSet;
    public List<SimpleGrantedAuthority> getAuthorities(){
        var authorities = new java.util.ArrayList<>(getPermissionSet().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
        return authorities;
    }
}