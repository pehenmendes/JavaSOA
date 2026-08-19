package br.com.fiap3espg.autoescola.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerarSenhaHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaHash1 = encoder.encode("admin");
        String senhaHash2 = encoder.encode("user");
        System.out.println("Admin: " + senhaHash1);
        System.out.println("User: " + senhaHash2);
    }
}
