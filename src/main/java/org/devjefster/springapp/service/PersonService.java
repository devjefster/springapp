package org.devjefster.springapp.service;

import lombok.extern.slf4j.Slf4j;
import org.devjefster.springapp.controller.dto.CreatePersonDTO;
import org.devjefster.springapp.controller.dto.UpdatePersonDTO;
import org.devjefster.springapp.model.entities.Person;
import org.devjefster.springapp.model.mapper.AddressMapper;
import org.devjefster.springapp.model.mapper.PersonMapper;
import org.devjefster.springapp.model.repository.PersonRepository;
import org.devjefster.springapp.model.specification.PersonSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Page<Person> getAllPersons(String name, String email, LocalDate dateOfBirth, Pageable pageable) {
        Specification<Person> spec = Specification.where(
                PersonSpecification.hasName(name)
                        .and(PersonSpecification.hasEmail(email))
                        .and(PersonSpecification.hasDateOfBirth(dateOfBirth))
        );

        return personRepository.findAll(spec, pageable);
    }


    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }

    public List<Person> searchByName(String name) {
        return personRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Person createPerson(CreatePersonDTO dto) {
        Person person = PersonMapper.mapPerson(dto);
        if (person.getDateOfBirth().isBefore(LocalDate.now().minusYears(18))) {
            log.info("Person {} is under age of 18 years", person.getName());
            if (dto.responsibleAdult() == null)
                throw new IllegalArgumentException("Under age of 18 years requires an Adult");
            Person adult = getPersonById(dto.responsibleAdult()).orElseThrow(() -> new IllegalArgumentException("Under age of 18 years requires an Adult"));
            person.setAdult(adult);
        }

        person.setAddress(Collections.singletonList(AddressMapper.mapAddress(dto.address())));
        return personRepository.save(person);
    }

    @Transactional
    public Optional<Person> updatePerson(Long id, UpdatePersonDTO updatedPerson) {
        return personRepository.findById(id).map(existingPerson -> {
            existingPerson.setName(updatedPerson.name());
            existingPerson.setEmail(updatedPerson.email());
            existingPerson.setDateOfBirth(updatedPerson.dateOfBirth());
            return personRepository.save(existingPerson);
        });
    }

    @Transactional
    public boolean deletePerson(Long id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
