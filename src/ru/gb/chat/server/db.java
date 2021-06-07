package ru.gb.chat.server;
import java.sql.*;
public class db {
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        conn.Conn();
       conn.ReadDB();
        conn.CloseDB();
    }
    
}

