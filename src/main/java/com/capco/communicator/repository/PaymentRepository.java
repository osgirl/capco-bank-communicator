package com.capco.communicator.repository;

import com.capco.communicator.schema.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
