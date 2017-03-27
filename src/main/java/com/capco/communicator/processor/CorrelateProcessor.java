package com.capco.communicator.processor;

import com.capco.communicator.schema.*;
import org.h2.util.StringUtils;
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

/**
 * CorrelateProcessor receives a paymentContext and performs
 * correlation of the payment. This means that we need to find the banks
 * and the accounts on which the payment was performed. If this process
 * is successful, next step (Rules) can be initiated
 * */
@Service
public class CorrelateProcessor extends PaymentProcessor {

    private static final String ELEMENT_BANK_CODE = "bank";
    private static final String ELEMENT_ACCOUNT_CODE = "account";
    private static final String ELEMENT_DEBIT = "debit";
    private static final String ELEMENT_CREDIT = "credit";
    private static final String ELEMENT_IBAN = "iban";
    private static final String ELEMENT_NOTICE = "notice";

    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();


    @Override
    public void process(PaymentContext paymentContext) {

        if (paymentContext.getPayload() == null || paymentContext.getPayload().length == 0) {
            paymentContext.setState(State.CORRELATE_ERROR);
            paymentContext.addErrorLog("Context correlation failed. State: " + paymentContext.getState() +
                    ", Error: missing payload in context.");
            paymentContextRepository.save(paymentContext);
            return;
        }

        try {
            String transformedPayment = new String(paymentContext.getPayload(), TransformProcessor.DEFAULT_CHAR_ENCODING);

            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(transformedPayment));
            Document doc = db.parse(is);

            //Retrieve important elements
            Element accountElement = (Element) doc.getElementsByTagName(ELEMENT_ACCOUNT_CODE).item(0);
            Element creditElement = (Element) doc.getElementsByTagName(ELEMENT_CREDIT).item(0);
            Element debitElement = (Element) doc.getElementsByTagName(ELEMENT_DEBIT).item(0);
            Element noticeElement = (Element) doc.getElementsByTagName(ELEMENT_NOTICE).item(0);
            Element ibanElement = (Element) doc.getElementsByTagName(ELEMENT_IBAN).item(0);

            //Retrieve and validate bank and account code
            String accountCode = getCharacterDataFromElement(accountElement);

            if (accountCode == null) {
                paymentContext.setState(State.CORRELATE_ERROR);
                paymentContext.addErrorLog("Context correlation failed. State: " + paymentContext.getState() +
                        ", Error: Missing account code.");
                paymentContextRepository.save(paymentContext);
                return;
            }

            //Retrieve and validate credit and debit values
            String creditStringForm = getCharacterDataFromElement(creditElement);
            String debitStringForm = getCharacterDataFromElement(debitElement);

            if (creditStringForm == null || debitStringForm == null ||
                    !StringUtils.isNumber(creditStringForm) || !StringUtils.isNumber(debitStringForm)) {

                paymentContext.setState(State.CORRELATE_ERROR);
                paymentContext.addErrorLog("Context correlation failed. State: " + paymentContext.getState() +
                        ", Error: Missing credit or debit value. Credit value: " + creditStringForm +
                        ", Debit value: " + debitStringForm);
                paymentContextRepository.save(paymentContext);
                return;
            }

            //Retrieve band and account from the repository
            Bank bank = findBank(doc, paymentContext);
            Account account = accountRepository.findByCode(accountCode);

            if (bank == null || account == null) {
                paymentContext.setState(State.CORRELATE_ERROR);
                paymentContext.addErrorLog("Context correlation failed. State: " + paymentContext.getState() +
                        ", Error: No bank or account with specified codes in repository.");
                paymentContextRepository.save(paymentContext);
                return;
            }

            String iban = getCharacterDataFromElement(ibanElement);
            String notice = getCharacterDataFromElement(noticeElement);

            //Create and persist new payment
            Payment payment = new Payment();
            payment.setBank(bank);
            payment.setAccount(account);
            payment.setCredit(Long.parseLong(creditStringForm));
            payment.setDebit(Long.parseLong(debitStringForm));
            payment.setIban(iban);
            payment.setIban(notice);

            paymentContext.setPayment(payment);
            paymentContext.setState(State.RULES);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            paymentContext.setState(State.CORRELATE_ERROR);
            paymentContext.addErrorLog("Context correlation failed. State: " + paymentContext.getState() +
                    ", Error: " + e.getMessage());
        }

        paymentContextRepository.save(paymentContext);
    }

    private Bank findBank(Document doc, PaymentContext paymentContext){
        //TODO - implement this method - use similar approach as used in finding account code

        return null;
    }


    /*==============================*/
    /*        Helper methods        */
    /*==============================*/
    private String getCharacterDataFromElement(Element e) {
        if(e == null) {
            return null;
        }
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }

        return null;
    }
}
