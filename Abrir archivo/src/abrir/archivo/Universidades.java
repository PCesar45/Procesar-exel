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
public class Universidades {
    private static String[] UInternacional;
    private static  String[] UNacional;
    public static void CrearListaUinternacional(){
        File myObj = new File("Universidades Internacionales.txt");
        Scanner sc;
        try {
            sc = new Scanner(myObj);
            sc.useDelimiter("\\Z");
            UInternacional=sc.next().split(",");
           
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AbrirArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
     public static void CrearListaUnacional(){
        File myObj = new File("Universidades de Costa Rica.txt");
        Scanner sc;
        try {
            sc = new Scanner(myObj);
            sc.useDelimiter("\\Z");
            UNacional=sc.next().split(",");
           
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AbrirArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String[] getUInternacional() {
        return UInternacional;
    }

    public static String[] getUNacional() {
        return UNacional;
    }
    
    
}
