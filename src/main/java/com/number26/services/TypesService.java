package com.number26.services;


import com.number26.load.TransactionsLoader;
import com.number26.transactions.Transaction;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/services/types")
public class TypesService {
    @GET
    @Path("/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Long> getTransactionByType(@PathParam("type") String type) {
        Map<Long, Transaction> transactions = TransactionsLoader.getInstance().getTransactions();
        return transactions.entrySet().stream().filter(t -> type.equals(t.getValue().getType())).map(t -> t.getKey()).collect(Collectors.toList());
    }
}






