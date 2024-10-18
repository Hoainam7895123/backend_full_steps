package dev.hoainamtd.repository;

import dev.hoainamtd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // --- Distinct
//    @Query(value = "select distinct from User u where u.firstName=:firstName and u.lastName=:lastName")
    List<User> findDistinctByFirstNameAndLastName(String firtName, String lastName);

    // --- single field
//    @Query(value = "select * from User u where u.email=: email")
    List<User> findByEmail(String email);

    // --- OR
//    @Query(value = "select * from User u where u.firstName=:name or u.lastName=:name")
    List<User> findByFirstNameOrLastName(String name);

    // --- Is, Equals
//    @Query(value = "select * from User u where u.firstName=:name")
    List<User> findByFirstNameIs(String name);
    List<User> findByFirstNameEquals(String name);
    List<User> findByFirstName(String name);

    // --- BETWEEN
    //    @Query(value = "select * from User u where u.createdAt between ?1 and ?2")
    List<User> findByCreatedAtBetween(Date startDate, Date endDate);

    // --- LESSTHAN
    //    @Query(value = "select * from User u where u.age < :age")
    //    List<User> findByAgeLessThan(int age);
    //    List<User> findByAgeLessThanEquals(int age);
    //    List<User> findByAgeGreaterThan(int age);
    //    List<User> findByAgeGreaterThanEquals(int age);


    // --- BEFORE
    // @Query(value = "select * from User u where u.createdAt < :date)
    List<User> findByCreatedAtBefore(Date date);
    List<User> findByCreatedAtAfter(Date date);

    // --- ISNULL, NULL
    // @Query(value = "select * from User u where u.age is null)
    // List<User> findByAgeIsNull();

    // --- NOTNULL, IsNotNULL
    // @Query(value = "select * from User u where u.age is not null)
    // List<User> findByAgeNotNull();

    // --- Like
    // @Query(value = "select * from User u where u.lastName like %:lastName%")
    List<User> findByLastNameLike(String lastName);

    // @Query(value = "select * from User u where u.lastName not like %:lastName%")
    List<User> findByLastNameNotLike(String lastName);

    // StartWith
    // @Query(value = "select * from User u where u.lastname like :lastname%")
    List<User> findByLastNameStartingWith(String lastName);

    // EndWith
    // @Query(value = "select * from User u where u.lastname like %:lastname")
    List<User> findByLastNameEndingWith(String lastName);

    // Containing
    // @Query(value = "select * from User u where u.lastname like %:lastname%")
    List<User> findByLastNameContaining(String name);

    // --- Not
    // @Query(value = "select * from User u where u.lastname <> :name")
    List<User> findByLastNameNot(String name);

    // In
    // @Query(value = "select * from User u where u.age in (18, 25, 30)")
    List<User> findByAgeIn(Collection<Integer> ages);

    // NotIn
    // @Query(value = "select * from User u where u.age not in (18, 25, 30)")
    List<User> findByAgeNotIn(Collection<Integer> ages);

    // True/False
    // @Query(value = "select * from User u where u.activated=true")
    // List<User> findByActivatedTrue();
    // @Query(value = "select * from User u where u.activated=false")
    // List<User> findByActivatedFalse();

    // IgnoreCase
    // @Query(value = "select * from User u where LOWER(u.firstName) <> :name")
    List<User> findByFirstNameIgnoreCase(String name);
}

