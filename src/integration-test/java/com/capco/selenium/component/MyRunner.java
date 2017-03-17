package com.capco.selenium.component;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * Created by SBRJ on 16. 3. 2017.
 */
public class MyRunner extends BlockJUnit4ClassRunner {
    public MyRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override public void run(RunNotifier notifier){
        notifier.addListener(new JUnitExecutionListener());
        super.run(notifier);
    }
}
