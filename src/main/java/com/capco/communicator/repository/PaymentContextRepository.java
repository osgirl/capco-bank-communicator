package com.capco.communicator.repository;

import com.capco.communicator.schema.PaymentContext;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository handling PaymentContext entities.
 * */
public interface PaymentContextRepository extends JpaRepository<PaymentContext, Long> {

}
