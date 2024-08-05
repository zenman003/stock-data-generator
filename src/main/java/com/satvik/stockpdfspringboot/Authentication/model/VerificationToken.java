package com.satvik.stockpdfspringboot.Authentication.model;

import com.satvik.stockpdfspringboot.User.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;



@Entity
@Getter
@Table(name = "verification_token")
@Setter
@NoArgsConstructor
public class VerificationToken {

    private static final int EXPIRATION = 1;


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long Id;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date ExpiryDate;

    public VerificationToken( String token, User user) {
        this.user = user;
        this.token = token;
        this.ExpiryDate = calculateExpiryDate(EXPIRATION);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE,expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public void updateToken(String token){
        this.token=token;
        this.ExpiryDate=calculateExpiryDate(EXPIRATION);
    }

}
