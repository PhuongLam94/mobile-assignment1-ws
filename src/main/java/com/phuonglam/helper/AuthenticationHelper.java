/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phuonglam.helper;

/**
 *
 * @author Phuo
 */
import com.phuonglam.database.Database;
import java.io.IOException;
import javax.xml.bind.DatatypeConverter;

public class AuthenticationHelper {

    public int CheckUser(String authCredentials){
        return CheckUser(authCredentials, 0, 0, "");
    }
    public int CheckUser(String authCredentials, float longi, float latti, String token) {
        if (authCredentials != null) {
            String[] token1 = _parseUserNameAndPassword(authCredentials);
            Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
            return db.CheckUser(token1[0], token1[1], longi, latti, token);
        }
        return -1;
    }

    public int CheckGetUser(String authCredentials, int userId) {
        if (authCredentials != null) {
            String[] token = _parseUserNameAndPassword(authCredentials);
            Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
            int id = db.CheckUser(token[0], token[1], 0, 0, "");
            System.out.println(id);
            if (id == userId) {
                return 0;
            } else {
                db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
                return db.CheckGetUser(id, userId);
            }
        }
        return -1;
    }

    public boolean CheckAdmin(String authCredentials) {
        if (authCredentials != null) {
            String[] token = _parseUserNameAndPassword(authCredentials);
            Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
            int id = db.CheckUser(token[0], token[1], 0, 0, "");
            db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
            
            return db.GetUserStatus(id) == ConstantHelper.ADMINSTATUS;
        }
        return false;
    }

    public boolean CheckGetUserFriend(String authCredentials, int userId) {
        if (authCredentials != null) {
            String[] token = _parseUserNameAndPassword(authCredentials);
            Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
            int id = db.CheckUser(token[0], token[1], 0, 0, "");

            return id == userId;
        }
        return false;
    }
    
    public int CheckPassword(String authCredentials, String password){
        if (authCredentials != null) {
            String[] token = _parseUserNameAndPassword(authCredentials);
            Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
            int id = db.CheckUser(token[0], token[1], 0, 0, "");
            if (token[1].charAt(token[1].length() - 1) == 10) {
                    token[1] = token[1].substring(0, token[1].length() - 1);
                }
           System.out.println("PASSWORD: "+token[1]+", "+password);
            if (token[1].equals(password)){
                return id;
            } else {
                return -1;
            }
        }
        return -1;
    }

    private String[] _parseUserNameAndPassword(String authCredentials) {
        String encodedUserPassword = authCredentials.replaceFirst("Basic ", "");
        String usernameAndPassword = "";
        try {
            byte[] decodedBytes = DatatypeConverter.parseBase64Binary(encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        return usernameAndPassword.split(":");
    }
}
