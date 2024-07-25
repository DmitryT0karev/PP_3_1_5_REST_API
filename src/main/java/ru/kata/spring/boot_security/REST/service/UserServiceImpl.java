package ru.kata.spring.boot_security.REST.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.REST.model.User;
import ru.kata.spring.boot_security.REST.repository.UserRepository;
import ru.kata.spring.boot_security.REST.security.PersonDetails;
import ru.kata.spring.boot_security.REST.util.UserNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void update(User updateUser) {
        userRepository.save(updateUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

//    @Override
//    @Transactional(readOnly = true)
//    public User getUserById(Long id) {
//        return userRepository.getById(id);
//    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        Optional<User> userById = userRepository.findById(id);
        //return userById.orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userById.orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with '%s' email not found", email));
        }
        return new PersonDetails(user.get());
    }
}
