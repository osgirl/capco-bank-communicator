package com.capco.communicator.processor;

import com.capco.communicator.schema.*;
import org.springframework.stereotype.Service;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

@Service
public class CorrelateProcessor extends PaymentProcessor {

    private static final String ELEMENT_BANK_CODE = "bank";
    private static final String ELEMENT_ACCOUNT_CODE = "account";

    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private DocumentBuilder db = null;


    @Override
    public void process(PaymentContext paymentContext) {

        if(paymentContext.getPayload() == null || paymentContext.getPayload().length == 0){
            paymentContext.setState(State.CORRELATE_ERROR);
            paymentContextRepository.save(paymentContext);
            return;
        }

        try {
            String transformedPayment = new String(paymentContext.getPayload(), TransformProcessor.DEFAULT_CHAR_ENCODING);

            db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(transformedPayment));
            Document doc = db.parse(is);

            Element bankElement = (Element)doc.getElementsByTagName(ELEMENT_BANK_CODE).item(0);
            Element accountElement = (Element)doc.getElementsByTagName(ELEMENT_ACCOUNT_CODE).item(0);

            String bankCode = getCharacterDataFromElement(bankElement);
            String accountCode = getCharacterDataFromElement(accountElement);

            if(bankCode == null || accountCode == null){
                paymentContext.setState(State.CORRELATE_ERROR);
                paymentContextRepository.save(paymentContext);
                return;
            }

            Bank bank = bankRepository.findByCode(bankCode);
            Account account = accountRepository.findByCode(accountCode);

            if(bank == null || account == null){
                paymentContext.setState(State.CORRELATE_ERROR);
                paymentContextRepository.save(paymentContext);
                return;
            }

            Payment payment = new Payment();
            payment.setBank(bank);
            payment.setAccount(account);

            paymentContext.setPayment(payment);
            paymentContext.setState(State.RULES);
            paymentContextRepository.save(paymentContext);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            paymentContext.setState(State.CORRELATE_ERROR);
//            e.printStackTrace();
        }
    }

    /*==============================*/
    /*        Helper methods        */
    /*==============================*/

    private String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }

        return null;
    }
}
