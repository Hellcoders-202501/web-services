package com.techcompany.fastporte.users.domain.model.aggregates.entities;

import com.techcompany.fastporte.users.domain.model.commands.client.RegisterClientCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "client")
@AllArgsConstructor
@NoArgsConstructor
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Client(RegisterClientCommand command) {
        this.user = new User(command);
    }
}
