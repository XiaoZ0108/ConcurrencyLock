package com.example.ConcurrencyLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class myController {

    @Autowired
    private MyService myService;

//    @GetMapping("/withdraw")
//    public Account withdraw(){
//    return  myService.withdraw();
//    }

    //optimistic lock ,if version not match will throw error
    @GetMapping("/deposit/{amount}")
    public Account deposit(@PathVariable double amount){
        return myService.deposit(amount);
    }
    //pessimistic lock
    @GetMapping("/deposit2/{amount}")
    public Account deposit2(@PathVariable double amount){
        return myService.deposit2(amount);
    }

    @GetMapping()
    public List<Account> check(){
        return myService.getAccounts();
    }



}
