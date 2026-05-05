package br.com.zenon.repositories;

import br.com.zenon.configs.MySQLDatabaseFactory;
import br.com.zenon.exceptions.DatabaseConstraintsException;
import br.com.zenon.exceptions.ErrorDetail;
import br.com.zenon.fraud.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionMySQLRepository implements TransactionRepository {

    @Override
    public Optional<Transaction> findByNameOrig(String nameOrig) {
        return Optional.empty();
    }

    @Override
    public List<Transaction> findAll() {
        final String SQL = "SELECT * FROM transactions";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = MySQLDatabaseFactory.getInstance().getConnection();
             PreparedStatement query = connection.prepareStatement(SQL);
             ResultSet resultSet = query.executeQuery()
        ) {

            while (resultSet.next()) {
                resultSet.getInt("id");
            }

        } catch (Exception e) {
            throw DatabaseConstraintsException.with(ErrorDetail.of("find-all-transactions", e.getMessage(), this.getClass()));
        }

        return transactions;
    }

    @Override
    public Optional<Transaction> findLast() {
        return Optional.empty();
    }

    @Override
    public Optional<Transaction> findFirst() {
        return Optional.empty();
    }
}
