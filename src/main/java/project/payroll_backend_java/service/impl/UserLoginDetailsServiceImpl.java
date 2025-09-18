package project.payroll_backend_java.service.impl;

import project.payroll_backend_java.entity.UserLoginDetails;
import project.payroll_backend_java.repository.UserLoginDetailsRepository;
import project.payroll_backend_java.service.UserLoginDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserLoginDetailsServiceImpl implements UserLoginDetailsService {

    @Autowired
    private UserLoginDetailsRepository userRepository;

    @Override
    public UserLoginDetails saveUser(UserLoginDetails user) {
        // No additional validation here - all validation done in controller to match Node.js
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserLoginDetails> getUserByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserLoginDetails> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserLoginDetails updateUser(UserLoginDetails user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String userName) {
        userRepository.deleteById(userName);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateCredentials(String userName, String password) {
        return userRepository.findByUserNameAndUserPassword(userName, password).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserLoginDetails> authenticateUser(String userName, String password) {
        return userRepository.findByUserNameAndUserPassword(userName, password);
    }

    @Override
    public boolean updatePassword(String userName, String newPassword) {
        try {
            int updatedRows = userRepository.updatePassword(userName, newPassword);
            return updatedRows > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String userName) {
        return userRepository.existsByUserName(userName);
    }
}
