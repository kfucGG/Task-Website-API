package ru.kolomiec.database.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "auth_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;
}
