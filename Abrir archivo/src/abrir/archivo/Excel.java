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
    
    private static final XSSFWorkbook book2=new XSSFWorkbook(); ;
    private int columnaCodigo;
    private int columnaTitulo;
    private int columnaAuthorsWithAff;

    //esto va a cambiar segun la fila ,por ciclo
    private String Codigo;
    private String Titulo;
    //la de AuthorsWithAff hay que dividirla en mas partes
    private String AuthorsWithAff;
    private String Autor;
    private String Escuela;
    private String Campus;
    private String Universidad;
    private String Pais;
    //Va a guardar la informacion de Authors with affiliations separada por comas
    private String[] AuthorsWithAffDiv;
    private static JTable tabla;
    private static final DefaultTableModel modelo=new DefaultTableModel();
    public Excel() {
    }
    public String Importar(File archivo,ProcesandoArchivo Progreso, JTable tablaExcep){
        String mensaje="Error en la Importacion";
       // DefaultTableModel modelo=new DefaultTableModel();
        tabla=tablaExcep;
        tabla.setModel(modelo);
        //tabla.setDefaultRenderer(Object.class, new Render());
       //Para guardar el nuevo exel
       //----------------
       modelo.addColumn("Codigo");
       modelo.addColumn("Fila");
       modelo.addColumn("Tipo");
       modelo.addColumn("Excel");
        
        Sheet hojaGuardar = book2.createSheet("AutoresTEC");
        //nombres de las columnas del excel nuevo
        Row fila1=hojaGuardar.createRow(0);
        Cell celda0=fila1.createCell(0);
        celda0.setCellValue("EID");
        Cell celda1=fila1.createCell(1);
        celda1.setCellValue("Title");
        Cell celda2=fila1.createCell(2);
        celda2.setCellValue("Autores");
        Cell celda3=fila1.createCell(3);
        celda3.setCellValue("Unidad de investigación");
        Cell celda4=fila1.createCell(4);
        celda4.setCellValue("Campus");
        Cell celda5=fila1.createCell(5);
        celda5.setCellValue("Universidad");
        Cell celda6=fila1.createCell(6);
        celda6.setCellValue("Pais");

        
        try {
            book=new XSSFWorkbook(new FileInputStream(archivo));
            Sheet hoja=book.getSheetAt(0);
            Iterator FilaIterator=hoja.rowIterator();
            System.out.println(hoja.getLastRowNum());
            double Porciento=hoja.getLastRowNum()/100;
            int x = 1;
            int cont=0;
            int IndiceFila=-1;
            int ContFilas=1;
            //VA SER VERDADERO SI EXISTEN FILAS POR RECORRER
            while (FilaIterator.hasNext()) {                
                //INDICE FILA AUMENTA 1 POR CADA RECORRIDO
                IndiceFila++;
                Row fila=(Row)FilaIterator.next();
                //RECORRE LAS COLUMNAS O CELDAS DE UNA FILA YA CREADA
                Iterator ColumnaIterator=fila.cellIterator();
                //ASIGNAMOS
                Object[]ListaColumna=new Object[5];
               //el indice columna vuelve a 0 en cada cambio de fila
                int IndiceColumna=-1;
                //VA SER VERDADERO SI EXISTEN COLUMNAS POR RECORRER
                while (ColumnaIterator.hasNext()) {                    
                    //INDICE COLUMNA AUMENTA 1 POR CADA RECORRIDO
                    IndiceColumna++;
                    
                    Cell celda=(Cell)ColumnaIterator.next();
                    if(celda!=null){
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
                               //System.out.println(Codigo);
                            }
                            //Saca el titulo y lo guarda temporalmente
                            if(IndiceColumna==columnaTitulo){
                               Titulo=celda.getStringCellValue();
                              // System.out.println(Titulo);
                            }
                            //Saca toda la informacion de Authors with affiliations y la guarda en una variable para posteriomente procesarla
                            if(IndiceColumna==columnaAuthorsWithAff){
                               // System.out.println(celda.getStringCellValue());
                                AuthorsWithAff=celda.getStringCellValue();
                                
                                //Aqui se va a analizar y separar la columan de Authors with affiliations
                                //primero voy a remplazar los ; por , para procesar toda la informacion por igual (revisar)
                                String[] AuthorsWithAffDiv1 = AuthorsWithAff.split("; ");
                                
                                for (int i = 0; i < AuthorsWithAffDiv1.length; i++) {
                                   // voy a separar la informacion por las comas
                                    String[] AuthorsWithAffDiv2 = AuthorsWithAffDiv1[i].split(", ");
                                    
                                    for (int j = 0; j < AuthorsWithAffDiv2.length; j++) {
                                        String AuthorsWithAffDivInfo=AuthorsWithAffDiv2[j];
                                        //Se identifica si la informacion se trata del TEC 
                                        if(AnalisisUTec(AuthorsWithAffDivInfo)){
                                            //Analizar Autor Comprueba si de verdad es un autor y ademas lo une con las iniciales
                                            if(!"No encontrado".equals(AnalisisAutor(AuthorsWithAffDiv2[0],AuthorsWithAffDiv2[1]))){
                                                Autor=AnalisisAutor(AuthorsWithAffDiv2[0],AuthorsWithAffDiv2[1]);
                                            }else{//en caso de que no lo encuentre lo busca en toda la linea 
                                                if(!"No encontrado".equals(buscaAutor(AuthorsWithAffDiv2))){
                                                    Autor=buscaAutor(AuthorsWithAffDiv2);
                                                }
                                                else{
                                                   // si del todo no lo encuentra  lo mandaria a excepciones 
                                                   Autor="No encontrado";
                                                   ListaColumna[0]=Codigo;
                                                   ListaColumna[1]=ContFilas+1;
                                                   ListaColumna[2]="Autor";
                                                   ListaColumna[3]="AutoresTEC";
                                                   modelo.addRow(ListaColumna);
                                                   // System.out.println("Este no lo encuentra");
                                                    //System.out.println(AuthorsWithAffDiv1[i]);
                                                }
                                            }
                                            //Escuela
                                            String resultadoEscuela=buscaEscuela(AuthorsWithAffDiv2);
                                            if(!"No encontrado".equals(resultadoEscuela)){
                                                Escuela=resultadoEscuela;
                                               // System.out.println(Escuela);
                                            }
                                            else{
                                                 //System.out.println("Este no lo encuentra Escuela");
                                                 //System.out.println(Codigo);
                                                 //System.out.println(AuthorsWithAffDiv1[i]);
                                                //Estos a excepciones 
                                                Escuela="No encontrado";
                                                ListaColumna[0]=Codigo;
                                                ListaColumna[1]=ContFilas+1;
                                                ListaColumna[2]="Escuela o Unidad";
                                                ListaColumna[3]="AutoresTEC";
                                                modelo.addRow(ListaColumna);
                                            }
                                            //Campus
                                            String resultadoCampus=buscaCampus(AuthorsWithAffDiv2);
                                            if(!"No encontrado".equals(resultadoCampus)){
                                                Campus=resultadoCampus;
                                               // System.out.println(Escuela);
                                            }
                                            else{
                                                 //System.out.println("Este no lo encuentra Escuela");
                                                 //System.out.println(Codigo);
                                                 //System.out.println(AuthorsWithAffDiv1[i]);
                                                //Estos a excepciones 
                                                Campus="No encontrado";
                                                ListaColumna[0]=Codigo;
                                                ListaColumna[1]=ContFilas+1;
                                                ListaColumna[2]="Campus";
                                                ListaColumna[3]="AutoresTEC";
                                                modelo.addRow(ListaColumna);
                                            }
                                            //Universidad y Pais autores TEC son fijos
                                            Universidad="Instituto Tecnologico de Costa Rica";
                                            Pais="Costa Rica";

                                           // System.out.println(Autor);
                                            
                                            //TEC:System.out.println(AuthorsWithAffDivInfo) 
                                            //System.out.println(ContFilas);
                                            Row filaNueva=hojaGuardar.createRow(ContFilas);
                                            Cell celda00=filaNueva.createCell(0);
                                            celda00.setCellValue(Codigo);
                                            Cell celda01=filaNueva.createCell(1);
                                            celda01.setCellValue(Titulo);
                                            Cell celda02=filaNueva.createCell(2);
                                            celda02.setCellValue(Autor);
                                            Cell celda03=filaNueva.createCell(3);
                                            celda03.setCellValue(Escuela);
                                            Cell celda04=filaNueva.createCell(4);
                                            celda04.setCellValue(Campus);
                                            Cell celda05=filaNueva.createCell(5);
                                            celda05.setCellValue(Universidad);
                                            Cell celda06=filaNueva.createCell(6);
                                            celda06.setCellValue(Pais);
                                            ContFilas++;
                                        }   
                                    }
                                } 
                            } 
                        }
                    }
                }       
//               if(cont>Porciento){
//                    System.out.println(x);
//                    x++;
//                    cont=0;
//                    
//               }
//               cont++;      
//                if(IndiceFila==1){
//                   break; 
//                }
               
               // if(IndiceFila!=0)modelo.addRow(ListaColumna);
            }
            mensaje="Importacion Exitosa";
            
        } catch (IOException | EncryptedDocumentException e) {
        }
        
        return mensaje;
    }
//    public static String Exportar(){
//        String mensaje="Error en la Exportacion";
//       
//        
//            for (int i = -1; i < NumeroFila; i++) {
//                Row fila=hoja.createRow(i+1);
//                for (int j = 0; j <NumeroColumna-1; j++) {
//                    Cell celda=fila.createCell(j);
//                    if(i==-1){
//                        celda.setCellValue(String.valueOf(tabla.getColumnName(j)));
//                    }else{
//                        
//                    }
//                }
//            }
//        
//    }
    boolean GuardarExcel(File archivo) throws IOException{
        File fileC = new File (archivo.getAbsolutePath(),"AutoresTEC.xlsx");
       // System.out.println(fileC.getAbsolutePath());
        FileOutputStream fileout = new FileOutputStream(fileC.getAbsolutePath());
        book2.write(fileout);
        fileout.close();
        return true; 
    }
    String buscaAutor( String[] InfoFila){
         String autor;
         //Siempre despues del nombre viene una Inicial,identificamos esa inicial para encontrar el nombre
         //System.out.println(InfoFila[i]);
         for (int i = 0; i < InfoFila.length; i++) {
            //Las iniciales siempre estan en mayuscula y Las iniciales terminan con un punto
            if((InfoFila[i].matches("[A-Z].*")||(InfoFila[i].matches("[Á-Ú].*")))&&((InfoFila[i].matches("(.*)[.]")))){
              //  System.out.println("ENTRAAAAA");
                 //retornamos el nombre con la(s) iniciale(s)
                 //Antes de la inicial esta el nombre por eso i-1
                 autor=InfoFila[i-1]+" ".concat(InfoFila[i]);
                 return autor;
            }   
         }
        
        return "No encontrado";
    }
    String AnalisisAutor( String Autor,String Iniciales){
         String autor;
         //Siempre despues del nombre viene una Inicial,identificamos esa inicial para encontrar el nombre
         //System.out.println(InfoFila[i]);
        //Las iniciales siempre estan en mayuscula y Las iniciales terminan con un punto
        if((Iniciales.matches("[A-Z].*")||(Iniciales.matches("[Á-Ú].*")))&&((Iniciales.matches("(.*)[.]")))){
          //  System.out.println("ENTRAAAAA");
             //retornamos el nombre con la(s) iniciale(s)
             autor=Autor+" ".concat(Iniciales);
             return autor;
        }
       
        
        return "No encontrado";
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
    String buscaEscuela( String[] InfoFila){
      
        for (int i = 0; i < InfoFila.length; i++) {
            
            //Si solo son siglas
             if(InfoFila[i].equals(InfoFila[i].toUpperCase())&&(!InfoFila[i].matches("(.*)[.]"))){
                 return InfoFila[i];
             }
            
            
             //Convierte toda la info en minusculas
            String Info=InfoFila[i].toLowerCase();
            //Si el nombre contiene escuela                                                                                                                   
            if(Info.matches("(.*)escuela(.*)")||(Info.matches("(.*)area(.*)"))||(Info.matches("(.*)unidad(.*)"))||(Info.matches("(.*)centro(.*)"))){
                return InfoFila[i];
            }
            if(Info.matches("(.*)carrera(.*)")||(Info.matches("(.*)laboratorio(.*)"))){
                return InfoFila[i];
            }
            //preguntar por si solo espa;ol
            if((Info.matches("(.*)school(.*)"))||(Info.matches("(.*)department(.*)"))||(Info.matches("(.*)centre(.*)"))||(Info.matches("(.*)center(.*)"))||(Info.matches("(.*)lab(.*)"))||(Info.matches("(.*)laboratory(.*)"))||(Info.matches("(.*)engineering(.*)"))||(Info.matches("(.*)management(.*)"))){
                return InfoFila[i];
            }
            if(Info.matches("(.*)Business administration(.*)")){
                return "CIADEG-TEC";
            }
            if(Info.matches("(.*)inclutec(.*)")||Info.matches("(.*)computación(.*)")||Info.matches("(.*)computación(.*)")){
                return "CIC";
            }
            if(Info.matches("(.*)mechatronics(.*)")){
                return "Area Académica de Ingeniería en Mecatrónica";
            }
             //\\buscar siglas entre parentesis
             int Par1=InfoFila[i].indexOf("(");
             if(Par1!=-1){
                 
                 int Par2=InfoFila[i].indexOf(")");
                 String siglas=InfoFila[i].substring(Par1+1, Par2);
                 //System.out.println(siglas);
                 //Este es muy especifico 
                 if("DOCINADE".equals(siglas)||"CIADEG-TEC".equals(siglas)||"CIB".equals(siglas)||"CIC".equals(siglas)||"CIF".equals(siglas)||"CIPA".equals(siglas)||"CIVCO".equals(siglas)
                    ||"CEQIATEC".equals(siglas)||"CIDASTH".equals(siglas)||"CIEMTEC".equals(siglas)||"CIGA".equals(siglas)){
                    // System.out.println("Reconocida");
                     return siglas;
                 }
                 
             }
            
            
            
           
        }
         return "No encontrado"; 
    }
    String buscaCampus( String[] InfoFila){
      
        for (int i = 0; i < InfoFila.length; i++) {
            //Convierte toda la info en minusculas 
            String Info=InfoFila[i].toLowerCase();
            
            if((Info.matches("(.*)cartago(.*)"))){
                return "1 - CAMPUS TECNOLOGICO CENTRAL CARTAGO";
                
            }
            if((Info.matches("(.*)san jose(.*)"))||(Info.matches("(.*)san josé(.*)"))){
                return "2 - CAMPUS TECNOLOGICO LOCAL SAN JOSE";
            }
            if((Info.matches("(.*)san carlos(.*)"))){
                return "3 - CAMPUS TECNOLOGICO LOCAL SAN CARLOS";
            }
            if((Info.matches("(.*)limón(.*)"))){
                return "4 - CENTRO ACADÉMICO DE LIMÓN";
            }
            if((Info.matches("(.*)alajuela(.*)"))){
                return "5 - CENTRO ACADEMICO DE ALAJUELA";
            }
        }
        return "No encontrado";
    }
    
    
}

