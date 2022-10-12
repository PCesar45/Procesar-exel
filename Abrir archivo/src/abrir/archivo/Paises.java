/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abrir.archivo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pablo
 */
public class Paises {
    private static final ArrayList<String> paises=new ArrayList<>();
     private static  String[] codPaises;
    public static void CrearListaPaises(){
        File myObj = new File("Paises en ing,es,fr y pt.txt");
        Scanner sc;
        try {
            sc = new Scanner(myObj);
            sc.useDelimiter("\\Z");
            String[] ArrPaises=sc.next().split(",");
            Collections.addAll(paises, ArrPaises);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AbrirArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        myObj = new File("Codigos Paises.txt");
        try {
            sc = new Scanner(myObj);
            sc.useDelimiter("\\Z");
            codPaises=sc.next().split(",");
           
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AbrirArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(paises.contains("Georgia"));
    } 

    public static ArrayList<String> getPaises() {
        return paises;
    }

    public static String[] getCodPaises() {
        return codPaises;
    }
    
    
    
}
