package com.beerasta.repository;

import com.beerasta.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

    Optional<User> findById(Long id);

}
