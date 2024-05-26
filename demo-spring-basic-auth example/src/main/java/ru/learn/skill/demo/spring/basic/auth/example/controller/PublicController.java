package ru.learn.skill.demo.spring.basic.auth.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.learn.skill.demo.spring.basic.auth.example.entity.Role;
import ru.learn.skill.demo.spring.basic.auth.example.entity.RoleType;
import ru.learn.skill.demo.spring.basic.auth.example.entity.User;
import ru.learn.skill.demo.spring.basic.auth.example.model.UserDto;
import ru.learn.skill.demo.spring.basic.auth.example.service.UserService;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<String> getPublic() {
        return ResponseEntity.ok("Called public method");
    }

    @PostMapping("/account")
    public ResponseEntity<UserDto> createUserAccount(@RequestBody UserDto userDto, @RequestParam RoleType roleType) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createAccount(userDto, roleType));
    }

    private UserDto createAccount(UserDto userDto, RoleType roleType) {
        var user = new User();
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());

        var createdUser = userService.createNewAccount(user, Role.from(roleType));

        return UserDto.builder()
                .username(createdUser.getUsername())
                .password(createdUser.getPassword())
                .build();
    }

}
