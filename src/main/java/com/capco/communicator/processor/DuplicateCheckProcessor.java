package com.capco.communicator.processor;

import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
import org.springframework.stereotype.Service;

/**
 * DuplicateCheckProcessor receives a paymentContext and performs
 * checks for duplicate payment. If there are no such payments
 * registered in the system, next step (Correlation) can be initiated
 * */
@Service
public class DuplicateCheckProcessor extends PaymentProcessor {

    @Override
    public void process(PaymentContext paymentContext) {

        paymentContext.setState(State.CORRELATE);
        paymentContextRepository.save(paymentContext);
    }
}
