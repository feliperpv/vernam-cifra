import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.Random;
import java.util.regex.Pattern;

/**
 *
 * @author Felipe
 */
public class CifraVernam {
    
    private final static char array[] = {'0','1','2','3','4','5','6','7','8',
        '9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R',
        'S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k',
        'l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','!','#','$','%',
        '&','(',')','*','+','-',',','.','/',':',';','<','>','=','?','@','[',']',
        '{','}'};

    public static void main(String[] args) throws IOException {
        
        BufferedReader buff = 
                new BufferedReader(new InputStreamReader(System.in));
        
        String texto = "";
        String str = null;
        try{
            while ((str = buff.readLine()) != null) {
                texto = texto + str;
            }
        } catch(IOException e){
            e.printStackTrace();
        }
               
        String type = args[0];
        String file = args[1];
                
        //String texto = "Um texto cuja modalidade se define pela natureza argumentativa representa, sobretudo, aquele texto em que se atesta a capacidade de o emissor discorrer, defender seu ponto de vista acerca deste ou daquele assunto.\n" +
//"Isso não é cachaça é mensagem do 99725400.";
        //String textod = "iPQQO51NQBEB6rHGJ38OR5IAVGTR0WBG6NyQTWMkYKLYDNESe69XPWIHAKLX2WTK6XG2ERV95WeJ599J61P8Ac234OTXy16EHQUAGZKKLKVWUKPX767ZLPEGGR0XLM2NjYYYXVQDQ3128MQOH4CG5ULhSBZNOXANGUU01TNC4AIoWSN0MA9MdPNM8UUGBTZX1cJJiKUO6NDRzHF27UXJOaa6YVhKESvUeW3GXX4GuJr9CFE461Ct12uZvYHFVaGs";
        
        //String type = "d";
        //String file = "chave.dat";
        File chaveFile = new File(file);
        
        texto = removeAcento(texto);       
        
        if(type.equalsIgnoreCase("c")){
            
            encrypt(texto, chaveFile);
            
        } else if(type.equalsIgnoreCase("d")){
            
            decrypt(texto, chaveFile);
            
        } else {
            System.out.println("Operação '" + type + "' inválido");
        }
        
    }
    
    public static void encrypt(String msg, File file) throws FileNotFoundException, IOException{
        
        int tamanho = msg.length();
        
        if (tamanho <= 0){
            System.out.println("Mensagem precisa ser maior que " + tamanho);
            return;
        }
        
        String chave = "";
        String encrypted = "";
        byte[] im = msg.getBytes();
        Random random =  new Random();
        
        boolean flag;
        char c, b;
        
        for(int i=0; i<tamanho; i++){
            flag = true;
            while(flag){
                c = array[random.nextInt(86)];  
                b  = (char) (im[i] ^ (byte) c);
                
                if(Character.isDigit(b) || Character.isLetter(b)){
                    encrypted += b;
                    chave += c;
                    flag = false;
                }
            }
        }
        
        if(file.exists()){
            file.delete();
        }
        
        FileOutputStream f = new FileOutputStream(file);
        f.write(chave.getBytes());
        f.close();
        
        System.out.println(encrypted);
        
    }

    public static void decrypt(String msg, File file) throws FileNotFoundException {
        
        if(!file.exists()){
            System.out.println("Arquivo '" + file.getName() + "' não existe!");
            return;
        }
        
        BufferedReader buff = 
                new BufferedReader(new FileReader(file));
        
        String chave = "";
        String str = "";
        try{
            while ((str = buff.readLine()) != null) {
                chave = chave + str;
            }
        } catch(IOException e){
            e.printStackTrace();
        }        
        
        if(msg.length() > chave.length()) {
            System.out.println("O tamanho do texto " + msg.length() + " e da chave " + chave.length() + " devem ser iguais");
            return;
        }

        byte[] im = msg.getBytes();
        byte[] ik = chave.getBytes();
        byte[] decrypted = new byte[msg.length()];
                
        for (int i = 0; i < msg.length(); i++) {
            decrypted[i] = (byte) (im[i] ^ ik[i]);
        }
        
        System.out.println(new String(decrypted));

    }
    
    public static String removeAcento(String str) {
        
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}
