package com.example.juniorjavadeveloperbvpsoftware.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ExceptionResponse {
    private String errorType;
    private String errorMessage;
}
