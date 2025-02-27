package org.devjefster.springapp.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String streetName;
    @Setter
    @Column(nullable = false)
    private String city;
    @Setter
    @Column(nullable = false)
    private String zip;
    @Setter
    @Column(nullable = false)
    private String state;
    @Setter
    @Column(nullable = false)
    private String country;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    public Address(String streetName, String city, String zip, String state, String country) {
        this.streetName = streetName;
        this.city = city;
        this.zip = zip;
        this.state = state;
        this.country = country;
    }
}
