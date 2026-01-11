package com.licencias.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {


    private static final String URL = "jdbc:postgresql://aws-0-us-west-2.pooler.supabase.com:6543/postgres?";
    private static final String USER = "postgres.qjxnofdnirxzjlkglqbe";
    private static final String PASSWORD = "Pfr2qAlKDCsRuWiB";

    public static Connection obtenerConexion() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");


            DriverManager.setLoginTimeout(10);

            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (conn != null) {
                System.out.println("Conexión exitosa a Supabase via Port 6543!");
            }

        } catch (ClassNotFoundException e) {
            System.err.println(" Error: No se encontró el Driver de PostgreSQL.");
        } catch (SQLException e) {
            System.err.println(" Error de Conexión: La red actual podría estar bloqueando el acceso o no hay internet.");
            System.err.println("Causa: " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        obtenerConexion();
    }
}