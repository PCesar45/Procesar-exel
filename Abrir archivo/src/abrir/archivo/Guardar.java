/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abrir.archivo;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTable;

/**
 *
 * @author Pablo
 */
public class Guardar {
    Conflictos conflic=new Conflictos();
    public Guardar(Excel ex) throws IOException {
        JFrame parentFrame = new JFrame();
 
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Especificar donde guardar el archivo procesado");   
        fileChooser.setFileSelectionMode(1);
        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            //ProcesandoArchivo Cargando = new ProcesandoArchivo();
           // Cargando.setVisible(true);
           ex.GuardarExcel(fileToSave);
           
           conflic.setTitle("Conflictos encontrados");
           conflic.setVisible(true);
           
        }
    }
    public JTable getTablaConflictos(){
        return conflic.getjTable1();
    } 
    
}
