package com.marvel.gps.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
public class GPSUser extends AuditModel {

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

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval=true)
    @JsonIgnore
    private List<UserVMProvison> userVMProvisonList;
}
