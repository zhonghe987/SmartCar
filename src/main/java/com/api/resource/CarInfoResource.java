package com.api.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.api.dao.CarInfoDao;
import com.api.model.CarInfo;
import com.api.serializer.CarInfoSerializer;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import com.FinsTCP.omron.OmronFinsNet;
import com.api.util.*;

import com.FinsTCP.SocketConfig;
import com.FinsTCP.SocketPoolConfig;
import com.FinsTCP.SocketPool;

@Path("car")
@Produces(MediaType.APPLICATION_JSON)
public class CarInfoResource {
    private static volatile SocketPool clientPool = null;
    private static SocketConfig socketConfig = new SocketConfig();
    static {
        try {
            SocketPoolConfig SocketPoolConfig = new SocketPoolConfig();
            clientPool = new SocketPool(SocketPoolConfig, socketConfig);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private GsonBuilder builder = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(CarInfo.class, new CarInfoSerializer());
    private Gson gson = builder.create();
    
    private CarInfoDao carInfoDao = new CarInfoDao();

    @GET
    public String list() {
        OmronFinsNet omf = clientPool.getResource();
        return gson.toJson(carInfoDao.findAll());
    }
    
    @GET
    @Path("/carid={cid}")
    public Response get(@PathParam("cid") String cid) {
        CarInfo carinfo = carInfoDao.get(cid);

        if (carinfo != null) {
            String response = gson.toJson(carinfo);
            return Response.ok(response).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
	@Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String json) {
        CarInfo carinfo = gson.fromJson(json, CarInfo.class);

        if (carinfo != null && carInfoDao.create(carinfo)) {
            return Response.status(Response.Status.OK).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/update/carid={id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, String json) {
        CarInfo carinfo = gson.fromJson(json, CarInfo.class);
        carinfo.setId(id);

        if (carinfo != null && carInfoDao.update(carinfo)) {
            return Response.status(Response.Status.OK).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @DELETE
    @Path("/delete/carid={id}")
    public Response delete(@PathParam("id") String id) {
        CarInfo carinfo = carInfoDao.get(id);

        if (carinfo != null && carInfoDao.delete(carinfo)) {
            return Response.status(Response.Status.OK).build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}
