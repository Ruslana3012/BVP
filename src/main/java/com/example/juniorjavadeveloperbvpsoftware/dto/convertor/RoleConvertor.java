package com.example.juniorjavadeveloperbvpsoftware.dto.convertor;

import com.example.juniorjavadeveloperbvpsoftware.model.Role;
import com.example.juniorjavadeveloperbvpsoftware.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleConvertor extends AbstractConverter<Long, Role> {
    private final RoleService roleService;

    @Override
    protected Role convert(Long roleId) {
        return roleService.readById(roleId);
    }
}
