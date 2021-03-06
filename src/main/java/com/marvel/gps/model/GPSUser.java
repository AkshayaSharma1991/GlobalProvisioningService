package com.marvel.gps.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
public class GPSUser extends AuditModel {

    public GPSUser(String userName, String password, String email, String mobileNo, Role userRole){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.mobileNo = mobileNo;
        this.role = userRole;
    }

    @Id
    @GeneratedValue
    private long userId;

    @NotBlank
    @Column(columnDefinition = "VARCHAR(40)")
    private String userName;

    @NotBlank
    @Column(columnDefinition = "text")
    private String password;

    @NotBlank
    @Column(columnDefinition = "VARCHAR", unique = true)
    private String email;

    @NotBlank
    @Column(columnDefinition = "VARCHAR", unique = true)
    private String mobileNo;

    @ManyToOne
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Role role;

    @OneToMany(mappedBy = "GPSUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserVMProvison> userVMProvisonList;
}
