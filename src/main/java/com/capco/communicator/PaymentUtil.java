package com.capco.communicator;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PaymentUtil {

    private static final String schemaLocation_01 = "src/main/resources/schemas/payment.001.001.01.xsd";
    private static final String schemaLocation_02 = "src/main/resources/schemas/payment.001.001.02.xsd";

    private static final String xsltPath_01 = "src/main/resources/xslt/payment.001.001.01.xslt";
    private static final String xsltPath_02 = "src/main/resources/xslt/payment.001.001.02.xslt";


    public static String getFtpDirPath() {
        Path projectRelativePath = Paths.get("");
        File ftpDir = new File(projectRelativePath.toAbsolutePath().toString() + File.separator + "ftp");

        return ftpDir.getPath();
    }

    public static String getSchemaLocation(PaymentFormat paymentFormat) throws PaymentProcessingException {
        if (paymentFormat == PaymentFormat.PAIN_01) {
            return schemaLocation_01;
        } else if (paymentFormat == PaymentFormat.PAIN_02) {
            return schemaLocation_02;
        }
        throw new PaymentProcessingException("unsupported payment format: " + paymentFormat);
    }

    public static String getXsltPath(PaymentFormat paymentFormat) throws PaymentProcessingException {
        if (paymentFormat == PaymentFormat.PAIN_01) {
            return xsltPath_01;
        } else if (paymentFormat == PaymentFormat.PAIN_02) {
            return xsltPath_02;
        }
        throw new PaymentProcessingException("unsupported payment format: " + paymentFormat);
    }

}
