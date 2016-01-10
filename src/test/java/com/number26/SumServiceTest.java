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

import static org.junit.Assert.assertEquals;

public class SumServiceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client client = ClientBuilder.newClient();
        target = client.target(Main.BASE_URI);
        TransactionsLoader.getInstance().clearMaps();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    /**
     * test getting a sum from tree of transactions
     * <p>
     * A
     * / \
     * B   C
     * / \
     * D   E
     * \
     * F
     */
    @Test
    public void testSumOfTree() throws JSONException {
        //given
        TransactionsLoader.getInstance().putNewTransaction(Long.valueOf(11), Transaction.newBuilder().amount(1.0).type("A").build());
        TransactionsLoader.getInstance().putNewTransaction(Long.valueOf(12), Transaction.newBuilder().amount(2.1).type("B").parentId(Long.valueOf(11)).build());
        TransactionsLoader.getInstance().putNewTransaction(Long.valueOf(13), Transaction.newBuilder().amount(3.2).type("C").parentId(Long.valueOf(11)).build());
        TransactionsLoader.getInstance().putNewTransaction(Long.valueOf(14), Transaction.newBuilder().amount(4.3).type("D").parentId(Long.valueOf(12)).build());
        TransactionsLoader.getInstance().putNewTransaction(Long.valueOf(15), Transaction.newBuilder().amount(5.4).type("E").parentId(Long.valueOf(12)).build());
        TransactionsLoader.getInstance().putNewTransaction(Long.valueOf(16), Transaction.newBuilder().amount(6.5).type("F").parentId(Long.valueOf(15)).build());
        //when
        JSONObject jsonObjA = new JSONObject(target.path("services/sum/11").request().get(String.class));
        JSONObject jsonObjB = new JSONObject(target.path("services/sum/12").request().get(String.class));
        JSONObject jsonObjC = new JSONObject(target.path("services/sum/13").request().get(String.class));
        JSONObject jsonObjD = new JSONObject(target.path("services/sum/14").request().get(String.class));
        JSONObject jsonObjE = new JSONObject(target.path("services/sum/15").request().get(String.class));
        JSONObject jsonObjF = new JSONObject(target.path("services/sum/16").request().get(String.class));

        //then
        assertEquals(22.5, jsonObjA.getDouble("sum"), 0.0);
        assertEquals(18.3, jsonObjB.getDouble("sum"), 0.0);
        assertEquals(3.2, jsonObjC.getDouble("sum"), 0.0);
        assertEquals(4.3, jsonObjD.getDouble("sum"), 0.0);
        assertEquals(11.9, jsonObjE.getDouble("sum"), 0.0);
        assertEquals(6.5, jsonObjF.getDouble("sum"), 0.0);
    }


    /**
     * test put a new transaction with no existing parent yet
     * <p>
     * A
     * / \
     * B   C
     */
    @Test
    public void testSumOfTransactionNoExistingParent() throws JSONException {
        //given
        TransactionsLoader.getInstance().putNewTransaction(Long.valueOf(122), Transaction.newBuilder().amount(2.1).type("B").parentId(Long.valueOf(111)).build());
        TransactionsLoader.getInstance().putNewTransaction(Long.valueOf(133), Transaction.newBuilder().amount(3.2).type("C").parentId(Long.valueOf(111)).build());
        TransactionsLoader.getInstance().putNewTransaction(Long.valueOf(111), Transaction.newBuilder().amount(1.0).type("A").build());

        //when
        JSONObject jsonObjA = new JSONObject(target.path("services/sum/111").request().get(String.class));
        JSONObject jsonObjB = new JSONObject(target.path("services/sum/122").request().get(String.class));
        JSONObject jsonObjC = new JSONObject(target.path("services/sum/133").request().get(String.class));

        //then
        assertEquals(6.3, jsonObjA.getDouble("sum"), 0.0000001);
        assertEquals(2.1, jsonObjB.getDouble("sum"), 0.0000001);
        assertEquals(3.2, jsonObjC.getDouble("sum"), 0.0000001);

    }


    /**
     * test getting a sum from one transaction
     */
    @Test
    public void testSumOfOne() throws JSONException {
        //given
        TransactionsLoader.getInstance().putNewTransaction(Long.valueOf(14), Transaction.newBuilder().amount(2.2).type("fast").build());
        //when
        JSONObject jsonObj = new JSONObject(target.path("services/sum/14").request().get(String.class));
        //then
        assertEquals(2.2, jsonObj.getDouble("sum"), 0.0);
    }

}
