package com.api;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.finsTcp.omron.*;
import com.finsTcp.core.types.*;


@Path("/cars")
public class MessageController {
    @GET
    @Path("/id")
    @Produces(MediaType.APPLICATION_JSON)
    public String getValue(){
        return "{'id':'heee'}";
    }
}