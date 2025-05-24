package com.br.projetoyaskara.dto;

import com.br.projetoyaskara.model.ClientUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientUserDTO {

    public ClientUserDTO(ClientUser client) {
        setName(client.getName());
        setEmail(client.getEmail());
        setId(client.getId());
        setRole(client.getRole().toString());
        setCreated(LocalDateTime.now());
        setModified(LocalDateTime.now());

    }

    private UUID id;

    private String name;

    private String email;

    private String role;

    private LocalDateTime created;

    private LocalDateTime modified;


}
