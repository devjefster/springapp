package org.devjefster.springapp.model.mapper;

import org.devjefster.springapp.controller.dto.CreatePersonDTO;
import org.devjefster.springapp.model.entities.Person;

public class PersonMapper {

    public static Person mapPerson(CreatePersonDTO dto) {
        return new Person(dto.name(), dto.email(), dto.dateOfBirth());

    }
}
