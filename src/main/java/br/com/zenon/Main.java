package br.com.zenon;


import br.com.zenon.enums.LocaleType;
import br.com.zenon.exceptions.DomainException;
import br.com.zenon.exceptions.ErrorDetail;
import br.com.zenon.exceptions.TransactionIngestorConstraintsException;
import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionIngestor;
import br.com.zenon.fraud.TransactionReport;
import br.com.zenon.repositories.TransactionListRepository;
import br.com.zenon.repositories.TransactionMapRepository;
import br.com.zenon.repositories.TransactionRepository;
import br.com.zenon.validations.handlers.NotificationHandler;
import br.com.zenon.validations.handlers.ValidationHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main {

    void main(String[] args) {

        final String locale = args.length > 0 ? args[0] : "PT_BR";
        final LocaleType localeType = LocaleType.valueOf(locale.toUpperCase());

        final var displayLimit = 1;
        final var listSize = 100_000;
        final var fileMap = Map.of(
                1, "data/PS_20174392719_1491204439457_log.csv",
                2, "data/wrong_file.csv"
        );


//        final var loadedTransactions = recoveryTransactionByFile(fileMap.get(1), null, displayLimit, true);
//        final var fraudAnalysis = FraudAnalyzer.of(loadedTransactions, Transaction::isFraud);
//        fraudAnalysis.printFraudAnalysis();

//        benchmarkListVsMap(loadedTransactions);

        final var report = TransactionReport.of(fileMap.get(1), localeType);
        report.generateReport();

    }

    /**
     * <ol>
     *     <li>Last de 100_000 - (List) Tempo de execução: 20 ms</li>
     *     <li>Last de 100_000 - (Map) Tempo de execução: 15 ms</li>
     *     <li>Last de 1_000_000 - (List) Tempo de execução: 37 ms </li>
     *     <li>Last de 1_000_000 - (Map) Tempo de execução: 14 ms</li>
     *     <li>Last de +6_000_000 - (List) Tempo de execução: 169 ms </li>
     *     <li>Last de +6_000_000 - (Map) Tempo de execução: 12 ms</li>
     *     <li>Inexistente +6_000_000  - (List) Tempo de execução: 139 ms</li>
     *     <li>Inexistente +6_000_000  - (Map) Tempo de execução: 1 ms</li>
     * </ol>
     *
     * @param loadedTransactions
     */
    private static void benchmarkListVsMap(List<Transaction> loadedTransactions) {
        final String last100_000 = "C1868032458";
        final String last1_000_000 = "C1142299632";
        final String notFoundName = "Cinvalido";

        aquecerJvm(loadedTransactions);

        IO.println("Iniciando benchmark...");

        IO.println("\nBenchmark - List | Nome na posição 100.000");
        findTransactionByOrigNameWithList(loadedTransactions, last100_000);
        IO.println("*".repeat(80));
        IO.println("\nBenchmark - List | Nome na posição 1.000.000");
        findTransactionByOrigNameWithList(loadedTransactions, last1_000_000);
        IO.println("*".repeat(80));
        IO.println("\nBenchmark - List | Nome não encontrado");
        findTransactionByOrigNameWithList(loadedTransactions, notFoundName);
        IO.println("*".repeat(80));

        IO.println("=".repeat(80));

        IO.println("\nBenchmark - Map | Nome na posição 100.000");
        findTransactionByOrigNameWithMap(loadedTransactions, last100_000);
        IO.println("*".repeat(80));
        IO.println("\nBenchmark - Map | Nome na posição 1.000.000");
        findTransactionByOrigNameWithMap(loadedTransactions, last1_000_000);
        IO.println("*".repeat(80));
        IO.println("\nBenchmark - Map | Nome não encontrado");
        findTransactionByOrigNameWithMap(loadedTransactions, notFoundName);
        IO.println("*".repeat(80));


    }

    private static void aquecerJvm(List<Transaction> loadedTransactions) {
        IO.println("Aquecendo JVM...");
        for (int i = 0; i < 10; i++) {

            findTransactionByOrigNameWithList(loadedTransactions, "C1142299632");

        }
    }


    private static void findTransactionByOrigNameWithList(final List<Transaction> transactions, final String nameOrig) {
        TransactionRepository repository = null;
        repository = TransactionListRepository.create(transactions);
        printTransaction(repository, nameOrig);
    }

    private static void findTransactionByOrigNameWithMap(final List<Transaction> transactions, final String nameOrig) {
        TransactionRepository repository = null;
        repository = TransactionMapRepository.create(transactions);
        printTransaction(repository, nameOrig);
    }


    private static void printTransaction(TransactionRepository repository, String nameOrig) {
        Long ini, fim;
        ini = System.nanoTime();
        repository.findByNameOrig(nameOrig)
                .ifPresentOrElse(
                        IO::println,
                        () -> {
                            IO.println("Transacao nao encontrada para o cliente: %s".formatted(nameOrig));
                        }
                );
        fim = System.nanoTime();
        IO.println("Size: %,d".formatted(repository.findAll().size()));
        IO.println();
        IO.println("Tempo de execução: " + TimeUnit.NANOSECONDS.toMillis(fim - ini) + " ms");
        IO.println("Tempo de execução: " + (fim - ini) / 1_000_000.0 + " ms");
    }


    private static List<Transaction> recoveryTransactionByFile(
            final String fileName,
            final Integer listSize,
            final int displayLimit
    ) {
        return recoveryTransactionByFile(fileName, listSize, displayLimit, true);
    }

    private static List<Transaction> recoveryTransactionByFile(
            final String fileName,
            final Integer listSize,
            final int displayLimit,
            final boolean errorLog
    ) {
        try {
            ValidationHandler notificationProcessor = NotificationHandler.create();
            final var ingester = TransactionIngestor.create(notificationProcessor, fileName, listSize);

            List<Transaction> transactionRecords = ingester.getTransactions();

            if (displayLimit > 0) {
                transactionRecords.stream().limit(displayLimit).forEach(IO::println);
                displayResult(ingester, transactionRecords, displayLimit, notificationProcessor, errorLog);
            } else {
                displayResult(ingester, transactionRecords, 0, notificationProcessor, false);
            }
            return transactionRecords;
        } catch (final DomainException e) {
            IO.println(e.getLocalizedMessage());
            e.getErrors().forEach(IO::println);
            throw TransactionIngestorConstraintsException.with(ErrorDetail.of("File", "Invalid file format", Main.class));
        }
    }

    private static void displayResult(
            final TransactionIngestor ingester,
            final List<Transaction> transactionRecords,
            final int displayLimit,
            final ValidationHandler notificationProcessor,
            final boolean errorLog
    ) {
        IO.println("""
                %n[%,03d] - Total of fraud transactions processed with success
                [%,03d] - Total found transactions
                [%,03d] - Total displayed transactions
                [%,03d] - Errors found
                [%,02d] - Load time (ms)
                """.formatted(
                ingester.getTransactions().size(),
                transactionRecords.size(),
                Math.min(transactionRecords.size(), displayLimit),
                notificationProcessor.getErrors().size(),
                ingester.getLoadTime()
        ));

        if (notificationProcessor.hasErrors() && errorLog) {
            List<ErrorDetail> processorErrors = notificationProcessor.getErrors();
            if (!processorErrors.isEmpty()) {
                IO.println("%n[%s] - Errors found during processing:".formatted(processorErrors.size()));
                processorErrors.stream().limit(displayLimit).forEach(IO::println);
            }
        }
    }
}
