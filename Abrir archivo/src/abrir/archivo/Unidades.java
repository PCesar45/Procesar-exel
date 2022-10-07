
package abrir.archivo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pablo
 */
public class Unidades{
    private static final ArrayList<String[]> PalabrasClave=new ArrayList<>();
    private static final ArrayList<String[]> FrasesClave=new ArrayList<>();
    public Unidades() {}
     public static void CrearListaPalabrasClave(){
        try {
            File myObj = new File("Unidades palabras clave.txt");
            try (Scanner myReader = new Scanner(myObj)) {
                while (myReader.hasNextLine()) {
                    String Pclaves = myReader.nextLine();        
                    PalabrasClave.add(Pclaves.split(","));
                }
            }
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
         }
         
    }
    public static void CrearListafrasesClave(){
        try {
            File myObj = new File("Unidades frases clave.txt");
            try (Scanner myReader = new Scanner(myObj)) {
                while (myReader.hasNextLine()) {
                    String Fclaves = myReader.nextLine();        
                    FrasesClave.add(Fclaves.split(","));
                }
            }
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
         }
    }
    public static ArrayList<String[]> getPalabrasClave() {
        return PalabrasClave;
    }

    public static ArrayList<String[]> getFrasesClave() {
        return FrasesClave;
    }
    

    
    
        
    
}
