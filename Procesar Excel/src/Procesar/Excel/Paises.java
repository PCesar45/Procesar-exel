/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Procesar.Excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final ArrayList<String[]> TraducPaises=new ArrayList<>();
     private static  ArrayList<String[]> codPaises=new ArrayList<>();
    public static void CrearListaPaises(){       
        Scanner sc;
        sc = new Scanner(Paises.class.getResourceAsStream("Archivos/Paises en ing,es,fr y pt.txt"));
        sc.useDelimiter("\\Z");
        String[] ArrPaises=sc.next().split(",");
        Collections.addAll(paises, ArrPaises);
        //myObj = new File("Codigos Paises.txt");
        sc = new Scanner(Paises.class.getResourceAsStream("Archivos/Codigos de paises con nombre.txt")); //System.out.println(paises.contains("Georgia"));
        sc.useDelimiter("\\Z");
        String[] ArrCod=sc.next().split(";");
        for (int i = 0; i < ArrCod.length; i++) {
            String[] ArrCod2 = ArrCod[i].split(",");
            System.out.println(Arrays.toString(ArrCod2));
            codPaises.add(ArrCod2);
        }
        //------------------------
        sc = new Scanner(Paises.class.getResourceAsStream("Archivos/Traduccion paises ingles.txt"));
        sc.useDelimiter("\\Z");
        String[] ArrTraducc=sc.next().split(";");
        for (int i = 0; i < ArrTraducc.length; i++) {
            String[] ArrTraducc2 = ArrTraducc[i].split(",");
            TraducPaises.add(ArrTraducc2);
            
        }
        
    } 
    
    public static ArrayList<String> getPaises() {
        return paises;
    }

    public static ArrayList<String[]> getCodPaises() {
        return codPaises;
    }

    public static ArrayList<String[]> getTraducPaises() {
        return TraducPaises;
    }
    
    
    
    
}
