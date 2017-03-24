package com.capco.communicator;

import com.capco.communicator.repository.AccountRepository;
import com.capco.communicator.repository.BankRepository;
import com.capco.communicator.repository.PaymentContextRepository;
import com.capco.communicator.repository.PaymentRepository;
import com.capco.communicator.schema.*;
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
    private static final Integer NUM_OF_GENERATED_PAYMENTS = 4;
    private static final Integer NUM_OF_GENERATED_PAYMENT_CONTEXTS = 1;

    private static final String TEST_ACCOUNTS_CODE = "DE44-5001-0517-5407-3249-31";
    private static final String TEST_BANK_CODE = "CRAFT_STAR404";

    @Autowired
    private FtpWorker ftpWorker;

    @Autowired
    private PaymentWorker paymentWorker;

    private static BankRepository bankRepository;
    private static AccountRepository accountRepository;
    private static PaymentContextRepository paymentContextRepository;
    private static PaymentRepository paymentRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public CommandLineRunner loadData(BankRepository banksRepository, AccountRepository accountRepository,
                                      PaymentContextRepository paymentContextRepository, PaymentRepository paymentRepository) {

        Application.bankRepository = banksRepository;
        Application.accountRepository = accountRepository;
        Application.paymentContextRepository = paymentContextRepository;
        Application.paymentRepository = paymentRepository;

        return (args) -> {
            initWorkers();

            initBanks();
            initAccounts();
            initPaymentContexts();
            initPayments();
        };
    }

    private void initWorkers() {
        Thread ftpThread = new Thread(() -> ftpWorker.startJob());
        ftpThread.start();

        Thread paymentThread = new Thread(() -> paymentWorker.startJob());
        paymentThread.start();
    }

    private static void initBanks() {

        // save a couple of banks
        bankRepository.save(new Bank(TEST_BANK_CODE, "Star bank a.s."));
        bankRepository.save(new Bank("FROZEN918", "Frozen official a.s"));

        for(int i = 0; i < NUM_OF_GENERATED_BANKS; i++){
            bankRepository.save(new Bank("BANK" + i + "_code", "Bank " + i + " a.s."));
        }

        // fetch all banks
        log.info("Banks found with findAll():");
        log.info("-------------------------------");
        for (Bank bank : bankRepository.findAll()) {
            log.info(bank.toString());
        }
        log.info("");

        // fetch an individual bank by ID
        Bank fetchedBank = bankRepository.findOne(1L);
        log.info("Bank found with findOne(1L):");
        log.info("--------------------------------");
        log.info(fetchedBank.toString());
        log.info("");

        // fetch banks by code
        log.info("Bank found with findByCodeStartsWithIgnoreCase('CRAFT_STAR404'):");
        log.info("--------------------------------------------");
        for (Bank bank : bankRepository
                .findByCodeStartsWithIgnoreCase("CRAFT_STAR404")) {
            log.info(bank.toString());
        }
        log.info("");
    }

    private static void initAccounts() {
        accountRepository.save(new Account(TEST_ACCOUNTS_CODE, "test", "123456", "Anakin", "Skywalker"));

        for(int i = 0; i < NUM_OF_GENERATED_ACCOUNTS; i++){
            accountRepository.save(new Account("Account_" + i + "_CODE", "Account_" + i + "_login", "123456",
                    "Account_" + i + "-firstName", "Account_" + i + "-lastName"));
        }
    }

    private static void initPaymentContexts(){
        for(int i = 0; i < NUM_OF_GENERATED_PAYMENT_CONTEXTS; i++){
            PaymentContext paymentContext = new PaymentContext();
            paymentContext.setResource("Context_" + i + "_resource");
            paymentContext.setState(State.VALIDATE);
            paymentContext.setCreatedAt(new Date());
            paymentContext.setChannel("Context_" + i + "-channel");

            Payment payment = new Payment();
            payment.setBank(bankRepository.findByCode(TEST_BANK_CODE));
            payment.setAccount(accountRepository.findByCode(TEST_ACCOUNTS_CODE));
            payment.setCredit(126);
            payment.setDebit(0);

            paymentContext.setPayment(payment);

            paymentContextRepository.save(paymentContext);
        }
    }

    private static void initPayments(){

        for(int i = 0; i < NUM_OF_GENERATED_PAYMENTS; i++){
            Payment payment = new Payment();
            payment.setBank(bankRepository.findByCode(TEST_BANK_CODE));
            payment.setAccount(accountRepository.findByCode(TEST_ACCOUNTS_CODE));
            payment.setCredit(10*(i+1));
            payment.setDebit((i + 3));

            paymentRepository.save(payment);
        }
    }
}
