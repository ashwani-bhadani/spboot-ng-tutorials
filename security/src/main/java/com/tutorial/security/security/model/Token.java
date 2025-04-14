package com.tutorial.security.security.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getExpiryOn() {
		return expiryOn;
	}

	public void setExpiryOn(LocalDateTime expiryOn) {
		this.expiryOn = expiryOn;
	}

	public LocalDateTime getValidatedOn() {
		return validatedOn;
	}

	public void setValidatedOn(LocalDateTime validatedOn) {
		this.validatedOn = validatedOn;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**All args constructor
	 * @param id
	 * @param token
	 * @param createdOn
	 * @param expiryOn
	 * @param validatedOn
	 * @param user
	 */
	public Token(Long id, String token, LocalDateTime createdOn, LocalDateTime expiryOn, LocalDateTime validatedOn,
			User user) {
		super();
		this.id = id;
		this.token = token;
		this.createdOn = createdOn;
		this.expiryOn = expiryOn;
		this.validatedOn = validatedOn;
		this.user = user;
	}

	/**
	 * Default constructor
	 */
	public Token() {
		super();
	}

}
