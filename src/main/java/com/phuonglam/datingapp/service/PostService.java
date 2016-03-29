/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phuonglam.datingapp.service;

import com.phuonglam.database.Database;
import com.phuonglam.helper.AuthenticationHelper;
import com.phuonglam.helper.ConstantHelper;
import com.phuonglam.pojo.Picture;
import com.phuonglam.pojo.User;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Phuo
 */
@Path("/postservice")
public class PostService {

    private AuthenticationHelper authHelper;

    public PostService() {
        authHelper = new AuthenticationHelper();
    }

    @POST
    @Path("/user/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        System.out.println(user.getTest());
        Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
        user.setId(db.GetMaxId() + 1);
        db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);

        if (db.AddUser(user)) {
            return Response.status(Response.Status.OK).entity("{\"message\": \"Successful\"}").build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).entity("{\"message\": \"Failed\"}").build();

        }
    }

    @POST
    @Path("/picture/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPicture(@Context HttpHeaders httpHeaders, Picture picture) {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
        picture.setId(db.GetMaxPictureId()+1);
        db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
        int userId = authHelper.CheckUser(authCredentials);
        if (userId != -1) {
            if (db.AddPicture(picture, userId)) {
                return Response.status(Response.Status.OK).entity("{\"message\": \"Successful\"}").build();
            } else {
                return Response.status(Response.Status.OK).entity("{\"message\":\"Failed\"}").build();
            }
        } else {
            return Response.status(Response.Status.OK).entity("{\"message\":\"Forbidden\"}").build();
        }
    }
}
