package com.tutorial.security.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
