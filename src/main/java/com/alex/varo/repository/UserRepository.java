package com.alex.varo.repository;

import com.alex.varo.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Find users with matching email
     *
     * @param email the string email of the user
     * @return the user
     */
    List<User> findByEmail(String email);

    /**
     * Updates a user's email
     *
     * @param email the user email to update
     * @param id    the users to update with
     */
    @Modifying
    @Query("UPDATE User u SET u.email = :email WHERE u.id = :id")
    void updateUserEmailAddress(@Param("email") String email, @Param("id") Long id);
}
