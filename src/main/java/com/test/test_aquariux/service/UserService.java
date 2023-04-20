package com.test.test_aquariux.service;

import com.test.test_aquariux.entity.User;
import com.test.test_aquariux.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    UserRepository userRepository;
    public void save(User user) {
        userRepository.save(user);
    }

    public BigDecimal getWalletBalance(Long id) {
        return userRepository.findWalletBalanceById(id);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
