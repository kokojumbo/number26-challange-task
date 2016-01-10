package com.number26.services;


import com.number26.load.TransactionsLoader;
import com.number26.transactions.Transaction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;


@Path("/services/transaction")
public class TransactionService {

    Map<Long, Transaction> transactionMap = TransactionsLoader.getInstance().getTransactionMap();

    @GET
    @Path("/{transaction_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Transaction getTransactionById(@PathParam("transaction_id") Long transactionId) {
        return transactionMap.get(transactionId);
    }

    // PUT method is used to update existing transactionMap, but from task description I assume that I should create new one.
    @PUT
    @Path("/{transaction_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putTransactionById(Transaction newTransaction, @PathParam("transaction_id") Long transactionId) throws Exception {
        if (transactionMap.containsKey(transactionId)) {
            return Response.status(409).build();
        } else {
            TransactionsLoader.getInstance().putNewTransaction(transactionId, newTransaction);
            return Response.status(200).build();
        }
    }


}



