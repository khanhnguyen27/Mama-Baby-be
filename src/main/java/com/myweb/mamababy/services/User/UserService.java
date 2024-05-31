package com.myweb.mamababy.services.User;

import com.myweb.mamababy.components.JwtTokenUtil;
import com.myweb.mamababy.dtos.UserDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.exceptions.ExpiredTokenException;
import com.myweb.mamababy.models.BlacklistedToken;
import com.myweb.mamababy.models.Role;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.repositories.BlacklistedTokenRepository;
import com.myweb.mamababy.repositories.RoleRepository;
import com.myweb.mamababy.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final int roleDefault = 1; //Customer
    private final Boolean statusDefault = true;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        //register user
        // Kiểm tra xem số username đã tồn tại hay chưa
        if(userRepository.existsByUsername(userDTO.getUsername())) {
            throw new DataIntegrityViolationException("Username already exists");
        }
        // Kiểm tra xem phoneNumber đã tồn tại hay chưa
        if(userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role = roleRepository.findById(roleDefault)
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        //convert from userDTO => user
        User newUser = User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .fullName(userDTO.getFullName())
                .address(userDTO.getAddress())
                .phoneNumber(userDTO.getPhoneNumber())
                .isActive(statusDefault)
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

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new ExpiredTokenException("Token is expired");
        }
        String subject = jwtTokenUtil.extractUserName(token);
        Optional<User> user;
        user = userRepository.findByUsername(subject);
        return user.orElseThrow(() -> new Exception("User not found"));
    }

    public void logout(String token) throws Exception{
        // Extract expiration date from token
        Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);

        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpirationDate(expirationDate);

        blacklistedTokenRepository.save(blacklistedToken);
    }

    @Override
    public User isActive(UserDTO userDTO) throws Exception {
        Optional<User> user = userRepository.findByUsername(userDTO.getUsername());
        User exitUser = user.get();
        exitUser.setIsActive(userDTO.getStatus());
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if(role == null) exitUser.setRole(exitUser.getRole());
        else exitUser.setRole(role);
        userRepository.save(exitUser);
        return exitUser;
    }

    @Override
    public User updateAccount(String token, UserDTO userDTO) throws Exception {
        String username = userDTO.getUsername();
        // Kiểm tra xem số username đã tồn tại hay chưa
        if(userRepository.existsByUsername(username)) {
            throw new DataIntegrityViolationException("Username already exists");
        }
        // Kiểm tra xem phoneNumber đã tồn tại hay chưa
        if(userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }

        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new ExpiredTokenException("Token is expired");
        }
        String subject = jwtTokenUtil.extractUserName(token);
        Optional<User> user;
        user = userRepository.findByUsername(subject);

        if (user.isPresent()) {
            User retrievedUser = user.get();
            if (!retrievedUser.getUsername().equals(userDTO.getUsername())) {
                throw new Exception("Username does not match");
            } else {
                String password = userDTO.getPassword();
                String encodedPassword = passwordEncoder.encode(password);

                retrievedUser.setUsername(userDTO.getUsername());
                retrievedUser.setPassword(encodedPassword);
                retrievedUser.setFullName(userDTO.getFullName());
                retrievedUser.setAddress(userDTO.getAddress());
                retrievedUser.setPhoneNumber(userDTO.getPhoneNumber());
                userRepository.save(retrievedUser);
            }
        } else {

            throw new Exception("User not found");
        }

        return user.get();
    }

    @Override
    public List<User> getAllAccount() throws Exception {
        try {
            // Retrieve all users from the repository
            List<User> users = userRepository.findAll();

            // If no users found, you can either return an empty list or throw an exception based on your requirements
            if (users.isEmpty()) {
                // Log and/or throw an exception if necessary
                // Example: throw new Exception("No users found");
                return new ArrayList<>(); // or simply return users; which will be an empty list
            }

            return users;
        } catch (Exception e) {
            // Log the exception and rethrow it
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Exception while fetching all accounts: " + e.getMessage());
            throw new Exception("Error retrieving all accounts", e);
        }
    }


    public void cleanupExpiredTokens() {
        Date now = new Date();
        blacklistedTokenRepository.deleteAllByExpirationDateBefore(now);
    }
}
