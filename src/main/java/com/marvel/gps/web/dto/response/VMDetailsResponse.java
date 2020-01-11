package com.marvel.gps.web.dto.response;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VMDetailsResponse {

    private long provisionId;
    private String os;
    private int ram;
    private int hardDisk;
    private int cpuCores;
    private long userId;
    private String userName;
}
