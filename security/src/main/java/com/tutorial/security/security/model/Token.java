package com.tutorial.security.security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Token { //belongs only to user

    @Id
    @GeneratedValue
    private Long id;
    private String token;
    private LocalDateTime createdOn;
    private LocalDateTime expiryOn;
    private LocalDateTime validatedOn;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    /*
    //    Many-to-One mapping: Each token belongs to exactly one user.
    //    The foreign key column in the "token" table will be "userId".
    //    'nullable = false' ensures every token must be linked to a user (mandatory).
    //    In the DB, userId will be the foreign key column in the token table.
    //    If the inverse is mapped in User (like List<Token> tokens), that would use @OneToMany(mappedBy = "user").
    //    This is the owning side of the relationship.
    */

}
