package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
public class BasicTxTest {
    @Autowired
    PlatformTransactionManager txManger;

    @TestConfiguration
    static class Config {
        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Test
    void commit() {
        log.info("트랜잭션 시작");
        TransactionStatus status = txManger.getTransaction(new DefaultTransactionDefinition());

        log.info("트랜잭션 커밋 시점");
        txManger.commit(status);
        log.info("트랜잭션 커밋 완료");
    }

    @Test
    void rollback() {
        log.info("트랜잭션 시작");
        TransactionStatus status = txManger.getTransaction(new DefaultTransactionDefinition());

        log.info("트랜잭션 롤백 시점");
        txManger.rollback(status);
        log.info("트랜잭션 롤백 완료");
    }

    @Test
    void double_commit() {
        log.info("트랜잭션 시작");
        TransactionStatus tx1 = txManger.getTransaction(new DefaultTransactionDefinition());

        log.info("트랜잭션1 커밋");
        txManger.commit(tx1);

        log.info("트랜잭션2 커밋");
        TransactionStatus tx2 = txManger.getTransaction(new DefaultTransactionDefinition());
        txManger.commit(tx2);
        log.info("트랜잭션 커밋 완료");
    }

    @Test
    void double_commit_rollback() {
        log.info("트랜잭션1 시작");
        TransactionStatus tx1 = txManger.getTransaction(new DefaultTransactionDefinition());
        log.info("트랜잭션1 커밋");
        txManger.commit(tx1);

        log.info("트랜잭션2 시작");
        TransactionStatus tx2 = txManger.getTransaction(new DefaultTransactionDefinition());
        log.info("트랜잭션 롤백");
        txManger.rollback(tx2);

    }

    @Test
    void inner_commit(){
        log.info("외부 트랜잭션");
        TransactionStatus outer = txManger.getTransaction(new DefaultTransactionDefinition());
        log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

        log.info("내부트랜잭션");
        TransactionStatus inner = txManger.getTransaction(new DefaultTransactionDefinition());
        log.info("inner.isNewTransaction()={}", inner.isNewTransaction());
        log.info("내부 트랜잭션 커밋");
        txManger.commit(inner);

        log.info("외부 트랜잭션 커밋");
        txManger.commit(outer);
    }

    @Test
    void outer_rollback(){
        log.info("외부 트랜잭션");
        TransactionStatus outer = txManger.getTransaction(new DefaultTransactionAttribute());

        log.info("내부트랜잭션");
        TransactionStatus inner = txManger.getTransaction(new DefaultTransactionDefinition());
        log.info("내부 트랜잭션 커밋");
        txManger.commit(inner);

        log.info("외부 트랜잭션 롤백");
        txManger.rollback(outer);
    }

    @Test
    void inner_rollback() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManger.getTransaction(new DefaultTransactionAttribute());
        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = txManger.getTransaction(new DefaultTransactionAttribute());
        log.info("내부 트랜잭션 롤백");
        txManger.rollback(inner);
        log.info("외부 트랜잭션 커밋");
        assertThatThrownBy(() -> txManger.commit(outer))
                .isInstanceOf(UnexpectedRollbackException.class);
    }

    @Test
    void inner_rollback_requires_new(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManger.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outer.isNewTransaction());
        log.info("내부 트랜잭션 시작");
        DefaultTransactionAttribute definition = new DefaultTransactionAttribute();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus inner = txManger.getTransaction(definition);
        log.info("inner.isNewTransaction()={}", inner.isNewTransaction());
        log.info("내부 트랜잭션 롤백");
        txManger.rollback(inner); //롤백
        log.info("외부 트랜잭션 커밋");
        txManger.commit(outer); //커밋
    }
}
