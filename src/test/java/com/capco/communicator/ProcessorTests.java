package com.capco.communicator;

import com.capco.communicator.processor.CorrelateProcessor;
import com.capco.communicator.processor.TransformProcessor;
import com.capco.communicator.processor.ValidateProcessor;
import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ProcessorTests {

    @Autowired
    private ValidateProcessor validateProcessor;

    @Autowired
    private TransformProcessor transformProcessor;

    @Autowired
    private CorrelateProcessor correlateProcessor;

    private String validPaymentXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<Document>\n" +
            "    <pain.001.001.02>\n" +
            "        <bank>CRAFT_STAR404</bank>\n" +
            "        <account>DE44-5001-0517-5407-3249-31</account>\n" +
            "        <debit>17</debit>\n" +
            "        <credit>0</credit>\n" +
            "    </pain.001.001.02>\n" +
            "</Document>";

    private String malformedPaymentXML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<Document>\n" +
            "    <pain.001.001.02>\n" +
            "        <bnk>CRAFT_STAR404</bank>\n" +
            "        <account>DE44-5001-0517-5407-3249-31</account>\n" +
            "        <debit>17</debit>\n" +
            "        <credit>0</credit>\n" +
            "    </pain.001.001.02>\n" +
            "</Document>";


    @Test
    public void validationTestValidXML(){
        PaymentContext context = new PaymentContext();
        context.setState(State.VALIDATE);
        context.setCreatedAt(new Date());
        context.setResource(validPaymentXML);

        validateProcessor.process(context);

        Assert.assertEquals(State.TRANSFORM, context.getState());
    }

    @Test
    public void validationTestInvalidXML(){
        PaymentContext context = new PaymentContext();
        context.setState(State.VALIDATE);
        context.setCreatedAt(new Date());
        context.setResource(malformedPaymentXML);

        validateProcessor.process(context);

        Assert.assertEquals(State.VALIDATE_ERROR, context.getState());
    }

    @Test
    public void transformationTestValidXML(){
        PaymentContext context = new PaymentContext();
        context.setState(State.TRANSFORM);
        context.setCreatedAt(new Date());
        context.setResource(validPaymentXML);

        transformProcessor.process(context);

        Assert.assertEquals(State.DUPLICATE_CHECK, context.getState());
        Assert.assertNotNull(context.getPayload());
        Assert.assertTrue(context.getPayload().length != 0);
    }

    @Test
    public void transformationTestMalformedXML(){
        PaymentContext context = new PaymentContext();
        context.setState(State.TRANSFORM);
        context.setCreatedAt(new Date());
        context.setResource(malformedPaymentXML);

        transformProcessor.process(context);

        Assert.assertEquals(State.TRANSFORM_ERROR, context.getState());
    }

    @Test
    public void correlateProcessorTest(){
        PaymentContext context = new PaymentContext();
        context.setState(State.VALIDATE);
        context.setCreatedAt(new Date());
        context.setResource(validPaymentXML);

        validateProcessor.process(context);
        transformProcessor.process(context);
        correlateProcessor.process(context);

        Assert.assertNotNull(context.getPayment());
    }
}
