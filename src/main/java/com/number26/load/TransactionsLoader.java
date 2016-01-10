package com.number26.load;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.number26.transactions.Transaction;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This class is responsible for storing transactions and load them.
 */
public class TransactionsLoader {


    private Map<Long, Transaction> transactionMap = new HashMap<>();
    private Map<Long, Double> sumMap = new HashMap<>();
    private Map<String, List<Long>> typeMap = new HashMap<>();


    private TransactionsLoader() {
        //simple load transaction from a file
        URL url = Resources.getResource("transactions");
        try {
            String text = Resources.toString(url, Charsets.UTF_8);
            Scanner scanner = new Scanner(text);
            while (scanner.hasNextLine()) {
                Long transactionId = scanner.nextLong();
                Transaction transaction = Transaction.newBuilder().amount(scanner.nextDouble()).type(scanner.next()).parentId(scanner.nextLong()).build();
                putNewTransaction(transactionId, transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //load transactionMap manually
        putNewTransaction(Long.valueOf(4), Transaction.newBuilder().amount(3.4).type("fast").parentId(Long.valueOf(5)).build());
        putNewTransaction(Long.valueOf(5), Transaction.newBuilder().amount(3.4).type("fast").parentId(Long.valueOf(6)).build());
        putNewTransaction(Long.valueOf(6), Transaction.newBuilder().amount(3.4).type("fast").build());

    }

    public static TransactionsLoader getInstance() {
        return SingletonHolder.instance;
    }


    /**
     * This method is responsible for load of a new transaction.
     *
     * @param transactionId
     * @param newTransaction
     */
    public void putNewTransaction(Long transactionId, Transaction newTransaction) {

        //put to transactionsMap - complexity O(1)
        transactionMap.put(transactionId, newTransaction);

        //put to typesMap - complexity O(1)
        putToTypeMap(transactionId, newTransaction);

        //put to sumMap - complexity O(p), where p is the longest path in the transactions graph
        putToSumMap(transactionId, newTransaction);
    }

    private void putToTypeMap(Long transactionId, Transaction newTransaction) {
        if (typeMap.containsKey(newTransaction.getType())) {
            typeMap.get(newTransaction.getType()).add(transactionId);
        } else {
            List<Long> transactions = new LinkedList<>();
            transactions.add(transactionId);
            typeMap.put(newTransaction.getType(), transactions);
        }
    }

    private void putToSumMap(Long transactionId, Transaction newTransaction) {
        if (sumMap.containsKey(transactionId)) {
            sumMap.put(transactionId, sumMap.get(transactionId) + newTransaction.getAmount());
        } else {
            sumMap.put(transactionId, newTransaction.getAmount());
        }
        Long parentId = newTransaction.getParentId();
        while (parentId != null) {
            if (sumMap.containsKey(parentId)) {
                sumMap.put(parentId, sumMap.get(parentId)
                        + newTransaction.getAmount());
            } else {
                sumMap.put(parentId, newTransaction.getAmount());
            }
            if (transactionMap.containsKey(parentId)) {
                parentId = transactionMap.get(parentId).getParentId();
            } else {
                parentId = null;
            }

        }
    }

    public void clearMaps() {
        transactionMap.clear();
        sumMap.clear();
        typeMap.clear();
    }

    public Map<Long, Transaction> getTransactionMap() {
        return transactionMap;
    }

    public Map<Long, Double> getSumMap() {
        return sumMap;
    }

    public Map<String, List<Long>> getTypeMap() {
        return typeMap;
    }

    private static class SingletonHolder {
        private final static TransactionsLoader instance = new TransactionsLoader();
    }


}
