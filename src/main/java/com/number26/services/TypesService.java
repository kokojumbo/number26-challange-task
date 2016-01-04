package com.number26.services;


import com.number26.load.LoaderTransactions;
import com.number26.transactions.Transaction;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/services/types")
public class TypesService {
    @GET
    @Path("/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Long> getTransactionByType(@PathParam("type") String type) {
        List<Transaction> transactions = LoaderTransactions.getInstance().getTransactions();
        // filter types and get ids
        return transactions.stream().filter(t -> type.equals(t.getType())).map(Transaction::getTransactionId).collect(Collectors.toList());
    }
}






