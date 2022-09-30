/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abrir.archivo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
//        String Gen="";
//        String[] A="[Pino-Gomez, Macario] Inst Tecnol Costa Rica, Sch Envirom Engn, Ctr Invest & Protecc Ambiental CIPA, Cartago, Costa Rica; [Soto-Cordoba, Silvia M.; Gaviria-Montoya, Lilliana] Inst Tecnol Costa Rica, Sch Chem, Ctr Invest & Protecc Ambiental CIPA, Apartado 159-7050, Cartago, Costa Rica".split("\\[");
//        for (int i = 0; i < A.length; i++) {
////            String[] B=A[i].split("\\]");
//              int cerrar=A[i].indexOf("]");
//              if(cerrar!=-1){
//                String Autores=A[i].substring(0, cerrar);
//                String Info=A[i].substring(cerrar+1);
//                
//                //Procesamiento de los autores
//                String[] C=Autores.split("; ");
//                  for (int j = 0; j < C.length; j++) {
//                    //System.out.println("Autor:"+C[j]);
//                    //System.out.println("Informacion:"+Info);
//                    Gen=Gen.concat(C[j]+Info);
//                    Gen  = Gen+";";
//                  }
//                  
//              }    
//        }
//        System.out.println(Gen);
//        int Par1=A[1].indexOf("[");
//        if(Par1!=-1){
//
//            int Par2=A[1].indexOf("]");
//            System.out.println(Par1);
//            String siglas=A[1].substring(Par1+1, Par2);
//            System.out.println(siglas);
//        }
  
        
       

       
        Paises.CrearListaPaises();
        
        
        Abrir abrir = new Abrir();
        
//        //API Universidades: http://universities.hipolabs.com/search?name=Universidad%20de%20Costa%20Rica
          //API Paises:https://restcountries.com/v2/name/
//        
//        String link="https://restcountries.com/v2/name/Univ%20Costa%20Rica";
//        URL url;
//        try {
//            url = new URL(link);
//            HttpURLConnection http=(HttpURLConnection)url.openConnection();
//            System.out.println(http.getResponseMessage());
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(AbrirArchivo.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(AbrirArchivo.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        
        
    }
}

    

