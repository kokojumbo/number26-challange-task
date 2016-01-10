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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransactionServiceTest {

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
     * test for unknown transactions
     */
    @Test
    public void testNoTransactionFound() throws JSONException {
        //given
        TransactionsLoader.getInstance().putNewTransaction(Long.valueOf(14), Transaction.newBuilder().amount(2.2).type("test").build());
        //when
        String response = target.path("services/transaction/666").request().get(String.class);
        //then
        assertTrue(response.isEmpty());
    }


    /**
     * test getting a transaction by id
     */
    @Test
    public void testGetTransactionById() throws JSONException {

        //given
        Transaction newTransaction = Transaction.newBuilder().amount(2.2).type("test").parentId(Long.valueOf(153)).build();
        TransactionsLoader.getInstance().putNewTransaction(Long.valueOf(543), newTransaction);
        //when
        JSONObject jsonObj = new JSONObject(target.path("services/transaction/543").request().get(String.class));
        //then
        assertEquals(newTransaction.getAmount(), jsonObj.getDouble("amount"), 0.0);
        assertEquals(newTransaction.getType(), jsonObj.getString("type"));
        assertEquals((long) newTransaction.getParentId(), jsonObj.getLong("parentId"));
    }


    /**
     * test put a new transaction
     *
     * @throws JSONException
     */
    @Test
    public void testPutTransactionById() throws JSONException {
        //given
        Transaction newTransaction = Transaction.newBuilder().amount(22.2).type("testNew").parentId(Long.valueOf(123)).build();

        //when
        Response responsePut = target.path("services/transaction/999").request().put(Entity.json(newTransaction), Response.class);
        JSONObject responseGet = new JSONObject(target.path("services/transaction/999").request().get(String.class));
        //then

        assertEquals(200, responsePut.getStatus());

        assertEquals(newTransaction.getAmount(), responseGet.getDouble("amount"), 0.0);
        assertEquals(newTransaction.getType(), responseGet.getString("type"));
        assertEquals((long) newTransaction.getParentId(), responseGet.getLong("parentId"));

    }

    /**
     * test put a new transaction but exists
     *
     * @throws JSONException
     */
    @Test
    public void testPutExistingTransactionById() throws JSONException {
        //given
        Transaction newTransaction = Transaction.newBuilder().amount(22.2).type("testNew").parentId(Long.valueOf(123)).build();

        //when
        Response responsePutA = target.path("services/transaction/876").request().put(Entity.json(newTransaction), Response.class);
        Response responsePutB = target.path("services/transaction/876").request().put(Entity.json(newTransaction), Response.class);

        //then
        assertEquals(200, responsePutA.getStatus());
        assertEquals(409, responsePutB.getStatus());


    }


}
