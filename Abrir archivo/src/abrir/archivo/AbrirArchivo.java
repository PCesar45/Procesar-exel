/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abrir.archivo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Pablo
 */
public class AbrirArchivo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Aparencia 
         try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch(Exception e){
            System.out.println(e);
        }
        //System.out.println("DOCINADE".equals("DOCINADE".toUpperCase()));
        //System.out.println("DOCINADE".matches("[A-Z]"));
        //System.out.println("Ó.A.".matches("[Á-Ú].*"));
         //System.out.println("J.C".matches("(.*)[.]"));
        // int pA="Centre for Computational Intelligence (CCI)".indexOf("(");
         //int pC="Centre for Computational Intelligence (CCI)".indexOf(")");
         //System.out.println(pA);
         //System.out.println("Centre for Computational Intelligence (CCI)".substring(pA+1, pC));
        //Ventana de abrir el exel
        Abrir abrir = new Abrir();
        
        
    }
}

    

