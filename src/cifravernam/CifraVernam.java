package cifravernam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Random;
import java.util.regex.Pattern;

/**
 *
 * @author Felipe
 */
public class CifraVernam {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        BufferedReader buff = 
                new BufferedReader(new InputStreamReader(System.in));
        
        String texto = "";
        String str = null;
        try{
            while ((str = buff.readLine()) != null) {
                texto = texto + str + "\r\n";
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        
        
        String type = args[0];
        String file = args[1];
        //String texto = "HELLO WORLD ISSO É SÓ UM TEXTO FILHO";
        
        //String type = "d";
        //String file = "chave.dat";
        File chaveFile = new File(file);
        
        texto = removeAcento(texto);       
        
        if(type.equalsIgnoreCase("c")){
            
            String chave = keyGen(texto.length(), chaveFile);
            String encrypted = encrypt(texto, chave);
            System.out.println(encrypted);
            
        } else if(type.equalsIgnoreCase("d")){
            
            String decrypted = decrypt(texto, chaveFile);
            System.out.println(decrypted);
            
        } else {
            System.out.println("Operação '" + type + "' inválido");
        }
        
    }
    
    public static String keyGen(int tamanho, File file) throws FileNotFoundException, IOException{
        
        if (tamanho <= 0){
            return null;
        }
        
        byte[] data = new byte[tamanho];
        
        Random random =  new Random();
        for(int i=0; i<tamanho; i ++){
            data[i] = (byte) random.nextInt(126);
        }
                
        if(file.exists()){
            file.delete();
        }
        
        FileOutputStream f = new FileOutputStream(file);
        f.write(data);
        f.close();
        
        return new String(data, StandardCharsets.UTF_8);
    }

    public static String encrypt(String msg, String chave) throws FileNotFoundException, IOException {

        if (msg.length() < chave.length()) {
            return "O tamanho do texto e da chave devem ser iguais";
        }
        
        byte[] im = msg.getBytes();
        byte[] ik = chave.getBytes();
        byte[] data = new byte[msg.length()];
        
        for (int i = 0; i < msg.length(); i++) {
            data[i] = (byte) (im[i] ^ ik[i]);
        }
        
        return new String(data);
    }

    public static String decrypt(String msg, File file) throws FileNotFoundException {
        
        if(!file.exists()){
            return "Arquivo '" + file.getName() + "' não existe!";
        }
        
        BufferedReader buff = 
                new BufferedReader(new FileReader(file));
        
        String chave = "";
        String str = null;
        try{
            while ((str = buff.readLine()) != null) {
                chave = chave + str;
            }
        } catch(IOException e){
            e.printStackTrace();
        }        
        
        if(msg.length() < chave.length()) {
            return "O tamanho do texto e da chave devem ser iguais";
        }

        byte[] im = msg.getBytes();
        byte[] ik = chave.getBytes();
        byte[] data = new byte[msg.length()];
        
        for (int i = 0; i < msg.length(); i++) {
            data[i] = (byte) (im[i] ^ ik[i]);
        }

        return new String(data);

    }
    
    public static String removeAcento(String str) {
        
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}
