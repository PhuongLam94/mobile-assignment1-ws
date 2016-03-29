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
public class ConstantHelper {
    public static final String SQLSERVER = "sqlserver";
    public static final String SQLSERVERDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String MYSQL = "mysql";
    public static final String MYSQLDRIVER = "com.mysql.jdbc.Driver";
    public static final String POSTGRESQL = "postgresql";
    public static final String POSTGRESQLDRIVER = "org.postgresql.Driver";
    
    
    public static final String DBDRIVER = POSTGRESQL;
    public static final String HOST = "ec2-107-22-248-209.compute-1.amazonaws.com";
    public static final String DBNAME = "da7m95j4p26es8";
    public static final String USER = "xzfaroskvpqusb";
    public static final String PASS = "cNXhT7m_hJP0ApnLkBVzr6_9NG";
    public static final int PORT = 5432;
    public static final String CONNECTTODB = "psql -h ec2-107-22-248-209.compute-1.amazonaws.com -p 5432 -U xzfaroskvpqusb da7m95j4p26es8";
    public static final String DEFAULTPASSWORD="123456@#";
    
    public static final int ADMINSTATUS = 3;
}
