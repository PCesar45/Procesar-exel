/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abrir.archivo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
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
        String[] A="[Pino-Gomez, Macario] Inst Tecnol Costa Rica, Sch Envirom Engn, Ctr Invest & Protecc Ambiental CIPA, Cartago, Costa Rica; [Soto-Cordoba, Silvia M.; Gaviria-Montoya, Lilliana] Inst Tecnol Costa Rica, Sch Chem, Ctr Invest & Protecc Ambiental CIPA, Apartado 159-7050, Cartago, Costa Rica".split("\\[");
        for (int i = 0; i < A.length; i++) {
//            String[] B=A[i].split("\\]");
              int cerrar=A[i].indexOf("]");
              if(cerrar!=-1){
                String Autores=A[i].substring(0, cerrar);
                String Info=A[i].substring(cerrar+1);
                
                //Procesamiento de los autores
                String[] C=Autores.split("; ");
                  for (int j = 0; j < C.length; j++) {
                      System.out.println("Autor:"+C[j]);
                      System.out.println("Informacion:"+Info);
                      
                  }
                  
              }
//            for (int j = 0; j < B.length; j++) {
//                System.out.println(B[j]);
//                
//            }
           // System.out.println(A[i]);
            
        }
//        int Par1=A[1].indexOf("[");
//        if(Par1!=-1){
//
//            int Par2=A[1].indexOf("]");
//            System.out.println(Par1);
//            String siglas=A[1].substring(Par1+1, Par2);
//            System.out.println(siglas);
//        }
       
        
        //Abrir abrir = new Abrir();
        
        
    }
}

    

