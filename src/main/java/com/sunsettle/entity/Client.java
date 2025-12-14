package com.sunsettle.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String phone;

    private String address;

    private String companyName;

    private LocalDateTime createdAt;

    // Link to User (CLIENT LOGIN OWNER)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
