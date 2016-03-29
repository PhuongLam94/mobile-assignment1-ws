/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phuonglam.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.phuonglam.helper.ConstantHelper;
import com.phuonglam.pojo.Picture;
import com.phuonglam.pojo.User;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Phuo
 */
public class Database {

    Connection dbConnection = null;

    public Database(String type, String host, String dbname, String user, String pwd) {
        this.dbConnection = getDBConnection(type, host, dbname, user, pwd);
    }

    //public functions
    //return boolean
    public int GetMaxId() {
        try {
            String SQL = "SELECT MAX(id) FROM userdb";

            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return 0;
    }

    public int GetMaxPictureId() {
        try {
            String SQL = "SELECT MAX(id) FROM Picture";

            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return 0;
    }

    public int GetMaxFriendId() {
        try {
            String SQL = "SELECT MAX(id) FROM friend";

            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return 0;
    }

    public int CheckUser(String userName, String password) {
        try {
            if (userName != null && password != null) {
                String SQL = "SELECT id FROM userdb WHERE username = '" + userName + "' AND password = '" + password + "';";
                Statement stmt = this.dbConnection.createStatement();
                ResultSet rs = stmt.executeQuery(SQL);
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return -1;
                }
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return -1;
    }

    public int CheckGetUser(int currentId, int getId) {
        try {
            String SQL = "SELECT id, status, user1id FROM Friend WHERE ((user1id = '" + currentId + "' AND user2id = '" + getId + "') OR (user1id = '" + getId + "' AND user2id='" + currentId + "')" + ");";
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                int status = rs.getInt(2);
                if (status == 1) {
                    return 1;
                } else if (rs.getInt(3) == currentId) {
                    return 2; //current user send request
                } else {
                    return 3; //current user received request
                }
            } else {
                return -1; //no relation
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return -1;
    }

    public boolean checkUserExist(String column, String value) {
        try {
            if (value != null && column != null) {
                String SQL = "SELECT id FROM userdb WHERE " + column + " = '" + value + "';";
                Statement stmt = this.dbConnection.createStatement();
                ResultSet rs = stmt.executeQuery(SQL);
                return rs.next();
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return false;
    }

    //return User
    public User GetUserFull(int userId) throws ParseException {
        try {
            String SQL = "SELECT userName, email, name, id, status, phoneNumber, address, height, weight, avatar, gender, birthdate FROM userdb WHERE id = " + userId;

            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                User res = _returnUserFull(rs);
                return res;
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return null;
    }

    public User GetUserLess(int userId) {
        try {
            String SQL = "SELECT name, avatar, id FROM userdb WHERE id = " + userId;

            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                User res = _returnUserLess(rs);
                return res;
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return null;
    }

    public int GetUserStatus(int userId) {
        try {
            String SQL = "SELECT status FROM userdb WHERE id = " + userId;

            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return -1;
    }

    public List<User> GetListAllUser() {
        try {
            String SQL = "SELECT name, avatar FROM userdb";
            List<User> lstUser = new ArrayList<>();
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                User res = _returnUserLess(rs);
                lstUser.add(res);
            }
            return lstUser;
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return null;
    }

    public List<User> GetUserFriendList(int userId) {
        try {
            String SQL = "SELECT user1id,user2id, status FROM Friend WHERE (user1id = '" + userId + "') OR (user2id = '" + userId + "');";
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            List<User> lstFriend = new ArrayList<>();
            while (rs.next()) {
                int user1 = rs.getInt(1);
                int user2 = rs.getInt(2);
                int status = rs.getInt(3);
                int getUser;
                if (user1 == userId) {
                    getUser = user2;
                } else {
                    getUser = user1;
                    if (status == 2) {
                        status = 3;
                    }
                }
                if (status != 2) {
                    User temp = GetUserLess(getUser);
                    temp.setFriendStatus(status);
                    lstFriend.add(temp);
                }
            }
            return lstFriend;
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return null;
    }

    //return picture
    public Picture GetPicture(int pictureId, int userId) {
        try {
            String SQL = "SELECT id, content, description FROM Picture WHERE userid = " + userId;

            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            Picture res=null;
            int prev=0;
            while (rs.next()){
                if (rs.getInt(1) == pictureId){
                    res=_returnPicture(rs);
                    res.setPrev(prev);
                    res.setNext(rs.next()?rs.getInt(1):0);
                }
            }
            return res;
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return null;
    }

    public List<Picture> GetListAllPicture(int userId) {
        try {
            System.out.println("in db");
            String SQL = "SELECT id, content, description FROM Picture WHERE userId='" + userId + "';";
            List<Picture> lstPicture = new ArrayList<>();
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            System.out.println("success");
            while (rs.next()) {
                Picture res = _returnPicture(rs);
                lstPicture.add(res);
            }
            return lstPicture;
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return null;
    }

    //add functions
    public boolean AddUser(User user) {
        try {
            String SQL = String.format("INSERT INTO userdb(userName, email, name, id, status, phoneNumber, address, height, weight, avatar, gender, birthdate, password)"
                    + "         VALUES('%s', '%s', '%s', '%d', '%d', '%s', '%s', '%d', '%d', '%s', '%d', '%s', '%s')", user.getTest(), user.getEmail(), user.getName(), user.getId(),
                    user.getStatus(), user.getTest2(), user.getAddress(), user.getHeight(), user.getWeight(), user.getAvatar(), user.getGender(), user.getBirthday() == null ? new Date().toString() : user.getBirthday(), user.getPassword());
            Statement stmt = this.dbConnection.createStatement();
            stmt.execute(SQL);
            return true;
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return false;
    }

    public boolean AddPicture(Picture picture, int userId) {
        try {
            String SQL = String.format("INSERT INTO Picture(id, content, description, userId) VALUES(%d,'%s','%s',%d)", picture.getId(), picture.getContent(), picture.getDescription(), userId);
            Statement stmt = this.dbConnection.createStatement();
            stmt.execute(SQL);
            return true;
        } catch (SQLException sqle) {
            System.out.println("abc");
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return false;
    }

    //edit functions
    public boolean EditUser(User user) {
        try {
            String SQL = String.format("UPDATE userdb SET name='%s', phoneNumber='%s', address='%s', height='%d', weight='%d', avatar='%s', gender='%d', birthdate='%s'"
                    + "        WHERE id='%d'", user.getName(), user.getTest2(), user.getAddress(), user.getHeight(), user.getWeight(), user.getAvatar(), user.getGender(), user.getBirthday() == null ? new Date().toString() : user.getBirthday(), user.getId());
            Statement stmt = this.dbConnection.createStatement();
            stmt.execute(SQL);
            return true;
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return false;
    }

    public boolean EditFriendStatus(int user1Id, int user2Id, int friendStatus, int friendId) {
        try {
            String SQL = "";
            System.out.println(user1Id + " " + user2Id + " " + friendStatus);
            switch (friendStatus) {
                case -1:
                    SQL = String.format("DELETE FROM friend WHERE (user1id=%d AND user2id=%d) OR (user1id=%d AND user2id=%d)", user1Id, user2Id, user2Id, user1Id);
                    break;
                case 1:
                    SQL = String.format("UPDATE friend SET status=1 WHERE user1id=%d AND user2id=%d AND status=2", user2Id, user1Id);
                    break;
                case 2:
                    SQL = String.format("INSERT INTO friend(id, user1id, user2id, status) VALUES (%d, %d, %d, %d)", friendId, user1Id, user2Id, 2);
                    break;
            }
            Statement stmt = this.dbConnection.createStatement();
            stmt.execute(SQL);
            System.out.println("abc");
            return true;
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } finally {
            if (this.dbConnection != null) {
                try {
                    this.dbConnection.close();
                } catch (SQLException sqle) {
                    System.err.println(sqle.getMessage());
                }
            }
        }
        return false;
    }

    //private functions
    private Connection getDBConnection(String type, String host, String dbname, String user, String pwd) {
        if (type != null && !type.isEmpty()) {
            try {
                if (type.equalsIgnoreCase(ConstantHelper.SQLSERVER)) {
                    Class.forName(ConstantHelper.SQLSERVERDRIVER);
                    dbConnection = DriverManager.getConnection("jdbc:sqlserver://" + host + ":1433;database=" + dbname + ";sendStringParametersAsUnicode=true;useUnicode=true;characterEncoding=UTF-8;", user, pwd);
                } else if (type.equalsIgnoreCase(ConstantHelper.MYSQL)) {
                    Class.forName(ConstantHelper.MYSQLDRIVER);
                    dbConnection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + dbname, user, pwd);
                } else if (type.equalsIgnoreCase(ConstantHelper.POSTGRESQL)) {
                    Class.forName(ConstantHelper.POSTGRESQLDRIVER);
                    Properties props = new Properties();
                    props.put("user", user);
                    props.put("password", pwd);
                    props.put("sslmode", "require");
                    dbConnection = DriverManager.getConnection("jdbc:postgresql://" + host + ":5432/" + dbname + "?sslmode=require&user=" + user + "&password=" + pwd);
                }
                return dbConnection;
            } catch (ClassNotFoundException | SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return dbConnection;
    }

    private User _returnUserFull(ResultSet rs) throws SQLException, ParseException {
        User res = new User();
        res.setUserName(rs.getString(1));
        res.setTest(rs.getString(1));
        res.setEmail(rs.getString(2));
        res.setName(rs.getString(3));
        res.setId(rs.getInt(4));
        res.setStatus(rs.getInt(5));
        res.setPhoneNumber(rs.getString(6));
        res.setTest2(rs.getString(6));
        res.setAddress(rs.getString(7));
        res.setHeight(rs.getInt(8));
        res.setWeight(rs.getInt(9));
        res.setAvatar(rs.getString(10));
        res.setGender(rs.getInt(11));
        System.out.println(rs.getString(12));
        res.setBirthday(rs.getString(12));
        return res;
    }

    private User _returnUserLess(ResultSet rs) throws SQLException {
        User res = new User();
        res.setName(rs.getString(1));
        res.setAvatar(rs.getString(2));
        res.setId(rs.getInt(3));
        return res;
    }

    private Picture _returnPicture(ResultSet rs) throws SQLException {
        Picture res = new Picture();
        res.setId(rs.getInt(1));
        res.setContent(rs.getString(2));
        res.setDescription(rs.getString(3));
        return res;
    }
}
