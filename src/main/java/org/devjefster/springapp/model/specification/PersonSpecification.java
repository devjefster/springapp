package org.devjefster.springapp.model.specification;

import org.devjefster.springapp.model.entities.Person;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class PersonSpecification {

    public static Specification<Person> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Person> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null ? null : criteriaBuilder.equal(root.get("email"), email);
    }

    public static Specification<Person> hasDateOfBirth(LocalDate dateOfBirth) {
        return (root, query, criteriaBuilder) ->
                dateOfBirth == null ? null : criteriaBuilder.equal(root.get("dateOfBirth"), dateOfBirth);
    }
}
