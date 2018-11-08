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


import com.finsTcp.omron.*;
import com.api.util.*;

@Path("car")
@Produces(MediaType.APPLICATION_JSON)
public class CarInfoResource {

    private OmronFinsNet cfn_client;
    private SocketPool sp;

    //protected void getConnection(){
    //    this.sp = SocketPool.getInstance();
    //    this.sp.init(true);
    //    this.cfn_client =  this.sp.getClient();

    //}
    //public MessageController(){
    //    this.getConnection();

    //}

    private GsonBuilder builder = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(CarInfo.class, new CarInfoSerializer());
    private Gson gson = builder.create();
    
    private CarInfoDao carInfoDao = new CarInfoDao();

    @GET
    public String list() {
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
