# capco-bank-communicator

1.implement ValidateProcessor

Validate resource in process context against valid xsd schema depending on paymentFormat
a) Load the schema
b) Create Source from XML String
c) Validate the XML against the schema
Note: check also PaymentUtil.java for schema location

If validation is successful set state to TRANSFORM, if not to VALIDATE_ERROR

2.implement DispatchProcessor