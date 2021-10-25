package com.alex.varo.service;

import com.alex.varo.model.User;
import com.alex.varo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void listAllUsersTest() {
        User user = new User(1L, "John", "Doe", "johndoe@varo.com");
        User user2 = new User(2L, "Bob", "Doe", "bobdoe@varo.com");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        List<User> users = userService.getAllUsers();

        assertThat(users.size()).isEqualTo(2);
        assertThat(users.contains(user)).isTrue();
        assertThat(users.contains(user2)).isTrue();
    }

    @Test
    public void createUserTest() {
        User user = new User(3L, "John", "Doe", "johndoe@varo.com");
        when(userRepository.save(user)).thenReturn(user);

        User res = userService.createUser(user);

        assertThat(res.getId()).isEqualTo(3L);
        assertThat(res.getEmail()).isEqualTo("johndoe@varo.com");
    }

    @Test
    public void getUserInfoTest() {
        User user = new User(4L, "John", "Doe", "johndoe@varo.com");
        when(userRepository.findById(4L)).thenReturn(Optional.of(user));

        Optional<User> res = userService.getUserInfo(4L);

        assertThat(res.isPresent()).isTrue();
        assertThat(res.get().getId()).isEqualTo(4L);
        assertThat(res.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void getNonExistentUserInfoTest() {
        when(userRepository.findById(4L)).thenReturn(Optional.empty());

        Optional<User> res = userService.getUserInfo(4L);
        assertThat(res.isPresent()).isFalse();
    }


    @Test
    public void getUserByEmailTest() {
        User user = new User(4L, "John", "Doe", "johndoe@varo.com");
        User user2 = new User(5L, "John2", "Doe2", "johndoe@varo.com");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Arrays.asList(user, user2));

        assertThat(userService.isUserExist(user.getEmail())).isTrue();
    }

    @Test
    public void getUserWithNonMatchingEmailTest() {
        when(userRepository.findByEmail(("test@varo.com"))).thenReturn(new ArrayList<>());

        assertThat(userService.isUserExist("test@varo.com")).isFalse();
    }

    @Test
    public void updateUserEmailTest() {
        User user = new User(4L, "John", "Doe", "johndoe@varo.com");
        when(userRepository.findById(4L)).thenReturn(Optional.of(user));

        List<String> pastEmail = new ArrayList<>();
        pastEmail.add("johndoe@varo.com");
        user.setPastEmails(user.getPastEmails());

        User userUpdated = new User(4L, "John", "Doe", "alex@varo.com");
        userUpdated.setPastEmails(pastEmail);
        when(userRepository.save(user)).thenReturn(userUpdated);

        User res = userService.updateUserEmail(4L, "alex@varo.com");

        assertThat(res.getPastEmails().size()).isEqualTo(1);
        assertThat(res.getPastEmails().get(0)).isEqualTo("johndoe@varo.com");
        assertThat(res.getEmail()).isEqualTo("alex@varo.com");
    }

}
