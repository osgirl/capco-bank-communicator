package com.capco.communicator.processor;

import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
import org.springframework.stereotype.Service;

/**
 * RulesProcessor receives a paymentContext and checks against
 * a set of rules for the specific payment. If this process
 * is successful, next step (Dispatch) can be initiated
 * */
@Service
public class RulesProcessor extends PaymentProcessor {

    @Override
    public void process(PaymentContext paymentContext) {

        paymentContext.setState(State.APPROVE);
        paymentContextRepository.save(paymentContext);
    }
}
