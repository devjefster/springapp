package org.devjefster.springapp.controller;

import org.devjefster.springapp.model.entities.Person;
import org.devjefster.springapp.service.PersonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Person>> getAllPersonsPaged(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer age,
            Pageable pageable
    ) {
        return ResponseEntity.ok(personService.getAllPersons(name, email, age, pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Person>> searchPersonsByName(@RequestParam String name) {
        return ResponseEntity.ok(personService.searchByName(name));
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        return ResponseEntity.ok(personService.createPerson(person));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person person) {
        return personService.updatePerson(id, person)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        return personService.deletePerson(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
