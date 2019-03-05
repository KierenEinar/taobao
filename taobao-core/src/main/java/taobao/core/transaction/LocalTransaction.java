package taobao.core.transaction;

public interface LocalTransaction {
    void submit(LocalTransactionChain localTransactionChain);
    void rollback(LocalTransactionChain localTransactionChain);
}
