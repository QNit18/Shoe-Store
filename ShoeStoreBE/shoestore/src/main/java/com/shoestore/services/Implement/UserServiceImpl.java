package com.shoestore.services.Implement;

import com.shoestore.components.JwtTokenUtils;
import com.shoestore.dtos.UserDTO;
import com.shoestore.dtos.UserUpdateDTO;
import com.shoestore.exceptions.DataNotFoundException;
import com.shoestore.exceptions.PermissionDenyException;
import com.shoestore.models.Role;
import com.shoestore.models.User;
import com.shoestore.repositories.RoleRepository;
import com.shoestore.repositories.UserRepository;
import com.shoestore.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;


    @Override
    @Transactional
    public User createUser(UserDTO userDTO) throws Exception {
        // Register User
        String phoneNumber = userDTO.getPhoneNumber();
        // Checking phone number has exists ?
        if(userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));

        if ( role.getName().toUpperCase().equals(Role.ADMIN)){
            throw new PermissionDenyException("You cannot register a account with role ADMIN");
        }

        //convert from userDTO => user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .isActive(true)
                .build();

        newUser.setRole(role);
        // If create by google, facebook then not encode password
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password, Long roleId) throws DataNotFoundException {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if(user.isEmpty()){
            throw new DataNotFoundException("Invalid phone number / password");
        }
        User existingUser = user.get();
        // Check password
        if (existingUser.getFacebookAccountId() == 0
                && existingUser.getGoogleAccountId() == 0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())){
                throw new BadCredentialsException("Wrong phone number / password");
            }
        }
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isEmpty() || !roleId.equals(existingUser.getRole().getId())){
            throw new DataNotFoundException("You must have a role");
        }
        if (!user.get().isActive()){
            throw new DataNotFoundException("User was denied by Admin");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password,
                existingUser.getAuthorities()
        );
        // authenticate with Java spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if(jwtTokenUtil.isTokenExpired(token)){
            throw new Exception("Token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent()){
            return user.get();
        }
        throw new Exception("User not found");
    }

    @Override
    @Transactional
    public User updateUser(UserUpdateDTO userDTO, Long userId) throws Exception {
        // Find User
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        String phoneNumber = userDTO.getPhoneNumber();
        // Checking phone number has exists ?
        if(!existingUser.getPhoneNumber().equals(phoneNumber)
            && userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }

        //convert from userDTO => user
        if (userDTO.getFullName() != null){
            existingUser.setFullName(userDTO.getFullName());
        }

        if (userDTO.getPhoneNumber() != null){
            existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        }

        if (userDTO.getAddress() != null){
            existingUser.setAddress(userDTO.getAddress());
        }

        if (userDTO.getDateOfBirth() != null){
            existingUser.setDateOfBirth(userDTO.getDateOfBirth());
        }

        if (userDTO.getFacebookAccountId() > 0){
            existingUser.setFacebookAccountId(userDTO.getFacebookAccountId());
        }

        if (userDTO.getGoogleAccountId() > 0){
            existingUser.setGoogleAccountId(userDTO.getGoogleAccountId());
        }

        if(userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()){
            String newPassword = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encodedPassword);
        }

        return userRepository.save(existingUser);
    }
}
