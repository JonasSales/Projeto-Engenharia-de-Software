package com.br.projetoyaskara.model.clientuser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin::read"),

    ADMIN_CREATE("admin::create"),

    ADMIN_PUT("admin::put"),

    ADMIN_DELETE("admin::delete"),

    ORGANIZATION_READ("organization::read"),

    ORGANIZATION_CREATE("organization::create"),

    ORGANIZATION_PUT("organization::put"),

    ORGANIZATION_DELETE("organization::delete");

    private final String permission;
}
