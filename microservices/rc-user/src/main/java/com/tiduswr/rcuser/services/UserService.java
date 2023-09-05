package com.tiduswr.rcuser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.tiduswr.rcuser.exceptions.EmailAlreadyExists;
import com.tiduswr.rcuser.exceptions.UserNotFoundException;
import com.tiduswr.rcuser.exceptions.UsernameAlreadyExists;
import com.tiduswr.rcuser.model.EmailTemplateType;
import com.tiduswr.rcuser.model.User;
import com.tiduswr.rcuser.model.dto.EmailDTO;
import com.tiduswr.rcuser.model.dto.EmailRequestDTO;
import com.tiduswr.rcuser.model.dto.InternalUserDTO;
import com.tiduswr.rcuser.model.dto.PublicUserDTO;
import com.tiduswr.rcuser.model.dto.UserDTO;
import com.tiduswr.rcuser.model.dto.UserEmailRequestDTO;
import com.tiduswr.rcuser.model.dto.UserFormalNameRequestDTO;
import com.tiduswr.rcuser.model.dto.UserPasswordRequestDTO;
import com.tiduswr.rcuser.rabbitmq.EmailPublisher;
import com.tiduswr.rcuser.repositories.UserRepository;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@SuppressWarnings("unused")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Value("${email.sender}")
    private String emailAddress;

    @Autowired
    private EmailPublisher emailPublisher;

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
        return registerNewUser(dto, null);
    }

    @Transactional
    public UserDTO createUser(UserDTO dto, String redirectUrl) {
        return registerNewUser(dto, redirectUrl);
    }


    @Transactional
    private UserDTO registerNewUser(UserDTO dto, String redirectUrl) {
        if(dto.getUserName().equalsIgnoreCase("public") ||
                userRepository.existsByUsername(dto.getUserName()))
            throw new UsernameAlreadyExists("Esse username ja está sendo usado");

        if(userRepository.existsByEmail(dto.getEmail()))
            throw new EmailAlreadyExists("Esse email ja está sendo usado");

        var user = User.builder()
                .userName(dto.getUserName())
                .formalName(dto.getFormalName())
                .email(dto.getEmail())
                .password(new BCryptPasswordEncoder().encode(dto.getPassword()))
                .build();

        var userDto = UserDTO.from(userRepository.save(user));

        if(redirectUrl != null) 
            emailPublisher.publishSendEmailRequest(generateWelcomeEmail(userDto, redirectUrl));
        return userDto;
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User user = getUserById(id);
        user.setUserName(updatedUser.getUserName());
        user.setEmail(updatedUser.getEmail());
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

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public void updateEmail(String username, EmailRequestDTO dto) {
        User user = findUserByUsername(username);

        if(userRepository.existsByEmailExceptUser(dto.getEmail(), username))
            throw new EmailAlreadyExists("Esse email ja está em uso.");

        user.setEmail(dto.getEmail());

        userRepository.save(user);
    }

    private EmailDTO generateWelcomeEmail(UserDTO dto, String redirectUrl){
        return EmailDTO.builder()
            .emailFrom(emailAddress)
            .emailSubject("Seja muito bem vindo " + dto.getFormalName() + " | RealTimeCHAT")
            .emailTemplateType(EmailTemplateType.WELCOME)
            .emailTo(dto.getEmail())
            .formalName(dto.getFormalName())
            .ownerId(dto.getId())
            .action_url(redirectUrl)
            .build();
    }

    @Transactional(readOnly = true)
    public List<PublicUserDTO> retrieveFormalName(Set<String> usernames) {
        return userRepository.retrieveFormalNameForUsernames(usernames);
    }

}
