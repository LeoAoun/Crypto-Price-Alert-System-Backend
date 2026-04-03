package com.cpas.identity_service.application.port.in;

import java.util.List;
import java.util.UUID;
import com.cpas.identity_service.domain.model.User;

public interface ManageUserUseCase {
    List<User> getAllUsers(int page, int size);
    User getUserById(UUID id);
    void deleteUser(UUID id);
}
