package com.test.test_aquariux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.test.test_aquariux.entity.User;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    BigDecimal findWalletBalanceById(Long id);
}
