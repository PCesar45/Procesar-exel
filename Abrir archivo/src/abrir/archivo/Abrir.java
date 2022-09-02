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
                //while (entrada.hasNext()) {
                //    System.out.println(entrada.nextLine());
                //}
                
                fileChooser.setVisible(false);
                Excel ex=new Excel();
                System.out.println(f.getName());
                ex.Importar(f);
                Guardar guardaArch=new Guardar();
                
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
