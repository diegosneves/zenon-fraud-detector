package br.com.zenon.repositories;

import br.com.zenon.fraud.Transaction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {

    private final List<Transaction> transactions;

    private TransactionListRepository(final List<Transaction> transactions) {
        Objects.requireNonNull(transactions, "Transactions list cannot be null");
        this.transactions = transactions;
    }

    public static TransactionListRepository create(final List<Transaction> transactions) {
        return new TransactionListRepository(transactions);
    }

    @Override
    public Optional<Transaction> findByNameOrig(final String nameOrig) {
        return this.transactions.stream()
                .filter(transaction -> transaction.origin().name().equals(nameOrig))
                .findFirst();
    }

    @Override
    public List<Transaction> findAll() {
        return this.transactions;
    }

    @Override
    public Optional<Transaction> findLast() {
        return Optional.ofNullable(this.transactions.isEmpty() ? null : this.transactions.getLast());
    }

    @Override
    public Optional<Transaction> findFirst() {
        return this.transactions.stream().findFirst();
    }
}
