package com.tutorial.security.security.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(unique= true)
    private String roleName;

    //mapped by should be the same variable name as in user class field, typos will be hard to debug
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore //prevent its serialization in response & ?? also break loading user & roles race condition loop???
    private List<User> users;

    @CreatedDate //to track creation timestamp as immutable in DB
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate //annotation from spring framework
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    //constructors
    /**
	 * 
	 */
	public Role() {
		super();
	}

	/**
	 * @param roleId
	 * @param roleName
	 * @param users
	 * @param createdDate
	 * @param lastModifiedDate
	 */
	public Role(Long roleId, String roleName, List<User> users, LocalDateTime createdDate,
			LocalDateTime lastModifiedDate) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.users = users;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
	}

	//getters & setters
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
    
    
}
