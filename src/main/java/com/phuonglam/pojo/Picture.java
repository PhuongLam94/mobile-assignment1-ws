/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phuonglam.pojo;

import java.util.List;

/**
 *
 * @author Phuo
 */
public class Picture {
    private String content;
    private String description;
    private int id;
    private String message;
    private int prev;
    private int next;
    private int userid;
    private String time;
    private List<Comment> lstcomment;
    private String name;
    
    
    public String getContent(){return this.content;}
    public void setContent(String content){this.content = content;}
    
    public String getDescription(){return this.description;}
    public void setDescription(String description){this.description = description;}
    
    public int getId(){return this.id;}
    public void setId(int id){this.id = id;}
    
    public String getMessage(){return this.message;}
    public void setMessage(String message){this.message = message;}
    
    public int getPrev(){return this.prev;}
    public void setPrev(int prev){this.prev = prev;}
    
     public int getNext(){return this.next;}
    public void setNext(int next){this.next = next;}
    
    public int getUserid(){return this.userid;}
    public void setUserid(int userid){this.userid = userid;}
    
    public String getTime(){return this.time;}
    public void setTime(String time){this.time = time;}
    
    public List<Comment> getLstcomment() {return this.lstcomment;}
    public void setLstcomment(List<Comment> lstcomment){this.lstcomment = lstcomment;}
    
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}
}
