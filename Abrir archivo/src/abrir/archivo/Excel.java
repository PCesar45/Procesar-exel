/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abrir.archivo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JTable;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.EncryptedDocumentException;
//import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.*;

/**
 *
 * @author Pablo
 */
public class Excel {
   
    private static XSSFWorkbook book;
    private int columnaCodigo;
    private int columnaTitulo;
    private int columnaAuthorsWithAff;
    //esto va a cambiar segun la fila ,por ciclo
    private String Codigo;
    private String Titulo;
    //la de AuthorsWithAff hay que dividirla en mas partes
    private String AuthorsWithAff;
    //Va a guardar la informacion de Authors with affiliations separada por comas
    private String[] AuthorsWithAffDiv;
    int cantidadAuTec;
    public Excel() {
    }
    public String Importar(File archivo){
        String mensaje="Error en la Importacion";
       // DefaultTableModel modelo=new DefaultTableModel();

        
        try {
            //CREA ARCHIVO CON EXTENSION XLS Y XLSX
            
            book=new XSSFWorkbook(new FileInputStream(archivo));
            Sheet hoja=book.getSheetAt(0);
            Iterator FilaIterator=hoja.rowIterator();
            int IndiceFila=-1;
            cantidadAuTec=0;
            //VA SER VERDADERO SI EXISTEN FILAS POR RECORRER
            while (FilaIterator.hasNext()) {                
                //INDICE FILA AUMENTA 1 POR CADA RECORRIDO
                IndiceFila++;
                Row fila=(Row)FilaIterator.next();
                //RECORRE LAS COLUMNAS O CELDAS DE UNA FILA YA CREADA
                Iterator ColumnaIterator=fila.cellIterator();
                //ASIGNAMOS EL MAXIMO DE COLUMNA PERMITIDO
               // Object[]ListaColumna=new Object[9999];
               //el indice columna vuelve a 0 en cada cambio de fila
                int IndiceColumna=-1;
                //VA SER VERDADERO SI EXISTEN COLUMNAS POR RECORRER
                while (ColumnaIterator.hasNext()) {                    
                    //INDICE COLUMNA AUMENTA 1 POR CADA RECORRIDO
                    IndiceColumna++;
                    
                    Cell celda=(Cell)ColumnaIterator.next();
                    if(celda!=null){
                        //System.out.println(celda.getStringCellValue());
                        //si es la primer fila localizamos en que columna va a estar el codigo ,titulo y Authors with affiliations
                        if (IndiceFila==0){
                            switch (celda.getStringCellValue()) {
                                case "EID"://codigo 
                                    columnaCodigo=IndiceColumna;
                                    break;
                                case "Title"://Titulo 
                                    columnaTitulo=IndiceColumna;
                                    break;
                                case "Authors with affiliations": 
                                    columnaAuthorsWithAff=IndiceColumna;
                                    break;
                                default:
                                    System.out.println("Error columna no identificada");
                                    break;
                            }
                        }else{
                            //contenido de la fila
                            
                            //Saca el codigo y lo gaurda temporalmente
                            if(IndiceColumna==columnaCodigo){
                                
                               Codigo=celda.getStringCellValue();
                               System.out.println(Codigo);
                            }
                            //Saca el titulo y lo guarda temporalmente
                            if(IndiceColumna==columnaTitulo){
                               Titulo=celda.getStringCellValue();
                            }
                            //Saca toda la informacion de Authors with affiliations y la guarda en una variable para posteriomente procesarla
                            if(IndiceColumna==columnaAuthorsWithAff){
                               // System.out.println(celda.getStringCellValue());
                                AuthorsWithAff=celda.getStringCellValue();
                                
                                //Aqui se va a analizar y separar la columan de Authors with affiliations
                                //primero voy a separar la informacion por las comas
                                AuthorsWithAffDiv=AuthorsWithAff.split(", ");
                                for (String AuthorsWithAffDivInfo : AuthorsWithAffDiv) {
                                    //System.out.println(AuthorsWithAffDivInfo);
                                    //Primero se conoce la U para saber si es un autor externo o del TEC
                                    if(AnalisisUTec(AuthorsWithAffDivInfo)){
                                        System.out.println(AuthorsWithAffDivInfo);
                                        cantidadAuTec++;
                                       
                                    }
                                    
                                }
                            }
                            
                            
                        }
                        
                    }
                    
                }
               // if(IndiceFila==1){
                  // break; 
                //}
               
               // if(IndiceFila!=0)modelo.addRow(ListaColumna);
            }
            System.out.println(cantidadAuTec);
            mensaje="Importacion Exitosa";
            
        } catch (IOException | EncryptedDocumentException e) {
        }
        
        return mensaje;
    }
    boolean AnalisisUTec(String Info){
      //Convierte toda la info en minusculas 
      Info=Info.toLowerCase();
      //Si el nombre contiene Instituto y Costa Rica es TEC
      if((Info.matches("(.*)instituto(.*)"))&&(Info.matches("(.*)costa rica(.*)"))){
          return true;
      }
      if((Info.matches("(.*)tecnologico(.*)"))&&(Info.matches("(.*)costa rica(.*)"))){
           return true;
      }
      if((Info.matches("(.*)tecnológico(.*)"))&&(Info.matches("(.*)costa rica(.*)"))){
           return true;
      }
      if((Info.matches("(.*)institute(.*)"))&&(Info.matches("(.*)costa rica(.*)"))){
           return true;
      }
      if((Info.matches("(.*)institute(.*)"))&&(Info.matches("(.*)costa rican(.*)"))){
           return true;
      }
      return false;  
    }
    
}
