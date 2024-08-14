package com.example.juniorjavadeveloperbvpsoftware.security.expression;

import com.example.juniorjavadeveloperbvpsoftware.dto.request.UserRequest;
import com.example.juniorjavadeveloperbvpsoftware.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("UserAccess")
@AllArgsConstructor
public class UserExpression {
    private final UserService userService;

    public boolean hasAccess(long id) {
        var user = userService.readById(id);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (user.getEmail().equals(authentication.getName())) {
            return true;
        } else {
            throw new BadCredentialsException("Failed request because of:" + authentication.getName() + " has no access or role to do this!");
        }
    }

    public boolean hasAccess(UserRequest request) {
        var user = userService.findByEmail(request.getLogin());
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (user.getEmail().equals(authentication.getName())) {
            return true;
        } else {
            throw new BadCredentialsException("Failed request because of: " + authentication.getName() + " has no access or role to do this!");
        }
    }
}
