package com.example.demo.transactions;

import com.example.demo.transactions.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;
import static org.hamcrest.MatcherAssert.*;

@SpringBootTest
@Transactional // Roll back the transaction in database after each test
//@Commit  // Commit the transaction in database after each test
public class AccountServiceTest {
    private static Logger log = Logger.getLogger("AccountServiceTest");
    @Autowired
    private AccountService service;
    @Test
    public void testDeposit() {
        BigDecimal start = service.getBalance(1L);
        BigDecimal amount = new BigDecimal("50.0");
        service.deposit(1L, amount);
        BigDecimal finish = start.add(amount);

        assertThat(finish, is(closeTo(service.getBalance(1L),
                new BigDecimal("0.01"))));
        assertThat(finish, is(
                closeTo(new BigDecimal(150), new BigDecimal("0.0"))));
    }

    @Test
    public void testWithdraw() {
        BigDecimal start = service.getBalance(1L);
        BigDecimal amount = new BigDecimal("50.0");
        service.withdraw(1L, amount);
        BigDecimal finish = start.subtract(amount);
        assertThat(finish, is(closeTo(service.getBalance(1L),
                new BigDecimal("0.01"))));
    }

    @Test
    public void testTransfer() {
        BigDecimal acct1start = service.getBalance(1L);
        BigDecimal acct2start = service.getBalance(2L);

        BigDecimal amount = new BigDecimal("50.0");
        service.transfer(1L, 2L, amount);

        BigDecimal acct1finish = acct1start.subtract(amount);
        BigDecimal acct2finish = acct2start.add(amount);

        assertThat(acct1finish, is(closeTo(service.getBalance(1L),
                new BigDecimal("0.01"))));
        assertThat(acct2finish, is(closeTo(service.getBalance(2L),
                new BigDecimal("0.01"))));
    }

  /*  @BeforeTransaction
    public void beforeTransactionBegin() {
        log.info("Transaction is starting ....");
        BigDecimal start = service.getBalance(1L);
        assertThat(start, is(
                closeTo(new BigDecimal(100), new BigDecimal("0.0"))));
    }

    @AfterTransaction
    public void afterTransactionEnd() {
        log.info("Transaction is ending ....");
        BigDecimal end = service.getBalance(1L);
        assertThat(end, is(
                closeTo(new BigDecimal(150), new BigDecimal("0.0"))));
    }

    @Rollback
    @Test
    public void thisTestWillRollBackDatabaseAfterTransaction() {
        log.info("thisTestWillRollBackDatabaseAfterTransaction");
        BigDecimal end = service.getBalance(1L);
        assertThat(end, is(
                closeTo(new BigDecimal(150), new BigDecimal("0.0"))));
    }*/
}