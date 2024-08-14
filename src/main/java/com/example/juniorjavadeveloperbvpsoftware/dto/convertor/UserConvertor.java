package com.example.juniorjavadeveloperbvpsoftware.dto.convertor;

import com.example.juniorjavadeveloperbvpsoftware.model.User;
import com.example.juniorjavadeveloperbvpsoftware.service.UserService;
import org.modelmapper.AbstractConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConvertor extends AbstractConverter<Long, User> {
    private final UserService userService;

    @Autowired
    public UserConvertor(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected User convert(Long userId) {
        return userService.readById(userId);
    }
}
