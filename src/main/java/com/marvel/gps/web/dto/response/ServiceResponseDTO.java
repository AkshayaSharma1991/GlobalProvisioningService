package com.marvel.gps.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ServiceResponseDTO {

    private String status;

    private String message;
}
