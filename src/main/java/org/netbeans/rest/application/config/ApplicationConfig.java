/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import com.phuonglam.datingapp.service.COSRFilter;
import com.phuonglam.datingapp.service.GetService;
import com.phuonglam.datingapp.service.PostService;
import com.phuonglam.datingapp.service.PutService;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Phuo
 */
@javax.ws.rs.ApplicationPath("")
public class ApplicationConfig extends Application{
    @Override
    public Set<Class<?>> getClasses(){
        Set<Class<?>> resources = new HashSet<>();
        resources.add(COSRFilter.class);
        resources.add(GetService.class);
        resources.add(PostService.class);
        resources.add(PutService.class);
        return resources;
    }
}
