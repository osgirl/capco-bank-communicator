package com.capco.communicator.repository;

import com.capco.communicator.schema.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository handling Payment entities.
 * */
public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
