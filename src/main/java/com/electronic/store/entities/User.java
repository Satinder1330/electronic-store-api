package com.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
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


}
