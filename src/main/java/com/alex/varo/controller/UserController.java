package com.alex.varo.controller;

import com.alex.varo.exception.ResourceNotFoundException;
import com.alex.varo.model.User;
import com.alex.varo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers(@Valid @RequestParam Optional<String> email) {
        if (email.isPresent()) {
            boolean isExist = userService.isUserExist(email.get());
            return isExist ? ResponseEntity.ok().body(null) : ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> fetchUser(@PathVariable(value = "id") Long id) {
        Optional<User> user = userService.getUserInfo(id);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user.get());
        } else {
            throw new ResourceNotFoundException("User does not exist");
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Long id) {
        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Could not delete non existent user");
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUserEmailAndArchiveEmail(@PathVariable(value = "id") Long id, @Valid @RequestBody User user) {
        if (userService.getUserInfo(id).isPresent()) {
            userService.updateUserEmail(id, user.getEmail());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        throw new ResourceNotFoundException("User with id: " + id + " does not exist");
    }
}
