package com.capco.communicator.processor;

import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
import org.springframework.stereotype.Service;

/**
 * DecodeProcessor receives a paymentContext and performs
 * initial decode process of the message. If the process is
 * successful, next step (Validation) can be initiated
 * */
@Service
public class DecodeProcessor extends PaymentProcessor {


    @Override
    public void process(PaymentContext paymentContext) {

        paymentContext.setState(State.VALIDATE);
        paymentContextRepository.save(paymentContext);
    }
}
