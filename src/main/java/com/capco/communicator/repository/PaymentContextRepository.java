package com.capco.communicator.repository;

import com.capco.communicator.schema.PaymentContext;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository handling PaymentContext entities.
 * */
public interface PaymentContextRepository extends CrudRepository<PaymentContext, Long> {

}
