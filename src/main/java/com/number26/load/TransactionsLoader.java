package com.number26.load;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.number26.transactions.Transaction;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by kmg on 2016-01-01.
 */
public class TransactionsLoader {


    private Map<Long, Transaction> transactions;

    private TransactionsLoader() {
        //simple load transactions from a file
        URL url = Resources.getResource("transactions");
        try {
            String text = Resources.toString(url, Charsets.UTF_8);
            Scanner scanner = new Scanner(text);
            transactions = new HashMap<>();
            while (scanner.hasNextLine()) {
                Long transactionId = scanner.nextLong();
                Transaction transaction = Transaction.newBuilder().amount(scanner.nextDouble()).type(scanner.next()).parentId(scanner.nextLong()).build();
                System.out.println(transaction.toString());
                transactions.put(transactionId, transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //load transactions manually
        transactions.put(Long.valueOf(4), Transaction.newBuilder().amount(3.4).type("fast").parentId(Long.valueOf(5)).build());
        transactions.put(Long.valueOf(5), Transaction.newBuilder().amount(3.4).type("fast").parentId(Long.valueOf(6)).build());
        transactions.put(Long.valueOf(6), Transaction.newBuilder().amount(3.4).type("fast").build());


    }

    public static TransactionsLoader getInstance() {
        return SingletonHolder.instance;
    }

    public Map<Long, Transaction> getTransactions() {
        return transactions;
    }

    private static class SingletonHolder {
        private final static TransactionsLoader instance = new TransactionsLoader();
    }

}
