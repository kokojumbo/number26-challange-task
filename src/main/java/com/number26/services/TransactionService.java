package com.number26.services;


import com.number26.load.LoaderTransactions;
import com.number26.transactions.Transaction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;


@Path("/services/transaction")
public class TransactionService {
    @GET
    @Path("/{transaction_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Transaction getTransactionById(@PathParam("transaction_id") Long transactionId) {
        List<Transaction> transactions = LoaderTransactions.getInstance().getTransactions();
        List<Transaction> filteredTransactions = transactions.stream().filter(t -> t.getTransactionId() == transactionId).collect(Collectors.toList());
        if (filteredTransactions.isEmpty()) {
            return null;
        } else {
            //Id should be unique, so there should be only one element on the list
            return filteredTransactions.get(0);
        }

    }

    // PUT method is used to update existing transactions, but from task description I assume that I should create new one.
    @PUT
    @Path("/{transaction_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putTransactionById(Transaction incomingTransaction, @PathParam("transaction_id") Long transactionId) throws Exception {
        List<Transaction> transactions = LoaderTransactions.getInstance().getTransactions();
        List<Transaction> filteredTransactions = transactions.stream().filter(t -> t.getTransactionId() == transactionId).collect(Collectors.toList());
        if (filteredTransactions.isEmpty()) {
            Transaction newTransaction = new Transaction(transactionId, incomingTransaction.getAmount(), incomingTransaction.getType(), incomingTransaction.getParentId());
            transactions.add(newTransaction);
            return Response.status(200).build();
        } else {
            //I return 409 status when resource exists
            return Response.status(409).build();
        }

    }
}



