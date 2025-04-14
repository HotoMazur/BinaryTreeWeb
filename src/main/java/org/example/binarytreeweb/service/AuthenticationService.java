package org.example.binarytreeweb.service;


import org.example.binarytreeweb.dto.LoginUserDto;
import org.example.binarytreeweb.dto.RegisterUserDto;
import org.example.binarytreeweb.entity.UserEntity;
import org.example.binarytreeweb.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public UserEntity signup(RegisterUserDto input) {
        String hashedPassword = passwordEncoder.encode(input.getPassword());
        UserEntity user = new UserEntity(input.getFullName(), input.getEmail(), hashedPassword);

        return userRepository.save(user);
    }

    @Transactional
    public UserEntity authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
