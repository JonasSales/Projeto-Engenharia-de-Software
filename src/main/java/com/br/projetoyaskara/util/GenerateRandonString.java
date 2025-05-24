package com.br.projetoyaskara.util;

import java.security.SecureRandom;

public class GenerateRandonString {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString(){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        int length = 12;
        for (int i = 0; i < length; i++){
            int index = secureRandom.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

}
