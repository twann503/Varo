package com.alex.varo.service;

import com.alex.varo.model.User;
import com.alex.varo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This is Service class that
 * handles the business logic between
 */
@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    /**
     * Finds all user in UserRepository
     *
     * @return list of user
     */
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        userRepository.findAll().forEach(userList::add);
        return userList;
    }

    /**
     * Saves users to repository, if doesn't exist
     * will create new one, else will update user
     *
     * @param user the user to create
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Finds user by id from UserRepository
     *
     * @param userId the id of the user
     * @return the user details if found
     */
    public Optional<User> getUserInfo(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * Checks if a user exists based on email address
     *
     * @param email the user email address
     * @return true if there are users with matching email
     */
    public boolean isUserExist(String email) {
        List<User> users = userRepository.findByEmail(email);
        return users.size() > 0;
    }

    /**
     * Deletes user with userId
     *
     * @param id the id of user to delete
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Updates a user's email address and archives it
     *
     * @param id    the user id
     * @param email the email to update with
     * @return the updated user
     * @throws EntityNotFoundException when user does not exist
     */
    public User updateUserEmail(Long id, String email) throws EntityNotFoundException {
        Optional<User> foundUser = getUserInfo(id);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            List<String> oldEmails = user.getPastEmails() == null ? new ArrayList<>() : user.getPastEmails();

            if (oldEmails.contains(email) || email.equals(user.getEmail())) {
                throw new Error("Cannot change same email.");
            } else {
                oldEmails.add(user.getEmail());
                user.setPastEmails(oldEmails);
                user.setEmail(email);
                return userRepository.save(user);
            }
        }
        throw new EntityNotFoundException("Id not found");
    }
}
