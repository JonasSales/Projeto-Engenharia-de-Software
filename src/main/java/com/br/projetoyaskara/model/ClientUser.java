package com.br.projetoyaskara.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_user")
public class ClientUser implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

    public enum Role {
        ROLE_USER,
        ROLE_ADMIN,
        ROLE_ORGANIZATION
    }

    public ClientUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    private String name;

    @Email(message = "O Email tem de ser válido!!!")
    @NotBlank(message = "Email não pode ser nulo!!!")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Uma senha precisa ser definida")
    @Size(min = 6, max = 100, message = "A senha deve ter no mínimo 6 caracteres e no máximo 100 caracteres")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    private LocalDateTime created;

    private LocalDateTime modified;

    private boolean active;

    private String token;

    @PrePersist
    protected void onCreate() {
        this.created = LocalDateTime.now();
        this.modified = LocalDateTime.now();
    }
}
