package com.capco.communicator;

import com.capco.communicator.repository.AccountRepository;
import com.capco.communicator.repository.BankRepository;
import com.capco.communicator.repository.PaymentContextRepository;
import com.capco.communicator.schema.Account;
import com.capco.communicator.schema.Bank;
import com.capco.communicator.schema.PaymentContext;
import com.capco.communicator.schema.State;
import com.capco.communicator.worker.FtpWorker;
import com.capco.communicator.worker.PaymentWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private static final Integer NUM_OF_GENERATED_BANKS = 4;
    private static final Integer NUM_OF_GENERATED_ACCOUNTS = 4;
    private static final Integer NUM_OF_GENERATED_PAYMENT_CONTEXTS = 0;

    @Autowired
    private FtpWorker ftpWorker;

    @Autowired
    private PaymentWorker paymentWorker;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public CommandLineRunner loadData(BankRepository banksRepository, AccountRepository accountRepository,
                                      PaymentContextRepository paymentContextRepository) {
        return (args) -> {

            initWorkers();

            initBanks(banksRepository);
            initAccounts(accountRepository);
            initPaymentContexts(paymentContextRepository);
        };
    }

    private void initWorkers() {
        Thread ftpThread = new Thread(() -> ftpWorker.startJob());
        ftpThread.start();

        Thread paymentThread = new Thread(() -> paymentWorker.startJob());
        paymentThread.start();
    }

    private static void initBanks(BankRepository repository) {

        // save a couple of banks
        repository.save(new Bank("CRAFT_STAR404", "Star bank a.s."));
        repository.save(new Bank("FROZEN918", "Frozen official a.s"));

        for(int i = 0; i < NUM_OF_GENERATED_BANKS; i++){
            repository.save(new Bank("BANK" + i + "_code", "Bank " + i + " a.s."));
        }

        // fetch all banks
        log.info("Banks found with findAll():");
        log.info("-------------------------------");
        for (Bank bank : repository.findAll()) {
            log.info(bank.toString());
        }
        log.info("");

        // fetch an individual bank by ID
        Bank fetchedBank = repository.findOne(1L);
        log.info("Bank found with findOne(1L):");
        log.info("--------------------------------");
        log.info(fetchedBank.toString());
        log.info("");

        // fetch banks by code
        log.info("Bank found with findByCodeStartsWithIgnoreCase('CRAFT_STAR404'):");
        log.info("--------------------------------------------");
        for (Bank bank : repository
                .findByCodeStartsWithIgnoreCase("CRAFT_STAR404")) {
            log.info(bank.toString());
        }
        log.info("");
    }

    private static void initAccounts(AccountRepository repository) {
        repository.save(new Account("test", "123456", "Anakin", "Skywalker"));

        for(int i = 0; i < NUM_OF_GENERATED_ACCOUNTS; i++){
            repository.save(new Account("Account_" + i + "_login", "123456",
                    "Account_" + i + "-firstName", "Account_" + i + "-lastName"));
        }
    }

    private static void initPaymentContexts(PaymentContextRepository repository){
        for(int i = 0; i < NUM_OF_GENERATED_PAYMENT_CONTEXTS; i++){
            repository.save(new PaymentContext("Context_" + i + "_resource", State.DONE,
                    new Date(), "Context_" + i + "-channel"));
        }
    }
}
