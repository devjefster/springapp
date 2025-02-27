package org.devjefster.springapp.model.specification;

import org.devjefster.springapp.model.entities.Person;
import org.springframework.data.jpa.domain.Specification;

public class PersonSpecification {

    public static Specification<Person> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Person> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null ? null : criteriaBuilder.equal(root.get("email"), email);
    }

    public static Specification<Person> hasAge(Integer age) {
        return (root, query, criteriaBuilder) ->
                age == null ? null : criteriaBuilder.equal(root.get("age"), age);
    }
}
