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
import tiduswr.RealTimeChat.exceptions.UsernameAlreadyExists;
import tiduswr.RealTimeChat.model.User;
import tiduswr.RealTimeChat.model.dto.UserDTO;
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
        if(userRepository.existsByUsername(dto.getUserName()))
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

    public User findUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}
