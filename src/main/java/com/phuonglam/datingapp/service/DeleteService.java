/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phuonglam.datingapp.service;

/**
 *
 * @author Phuo
 */
import com.phuonglam.database.Database;
import com.phuonglam.helper.AuthenticationHelper;
import com.phuonglam.helper.ConstantHelper;
import com.phuonglam.pojo.User;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Phuo
 */
@Path("/deleteservice")
public class DeleteService {

    private AuthenticationHelper authHelper;

    public DeleteService() {
        authHelper = new AuthenticationHelper();
    }

    @DELETE
    @Path("/deletepicture/{userId}/{pictureId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveBook(@PathParam("userId") int userId, @PathParam("pictureId") int pictureId, @Context HttpHeaders httpHeaders) {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        if (authHelper.CheckGetUserFriend(authCredentials, userId)){
            Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
            if (db.DeletePicture(pictureId)){
                return Response.status(Response.Status.OK).entity("{\"message\": \"Successful\"}").build();
            } else {
                return Response.status(Response.Status.OK).entity("{\"message\": \"Failed\"}").build();
            }
        } else {
            return Response.status(Response.Status.OK).entity("{\"message\": \"You are not allowed to set friend status of this two users\"}").build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/setfriendstatus/{user1Id}/{user2Id}/{friendStatus}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setFriendStatus(@Context HttpHeaders httpHeaders, @PathParam("user1Id") int user1Id, @PathParam("user2Id") int user2Id, @PathParam("friendStatus") int friendStatus) {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
        int friendId = db.GetMaxFriendId() + 1;
        db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
        
        int userId=authHelper.CheckUser(authCredentials);
        if (userId == user1Id || userId == user2Id) {
            if (db.EditFriendStatus(user1Id, user2Id, friendStatus, friendId)) {
                return Response.status(Response.Status.OK).entity("{\"message\": \"Successful\"}").build();
            } else {
                return Response.status(Response.Status.OK).entity("{\"message\": \"Failed\"}").build();
            }
        } else {
            return Response.status(Response.Status.OK).entity("{\"message\": \"You are not allowed to set friend status of this two users\"}").build();
        }
    }
}