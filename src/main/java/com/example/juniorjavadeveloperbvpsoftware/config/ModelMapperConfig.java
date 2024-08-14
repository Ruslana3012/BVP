package com.example.juniorjavadeveloperbvpsoftware.config;

import com.example.juniorjavadeveloperbvpsoftware.dto.convertor.RoleConvertor;
import com.example.juniorjavadeveloperbvpsoftware.dto.convertor.UserConvertor;
import com.example.juniorjavadeveloperbvpsoftware.dto.request.UserRequest;
import com.example.juniorjavadeveloperbvpsoftware.dto.response.UserResponse;
import com.example.juniorjavadeveloperbvpsoftware.model.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(RoleConvertor roleConvertor,
                                   UserConvertor userConvertor) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(roleConvertor);
        modelMapper.addConverter(userConvertor);
        modelMapper.typeMap(UserRequest.class, User.class);
        modelMapper.typeMap(User.class, UserResponse.class);
        modelMapper.addMappings(new PropertyMap<User, UserResponse>() {
            @Override
            protected void configure() {
                map().setRoleName(source.getRole().getName());
            }
        });
        return modelMapper;
    }
}
