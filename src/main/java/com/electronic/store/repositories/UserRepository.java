package com.electronic.store.repositories;

import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByEmail(String userEmail);
    List<User> findByNameContaining(String keyword);
    Optional<User> findByNameAndPassword(String userName,String UserPassword);


}
