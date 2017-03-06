package com.capco.communicator.worker;

import com.capco.communicator.processor.*;
import com.capco.communicator.repository.PaymentContextRepository;
import com.capco.communicator.schema.PaymentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PaymentWorker extends AbstractWorker {

    private static final Logger logger = Logger.getLogger(PaymentWorker.class.toString());

    @Autowired
    private PaymentContextRepository paymentContextRepository;
    @Autowired
    private DecodeProcessor decodeProcessor;
    @Autowired
    private ValidateProcessor validateProcessor;
    @Autowired
    private TransformProcessor transformProcessor;
    @Autowired
    private DuplicateCheckProcessor duplicateCheckProcessor;
    @Autowired
    private CorrelateProcessor correlateProcessor;
    @Autowired
    private RulesProcessor rulesProcessor;
    @Autowired
    private ApproveProcessor approveProcessor;
    @Autowired
    private DispatchProcessor dispatchProcessor;


    protected void performJob() {
        List<PaymentContext> allPaymentContext = paymentContextRepository.findAll();
        PaymentProcessor processor = null;
        for (PaymentContext paymentContext : allPaymentContext) {
            switch (paymentContext.getState()) {
                case DECODE:
                    processor = decodeProcessor;
                    break;
                case VALIDATE:
                    processor = validateProcessor;
                    break;
                case TRANSFORM:
                    processor = transformProcessor;
                    break;
                case DUPLICATE_CHECK:
                    processor = duplicateCheckProcessor;
                    break;
                case CORRELATE:
                    processor = correlateProcessor;
                    break;
                case RULES:
                    processor = rulesProcessor;
                    break;
                case APPROVE:
                    processor = approveProcessor;
                    break;
                case DISPATCH:
                    processor = dispatchProcessor;
                    break;
                default:
                    continue;
            }
            try {
                processor.process(paymentContext);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.toString());
            }
        }
    }

}
