package bg.softuni.travelNest.web;

import bg.softuni.travelNest.model.dto.RegisterDto;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.repository.UserRepository;
import bg.softuni.travelNest.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.FlashMap;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    private static final String USERNAME = "testUsername";
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "testPassword1@";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Test
    void showLogin() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("registerData"));
    }

    @Test
    void loginError() throws Exception {
        mockMvc.perform(get("/users/login-error"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("showError"))
                .andExpect(model().attributeExists("registerData"))
                .andExpect(model().attribute("showError", true));
    }

    @Test
    void showRegister() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"));
    }

    @Transactional
    @Test
    void register_With_Correct_Data() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", USERNAME)
                        .param("email", EMAIL)
                        .param("password", PASSWORD)
                        .param("confirmPassword", PASSWORD)
                        .param("agree", String.valueOf(true))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        Optional<User> optionalUser = userRepository.findByUsername(USERNAME);
        assertTrue(optionalUser.isPresent());
        assertEquals(EMAIL, optionalUser.get().getEmail());
        assertTrue(passwordEncoder.matches(PASSWORD, optionalUser.get().getPassword()));
    }

    @Test
    void register_With_Incorrect_Data() throws Exception {
        MvcResult result = mockMvc.perform(post("/users/register")
                        .param("username", "no")
                        .param("email", "notEmail")
                        .param("password", "pa1")
                        .param("confirmPassword", "pa2")
                        .param("agree", String.valueOf(false))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andReturn();

        FlashMap flashMap = result.getFlashMap();
        BindingResult bindingResult = (BindingResult) flashMap.get("org.springframework.validation.BindingResult.registerData");

        List<String> confirmPasswordCodes = bindingResult.
                getFieldErrors("confirmPassword")
                .stream()
                .map(fieldError -> Objects.requireNonNull(fieldError.getCode()))
                .sorted()
                .toList();

        assertEquals(6, bindingResult.getErrorCount());
        assertEquals("Size", Objects.requireNonNull(bindingResult.getFieldError("username")).getCode());
        assertEquals("Email", Objects.requireNonNull(bindingResult.getFieldError("email")).getCode());
        assertEquals("Password", Objects.requireNonNull(bindingResult.getFieldError("password")).getCode());
        assertEquals("EqualPasswords", confirmPasswordCodes.getFirst());
        assertEquals("Password", confirmPasswordCodes.getLast());
        assertEquals("AssertTrue", Objects.requireNonNull(bindingResult.getFieldError("agree")).getCode());
    }

    @Transactional
    @Test
    void register_With_Existing_UserData() throws Exception {

        RegisterDto registerDto = new RegisterDto();
        registerDto.setAgree(true);
        registerDto.setUsername(USERNAME);
        registerDto.setPassword(PASSWORD);
        registerDto.setConfirmPassword(PASSWORD);
        registerDto.setEmail(EMAIL);
        userService.register(registerDto);

        MvcResult result = mockMvc.perform(post("/users/register")
                        .param("username", USERNAME)
                        .param("email", EMAIL)
                        .param("password", PASSWORD)
                        .param("confirmPassword", PASSWORD)
                        .param("agree", String.valueOf(true))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andReturn();

        FlashMap flashMap = result.getFlashMap();
        boolean userOrEmailExist = (boolean) flashMap.get("userOrEmailExist");

        assertTrue(userOrEmailExist);
    }
}