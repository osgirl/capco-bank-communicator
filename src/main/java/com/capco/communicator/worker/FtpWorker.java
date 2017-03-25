package com.capco.communicator.worker;

import com.capco.communicator.PaymentFormat;
import com.capco.communicator.repository.PaymentContextRepository;
import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * periodically check ftp folder for new payment files
 * create PaymentContext entities from these files
 * after successful creation payment files are deleted
 */
@Service
public class FtpWorker extends AbstractWorker {

    private static final Logger logger = Logger.getLogger(FtpWorker.class.toString());

    @Autowired
    private PaymentContextRepository paymentContextRepository;

    protected void performJob() {
        File payment01Dir = new File(getFtpDirPath() + File.separator + "payment.001.001.001");
        if (payment01Dir.exists()) {
            createContexts(payment01Dir, PaymentFormat.PAIN_01);
        }
        File payment02Dir = new File(getFtpDirPath() + File.separator + "payment.001.001.002");
        if (payment02Dir.exists()) {
            createContexts(payment02Dir, PaymentFormat.PAIN_02);
        }
    }

    private void createContexts(File paymentDir, PaymentFormat paymentFormat) {
        for (File paymentFile : paymentDir.listFiles()) {
            PaymentContext paymentContext = createAndGetPaymentContext(paymentFile, paymentFormat);
            paymentContextRepository.save(paymentContext);
            paymentFile.delete();
        }
    }

    private PaymentContext createAndGetPaymentContext(File paymentFile, PaymentFormat paymentFormat) {
        PaymentContext paymentContext = new PaymentContext();
        paymentContext.setCreatedAt(new Date());
        paymentContext.setState(State.DECODE);
        paymentContext.setPaymentFormat(paymentFormat);
        try {
            paymentContext.setResource(IOUtils.toString(new FileInputStream(paymentFile)));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.toString());
        }

        return paymentContext;
    }

    private static String getFtpDirPath() {
        Path projectRelativePath = Paths.get("");
        File ftpDir = new File(projectRelativePath.toAbsolutePath().toString() + File.separator + "ftp");

        return ftpDir.getPath();
    }

}
