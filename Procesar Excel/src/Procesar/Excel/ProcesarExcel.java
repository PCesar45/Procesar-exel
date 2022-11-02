/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Procesar.Excel;


import javax.swing.UIManager;

/**
 * Esta es la  Clase main de la aplicacion , aqui se traen palabras y frases claves de los archivos txt y se abre la ventana de Seleccionar el archivo excel fuente
 * @author Pablo
 * 
 * 
 */
public class ProcesarExcel {

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
        
        
        //Traer palabras y frases claves de los archivos txt
        Paises.CrearListaPaises();
        Unidades.CrearListaPalabrasClave();
        Unidades.CrearListafrasesClave();
        Universidades.CrearListaUinternacional();
        Universidades.CrearListaUnacional();

        //Ventana de abrir archivo
        Abrir abrir = new Abrir();

    }
}

    

