package com.satvik.stockpdfspringboot.User.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.satvik.stockpdfspringboot.Authentication.model.PasswordResetToken;
import com.satvik.stockpdfspringboot.Authentication.model.VerificationToken;
import com.satvik.stockpdfspringboot.Excel.UserStock;
import com.satvik.stockpdfspringboot.Authentication.util.ExtendedEmailValidator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;


    @ExtendedEmailValidator
    private String email;

    private String role;

    private boolean enabled;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserStock> userStocks;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private VerificationToken verificationToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private PasswordResetToken passwordResetToken;

    public User(){
        this.enabled=false;
    }
    public void updateVerificationToken(VerificationToken verificationToken){
        this.verificationToken=verificationToken;
    }

    public void addStock(UserStock userStock) {
        userStocks.add(userStock);
        userStock.setUser(this);
    }

    public void removeStock(UserStock userStock) {
        userStocks.remove(userStock);
        userStock.setUser(null);
    }
}
