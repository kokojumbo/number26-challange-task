package com.number26.load;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.number26.transactions.Transaction;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by kmg on 2016-01-01.
 */
public class TransactionsLoader {


    private List<Transaction> transactions;

    private TransactionsLoader() {
        //simple load transactions from a file
        URL url = Resources.getResource("transactions");
        try {
            String text = Resources.toString(url, Charsets.UTF_8);
            Scanner scanner = new Scanner(text);
            transactions = new ArrayList<>();
            while (scanner.hasNextLine()) {
                Transaction transaction = Transaction.newBuilder().transactionId(scanner.nextLong()).amount(scanner.nextDouble()).type(scanner.next()).parentId(scanner.nextLong()).build();
                System.out.println(transaction.toString());
                transactions.add(transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //load transactions manually
        transactions.add(Transaction.newBuilder().transactionId(4).amount(3.4).type("fast").parentId((long) 5).build());
        transactions.add(Transaction.newBuilder().transactionId(5).amount(3.4).type("fast").parentId((long) 6).build());
        transactions.add(Transaction.newBuilder().transactionId(6).amount(3.4).type("fast").build());


    }

    public static TransactionsLoader getInstance() {
        return SingletonHolder.instance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    private static class SingletonHolder {
        private final static TransactionsLoader instance = new TransactionsLoader();
    }

}
