package br.com.zenon.repositories;

import br.com.zenon.fraud.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository {

    private final Map<String, Transaction> transactions;

    private TransactionMapRepository(final List<Transaction> transactions) {
        Objects.requireNonNull(transactions, "Transactions list cannot be null");
        this.transactions = transactions.stream()
                .collect(
                        Collectors.toMap(
                                transaction -> transaction.origin().name(),
                                Function.identity(),
                                (existing, _) -> existing
                        )
                );
    }

    public static TransactionMapRepository create(final List<Transaction> transactions) {
        return new TransactionMapRepository(transactions);
    }

    @Override
    public Optional<Transaction> findByNameOrig(final String nameOrig) {
        return Optional.ofNullable(transactions.get(nameOrig));
    }

    @Override
    public List<Transaction> findAll() {
        return this.transactions.values().stream().toList();
    }

    @Override
    public Optional<Transaction> findLast() {
        List<Transaction> list = this.transactions.values().stream().toList();
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.getLast());
    }

    @Override
    public Optional<Transaction> findFirst() {
        return this.transactions.values().stream().findFirst();
    }
}
