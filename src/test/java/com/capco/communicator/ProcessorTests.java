package com.capco.communicator;

import com.capco.communicator.processor.ValidateProcessor;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ProcessorTests {

    @Autowired
    private ValidateProcessor validateProcessor;

    public void validationTestValidXML(){
        //TODO
    }

    public void validationTestInvalidXML(){
        //TODO
    }

}
