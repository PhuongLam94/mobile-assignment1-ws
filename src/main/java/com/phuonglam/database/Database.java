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
import com.phuonglam.pojo.Comment;
import com.phuonglam.pojo.Picture;
import com.phuonglam.pojo.User;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    public int GetMaxCommentId() {
        try {
            String SQL = "SELECT MAX(id) FROM comment";

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

    public int CheckUser(String userName, String password, float longi, float latti) {
        try {
            System.out.println(userName + " " + password + " " + longi + " " + latti);
            if (userName != null && password != null) {
                if (password.charAt(password.length() - 1) == 10) {
                    password = password.substring(0, password.length() - 1);
                }
                String SQL = "SELECT id FROM userdb WHERE username = '" + userName + "' AND password = '" + password + "';";
                System.out.println(SQL);
                Statement stmt = this.dbConnection.createStatement();
                ResultSet rs = stmt.executeQuery(SQL);
                if (rs.next()) {
                    System.out.println("Successful");
                    if (longi != 0 && latti != 0) {
                        String SQL2 = String.format(Locale.ENGLISH, "UPDATE userdb set longitude=%f, lattitude=%f WHERE id=%d", longi, latti, rs.getInt(1));
                        System.out.println(SQL2);
                        Statement stmt2 = this.dbConnection.createStatement();
                        stmt2.execute(SQL2);
                    }
                    return rs.getInt(1);
                } else {
                    System.out.println(SQL);
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
            String SQL = "SELECT name, avatar, id, status FROM userdb WHERE id = " + userId;

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
                System.out.println(rs.getInt(1));
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
            String SQL = "SELECT name, avatar, id, status FROM userdb";
            List<User> lstUser = new ArrayList<>();
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                User res = _returnUserLess(rs);
                System.out.println(res.getName());
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
                    Database db = new Database(ConstantHelper.DBDRIVER, ConstantHelper.HOST, ConstantHelper.DBNAME, ConstantHelper.USER, ConstantHelper.PASS);
                    User temp = db.GetUserLess(getUser);
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

    public List<User> GetUserNear(float lon, float lat, int userId) {
        try {
            String SQL = "SELECT name, avatar, id, status, longitude, lattitude FROM userdb WHERE id != " + userId;
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            List<User> lstUser = new ArrayList<>();
            while (rs.next()) {
                double distance = _returnDistance(lon, lat, rs.getFloat(5), rs.getFloat(6));
                if (distance <= 10) {
                    User res = _returnUserLess(rs);
                    lstUser.add(res);
                }
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

    public List<User> GetUserSearch(String userName, int userId) {
        try {
            String SQL = "SELECT name, avatar, id, status FROM userdb WHERE id != " + userId;
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            List<User> lstUser = new ArrayList<>();
            while (rs.next()) {
                if (rs.getString(1).toLowerCase().contains(userName.toLowerCase())) {
                    User res = _returnUserLess(rs);
                    lstUser.add(res);
                }
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
//return picture

    public Picture GetPicture(int pictureId, int userId) {
        try {
            System.out.println("in getpicture");
            String SQL = "SELECT p.id, p.content, p.description, p.userid, p.time, u.name FROM Picture p, userdb u WHERE p.userid = u.id AND p.userid = " + userId;
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            Picture res = null;
            int prev = 0;
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3));
                if (rs.getInt(1) == pictureId) {
                    res = _returnPicture(rs);
                    res.setPrev(prev);
                    res.setNext(rs.next() ? rs.getInt(1) : 0);
                } else {
                    prev = rs.getInt(1);
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
            String SQL = "SELECT p.id, p.content, p.description, p.userid, p.time, u.name FROM Picture p, userdb u WHERE p.userId='" + userId + "' AND p.userid=u.id;";
            List<Picture> lstPicture = new ArrayList<>();
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                System.out.println("success");
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
    
    public List<Picture> GetFriendPicture(int userId, int offset){
        try {
            String SQL = String.format("SELECT p.id, p.content, p.description, p.userid, p.time, u.name FROM Picture p, friend f, userdb u WHERE "
                    + "((p.userid=%d AND f.user1id!=%d AND f.user2id!=%d) OR (f.user1id=%d AND p.userid=f.user2id) OR (f.user2id=%d AND p.userid=f.user1id)) AND u.id = p.userid"
                    + " ORDER BY p.time DESC LIMIT 10 OFFSET %d;", userId, userId, userId, userId, userId, offset);
            List<Picture> lstPicture = new ArrayList<>();
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                System.out.println("success");
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
    
    //get comment
    private List<Comment> _getListComment(int pictureId) {
        try {
            String SQL = "SELECT c.id, c.userid, c.pictureid, c.content, c.time, u.name FROM comment c, userdb u WHERE pictureid='" + pictureId + "' AND c.userid = u.id;";
            List<Comment> lstComment = new ArrayList<>();
            Statement stmt = this.dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                Comment res = _returnComment(rs);
                lstComment.add(res);
            }
            return lstComment;
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
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
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            String SQL = String.format("INSERT INTO Picture(id, content, description, userId, time) VALUES(%d,'%s','%s',%d, '%s')", picture.getId(), picture.getContent(), picture.getDescription(), userId, df.format(date));
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

    public List<Comment> AddComment(Comment comment) {
        try {
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            String SQL = String.format("INSERT INTO comment(id, userid, pictureid, content, time) VALUES(%d,%d, %d, '%s', '%s')", comment.getId(), comment.getUserid(), comment.getPictureid(), comment.getContent(), df.format(date));
            Statement stmt = this.dbConnection.createStatement();
            stmt.execute(SQL);
            System.out.println("Getting comment");
            return _getListComment(comment.getPictureid());
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
        return null;
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

    public boolean EditPassword(int userId, String password) {
        try {
            String SQL = String.format("UPDATE userdb SET password='%s'"
                    + "        WHERE id='%d'", password, userId);
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

    public boolean EditPasswordToDefault(int userId) {
        try {
            String SQL = String.format("UPDATE userdb SET password='%s'"
                    + "        WHERE id='%d'", ConstantHelper.DEFAULTPASSWORD, userId);
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

    public boolean EditStatus(int userId, int status) {
        try {
            String SQL = String.format("UPDATE userdb SET status='%d'"
                    + "        WHERE id='%d'", status, userId);
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

    //delete functions
    public boolean DeletePicture(int pictureId) {
        try {
            String SQL = "DELETE FROM Picture WHERE id=" + pictureId;
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
        res.setStatus(rs.getInt(4));
        return res;
    }

    private Picture _returnPicture(ResultSet rs) throws SQLException {
        Picture res = new Picture();
        res.setId(rs.getInt(1));
        res.setContent(rs.getString(2));
        res.setDescription(rs.getString(3));
        res.setUserid(rs.getInt(4));
        res.setTime(rs.getString(5));
        res.setName(rs.getString(6));
        res.setLstcomment(_getListComment(res.getId()));
        return res;
    }

    private double _returnDistance(float lon1, float lat1, float lon2, float lat2) {
        double p = 0.017453292519943295;    // Math.PI / 180
        double a = 0.5 - Math.cos((lat2 - lat1) * p) / 2
                + Math.cos(lat1 * p) * Math.cos(lat2 * p)
                * (1 - Math.cos((lon2 - lon1) * p)) / 2;
        return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
    }

    private Comment _returnComment(ResultSet rs) throws SQLException {
        Comment res = new Comment();
        res.setId(rs.getInt(1));
        res.setUserid(rs.getInt(2));
        res.setPictureid(rs.getInt(3));
        res.setContent(rs.getString(4));
        res.setTime(rs.getString(5));
        res.setName(rs.getString(6));
        return res;
    }
}
