/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phuonglam.pojo;

/**
 *
 * @author Phuo
 */
public class Comment {
    private int userid;
    private int pictureid;
    private int id;
    private String content;
    private String time;
    
    public int getUserid(){ return this.userid;}
    public void setUserid(int userid){this.userid = userid;}
    
    public int getPictureid(){ return this.pictureid;}
    public void setPictureid(int pictureid){this.pictureid = pictureid;}
    
    public int getId(){ return this.id;}
    public void setId(int id){this.id = id;}
    
    public String getContent(){return this.content;}
    public void setContent(String content){this.content = content;}
    
    public String getTime(){return this.time;}
    public void setTime(String time){this.time = time;}
}