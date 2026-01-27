package com.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User implements UserDetails {
    @Id
    private String userId;
    @Column(name="user_name",length = 50)
    private String name;
    @Column(name = "user_email",unique = true,length = 50)
    private String email;
    @Column(name = "user_password")
    private String password;
    @Column(name = "user_gender",length = 10)
    private String gender;
    @Column(name = "user_imageName")
    private String imageName;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    List<Order> orderList= new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    List<Role>roles = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
