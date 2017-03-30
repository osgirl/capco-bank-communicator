package com.capco.communicator.repository;

import com.capco.communicator.schema.Payment;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository handling Payment entities.
 * */
public interface PaymentRepository extends CrudRepository<Payment, Long> {

}
