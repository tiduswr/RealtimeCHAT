package com.tiduswr.rcuser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.tiduswr.rcuser.exceptions.UserNotFoundException;
import com.tiduswr.rcuser.exceptions.UsernameAlreadyExists;
import com.tiduswr.rcuser.model.User;
import com.tiduswr.rcuser.model.dto.PublicUserDTO;
import com.tiduswr.rcuser.model.dto.UserDTO;
import com.tiduswr.rcuser.model.dto.UserFormalNameRequestDTO;
import com.tiduswr.rcuser.model.dto.UserPasswordRequestDTO;
import com.tiduswr.rcuser.repositories.UserRepository;

import java.util.List;

@Service
@SuppressWarnings("unused")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public UserDTO createUser(UserDTO dto) {
        if(dto.getUserName().equalsIgnoreCase("public") ||
                userRepository.existsByUsername(dto.getUserName()))
            throw new UsernameAlreadyExists("Esse username ja está sendo usado");

        var user = User.builder()
                .userName(dto.getUserName())
                .formalName(dto.getFormalName())
                .password(new BCryptPasswordEncoder().encode(dto.getPassword()))
                .build();

        return UserDTO.from(userRepository.save(user));
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User user = getUserById(id);
        user.setUserName(updatedUser.getUserName());
        user.setPassword(updatedUser.getPassword());
        user.setFormalName(updatedUser.getFormalName());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public User findUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + username));
    }


    @Transactional(readOnly = true)
    public UserDTO findUserDtoByUsername(String username) {
        User user = findUserByUsername(username);
        return UserDTO.from(user);
    }

    @Transactional(readOnly = true)
    public PublicUserDTO findPublicUserDtoByUsername(String username) {
        User user = findUserByUsername(username);
        return new PublicUserDTO(user.getUserName(), user.getFormalName());
    }

    @Transactional
    public void updateFormalName(String username, UserFormalNameRequestDTO dto) {
        User user = findUserByUsername(username);
        user.setFormalName(dto.formalName());
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(String username, UserPasswordRequestDTO dto) {
        User user = findUserByUsername(username);
        var encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<PublicUserDTO> getUsersByUsernameOrFormalname(String query, String username) {
        return userRepository.findUsersByUsernameOrFormalname(query, username);
    }

}