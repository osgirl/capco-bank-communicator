package com.capco.communicator.processor;

import com.capco.communicator.repository.AccountRepository;
import com.capco.communicator.repository.BankRepository;
import com.capco.communicator.repository.PaymentContextRepository;
import com.capco.communicator.repository.PaymentRepository;
import com.capco.communicator.schema.PaymentContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PaymentProcessor {

    @Autowired
    protected PaymentContextRepository paymentContextRepository;

    @Autowired
    protected BankRepository bankRepository;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected PaymentRepository paymentRepository;

    public abstract void process(PaymentContext paymentContext);

}
