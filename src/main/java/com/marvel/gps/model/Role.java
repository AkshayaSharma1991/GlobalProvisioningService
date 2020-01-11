package com.marvel.gps.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private RoleName name;

    public Role(RoleName name) {
        this.name = name;
    }

    public Role() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RoleName getName() {
        return this.name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public String toString() {
        return "Role(id=" + this.getId() + ", name=" + this.getName() + ")";
    }
}
