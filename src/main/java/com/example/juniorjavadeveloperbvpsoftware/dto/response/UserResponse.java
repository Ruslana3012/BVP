package com.example.juniorjavadeveloperbvpsoftware.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    Long id;
    String email;
    String roleName;
}
