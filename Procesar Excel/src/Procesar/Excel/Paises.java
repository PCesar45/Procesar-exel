/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Procesar.Excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Aqui se abre el archivo de "Paises en ing,es,fr y pt.txt" y se  pasan los nombres contenidos en el de los paises a un arraylist
 * @author Pablo
 *  
 */
public class Paises {
    private static final ArrayList<String> paises=new ArrayList<>();
     private static  String[] codPaises;
    public static void CrearListaPaises(){       
        Scanner sc;
        sc = new Scanner(Paises.class.getResourceAsStream("Archivos/Paises en ing,es,fr y pt.txt"));
        sc.useDelimiter("\\Z");
        String[] ArrPaises=sc.next().split(",");
        Collections.addAll(paises, ArrPaises);
        //myObj = new File("Codigos Paises.txt");
        sc = new Scanner(Paises.class.getResourceAsStream("Archivos/Codigos Paises.txt")); //System.out.println(paises.contains("Georgia"));
        sc.useDelimiter("\\Z");
        codPaises=sc.next().split(",");
    } 

    public static ArrayList<String> getPaises() {
        return paises;
    }

    public static String[] getCodPaises() {
        return codPaises;
    }
    
    
    
}
