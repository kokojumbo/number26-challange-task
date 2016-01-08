package com.number26.services;


import com.number26.load.TransactionsLoader;
import com.number26.transactions.Transaction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;


@Path("/services/transaction")
public class TransactionService {
    @GET
    @Path("/{transaction_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Transaction getTransactionById(@PathParam("transaction_id") Long transactionId) {
        Map<Long, Transaction> transactions = TransactionsLoader.getInstance().getTransactions();
        return transactions.get(transactionId);
    }

    // PUT method is used to update existing transactions, but from task description I assume that I should create new one.
    @PUT
    @Path("/{transaction_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putTransactionById(Transaction incomingTransaction, @PathParam("transaction_id") Long transactionId) throws Exception {
        Map<Long, Transaction> transactions = TransactionsLoader.getInstance().getTransactions();
        if (transactions.get(transactionId) == null) {
            transactions.put(transactionId, incomingTransaction);
            return Response.status(200).build();
        } else
            return Response.status(409).build();


    }
}



