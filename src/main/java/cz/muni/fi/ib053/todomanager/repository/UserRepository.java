package cz.muni.fi.ib053.todomanager.repository;

import cz.muni.fi.ib053.todomanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

        User findByUsername(String username);

        Optional<User> findById(Long id);

}
