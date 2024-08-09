package com.satvik.stockpdfspringboot.Authentication.model;


import com.satvik.stockpdfspringboot.User.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;


@Table(name = "password_reset_token")
@Data
@Entity
@NoArgsConstructor
public class PasswordResetToken {

    private static final int expiryInMinutes = 5;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;

    private boolean enabled;

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(expiryInMinutes);
        this.enabled=false;
    }


    private Date calculateExpiryDate(int expiryTimeInMinutes){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE,expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public boolean isExpired(){
        return new Date().after(expiryDate);
    }

}
