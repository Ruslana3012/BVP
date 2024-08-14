package com.example.juniorjavadeveloperbvpsoftware.repository;

import com.example.juniorjavadeveloperbvpsoftware.model.Role;
import com.example.juniorjavadeveloperbvpsoftware.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
