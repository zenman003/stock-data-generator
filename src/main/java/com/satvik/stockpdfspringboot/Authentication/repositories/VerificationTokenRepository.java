package com.satvik.stockpdfspringboot.Authentication.repositories;

import com.satvik.stockpdfspringboot.User.model.User;
import com.satvik.stockpdfspringboot.Authentication.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);
}
