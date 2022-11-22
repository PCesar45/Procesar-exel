/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Procesar.Excel;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Esta clase abre la ventana donde se selecciona donde se va a guardar el archivo nuevo
 * @author Pablo
 * 
 */
public class Guardar {
    
    public Guardar(Excel ex) throws IOException {
        JFrame parentFrame = new JFrame();
 
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Especificar donde guardar el archivo procesado");   
        fileChooser.setFileSelectionMode(1);
        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            
            //ventana donde se ve la barra de progreso
            Cargando cargando = new Cargando();
            cargando.setTitle("Guardando");
            cargando.setVisible(true);
            Runnable runnable = new Runnable() {
               @Override
               public void run() {
                    //boolean bandera=true;
                    int i=0;
                    while(i!=100){
                        Cargando.getjProgressBar1().setValue(i);
                        i++;
                    }
                    cargando.dispose();
               }
            };
            File fileToSave = fileChooser.getSelectedFile();
            
            ex.GuardarExcel(fileToSave);

            Thread hilo=new Thread(runnable);
            hilo.start();    
            ExitoGuardar exito=new ExitoGuardar();
            exito.setVisible(true);
        }
        else{
            //Para que no se salga por error sin guardar el archivo
            int resp=JOptionPane.showConfirmDialog(null, "¿Salir sin guardar?","No guardado",JOptionPane.ERROR_MESSAGE);
            if(resp==0){
                System.exit(0);
            }
            else{
                Guardar g=new Guardar(ex);
                
            }
        }
    }
    
    
}
