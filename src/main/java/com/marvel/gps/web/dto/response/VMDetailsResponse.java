package com.marvel.gps.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class VMDetailsResponse {

    private long provisionId;
    private String os;
    private int ram;
    private int hardDisk;
    private int cpuCores;
    private long userId;
    private String userName;
}
