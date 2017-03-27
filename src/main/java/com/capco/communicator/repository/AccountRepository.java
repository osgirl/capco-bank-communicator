package com.capco.communicator.repository;

import com.capco.communicator.schema.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository handling Account entities. Implements additional search methods.
 * */
public interface AccountRepository extends JpaRepository<Account, Long>{

    Account findByLogin(String login);

    Account findByCode(String code);

    List<Account> findByLoginStartsWithIgnoreCase(String login);
}
