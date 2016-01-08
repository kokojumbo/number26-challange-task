package com.number26;

import com.number26.load.TransactionsLoader;
import com.number26.transactions.Transaction;
import org.glassfish.grizzly.http.server.HttpServer;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TypesServiceTest {

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
     * test getting no ids for a unknown type
     */
    @Test
    public void testNoExistingType() throws JSONException {
        //given
        transactions.put(Long.valueOf(14), Transaction.newBuilder().amount(2.2).type("test").build());
        //when
        String response = target.path("services/types/notypetest").request().get(String.class);
        //then
        assertEquals("[]", response);
    }


    /**
     * test getting transactions ids for different types
     */
    @Test
    public void testExistingTypes() throws JSONException {
        //given
        transactions.put(Long.valueOf(11), Transaction.newBuilder().type("type1").build());
        transactions.put(Long.valueOf(12), Transaction.newBuilder().type("type2").build());
        transactions.put(Long.valueOf(13), Transaction.newBuilder().type("type3").build());
        transactions.put(Long.valueOf(14), Transaction.newBuilder().type("type2").build());
        transactions.put(Long.valueOf(15), Transaction.newBuilder().type("type2").build());
        //when
        JSONArray jsonArrayType1 = new JSONArray(target.path("services/types/type1").request().get(String.class));
        JSONArray jsonArrayType2 = new JSONArray(target.path("services/types/type2").request().get(String.class));
        JSONArray jsonArrayType3 = new JSONArray(target.path("services/types/type3").request().get(String.class));


        //then
        assertEquals(1, jsonArrayType1.length());
        assertEquals(3, jsonArrayType2.length());
        assertEquals(1, jsonArrayType3.length());

        List<Integer> listType2 = new ArrayList<>();
        for (int i = 0; i < jsonArrayType2.length(); i++) {
            listType2.add((Integer) jsonArrayType2.get(i));
        }
        assertTrue(listType2.contains(12));
        assertTrue(listType2.contains(14));
        assertTrue(listType2.contains(15));
    }

}
