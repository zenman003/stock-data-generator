package com.satvik.stockpdfspringboot.Repository;


import com.satvik.stockpdfspringboot.User.model.User;
import com.satvik.stockpdfspringboot.User.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testng.Assert;

import static org.mockito.Mockito.verify;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ExtendWith(MockitoExtension.class)
public class UserRepositoryTests {

    @Mock
    private UserRepository userRepository;

    @Test
    public void UserRepository_SaveUser_ReturnsUser() {
        User user = User.builder()
                .username("satvik")
                .password("password")
                .email("satvik8103@gmail.com")
                .role("USER")
                .enabled(false)
                .build();
        User savedUser = userRepository.save(user);
        Assert.assertNotNull(savedUser);

    }


//    public void wh
}
