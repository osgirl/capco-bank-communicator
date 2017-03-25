package com.capco.communicator;

import com.capco.communicator.repository.AccountRepository;
import com.capco.communicator.repository.BankRepository;
import com.capco.communicator.repository.PaymentContextRepository;
import com.capco.communicator.repository.PaymentRepository;
import com.capco.communicator.schema.*;
import com.capco.communicator.worker.FtpWorker;
import com.capco.communicator.worker.PaymentWorker;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
public class Application {

    private static final Integer NUM_OF_GENERATED_BANKS = 4;
    private static final Integer NUM_OF_GENERATED_ACCOUNTS = 4;
    private static final Integer NUM_OF_GENERATED_PAYMENTS = 4;
    private static final Integer NUM_OF_GENERATED_PAYMENT_CONTEXTS = 4;

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
        bankRepository.save(new Bank("CRAFT_STAR404", "Star bank a.s."));
        bankRepository.save(new Bank("FROZEN918", "Frozen official a.s"));
        bankRepository.save(new Bank("HISTORY_BANK", "History bank"));
        // generate some banks
        for (int i = 0; i < NUM_OF_GENERATED_BANKS; i++) {
            bankRepository.save(new Bank("BANK" + i + "_code", "Bank " + i + " a.s."));
        }
    }

    private static void initAccounts() {
        // save a couple of accounts
        accountRepository.save(new Account("DE44-5001-0517-5407-3249-31", "anakin", "123456", "Anakin", "Skywalker"));
        accountRepository.save(new Account("AU44-888-0517-5407-1111-22", "han", "solo", "Han", "Solo"));
        accountRepository.save(new Account("AU44-000-0000-0000-0000-00", "history", "history", "History", "user"));
        // generate some accounts
        for (int i = 0; i < NUM_OF_GENERATED_ACCOUNTS; i++) {
            accountRepository.save(new Account("Account_" + i + "_CODE", "Account_" + i + "_login", "123456",
                    "Account_" + i + "-firstName", "Account_" + i + "-lastName"));
        }
    }

    private static void initPaymentContexts() {
        for (int i = 0; i < NUM_OF_GENERATED_PAYMENT_CONTEXTS; i++) {
            PaymentContext paymentContext = new PaymentContext();
            paymentContext.setResource("Context_" + i + "_resource");
            paymentContext.setState(State.DONE);
            paymentContext.setCreatedAt(new Date(Math.abs(System.currentTimeMillis() - RandomUtils.nextLong(86400000, 326400000))));
            paymentContext.setChannel("OLD" + i + "-mq-channel");

            Payment payment = new Payment();
            payment.setBank(bankRepository.findByCode("HISTORY_BANK"));
            payment.setAccount(accountRepository.findByCode("AU44-000-0000-0000-0000-00"));
            payment.setCredit(RandomUtils.nextInt(0, 1000));
            payment.setDebit(0);

            paymentContext.setPayment(payment);

            paymentContextRepository.save(paymentContext);
        }
    }

    private static void initPayments() {
        for (int i = 0; i < NUM_OF_GENERATED_PAYMENTS; i++) {
            Payment payment = new Payment();
            payment.setBank(bankRepository.findByCode("HISTORY_BANK"));
            payment.setAccount(accountRepository.findByCode("AU44-000-0000-0000-0000-00"));
            payment.setCredit(10 * (i + 1));
            payment.setDebit((i + 3));

            paymentRepository.save(payment);
        }
    }
}
