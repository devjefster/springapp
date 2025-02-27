package org.devjefster.springapp.service;

import org.devjefster.springapp.model.entities.Person;
import org.devjefster.springapp.model.repository.PersonRepository;
import org.devjefster.springapp.model.specification.PersonSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Page<Person> getAllPersons(String name, String email, Integer age, Pageable pageable) {
        Specification<Person> spec = Specification.where(
                PersonSpecification.hasName(name)
                        .and(PersonSpecification.hasEmail(email))
                        .and(PersonSpecification.hasAge(age))
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
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    @Transactional
    public Optional<Person> updatePerson(Long id, Person updatedPerson) {
        return personRepository.findById(id).map(existingPerson -> {
            existingPerson.setName(updatedPerson.getName());
            existingPerson.setEmail(updatedPerson.getEmail());
            existingPerson.setAge(updatedPerson.getAge());
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
