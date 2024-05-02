package com.shoestore.services;

import com.shoestore.dtos.UserDTO;
import com.shoestore.dtos.UserUpdateDTO;
import com.shoestore.exceptions.DataNotFoundException;
import com.shoestore.models.User;

public interface UserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber, String password, Long roleId) throws DataNotFoundException;

    User getUserDetailsFromToken(String token) throws Exception;

    User updateUser(UserUpdateDTO userDTO, Long userId) throws Exception;
}
