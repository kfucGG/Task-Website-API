package ru.kolomiec.database.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "person")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @OneToOne(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private AuthToken authToken;
    @JsonIgnore
    private Long chatId;
}
