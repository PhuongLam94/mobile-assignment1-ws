/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phuonglam.datingapp.service;

import com.phuonglam.database.Database;
import com.phuonglam.helper.AuthenticationHelper;
import com.phuonglam.helper.ConstantHelper;
import com.phuonglam.pojo.Password;
import com.phuonglam.pojo.User;
import javax.ws.rs.Consumes;
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
@Path("/putservice")
public class PutService {

    private AuthenticationHelper authHelper;

    public PutService() {
        authHelper = new AuthenticationHelper();
    }

    @PUT
    @Path("/user/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveBook(User user, @Context HttpHeaders httpHeaders) {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        int id = authHelper.CheckUser(authCredentials);
        System.out.println(id);
        if (authHelper.CheckGetUserFriend(authCredentials, user.getId())) {
            System.out.print("In");
            Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
            if (db.EditUser(user)) {
                return Response.status(Response.Status.OK).entity("{\"message\": \"Successful\"}").build();
            } else {
                return Response.status(Response.Status.OK).entity("{\"message\": \"Failed\"}").build();
            }
        } else {
            return Response.status(Response.Status.OK).entity("{\"message\": \"You are not allowed to edit this user profile\"}").build();
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
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/setPassword")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPassword(@Context HttpHeaders httpHeaders, Password passInfo) {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
        int userId = authHelper.CheckPassword(authCredentials, passInfo.getOldPassword());
        if (userId != -1) {
            if (db.EditPassword(userId, passInfo.getNewPassword())) {
                return Response.status(Response.Status.OK).entity("{\"message\": \"Successful\"}").build();
            } else {
                return Response.status(Response.Status.OK).entity("{\"message\": \"Failed\"}").build();
            }
        } else {
            return Response.status(Response.Status.OK).entity("{\"message\": \"Old password's not right\"}").build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/resetPassword/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(@Context HttpHeaders httpHeaders, @PathParam("userId") int userId) {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
        if (authHelper.CheckAdmin(authCredentials)) {
            if (db.EditPasswordToDefault(userId)) {
                return Response.status(Response.Status.OK).entity("{\"message\": \"Successful\"}").build();
            } else {
                return Response.status(Response.Status.OK).entity("{\"message\": \"Failed\"}").build();
            }
        } else {
            return Response.status(Response.Status.OK).entity("{\"message\": \"You are not allowed to change this user password\"}").build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/setAdmin/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setAdmin(@Context HttpHeaders httpHeaders, @PathParam("userId") int userId) {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
        if (authHelper.CheckAdmin(authCredentials)) {
            if (db.EditStatus(userId, ConstantHelper.ADMINSTATUS)) {
                return Response.status(Response.Status.OK).entity("{\"message\": \"Successful\"}").build();
            } else {
                return Response.status(Response.Status.OK).entity("{\"message\": \"Failed\"}").build();
            }
        } else {
            return Response.status(Response.Status.OK).entity("{\"message\": \"You are not allowed to set this user as admin\"}").build();
        }
    }
}
