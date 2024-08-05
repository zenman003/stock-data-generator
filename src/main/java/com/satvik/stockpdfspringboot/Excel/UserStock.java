package com.satvik.stockpdfspringboot.Excel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.satvik.stockpdfspringboot.User.model.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_stock", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "symbol"}))
public class UserStock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    private String symbol;

}
