package dev.hoainamtd.repository;

import dev.hoainamtd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query(value = "select r from Role r inner join UserHasRole ur on r.id = ur.user.id where ur.id= :userId")
    List findAllRolesByUserId(Long userId);
}

