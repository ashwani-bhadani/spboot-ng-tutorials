package com.tutorial.security.security.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data // includes getters, setters, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "USER_TB")
@EntityListeners(AuditingEntityListener.class) //enabling JPA entity auditing to track timestamp of lastupdate etc, always in main add @EnableJPaAuditing
public class User implements Principal, UserDetails {
    //if you use this class as user in spring security, always implement user details(of security.core) from spring security so its helpful
    //also security context holder needs principal(of security) so implement them, have get name from principal & rest from userDetails

    @Id //from jakarta.persistence both annotations
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    private Boolean accountLocked;
    private Boolean enabled;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; //keep passwords secure Use BCryptPasswordEncoder to hash passwords:

//    TODO: Use this to encrypt & decrypt before usage & have securityConfig class to secure sensitive keys reading from properties file
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//    user.setPassword(passwordEncoder.encode(rawPassword));

    @CreatedDate //to track creation timestamp as immutable in DB
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate //annotation from spring framework
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;  //TODO: convert this to a Set type as roles can't have duplicates


    /**
     * Returns the name of this {@code Principal}.
     *
     * @return the name of this {@code Principal}.
     */
    @Override
    public String getName() {
        return email; //the unique identifier of user
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of();
        return this.roles.stream()
                .map( r -> new SimpleGrantedAuthority(
                        r.getRoleName()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() { //false = account expired
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //same reverse logic
//        return UserDetails.super.isAccountNonLocked();
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
        return enabled; //sending enabled itself
    }

    public String getFullName() {
        return firstName.concat(" ").concat(lastName);
    }

}
