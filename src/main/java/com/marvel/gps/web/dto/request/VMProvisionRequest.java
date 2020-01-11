package com.marvel.gps.web.dto.request;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VMProvisionRequest {

    @NotBlank(message = "OS is mandatory")
    private String OS;
    @NotNull
    @Min(1) @Max(1028)
    private int ram;
    @NotNull
    @Min(1) @Max(1028)
    private int hardDisk;
    @NotNull
    @Min(1) @Max(32)
    private int cpuCore;
}
