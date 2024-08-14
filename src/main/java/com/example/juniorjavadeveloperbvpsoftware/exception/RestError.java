package com.example.juniorjavadeveloperbvpsoftware.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RestError {
    private String statusCode;
    private String message;
}
