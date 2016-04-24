/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phuonglam.datingapp.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.phuonglam.helper.AuthenticationHelper;
import com.phuonglam.helper.ConstantHelper;
import com.phuonglam.database.Database;
import com.phuonglam.pojo.Picture;
import com.phuonglam.pojo.User;
import java.text.ParseException;
import java.util.List;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;

/**
 *
 * @author Phuo
 */
@Path("/getservice")
public class GetService {

    private AuthenticationHelper authHelper;

    public GetService() {
        authHelper = new AuthenticationHelper();
    }

    @GET
    @Path("/checkuser/{longi}/{latti}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkUser(@Context HttpHeaders httpHeaders, @PathParam("longi") float longi, @PathParam("latti") float latti) {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        int res = authHelper.CheckUser(authCredentials, longi, latti);
        if (res == -1) {
            return Response.status(Response.Status.OK).entity("{\"message\": \"User name or password is wrong\"}").build();
        } else {
            return Response.status(Response.Status.OK).entity("{\"message\": \"Successful\", \"userid\":" + res + "}").build();
        }
    }

    @GET
    @Path("/checkadmin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAdmin(@Context HttpHeaders httpHeaders) {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        if (authHelper.CheckAdmin(authCredentials)) {
            return Response.status(Response.Status.OK).entity("{\"message\": \"Admin\"}").build();
        } else {
            return Response.status(Response.Status.OK).entity("{\"message\": \"Normal user\"}").build();
        }
    }

    @GET
    @Path("/getuser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@Context HttpHeaders httpHeaders, @PathParam("id") int id) throws ParseException {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
        User res;
        int friendStatus = authHelper.CheckGetUser(authCredentials, id);
        if (friendStatus != 0 && friendStatus != 1) {
            res = db.GetUserLess(id);
            res.setMessage("You are not allowed to see full profile of " + res.getName());
        } else {
            res = db.GetUserFull(id);
            System.out.println("test");
        }
        res.setFriendStatus(friendStatus);
        return Response.status(Response.Status.OK).entity(res).build();
    }
    
    @GET
    @Path("/getuseradmin/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserAdmin(@Context HttpHeaders httpHeaders, @PathParam("id") int id) throws ParseException {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
        User res;
        boolean isAdmin = authHelper.CheckAdmin(authCredentials);
        if (!isAdmin) {
            res = db.GetUserLess(id);
            res.setMessage("You are not allowed to see full profile of " + res.getName());
        } else {
            res = db.GetUserFull(id);
        }
        res.setFriendStatus(5);
        return Response.status(Response.Status.OK).entity(res).build();
    }
    
    
    @GET
    @Path("/getuser/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListUser(@Context HttpHeaders httpHeaders, @PathParam("id") int id) {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        if (!authHelper.CheckAdmin(authCredentials)) {
            return Response.status(Response.Status.OK).entity("{\"message\": \"You are not allowed to get list user\"}").build();
        } else {
            Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
            List<User> lstUser = db.GetListAllUser();
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<User>>(lstUser) {
            }).build();
        }
    }

    @GET
    @Path("/getpicture/{userId}/{pictureId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOnePicture(@Context HttpHeaders httpHeaders, @PathParam("userId") int userId, @PathParam("pictureId") int pictureId) {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        System.out.println("in get one picture");
        if (authHelper.CheckGetUser(authCredentials, userId) == -1) {
            return Response.status(Response.Status.FORBIDDEN).entity("{\"message\": \"You are not allowed to see this picture\"}").build();
        } else {
            Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
            Picture res = db.GetPicture(pictureId, userId);
            return Response.status(Response.Status.OK).entity(res).build();
        }
    }

    @GET
    @Path("/getpicture/{userId}/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPicture(@Context HttpHeaders httpHeaders, @PathParam("userId") int userId) {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        if (authHelper.CheckGetUser(authCredentials, userId) == -1) {
            return Response.status(Response.Status.FORBIDDEN).entity("{\"message\": \"You are not allowed to see all pictures of this user\"}").build();
        } else {
            Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
            List<Picture> res = db.GetListAllPicture(userId);
            System.out.println(res.size());
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<Picture>>(res) {
            }).build();
        }
    }

    @GET
    @Path("/getfriend/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFriend(@Context HttpHeaders httpHeaders, @PathParam("userId") int userId) {
        String authCredentials = httpHeaders.getRequestHeaders().getFirst("authorization");
        if (!authHelper.CheckGetUserFriend(authCredentials, userId)) {
            return Response.status(Response.Status.OK).entity("{\"message\": \"You are not allowed to see this user's friend list\"}").build();
        } else {
            Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
            List<User> res = db.GetUserFriendList(userId);
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<User>>(res) {
            }).build();

        }
    }

    @GET
    @Path("/getnear/{userId}/{lon}/{lat}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNear(@PathParam("userId") int userId, @PathParam("lon") float lon, @PathParam("lat") float lat) {
        Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
        List<User> res = db.GetUserNear(lon, lat, userId);
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<User>>(res) {
        }).build();
    }

    @GET
    @Path("/searchuser/{userId}/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUser(@PathParam("userId") int userId, @PathParam("name") String name) {
        Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
        List<User> res = db.GetUserSearch(name, userId);
        return Response.status(Response.Status.OK).entity(new GenericEntity<List<User>>(res) {
        }).build();
    }

    @GET
    @Path("/checkusername/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkUserName(@PathParam("userName") String userName) {
        System.out.println("enter check username "+userName);
        Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
        String res = db.checkUserExist("username", userName) ? "Invalid" : "Valid";
        return Response.status(Response.Status.OK).entity("{\"message\": \"" + res + "\"}").build();
    }

    @GET
    @Path("/checkuseremail/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkEmail(@PathParam("email") String email) {
        Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
        String res = db.checkUserExist("email", email) ? "Invalid" : "Valid";
        return Response.status(Response.Status.OK).entity("{\"message\": \"" + res + "\"}").build();
    }
}
