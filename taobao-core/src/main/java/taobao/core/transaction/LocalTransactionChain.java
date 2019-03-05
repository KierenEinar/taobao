package taobao.core.transaction;

import com.google.common.collect.Lists;

import java.util.List;

public class LocalTransactionChain implements LocalTransaction {

    private List<LocalTransaction> localTransactions = Lists.newArrayList();

    private LocalTransactionContext localTransactionContext = new LocalTransactionContext();

    public void add (LocalTransaction localTransaction) {
        localTransactions.add(localTransaction);
    }

    public void remove (LocalTransaction localTransaction) {
        localTransactions.remove(localTransaction);
    }

    public LocalTransactionContext getLocalTransactionContext() {
        return localTransactionContext;
    }

    public void setLocalTransactionContext(LocalTransactionContext localTransactionContext) {
        this.localTransactionContext = localTransactionContext;
    }

    @Override
    public void submit(LocalTransactionChain localTransactionChain) {
        for (LocalTransaction localTransaction : localTransactions) {

        }
    }

    @Override
    public void rollback(LocalTransactionChain localTransactionChain) {

    }
}
