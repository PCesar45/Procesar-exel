
package Procesar.Excel;


import java.util.ArrayList;
import java.util.Scanner;


/**
 * Aqui se abre el archivo de "Unidades palabras clave.txt" y de "Unidades frases clave.txt" y se  pasan los palabras y las frases claves contenidos en el de las unidades a arraylist
 * @author Pablo
 * 
 */
public class Unidades{
    private static final ArrayList<String[]> PalabrasClave=new ArrayList<>();
    private static final ArrayList<String[]> FrasesClave=new ArrayList<>();
    
    public Unidades() {}
     public static void CrearListaPalabrasClave(){
         try (Scanner myReader = new Scanner(Unidades.class.getResourceAsStream("Archivos/Unidades palabras clave.txt"))) {
             while (myReader.hasNextLine()) {
                 String Pclaves = myReader.nextLine();
                 PalabrasClave.add(Pclaves.split(","));
             }
         }
         
    }
    public static void CrearListafrasesClave(){
        try (Scanner myReader = new Scanner(Unidades.class.getResourceAsStream("Archivos/Unidades frases clave.txt"))) {
            while (myReader.hasNextLine()) {
                String Fclaves = myReader.nextLine();
                FrasesClave.add(Fclaves.split(","));
            }
        }
    }
    public static ArrayList<String[]> getPalabrasClave() {
        return PalabrasClave;
    }

    public static ArrayList<String[]> getFrasesClave() {
        return FrasesClave;
    }
}
