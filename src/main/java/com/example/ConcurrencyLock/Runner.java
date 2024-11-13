package com.example.ConcurrencyLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private MyAccountRepo myAccountRepo;

    @Override
    public void run(String... args) throws Exception {
        Optional<Account> existingAccount = myAccountRepo.findById("1");

        if (existingAccount.isEmpty()) {
            // Only insert if it doesn't already exist
            myAccountRepo.save(new Account("1", 1000.0));
        } else {
            Account acc=existingAccount.get();
            acc.setBalance(1000);
            myAccountRepo.save(acc);
            System.out.println("Initiated");
        }
    }
}

