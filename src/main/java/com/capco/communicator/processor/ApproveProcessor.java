package com.capco.communicator.processor;

import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
import org.springframework.stereotype.Service;

/**
 * 
 * */
@Service
public class ApproveProcessor extends PaymentProcessor {

    @Override
    public void process(PaymentContext paymentContext) {

        paymentContext.setState(State.DISPATCH);
        paymentContextRepository.save(paymentContext);
    }
}
