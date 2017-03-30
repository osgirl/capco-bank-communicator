package com.capco.communicator.worker;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractWorker {

    private static final Logger logger = Logger.getLogger(AbstractWorker.class.toString());
    private boolean stopProcessing = false;
    private long stepInMilliseconds = 2000;
    private long interval = 5000;

    public void startJob() {
        while (!this.stopProcessing) {
            try {
                performJob();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.toString());
            }

            long start = System.currentTimeMillis();
            long diff = 0;
            while ((!this.stopProcessing && (diff < interval))) {
                try {
                    Thread.sleep(stepInMilliseconds);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.toString());
                }
                diff = System.currentTimeMillis() - start;
            }
        }
    }

    protected abstract void performJob();

}
