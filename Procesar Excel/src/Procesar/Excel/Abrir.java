/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Procesar.Excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Esta clase se encarga de abrir el excel a procesar,es la ventana donde selecciona el excel a procesar
 * @author Pablo
 * 
 * 
 */
public class Abrir {

    public Abrir() {
        Scanner entrada = null;
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel file", "xls", "xlsx");
        fileChooser.setFileFilter(filter);
        
        int valor = fileChooser.showOpenDialog(fileChooser);
        if (valor == JFileChooser.APPROVE_OPTION) {
            String ruta = fileChooser.getSelectedFile().getAbsolutePath();                                        
            try {
                File f = new File(ruta);
                entrada = new Scanner(f);
                fileChooser.setVisible(false);
              
                
                //Abre la siguiente ventana de Tipo de Archivo 
                TipoArchivoSelec TipoArch = new TipoArchivoSelec(f);
                TipoArch.getArchSelect().setText(f.getName());
                TipoArch.setVisible(true);
                
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            } finally {
                if (entrada != null) {
                    entrada.close();
                }
            }
        } else {
            System.out.println("No se ha seleccionado ningún fichero");
        }
    }
    
    
}
