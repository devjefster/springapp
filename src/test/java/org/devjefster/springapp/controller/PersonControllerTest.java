package org.devjefster.springapp.controller;

import org.devjefster.springapp.controller.dto.UpdatePersonDTO;
import org.devjefster.springapp.model.entities.Person;
import org.devjefster.springapp.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    private Person testPerson;

    @BeforeEach
    void setUp() {
        testPerson = new Person("John Doe", "johndoe@example.com", LocalDate.of(1990, 5, 15));
    }

    @Nested
    @DisplayName("GET /persons/{id}")
    class GetPersonByIdTests {


        @Test
        @DisplayName("Should return person when found")
        void shouldReturnPersonWhenFound() throws Exception {
            when(personService.getPersonById(1L)).thenReturn(Optional.of(testPerson));

            mockMvc.perform(get("/persons/1")
                            .with(jwt())) // 👈 Mock JWT token
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("John Doe"))
                    .andExpect(jsonPath("$.email").value("johndoe@example.com"));

            verify(personService, times(1)).getPersonById(1L);
        }

        @Test
        @DisplayName("Should return 404 when person not found")
        void shouldReturnNotFoundWhenPersonDoesNotExist() throws Exception {
            when(personService.getPersonById(2L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/persons/2")
                            .with(jwt())) // 👈 Mock JWT token
                    .andExpect(status().isNotFound());

            verify(personService, times(1)).getPersonById(2L);
        }
    }

    @Nested
    @DisplayName("POST /persons - Field Validations")
    class CreatePersonValidationTests {

        private final String validJson = """
                {
                    "name": "John Doe",
                    "email": "johndoe@example.com",
                    "dateOfBirth": "1990-05-15",
                    "address": {
                        "street": "123 Main St",
                        "city": "New York",
                        "state": "NY",
                        "zip": "10001",
                        "country": "USA"
                    }
                }
                """;


        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "John", "Single", "12345"})
        @DisplayName("Should return 400 when name is invalid (must contain at least first and last name)")
        void shouldReturnBadRequestWhenNameIsInvalid(String invalidName) throws Exception {
            String invalidJson = validJson.replace("\"John Doe\"", "\"" + invalidName + "\"");

            mockMvc.perform(post("/persons")
                            .with(jwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.name").exists());

            verify(personService, never()).createPerson(any());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalid-email", "johndoe@.com", "johndoe@"})
        @DisplayName("Should return 400 when email is invalid")
        void shouldReturnBadRequestWhenEmailIsInvalid(String invalidEmail) throws Exception {
            String invalidJson = validJson.replace("\"johndoe@example.com\"", "\"" + invalidEmail + "\"");

            mockMvc.perform(post("/persons")
                            .with(jwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.email").exists());

            verify(personService, never()).createPerson(any());
        }

        @Test
        @DisplayName("Should return 400 when dateOfBirth is in the future")
        void shouldReturnBadRequestWhenDateOfBirthIsFuture() throws Exception {
            String invalidJson = validJson.replace("\"1990-05-15\"", "\"" + LocalDate.now().plusYears(5) + "\"");

            mockMvc.perform(post("/persons")
                            .with(jwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.dateOfBirth").exists());

            verify(personService, never()).createPerson(any());
        }

        @Test
        @DisplayName("Should return 400 when address is null")
        void shouldReturnBadRequestWhenAddressIsNull() throws Exception {
            String invalidJson = validJson.replace("\"address\": {", "\"address\": null");

            mockMvc.perform(post("/persons")
                            .with(jwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.address").exists());

            verify(personService, never()).createPerson(any());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "123", "invalid"})
        @DisplayName("Should return 400 when ZIP code is invalid")
        void shouldReturnBadRequestWhenZipCodeIsInvalid(String invalidZip) throws Exception {
            String invalidJson = validJson.replace("\"10001\"", "\"" + invalidZip + "\"");

            mockMvc.perform(post("/persons")
                            .with(jwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.address.zip").exists());

            verify(personService, never()).createPerson(any());
        }

        @Test
        @DisplayName("Should create person successfully when all fields are valid")
        void shouldCreatePersonSuccessfully() throws Exception {
            when(personService.createPerson(any())).thenReturn(null);

            mockMvc.perform(post("/persons")
                            .with(jwt())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validJson))
                    .andExpect(status().isOk());

            verify(personService).createPerson(any());
        }
    }

    @Nested
    @DisplayName("PUT /persons/{id}")
    class UpdatePersonTests {

        @Test
        @DisplayName("Should update person successfully")
        void shouldUpdatePersonSuccessfully() throws Exception {
            when(personService.updatePerson(eq(1L), any(UpdatePersonDTO.class))).thenReturn(Optional.of(testPerson));

            String updatedPersonJson = """
                    {
                        "name": "John Doe Updated",
                        "email": "johnupdated@example.com",
                        "dateOfBirth": "1985-10-20"
                    }
                    """;

            mockMvc.perform(put("/persons/1")
                            .with(jwt()) // 👈 Mock JWT token
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updatedPersonJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("John Doe"));

            verify(personService).updatePerson(eq(1L), any(UpdatePersonDTO.class));
        }
    }

    @Nested
    @DisplayName("DELETE /persons/{id}")
    class DeletePersonTests {

        @Test
        @DisplayName("Should delete person successfully")
        void shouldDeletePersonSuccessfully() throws Exception {
            when(personService.deletePerson(1L)).thenReturn(true);

            mockMvc.perform(delete("/persons/1")
                            .with(jwt())) // 👈 Mock JWT token
                    .andExpect(status().isNoContent());

            verify(personService, times(1)).deletePerson(1L);
        }

        @Test
        @DisplayName("Should return 404 if person not found for deletion")
        void shouldReturnNotFoundIfPersonNotFoundForDeletion() throws Exception {
            when(personService.deletePerson(2L)).thenReturn(false);

            mockMvc.perform(delete("/persons/2")
                            .with(jwt())) // 👈 Mock JWT token
                    .andExpect(status().isNotFound());

            verify(personService, times(1)).deletePerson(2L);
        }
    }
}
