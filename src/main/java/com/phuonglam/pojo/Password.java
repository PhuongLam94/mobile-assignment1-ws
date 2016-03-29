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
public class Password {
    private String oldPassword;
    private String newPassword;
    
    public String getOldPassword(){return this.oldPassword;}
    public void setOldPassword(String oldPassword){this.oldPassword = oldPassword;};
    
    public String getNewPassword(){return this.newPassword;}
    public void setNewPassword(String newPassword){this.newPassword = newPassword;};
}
