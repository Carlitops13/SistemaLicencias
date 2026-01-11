package com.licencias.util;

import org.mindrot.jbcrypt.BCrypt;

public class GeneradorClaves {
    public static void main(String[] args) {
        String passwordDeseada = "admin";


        String hashGenerado = BCrypt.hashpw(passwordDeseada, BCrypt.gensalt());

        System.out.println("---  CÓDIGO HASHEADO ---");
        System.out.println(hashGenerado);
        System.out.println("-------------------------");
    }
}