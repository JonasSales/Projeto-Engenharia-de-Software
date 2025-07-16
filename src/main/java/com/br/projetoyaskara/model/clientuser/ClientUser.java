package com.br.projetoyaskara.model.clientuser;

import com.br.projetoyaskara.model.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
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

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Precisa de um nome")
    private String name;

    @Email(message = "O Email tem de ser válido!!!")
    @NotBlank(message = "Email não pode ser nulo!!!")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Uma senha precisa ser definida")
    @NotEmpty(message = "A senha não pode ser vazia")
    @Size(min = 6, max = 100, message = "A senha deve ter no mínimo 6 caracteres e no máximo 100 caracteres")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private LocalDateTime created;

    private LocalDateTime modified;

    private boolean active;

    private String token;

    @OneToMany(mappedBy = "clientUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    @OneToMany(mappedBy = "clientUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvaliacoesEventos> avaliacoes;

    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.REMOVE)
    private List<Organizacao> organizacoes;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    public ClientUser(String email, String password) {
        this.email = email;
        this.password = password;
        this.active = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return active; }

    @PrePersist
    protected void onCreate() {
        this.created = LocalDateTime.now();
        this.modified = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modified = LocalDateTime.now();
    }

}

