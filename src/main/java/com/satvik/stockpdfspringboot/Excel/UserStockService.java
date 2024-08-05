package com.satvik.stockpdfspringboot.Excel;

import com.satvik.stockpdfspringboot.User.model.User;
import com.satvik.stockpdfspringboot.User.repository.UserRepository;
import com.satvik.stockpdfspringboot.Authentication.repositories.UserStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserStockService {

    private final UserRepository userRepository;
    private final UserStockRepository userStockRepository;

    @Transactional
    public UserStock addStockToUser(String username, String symbol) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Check for duplicate stock symbols
        if (user.getUserStocks().stream().anyMatch(stock -> stock.getSymbol().equals(symbol))) {
            throw new IllegalArgumentException("Stock already exists for this user");
        }

        UserStock userStock = new UserStock();
        userStock.setUser(user);
        userStock.setSymbol(symbol);
        return userStockRepository.save(userStock);
    }

    @Transactional
    public void removeStockFromUser(String username, String symbol) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        UserStock userStock = userStockRepository.findByUserAndSymbol(user, symbol);
        if (userStock == null) {
            throw new IllegalArgumentException("Stock not found for this user");
        }

        userStockRepository.delete(userStock);
    }

    public Set<UserStock> getUserStocks(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user.getUserStocks();
    }
}
