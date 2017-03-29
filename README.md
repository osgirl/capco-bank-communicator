
1. create xslt schema for payment.001.001.99.xml and
    use it in TransformProcessor
 
2. Implement findBank() methods in CorrelateProcessor<br> 
    (Tip: find inspiration in the process of locating account code)

3. Implement ValidateProcessor:
    Validate resource in process context against valid xsd schema depending on paymentFormat<br>
    a) Load the schema<br>
    b) Create Source from XML String<br>
    c) Validate the XML against the schema<br>
    Note: check also PaymentUtil.java for schema location

    If validation is successful set state to TRANSFORM, if not to VALIDATE_ERROR
    
4. implement sending payment to MQ (use JmsService component)<br>
   Hint: use PaymentContext.channel attribute with value "mq"