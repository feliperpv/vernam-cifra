/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifravernam;

import java.util.Random;

/**
 *
 * @author Felipe
 */
public class CifraVernam {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        CifraVernam cifraVernam = new CifraVernam();
        
        String msg = "hello";
        String chave = cifraVernam.keyGen(msg.length());
        String encrypted = cifraVernam.encrypt(msg, chave);
        String decrypted = cifraVernam.decrypt(encrypted, chave);
        
        System.out.println(msg);
        System.out.println(chave);
        System.out.println(encrypted);
        System.out.println(decrypted);
        
    }
    
    public String keyGen(int tamanho){
        
        byte[] bytes = new byte[tamanho];
        char[] chave = new char[tamanho];
        
        Random random =  new Random();
        
        random.nextBytes(bytes);
        
        for(int i=0; i<tamanho; i ++){
            chave[i] = (char) random.nextInt(132);
        }
                
        //return new String(bytes);
        return new String(chave);
        
    }

    public String encrypt(String msg, String chave) {

        if (msg.length() != chave.length()) {
            return "O tamanho do texto e da chave devem ser iguais";
        }

        int[] im = charArrayToInt(msg.toCharArray());
        int[] ik = charArrayToInt(chave.toCharArray());
        int[] data = new int[msg.length()];

        for (int i = 0; i < msg.length(); i++) {
            data[i] = im[i] ^ ik[i];
        }

        return new String(intArrayToChar(data));
    }

    public String decrypt(String msg, String chave) {
        if (msg.length() != chave.length()) {
            return "O tamanho do texto e da chave devem ser iguais";
        }

        int[] im = charArrayToInt(msg.toCharArray());
        int[] ik = charArrayToInt(chave.toCharArray());
        int[] data = new int[msg.length()];

        for (int i = 0; i < msg.length(); i++) {
            data[i] = im[i] ^ ik[i];
        }

        return new String(intArrayToChar(data));

    }

    private int[] charArrayToInt(char[] c) {
        int[] i = new int[c.length];
        for (int j = 0; j < c.length; j++) {
            i[j] = (int) c[j];
        }
        return i;
    }

    private char[] intArrayToChar(int[] i) {
        char[] c = new char[i.length];
        for (int j = 0; j < i.length; j++) {
            c[j] = (char) i[j];
        }
        return c;
    }

}
