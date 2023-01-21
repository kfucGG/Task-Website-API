package ru.kolomiec.taskspring.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "taskName", nullable = false)
    private String taskName;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(columnDefinition = "person_id", referencedColumnName = "id")
    private Person owner;


}
