package org.devjefster.springapp.model.repository;

import org.devjefster.springapp.model.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    List<Person> findByNameContainingIgnoreCase(String name);
}
