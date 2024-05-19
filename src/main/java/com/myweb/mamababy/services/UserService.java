package com.myweb.mamababy.services;

import com.myweb.mamababy.components.JwtTokenUtil;
import com.myweb.mamababy.dtos.UserDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Role;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.repositories.RoleRepository;
import com.myweb.mamababy.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        //register user
        String username = userDTO.getUsername();
        // Kiểm tra xem số điện thoại đã tồn tại hay chưa
        if(userRepository.existsByUsername(username)) {
            throw new DataIntegrityViolationException("Username already exists");
        }
        Role role = roleRepository.findById(1)
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        //convert from userDTO => user
        User newUser = User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .fullName(userDTO.getFullName())
                .address(userDTO.getAddress())
                .phoneNumber(userDTO.getPhoneNumber())
                .isActive(true)
                .build();
        newUser.setRole(role);
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        return userRepository.save(newUser);
    }

    @Override
    public String login(String username, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()) {
            throw new DataNotFoundException("Invalid username / password");
        }
        //return optionalUser.get();//muốn trả JWT token ?
        User existingUser = optionalUser.get();
        //check password
        if(!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Wrong username or password");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password,
                existingUser.getAuthorities()
        );

        //authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }
}
