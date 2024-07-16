package bg.softuni.travelNest.init;

import bg.softuni.travelNest.model.entity.Role;
import bg.softuni.travelNest.model.enums.RoleEnum;
import bg.softuni.travelNest.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@AllArgsConstructor
public class RoleInit implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() != Arrays.stream(RoleEnum.values()).count()) {

            roleRepository.saveAllAndFlush(
            Arrays.stream(RoleEnum.values())
                    .map(RoleEnum::toString)
                    .filter(role -> !roleRepository.findAll()
                            .stream()
                            .map(Role::toString)
                            .toList().contains(role))
                    .map(role -> new Role(RoleEnum.valueOf(role)))
                    .toList());
        }
    }
}
