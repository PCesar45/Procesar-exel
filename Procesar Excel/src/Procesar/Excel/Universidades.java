/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Procesar.Excel;


import java.util.Scanner;


/**
 * Aqui se abre el archivo de "Universidades Internacionales.txt" y de "Universidades de Costa Rica.txt" y se  pasan los nombres contenidos en el a Array de String
 * @author Pablo
 * 
 */
public class Universidades {
    private static String[] UInternacional;
    private static  String[] UNacional;
    public static void CrearListaUinternacional(){
        Scanner sc;
        sc = new Scanner(Universidades.class.getResourceAsStream("Archivos/Universidades Internacionales.txt"));
        sc.useDelimiter("\\Z");
        UInternacional=sc.next().split(",");
       
    }
     public static void CrearListaUnacional(){
        Scanner sc;
        sc = new Scanner(Universidades.class.getResourceAsStream("Archivos/Universidades de Costa Rica.txt"));
        sc.useDelimiter("\\Z");
        UNacional=sc.next().split(",");
    }

    public static String[] getUInternacional() {
        return UInternacional;
    }

    public static String[] getUNacional() {
        return UNacional;
    }
    
    
}
