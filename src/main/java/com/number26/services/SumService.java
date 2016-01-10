package com.number26.services;

import com.number26.load.TransactionsLoader;
import org.json.JSONException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;


@Path("/services/sum")
public class SumService {

    Map<Long, Double> sumMap = TransactionsLoader.getInstance().getSumMap();

    /**
     * return sum of the transaction and their offspring
     */
    @GET
    @Path("/{transaction_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SumResponse getSum(final @PathParam("transaction_id") Long transactionId) throws JSONException {
        return new SumResponse(sumMap.get(transactionId));
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

