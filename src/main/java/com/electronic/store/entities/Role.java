package com.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Role {
    @Id
    private String roleId;
    private String name;
    @ManyToMany(mappedBy = "roles",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<User>users = new ArrayList<>();
 }
