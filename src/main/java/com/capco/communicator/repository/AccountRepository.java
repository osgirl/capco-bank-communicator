package com.capco.communicator.repository;

import com.capco.communicator.schema.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository handling Account entities. Implements additional search methods.
 * */
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findByLogin(String login);

    Account findByCode(String code);

    List<Account> findByLoginStartsWithIgnoreCase(String login);
}
