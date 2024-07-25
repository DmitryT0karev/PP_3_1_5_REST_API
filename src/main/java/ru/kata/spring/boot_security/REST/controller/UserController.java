package ru.kata.spring.boot_security.REST.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.REST.model.Role;
import ru.kata.spring.boot_security.REST.model.User;
import ru.kata.spring.boot_security.REST.service.RoleService;
import ru.kata.spring.boot_security.REST.service.UserService;
import ru.kata.spring.boot_security.REST.util.UserNotCreatedException;
import ru.kata.spring.boot_security.REST.util.UserNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }


    @GetMapping(value = "/roles")
    public ResponseEntity<Set<Role>> getRolesList() {
        return new ResponseEntity<>(roleService.getAllRoles(),HttpStatus.OK);
    }



    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid User user,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }
        userService.save(user);
        // отправляем HTTP ответ с пустым телом и статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PutMapping(value = "/users")
    public ResponseEntity<?> update(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid user data");
        }
        userService.update(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
