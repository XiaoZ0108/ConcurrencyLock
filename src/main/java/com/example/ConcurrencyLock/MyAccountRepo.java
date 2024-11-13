package com.example.ConcurrencyLock;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyAccountRepo extends JpaRepository<Account,String> {

    @Query("SELECT a FROM Account a WHERE a.id=:id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findByIdWithLock(@Param("id") String id);
}
