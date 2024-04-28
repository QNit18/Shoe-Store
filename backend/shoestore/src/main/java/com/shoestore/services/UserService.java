package com.shoestore.services;

import com.shoestore.dtos.UserDTO;
import com.shoestore.exceptions.DataNotFoundException;
import com.shoestore.models.User;

public interface UserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber, String password, Long roleId) throws DataNotFoundException;
}
