package ru.checkdev.auth.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.dto.ProfileDTO;
import ru.checkdev.auth.mapper.ProfileMapper;
import ru.checkdev.auth.repository.PersonRepository;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private ProfileMapper profileMapper;

    @Test
    void whenEmailExistsThenReturnsFalse() throws Exception {
        Profile profile = new Profile();
        profile.setEmail("test@example.com");

        when(personRepository.findByEmail("test@example.com"))
                .thenReturn(profile);

        mockMvc.perform(get("/person/isavaliableemail")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }

    @Test
    void whenEmailNotFoundThenReturnsTrue() throws Exception {
        when(personRepository.findByEmail("notfound@example.com"))
                .thenReturn(null);

        mockMvc.perform(get("/person/isavaliableemail")
                        .param("email", "notfound@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void findByEmailThenReturnProfile() throws Exception {
        String email = "existing@example.com";
        Profile profile = new Profile();
        profile.setId(99);
        profile.setEmail(email);
        profile.setUsername("John Doe");

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(99);
        profileDTO.setEmail(email);
        profileDTO.setUsername("John Doe");

        when(personRepository.findByEmail(email))
                .thenReturn(profile);
        when(profileMapper.getDtoFromEntity(profile))
                .thenReturn(profileDTO);

        mockMvc.perform(get("/person/by/email")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.username").value("John Doe"));
    }
}
