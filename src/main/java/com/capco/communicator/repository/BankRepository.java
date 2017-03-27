package com.capco.communicator.repository;

import com.capco.communicator.schema.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository handling Bank entities. Implements additional search methods.
 * */
public interface BankRepository extends JpaRepository<Bank, Long> {

	Bank findByCode(String code);

	List<Bank> findByCodeStartsWithIgnoreCase(String lastName);
}
