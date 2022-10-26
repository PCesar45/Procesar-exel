/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abrir.archivo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Pablo
 */
public class Guardar {
    
    public Guardar(Excel ex) throws IOException {
        JFrame parentFrame = new JFrame();
 
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Especificar donde guardar el archivo procesado");   
        fileChooser.setFileSelectionMode(1);
        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            ex.GuardarExcelTEC(fileToSave);
            ExitoGuardar exito=new ExitoGuardar();
            exito.setVisible(true);
        }
        else{
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
