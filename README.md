
capco-bank-communicator connects, translates and normalizes any kind of data. The business agility layer that bridges the gap between business and IT. 

Exercises for workshop:

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
   
   
   -----------------------------------------------------------------------------------------------------
   
   
   Win env for workshop is available here: https://drive.google.com/open?id=0B3AWLqDRDe5rX0I5U1V1Y1hUYlk
