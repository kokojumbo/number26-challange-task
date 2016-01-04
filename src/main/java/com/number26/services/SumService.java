package com.number26.services;

import com.number26.load.LoaderTransactions;
import com.number26.transactions.Transaction;
import org.json.JSONException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;


@Path("/services/sum")
public class SumService {

    /**
     * return sum of the transaction and its parent
     */
    @GET
    @Path("/{transaction_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SumResponse getSum(@PathParam("transaction_id") Long transactionId) throws JSONException {
        List<Transaction> transactions = LoaderTransactions.getInstance().getTransactions();
        Transaction transaction = getTransaction(transactionId, transactions);
        //This is not an optimal solution. We should store transactions in different structure for example in the incidence matrix or the neighbour list then we could use faster algorithms
        double sum = transaction.getAmount();
        while (transaction.getParentId() != null) {
            transaction = getTransaction(transaction.getParentId(), transactions);
            sum += transaction.getAmount();
        }
        return new SumResponse(sum);
    }

    private Transaction getTransaction(Long transactionId, List<Transaction> transactions) {
        return transactions.stream().filter(t -> t.getTransactionId() == transactionId).collect(Collectors.toList()).get(0);
    }

    static class SumResponse {

        private double sum;

        public SumResponse(double sum) {
            this.sum = sum;
        }

        public double getSum() {
            return sum;
        }

        public void setSum(double sum) {
            this.sum = sum;
        }
    }


}

