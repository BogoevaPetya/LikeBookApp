package com.likebookapp.service;

import com.likebookapp.config.UserSession;
import com.likebookapp.model.dto.UserLoginDTO;
import com.likebookapp.model.dto.UserRegisterDTO;
import com.likebookapp.model.entity.User;
import com.likebookapp.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSession userSession;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserSession userSession, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSession = userSession;
        this.modelMapper = modelMapper;
    }

    public boolean login(UserLoginDTO userLoginDTO) {
        Optional<User> byUsername = userRepository.findByUsername(userLoginDTO.getUsername());
        if (byUsername.isEmpty()){
            return false;
        }

        User user = byUsername.get();
        boolean passwordMatch = passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword());

        if (!passwordMatch){
            return false;
        }

        userSession.login(user);

        return true;
    }

    public boolean register(UserRegisterDTO userRegisterDTO) {
        Optional<User> optionalUser = userRepository.findByUsernameAndEmail(userRegisterDTO.getUsername(), userRegisterDTO.getEmail());
        if (optionalUser.isPresent()){
            return false;
        }

        boolean passwordMatch = userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword());
        if (!passwordMatch){
            return false;
        }

        User user = modelMapper.map(userRegisterDTO, User.class);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userRepository.save(user);
        return true;
    }

    public void logout() {
        userSession.logout();
    }
}
