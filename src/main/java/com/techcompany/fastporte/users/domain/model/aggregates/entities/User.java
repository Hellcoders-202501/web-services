package com.techcompany.fastporte.users.domain.model.aggregates.entities;

import com.techcompany.fastporte.users.domain.model.commands.driver.RegisterDriverCommand;
import com.techcompany.fastporte.users.domain.model.commands.client.RegisterClientCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "first_last_name", nullable = false, length = 50)
    private String firstLastName;

    @Column(name = "second_last_name", nullable = false, length = 50)
    private String secondLastName;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(RegisterDriverCommand command) {
        this.name = command.name();
        this.firstLastName = command.firstLastName();
        this.secondLastName = command.secondLastName();
        this.email = command.email();
        this.phone = command.phone();
        this.username = command.username();
        this.password = command.password();
        this.createdAt = new Date();
    }

    public User(RegisterClientCommand command) {
        this.name = command.name();
        this.firstLastName = command.firstLastName();
        this.secondLastName = command.secondLastName();
        this.email = command.email();
        this.phone = command.phone();
        this.username = command.username();
        this.password = command.password();
        this.createdAt = new Date();
    }

}
