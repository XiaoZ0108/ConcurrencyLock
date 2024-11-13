package com.example.ConcurrencyLock;

import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    private String id;

    private double balance;

    //for optimistic lock
//    @Version
//    private Integer version;

    public Account(String id, double balance) {
        this.id = id;
        this.balance = balance;

    }

    public Account() {

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

//    public Integer getVersion() {
//        return version;
//    }
//
//    public void setVersion(Integer version) {
//        this.version = version;
//    }
}
