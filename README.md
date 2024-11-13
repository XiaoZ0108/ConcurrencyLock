# Concurrency Lock Project

## Overview

This project demonstrates handling concurrency in a Spring Boot application using **Optimistic Locking** and **Pessimistic Locking** with JPA and Hibernate. The main goal is to ensure data consistency in a multi-user environment when performing concurrent database operations.

## Key Concepts

### Versioning (Optimistic Locking)
- **Optimistic Locking** uses a `version` field in the database to handle concurrent updates.
- When a record is read, the version number is fetched along with it.
- Upon saving, the version is checked to ensure no other transaction has modified the record since it was read.
- If the version doesn’t match (indicating another transaction updated the record), the operation is rejected, and an exception is thrown.
- This method is efficient when conflicts are rare, as it only checks for conflicts at the time of saving.

### Pessimistic Locking
- **Pessimistic Locking** prevents concurrent access to a record by locking it for the duration of the transaction.
- It ensures that only one transaction can update the record at a time, preventing race conditions.
- This method is useful when conflicts are more likely, and you need to ensure exclusive access to the data.

### Optimistic vs. Pessimistic Locking
- **Optimistic Locking** is ideal for scenarios where conflicts are rare, and you want to allow multiple transactions to read and work with the data concurrently.
- **Pessimistic Locking** is better for situations where conflicts are common, and you need to guarantee that only one transaction can modify the data at any given time.

### Transactions
- **Transactions** group multiple operations together to ensure atomicity, meaning either all operations succeed or none at all.
- If a transaction fails (due to an exception or conflict), it is rolled back, discarding all changes made during the transaction.
- In this project, the `@Transactional` annotation ensures that all actions, such as updating an account balance, are part of a single transaction. If any part fails, the transaction is rolled back.
## Pessimistic Locking in the Deposit Method

In the following `deposit2` method, **Pessimistic Locking** is used to ensure that the account being updated is locked until the transaction is complete. This prevents other transactions from accessing or modifying the same account simultaneously, which helps to avoid **data inconsistency**:

@Transactional
public Account deposit2(double amount) {
    Optional<Account> acc = myAccountRepo.findByIdWithLock("1");

    if (acc.isPresent()) {
        Account account = acc.get();
        account.setBalance(account.getBalance() + amount);
        try {
            Thread.sleep(5000); // Simulate a delay for testing concurrency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        myAccountRepo.save(account);
        return account;
    } else {
        return null;
    }
}

### Key Parts:
1. **`@Transactional`**: Ensures that the entire method is wrapped in a single transaction. If an exception occurs, the transaction is rolled back, ensuring data consistency.
2. **`findByIdWithLock("1")`**: This is a custom repository method using **Pessimistic Locking** to lock the record while it is being processed. No other transaction can modify the account until the lock is released.
3. **`Thread.sleep(5000)`**: Simulates a delay in the transaction, mimicking a real-world scenario where a transaction might take time (e.g., processing, waiting for external services, etc.). This helps to test concurrency and locking mechanisms.
4. **`save(account)`**: Once the account's balance is updated, it is saved back to the repository.

### Pessimistic Locking Repository Query

In the repository, you need to define a query that uses **Pessimistic Locking** to ensure that the account is locked during the transaction:

public interface MyAccountRepo extends JpaRepository<Account, String> {

    // Pessimistic Locking query
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Optional<Account> findByIdWithLock(@Param("id") String id);
}

### Without Locking: Risk of Data Inconsistency

If you don't use locking (either **optimistic** or **pessimistic**), two transactions could attempt to modify the same account balance at the same time. This can lead to **data inconsistency**.

For example, consider two users making a deposit at the same time:

1. **User A** reads the account balance (e.g., $1000).
2. **User B** also reads the account balance (e.g., $1000).
3. **User A** deposits $500 and updates the balance to $1500, but **User B** also deposits $500, overwriting **User A's** deposit, resulting in an incorrect balance of $1500 instead of $2000.

Without locking, both transactions can read the same data and perform operations on it simultaneously, leading to inconsistent or incorrect results.

**Using Pessimistic Locking** prevents this issue by ensuring that only one transaction can modify the account at a time. In this case, **User B** would have to wait for **User A** to complete their transaction before proceeding. 

### Conclusion

- **Pessimistic Locking** ensures that only one transaction can modify the data at a time, preventing concurrent update conflicts.
- **Optimistic Locking** provides a more lightweight approach by only checking for conflicts at the time of saving.
- **Without any locking** (whether pessimistic or optimistic), multiple concurrent transactions can lead to **data inconsistency**, which is a common issue in high-concurrency environments.
