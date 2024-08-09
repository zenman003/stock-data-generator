package com.satvik.stockpdfspringboot.Authentication.repositories;


import com.satvik.stockpdfspringboot.Authentication.model.PasswordResetToken;
import com.satvik.stockpdfspringboot.User.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);
}
