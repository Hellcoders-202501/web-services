package com.techcompany.fastporte.users.domain.model.aggregates.entities;

import com.techcompany.fastporte.users.domain.model.commands.supervisor.RegisterSupervisorCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "supervisor")
@AllArgsConstructor
@NoArgsConstructor
public class Supervisor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department", length = 50)
    private String department;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "supervisor", fetch = FetchType.LAZY)
    private List<Driver> drivers;

    public Supervisor(RegisterSupervisorCommand command) {
        this.user = new User(command);
        //this.department = command.department();
    }
}
