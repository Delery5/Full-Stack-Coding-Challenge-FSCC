package com.ibm.delery.loginservice.entity;

import io.micrometer.common.KeyValues;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Must have a Valid name")
    @Size(min = 2, max = 25, message = "Name must be between {min} and {max} characters")
    private String name;

    @Column(name = "username")
    @NotBlank(message = "Must have a Valid name")
    @Size(min = 2, max = 25, message = "Username must be between {min} and {max} characters")
    private String username;

    @Column(name = "email")
    @NotBlank(message = "Must type in a valid Email")
    @Size(min = 10, max = 150, message = "Email must be between {min} and {max} characters")
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "password")
    //@Size(min = 6, max = 20, message = "Password must be between {min} and {max} characters")
//    @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*", message = "Password must have at least one symbol")
//    @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least one digit")
//    @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
//    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_accounts",
    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"))
    private Set<Role> roles;
}

