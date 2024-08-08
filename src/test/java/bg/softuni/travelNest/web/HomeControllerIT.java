package bg.softuni.travelNest.web;

import bg.softuni.travelNest.service.TravelNestUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private TravelNestUserDetails userDetails;

    @BeforeEach
    void setUp(){
        userDetails = new TravelNestUserDetails(
                UUID.randomUUID(),
                "testUser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                "testUser@gmail.com"
        );
    }

    @Test
    void showHome_WithAuthenticatedUser() throws Exception {
        TravelNestUserDetails userDetails = new TravelNestUserDetails(
                UUID.randomUUID(),
                "testUser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                "testUser@gmail.com"
        );

        mockMvc.perform(get("/")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("username", "testUser"));
    }

    @Test
    @WithAnonymousUser
    void showHome_WithUnauthenticatedUser() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("username", "guest"));
    }

    @Test
    void contact() throws Exception {
        mockMvc.perform(get("/contact")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("contact"));
    }

    @Test
    void showAbout() throws Exception {
        mockMvc.perform(get("/about")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("about"));
    }

    @Test
    void getServices() throws Exception {
        mockMvc.perform(get("/services")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("services"));
    }
}