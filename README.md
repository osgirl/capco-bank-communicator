
# capco-bank-communicator 
Connects, translates and normalizes any kind of data. The business agility layer that bridges the gap between business and IT. 

## Exercises for workshop:

1. Implement ValidateProcessor:
    Validate resource in process context against valid xsd schema depending on paymentFormat<br>
    a) Load the schema<br>
    b) Create Source from XML String<br>
    c) Validate the XML against the schema<br>
    Note: check also PaymentUtil.java for schema location

    If validation is successful set state to TRANSFORM, if not to VALIDATE_ERROR    

2. create xslt schema for payment.001.001.99.xml and
    use it in TransformProcessor
 
3. Implement findBank() methods in CorrelateProcessor<br> 
    (Tip: find inspiration in the process of locating account code)

4. implement sending payment to MQ (use JmsService component)<br>
   Hint: use PaymentContext.channel attribute with value "mq"
      
###   Win env for workshop is available here: https://drive.google.com/open?id=0B3AWLqDRDe5rX0I5U1V1Y1hUYlk

## Task Solution
There are 4 tasks to be completed. As presented on workshop, there are 
8 steps of each payment processing. Each of workshop tasks require the 
user to implement a little piece of code that will help proceede with payment
processing. 

Each task can be implemented and then tested separately to confirm the
correctness of implementation.

If you want to implement the tasks yourself, please checkout the branch 'tasks-incomplete'.
Branch master contains source code with completed tasks. There is no need to create ftp
folders and subfolders (as we did on the workshop), as they are already prepared. 

There are 2 ways to test your progress:
1. Using test class WorkshopTaskTester.java (do not forget to comment out the @Ignore 
   annotation before running the tests). Each of the test methods are designed to check
   if the task was performed correctly. For example, after implementing the first task, run
   the first test (method task_1_test()) to validate your code. If the task is not implemented 
   correctly, the test will fail.<br>
   
2. Using the web application. After logging to the application (using credentials "han" as login 
    and "solo" as password), go to the Payment Contexts menu and check the status of payment
    context. It should be:<br>
     - VALIDATE_ERROR before completing any task<br>
     - TRANSFORM_ERROR after completion of Task 1<br>
     - CORRELATE_ERROR after completion of Task 2<br>
     - DISPATCH_ERROR after completion of Task 3<br>
     - DONE after completion of Task 4
   			 
###   Task 1: Validate Processor
In ValidateProcessor class, there is a need to implement the executeSchemaValidation() method. The purpose
of this method is to validate the incoming payment against the XSD schema. Of course, there are multiple 
methods of achieving this. One of them is following:

```
private void executeSchemaValidation(PaymentContext paymentContext) throws PaymentProcessingException, SAXException, IOException {
   // Validate resource in process context against valid xsd schema depending on paymentFormat in this method:
   // a) Load the schema (use Schema instance)
   // b) Create Source from XML String (use StreamSource instance)
   // c) Validate the XML against the schema Note: check also PaymentUtil.java for schema location (use Validator)

   String schemaLocation = PaymentUtil.getSchemaLocation(paymentContext.getPaymentFormat());

   // Load the schema
   Schema schema = schemaFactory.newSchema(new File(schemaLocation));

   //Create Source from XML String
   Source xmlSource = new StreamSource(new StringReader(paymentContext.getResource()));

   //Validate the XML against the schema
   Validator validator = schema.newValidator();
   validator.validate(xmlSource);
}
```
   	
Of course, do not forget to set the state of PaymentContext entity to State.TRANSFORM after succesfull validation.
   	
### Task 2: Transform Processor
The purpose of this task is to create a new XSLT template to transform the payment of type 99 to a target format.
To complete this task, you can inspire from other used XSLT templates, as there are only minor changes in our payment format.
Following XSLT template should be used:
   
```  
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <!-- Output with indentation. -->
   <xsl:output method="xml" encoding="UTF-8" omit-xml-declaration="no" indent="yes"/>

   <xsl:template match="/">
       <Document>
           <payment>
               <version><xsl:value-of select="local-name(Document/pain.001.001.99)"/></version>

               <xsl:for-each select="Document/pain.001.001.99">
                   <bank><xsl:value-of select="bank"/></bank>
                   <account><xsl:value-of select="account"/></account>
                   <confirmation><xsl:value-of select="confirmation"/></confirmation>
                   <iban><xsl:value-of select="iban"/></iban>
                   <debit><xsl:value-of select="debit"/></debit>
                   <credit><xsl:value-of select="credit"/></credit>
                   <reference><xsl:value-of select="reference"/></reference>
                   <notice><xsl:value-of select="note"/></notice>
               </xsl:for-each>
           </payment>
       </Document>
   </xsl:template>   
</xsl:stylesheet>
```
   	
### Task 3: Correlate Processor
The purpose of task 3 is to implement locating a target Bank of the payment. You can inspire by looking at
how other elements are searched for in CorrelateProcessor. The implementation of findBank() methods can be
following:
   
```
private Bank findBank(Document doc, PaymentContext paymentContext){
   Element bankElement = (Element) doc.getElementsByTagName(ELEMENT_BANK_CODE).item(0);
   String bankCode = getCharacterDataFromElement(bankElement);

   if (bankCode == null) {
       return null;
   }

   return bankRepository.findByCode(bankCode);
}
```
   	
### Task 4: Dispatch Processor
Currently, the DispatchProcessor only sends the data to one channel, and that is FTP. The purpose of this task
is to implement sending to another channel, MQ broker. We have used an embedded Active MQ broker instance,
and to send a JMS message to this instance, you can use very simple JmsService implementation we have prepared.
This service accepts String messages, so we can use toString() method of Payment instance to create the message body.
      
First, we need to @Autowire the JmsService instance:
   
```
@Autowired
private JmsService jmsService;
```
   	
Then, we need to implement sending the message to MQ:

```
if(Channel.FTP.equals(paymentContext.getChannel())) {
   File file = new File(getOutputFilePath(paymentContext.getPayment().getBank().getOutputChannel()));
   JAXBContext jaxbContext = JAXBContext.newInstance(Payment.class);
   Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

   jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

   jaxbMarshaller.marshal(paymentContext.getPayment(), file);
   paymentContext.setState(State.DONE);
   
} else if(Channel.MQ.equals(paymentContext.getChannel())) {
   jmsService.send(paymentContext.getPayment().toString());
} else {
   paymentContext.setState(State.DISPATCH_ERROR);
}
```
   		
Also, we need to change the channel of the message loaded from the XML file. To do that, change the paymentContext channel
using following code:
   
```
paymentContext.setChannel(Channel.MQ);
```
   	
in the createAndGetPaymentContext() method of FtpWorker class. After completing these steps, you should alse see a message
with the payment in the logs. Of course, in reality, there would be other application on the receiving end of the message broker,
we have added simple JmsReceiver to illustrate that the message is really created and send.
   	
All tasks are bnow implemented, good job and well done :)	
   			 
