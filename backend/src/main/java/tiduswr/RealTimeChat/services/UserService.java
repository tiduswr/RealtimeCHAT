package tiduswr.RealTimeChat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import tiduswr.RealTimeChat.exceptions.EmailAlreadyExists;
import tiduswr.RealTimeChat.exceptions.UsernameAlreadyExists;
import tiduswr.RealTimeChat.model.User;
import tiduswr.RealTimeChat.model.dto.EmailRequestDTO;
import tiduswr.RealTimeChat.model.dto.PublicUserDTO;
import tiduswr.RealTimeChat.model.dto.UserDTO;
import tiduswr.RealTimeChat.model.dto.UserFormalNameRequestDTO;
import tiduswr.RealTimeChat.model.dto.UserPasswordRequestDTO;
import tiduswr.RealTimeChat.repository.UserRepository;

import java.util.List;

@Service
@SuppressWarnings("unused")
public class UserService implements UserDetailsService {

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

        if(userRepository.existsByEmail(dto.getEmail()))
            throw new EmailAlreadyExists("Esse email ja está sendo usado");

        var user = User.builder()
                .userName(dto.getUserName())
                .email(dto.getEmail())
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
        user.setEmail(updatedUser.getEmail());
        user.setFormalName(updatedUser.getFormalName());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsername(username);

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    @Transactional(readOnly = true)
    public User findUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
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

    @Transactional
    public void updateEmail(String username, EmailRequestDTO dto) {
        User user = findUserByUsername(username);

        if(userRepository.existsByEmailExceptUser(dto.getEmail(), username))
            throw new EmailAlreadyExists("Esse email ja está em uso.");

        user.setEmail(dto.getEmail());

        userRepository.save(user);
    }

}
