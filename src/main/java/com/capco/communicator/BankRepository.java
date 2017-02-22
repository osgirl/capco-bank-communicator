package com.capco.communicator;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankRepository extends JpaRepository<Bank, Long> {

	List<Bank> findByCodeStartsWithIgnoreCase(String lastName);
}
