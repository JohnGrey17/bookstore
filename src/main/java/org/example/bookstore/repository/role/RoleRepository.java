package org.example.bookstore.repository.role;

import java.util.Optional;
import org.example.bookstore.model.roles.Role;
import org.example.bookstore.model.roles.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(RoleName name);
}
