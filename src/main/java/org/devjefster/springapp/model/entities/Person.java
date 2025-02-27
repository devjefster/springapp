package org.devjefster.springapp.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "persons")
@Getter
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;
    @Setter
    @Column(unique = true, nullable = false)
    private String email;
    @Setter
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adult_id", nullable = false)
    private Person adult;
    @Setter
    @OneToMany(mappedBy = "person")
    private List<Address> address;

    public Person(String name, String email, LocalDate dateOfBirth) {
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public Person() {

    }

}
