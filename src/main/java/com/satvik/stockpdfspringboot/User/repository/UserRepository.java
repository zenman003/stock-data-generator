package com.satvik.stockpdfspringboot.User.repository;

import com.satvik.stockpdfspringboot.User.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);

    User findByEmailIgnoreCase(String email);

    void deleteByUsername(String username);


}
