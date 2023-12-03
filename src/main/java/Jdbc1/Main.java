package Jdbc1;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/mydb?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "05167";
    static Connection conn;

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            initClients();
            System.out.println("\n");
            initOrders();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initClients() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Clients");
            st.execute("CREATE TABLE Clients (id INT NOT NULL " +
                    "AUTO_INCREMENT PRIMARY KEY, name VARCHAR(20) " +
                    "NOT NULL, phone VARCHAR(13) NOT NULL)");
        } finally {
            st.close();
            insertRandomClients();
        }
    }
    private static void initOrders() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Orders");
            st.execute("CREATE TABLE Orders (id INT NOT NULL " +
                    "AUTO_INCREMENT PRIMARY KEY, prName VARCHAR(20) " +
                    "NOT NULL, price VARCHAR(5) NOT NULL, customer INT NOT NULL)");
        } finally {
            st.close();
            insertRandomOrders();
        }
    }

    private static void insertRandomClients() throws SQLException {
        Random rnd = new Random();

        conn.setAutoCommit(false);
        try {
            try {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Clients (name, phone) VALUES(?, ?)");
                try {
                    for (int i = 0; i < 10; i++) {
                        StringBuilder phn = new StringBuilder ("+098");
                        ps.setString(1, "Name" + i);
                        for (int j = 0; j<7; j++){
                            int ranNumPh = rnd.nextInt(1,10);
                            phn.append(ranNumPh);
                        }
                        try{
                            ps.setString(2, phn.toString());
                            ps.executeUpdate();                        }
                        catch (Exception ex){
                            System.out.println(ex);
                        }

                    }
                    conn.commit();
                } finally {
                    ps.close();
                }
            } catch (Exception ex) {
                conn.rollback();
            }
        } finally {
            conn.setAutoCommit(true);
            viewClients();
        }
    }
    private static void insertRandomOrders() throws SQLException {
        Random rnd = new Random();
        String[] product = {"Socks", "Nails", "Cap  ", "Wheel", "Cover", "Handle", "Paper", "Bowl", "Toy  "};
        conn.setAutoCommit(false);
        try {
            try {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Orders (prName, price, customer) VALUES(?, ?, ?)");
                try {
                    for (int i = 0; i < 10; i++) {
                        int randomIndex = rnd.nextInt(product.length);
                        ps.setString(1, product[randomIndex]);
                        ps.setString(2, (00 +rnd.nextInt(500) + " $  "));
                        ps.setInt(3, rnd.nextInt( 1,11));
                        ps.executeUpdate();
                    }
                    conn.commit();
                } finally {
                    ps.close();
                }
            } catch (Exception ex) {
                conn.rollback();
            }
        } finally {
            conn.setAutoCommit(true);
            viewOrders();
        }
    }
    private static void viewClients() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Clients");
        try {
            ResultSet rs = ps.executeQuery();

            try {
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close();
            }
        } finally {
            ps.close();
        }
    }
    private static void viewOrders() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Orders");
        try {
            ResultSet rs = ps.executeQuery();
            try {
                ResultSetMetaData md = rs.getMetaData();
                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                    System.out.println();
                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close();
            }
        } finally {
            ps.close();
        }
    }
}
