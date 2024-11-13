package com.example.ConcurrencyLock;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyService {

    @Autowired
    private MyAccountRepo myAccountRepo;
    public List<Account> getAccounts(){
        return myAccountRepo.findAll();
    }
//    public Account withdraw(){
//        Optional<Account> acc=myAccountRepo.findById("1");
//
//        if(acc.isPresent()){
//            Account account=acc.get();
//            if (account.getBalance() < 100) {
//                throw new RuntimeException("Insufficient balance");
//            }
//             account.setBalance(account.getBalance()-100);
//            myAccountRepo.save(account);
//             return account;
//        }else{
//            return null;
//        }
//
//    }
    //synchronized also can

    //optimistic
    public Account deposit(double amount){
        try {
            Optional<Account> acc = myAccountRepo.findById("1");

            if (acc.isPresent()) {
                Account account = acc.get();
                account.setBalance(account.getBalance() + amount);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return myAccountRepo.save(account);

            } else {
                return null;
            }
        }catch (OptimisticLockException e) {
            // Handle optimistic locking conflict
            throw new RuntimeException("Optimistic locking conflict. Please try again. " + e.getMessage(), e);
        } catch (Exception e) {
            // Handle other exceptions
            throw new RuntimeException("An error occurred during the deposit. Please try again. " + e.getMessage(), e);
        }

    }

    //with lock
    @Transactional
    public Account deposit2(double amount){
        Optional<Account> acc=myAccountRepo.findByIdWithLock("1");

        if(acc.isPresent()){
            Account account=acc.get();
            account.setBalance(account.getBalance()+amount);
            try { Thread.sleep(5000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            myAccountRepo.save(account);
            return account;
        }else{
            return null;
        }

    }

}
