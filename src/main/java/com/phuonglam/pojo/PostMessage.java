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
public class PostMessage {
    public DataContent data;
    public List<String> registration_ids;
    
    public static class DataContent{
        public String title;
        public String body;
    }
}
