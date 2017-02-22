package com.capco.communicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner loadData(BankRepository repository) {
		return (args) -> {
			// save a couple of banks
			repository.save(new Bank("CRAFT_STAR404", "Star bank a.s."));
			repository.save(new Bank("FROZEN918", "Frozen official a.s"));

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
		};
	}

}
