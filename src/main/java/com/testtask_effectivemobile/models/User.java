package com.testtask_effectivemobile.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.testtask_effectivemobile.models.enums.Role;
import com.testtask_effectivemobile.utils.Views;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView(Views.Private.class)
    private Long id;
    @Column(name = "name")
    @JsonView({Views.Public.class, Views.PublicLow.class})
    private String name;
    @Column(name = "phoneNumber")
    @JsonView(Views.Private.class)
    private String phoneNumber;
    @Column(name = "email", unique = true)
    @JsonView({Views.Public.class, Views.PublicLow.class})
    private String email;
    @Column(name = "password", length = 1000)
    @JsonView(Views.Private.class)
    private String password;
    @Column(name = "active")
    @JsonView(Views.Private.class)
    private boolean active;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "author")
    @JsonView(Views.Public.class)
    private List<Task> author_tasks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "executor")
    @JsonView(Views.Public.class)
    private List<Task> executor_tasks = new ArrayList<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "userRole", joinColumns = @JoinColumn(name = "userId"))
    @Enumerated(EnumType.STRING)
    @JsonView(Views.Private.class)
    private Set<Role> roles = new HashSet<>();

    @JsonView(Views.Private.class)
    private LocalDateTime dateOfCreate;

    @PrePersist
    private void init(){
        dateOfCreate = LocalDateTime.now();
    }

    public boolean isAdmin(){return roles.contains(Role.ROLE_ADMIN);}

    //Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
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
        return active;
    }
}

