package com.capco.communicator.processor;

import com.capco.communicator.repository.PaymentContextRepository;
import com.capco.communicator.schema.PaymentContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PaymentProcessor {

    @Autowired
    protected PaymentContextRepository paymentContextRepository;

    public abstract void process(PaymentContext paymentContext);

}
