package com.number26.services;


import com.number26.load.TransactionsLoader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/services/types")
public class TypesService {
    Map<String, List<Long>> typeMap = TransactionsLoader.getInstance().getTypeMap();

    @GET
    @Path("/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Long> getTransactionByType(@PathParam("type") String type) {
        return typeMap.get(type);
    }
}






