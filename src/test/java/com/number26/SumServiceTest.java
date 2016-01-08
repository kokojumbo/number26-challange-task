package com.number26;

import com.number26.load.TransactionsLoader;
import com.number26.transactions.Transaction;
import org.glassfish.grizzly.http.server.HttpServer;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SumServiceTest {

    private HttpServer server;
    private WebTarget target;
    private Map<Long, Transaction> transactions = TransactionsLoader.getInstance().getTransactions();

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client client = ClientBuilder.newClient();
        target = client.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    /**
     * test getting a sum from three connected transactions
     */
    @Test
    public void testSumOfThree() throws JSONException {
        //given
        transactions.put(Long.valueOf(11), Transaction.newBuilder().amount(3.0).type("fast").parentId(Long.valueOf(12)).build());
        transactions.put(Long.valueOf(12), Transaction.newBuilder().amount(3.1).type("fast").parentId(Long.valueOf(13)).build());
        transactions.put(Long.valueOf(13), Transaction.newBuilder().amount(3.2).type("fast").build());
        //when
        JSONObject jsonObj = new JSONObject(target.path("services/sum/11").request().get(String.class));
        //then
        assertEquals(9.3, jsonObj.getDouble("sum"), 0.0);
    }


    /**
     * test getting a sum from one transaction
     */
    @Test
    public void testSumOfOne() throws JSONException {
        //given
        transactions.put(Long.valueOf(14), Transaction.newBuilder().amount(2.2).type("fast").build());
        //when
        JSONObject jsonObj = new JSONObject(target.path("services/sum/14").request().get(String.class));
        //then
        assertEquals(2.2, jsonObj.getDouble("sum"), 0.0);
    }

}
