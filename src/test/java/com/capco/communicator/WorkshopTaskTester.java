package com.capco.communicator;

import com.capco.communicator.processor.CorrelateProcessor;
import com.capco.communicator.processor.DispatchProcessor;
import com.capco.communicator.processor.TransformProcessor;
import com.capco.communicator.processor.ValidateProcessor;
import com.capco.communicator.schema.Channel;
import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
import com.capco.communicator.service.JmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Assert;

import java.io.IOException;
import java.util.Date;

//@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class WorkshopTaskTester {

    @Autowired
    private ValidateProcessor validateProcessor;

    @Autowired
    private TransformProcessor transformProcessor;

    @Autowired
    private CorrelateProcessor correlateProcessor;

    @Autowired
    private DispatchProcessor dispatchProcessor;

    private String payment =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<Document>\n" +
                    "    <pain.001.001.99>\n" +
                    "        <bank>STUDENT_BANK</bank>\n" +
                    "        <account>DE44-5001-0517-5407-3249-31</account>\n" +
                    "        <confirmation>true</confirmation>\n" +
                    "        <debit>0</debit>\n" +
                    "        <credit>777</credit>\n" +
                    "        <reference>PN_707</reference>\n" +
                    "        <note>OldFieldNotice</note>\n" +
                    "    </pain.001.001.99>\n" +
                    "</Document>";


    @Test
    public void task_1_test(){
        PaymentContext context = new PaymentContext();
        context.setState(State.VALIDATE);
        context.setCreatedAt(new Date());
        context.setPaymentFormat(PaymentFormat.PAIN_99);
        context.setResource(payment);

        validateProcessor.process(context);

        Assert.assertEquals(State.TRANSFORM, context.getState());
    }

    @Test
    public void task_2_test() throws IOException {
        PaymentContext context = new PaymentContext();
        context.setState(State.VALIDATE);
        context.setCreatedAt(new Date());
        context.setPaymentFormat(PaymentFormat.PAIN_99);
        context.setResource(payment);

        transformProcessor.process(context);

        Assert.assertEquals(State.DUPLICATE_CHECK, context.getState());
        Assert.assertNotNull(context.getPayload());
        Assert.assertTrue(context.getPayload().length != 0);

        String transformedPayload = new String(context.getPayload(), TransformProcessor.DEFAULT_CHAR_ENCODING);
    }

    @Test
    public void task_3_test(){
        PaymentContext context = new PaymentContext();
        context.setState(State.TRANSFORM);
        context.setCreatedAt(new Date());
        context.setPaymentFormat(PaymentFormat.PAIN_99);
        context.setResource(payment);

        transformProcessor.process(context);

        Assert.assertNotNull(context.getPayload());
        Assert.assertTrue(context.getPayload().length != 0);

        correlateProcessor.process(context);
        Assert.assertEquals(State.RULES, context.getState());
        Assert.assertNotNull(context.getPayment());
        Assert.assertNotNull(context.getPayment().getBank());
    }

    @Test
    public void task_4_test(){
        PaymentContext context = new PaymentContext();
        context.setState(State.DISPATCH);
        context.setCreatedAt(new Date());
        context.setPaymentFormat(PaymentFormat.PAIN_99);
        context.setResource(payment);
        context.setChannel(Channel.MQ);

        validateProcessor.process(context);
        transformProcessor.process(context);
        correlateProcessor.process(context);
        dispatchProcessor.process(context);

        Assert.assertEquals(State.DONE, context.getState());
    }
}
