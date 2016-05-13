/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phuonglam.helper;

import com.phuonglam.pojo.PostMessage;
import com.phuonglam.pojo.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 * @author Phuo
 */
public interface ApiEndpointInterface {
    @POST("gcm/send")
    Call<ResponseBody> sendMessage(@Body PostMessage message);
    
}
