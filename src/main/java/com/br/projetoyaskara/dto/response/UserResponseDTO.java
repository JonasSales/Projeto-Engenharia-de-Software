package com.br.projetoyaskara.dto.response;

import com.br.projetoyaskara.model.clientuser.ClientUser;
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
public class UserResponseDTO {

    private UUID id;
    private String name;
    private String email;
    private String role;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean active;
    private EnderecoResponseDTO endereco;

    public UserResponseDTO(ClientUser clientUser) {
        this.id = clientUser.getId();
        this.name = clientUser.getName();
        this.email = clientUser.getEmail();
        this.role = clientUser.getRole().toString();
        this.created = clientUser.getCreated();
        this.modified = clientUser.getModified();
        this.active = clientUser.isActive();
        this.endereco = clientUser.getEndereco() != null ? new EnderecoResponseDTO(clientUser.getEndereco()) : null;
    }
}