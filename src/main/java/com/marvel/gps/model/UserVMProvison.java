package com.marvel.gps.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "userVMProvision")
@Getter
@Setter
@ToString
public class UserVMProvison extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long provisionId;

    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private GPSUser GPSUser;

    @NotNull
    @Column(columnDefinition = "VARCHAR(40)")
    private String os;

    @NotNull
    @Column(columnDefinition = "INT")
    private int ram;

    @NotNull
    @Column(columnDefinition = "INT")
    private int hardDisk;

    @NotNull
    @Column(columnDefinition = "INT")
    private int cpuCores;

    public UserVMProvison(){

    }

    public UserVMProvison(String OS, int ram, int hardDisk, int cpuCores, GPSUser user) {
        this.os = OS;
        this.ram = ram;
        this.hardDisk = hardDisk;
        this.cpuCores = cpuCores;
        this.GPSUser = user;
    }

}
