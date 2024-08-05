package com.satvik.stockpdfspringboot.Authentication.repositories;

import com.satvik.stockpdfspringboot.User.model.User;
import com.satvik.stockpdfspringboot.Excel.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStockRepository extends JpaRepository<UserStock, Long> {
    UserStock findByUserAndSymbol(User user, String symbol);
}
