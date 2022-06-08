package com.epam.audio.streaming.authorization.microservice.models;

import com.epam.audio.streaming.authorization.microservice.utils.providers.ProviderTypes;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 128, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 64)
    private String password;

    @Column(name = "email", length = 128, nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", length = 32, nullable = false)
    private ProviderTypes provider;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name =  "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private Collection<Role> roles;

    @Column(name = "enabled", nullable = false, columnDefinition="boolean default false")
    private Boolean enabled;

    public User(Long id, String username, String password, String email, Collection<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.provider = ProviderTypes.LOCAL;
        this.roles = roles;
    }

    public User(String username, String password, String email, Collection<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.provider = ProviderTypes.LOCAL;
        this.roles = roles;
    }

    public User(String username, String password, String email, Collection<Role> roles, ProviderTypes providerTypes) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.provider = providerTypes;
        this.roles = roles;
    }

    public User(String username, String password, String email, ProviderTypes provider) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.provider = provider;
    }
}
