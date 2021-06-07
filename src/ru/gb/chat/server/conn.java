package ru.gb.chat.server;
import java.sql.*;

public class conn {
    
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;
    
    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static void Conn() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:DB.s3db");
        
        System.out.println("База Подключена!");
    }
    
    // -------- Вывод таблицы--------
    public static void ReadDB() throws ClassNotFoundException, SQLException {
        resSet = statmt.executeQuery("SELECT * FROM userdb");
        
        while (resSet.next()) {
            int ID = resSet.getInt("ID");
            String login = resSet.getString("login");
            String password = resSet.getString("password");
            System.out.println("ID = " + ID);
            System.out.println("login = " + login);
            System.out.println("password = " + password);
            System.out.println();
        }
        
        System.out.println("Таблица выведена");
    }
    
    // --------Закрытие--------
    public static void CloseDB() throws ClassNotFoundException, SQLException {
        conn.close();
        statmt.close();
        resSet.close();
        
        System.out.println("Соединения закрыты");
    }
}
