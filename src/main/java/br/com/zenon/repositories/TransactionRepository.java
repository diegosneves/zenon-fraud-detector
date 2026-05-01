package br.com.zenon.repositories;

import br.com.zenon.fraud.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> findByNameOrig(String nameOrig);

    List<Transaction> findAll();

    Optional<Transaction> findLast();

    Optional<Transaction> findFirst();

}
