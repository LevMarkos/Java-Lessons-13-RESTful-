package com.example.work.controller;

import com.example.work.dto.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
public class PersonService {

    private List<Person> persons = new ArrayList<>(Arrays.asList(
            new Person(1, "Ivan", "Ivanovich", "Ivanov", LocalDate.of(1999, 2, 3)),
            new Person(2, "Петр", "Петрович", "Петров", LocalDate.of(2002, 2, 2)),
            new Person(3, "Евгений", "Васильевич", "Васин", LocalDate.of(2005, 4, 8)),
            new Person(4, "Максим", "Яковлевич", "Окопский", LocalDate.of(1978, 6, 5))
    ));

    public static void main(String[] args) {
        SpringApplication.run(PersonService.class, args);
    }

    @GetMapping("/person")
    public List<Person> findAllPersons() {
        return persons;
    }

    @GetMapping("/person/{id}")
    public Optional<Person> findPersonById(@PathVariable int id) {
        return persons.stream().filter(p -> p.getId() == id).findFirst();
    }

    @PostMapping("/person")
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        if (persons.stream().anyMatch(p -> p.getId() == person.getId())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Возвращаем 409 Conflict
        }
        persons.add(person);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PutMapping("/person/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable int id, @RequestBody Person updatedPerson) {
        // Найти индекс существующего объекта
        int index = -1;
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getId() == id) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Person existingPerson = persons.get(index);
        existingPerson.setFirstname(updatedPerson.getFirstname());
        existingPerson.setSurname(updatedPerson.getSurname());
        existingPerson.setLastname(updatedPerson.getLastname());
        existingPerson.setBirthday(updatedPerson.getBirthday());

        return new ResponseEntity<>(existingPerson, HttpStatus.OK);
    }

    @DeleteMapping("/person/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable int id) {
        boolean removed = persons.removeIf(p -> p.getId() == id);
        return removed ? new ResponseEntity<>(HttpStatus.NO_CONTENT) // Успешное удаление
                : new ResponseEntity<>(HttpStatus.NOT_FOUND); // Если не найден, возвращаем 404 Not Found
    }
}
