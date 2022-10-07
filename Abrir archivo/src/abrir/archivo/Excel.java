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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    private static XSSFWorkbook book2;
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
    private  ArrayList<String> AutoresWoS=new ArrayList<>();
    //Va a guardar la informacion de Authors with affiliations separada por comas
    //private String[] AuthorsWithAffDiv;
    private static ArrayList<String> Conflictos;
    private static JTable tabla;
    private static DefaultTableModel modelo;
    boolean Utec=false;
    //Botones de resolver conflictos
    //private static ArrayList<JButton> botones=new ArrayList<>();
    public Excel() {
        System.out.println("Inicializa");
        book2=new XSSFWorkbook(); 
        AutoresWoS=new ArrayList<>();
        Conflictos=new ArrayList<>();
        modelo=new DefaultTableModel();
        
    }
    public String Importar(File archivo,ProcesandoArchivo Progreso, JTable tablaExcep,Documento doc){
        String mensaje="Error en la Importacion";
       // DefaultTableModel modelo=new DefaultTableModel();
        tabla=tablaExcep;
        tabla.setModel(modelo);
        tabla.setDefaultRenderer(Object.class, new Render());
       //Para guardar el nuevo exel
       //----------------
       modelo.addColumn("Codigo");
       modelo.addColumn("Fila");
       modelo.addColumn("Tipo");
       modelo.addColumn("Excel");
       modelo.addColumn("Resolver");
        
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
           // System.out.println(hoja.getLastRowNum());
            double Porciento=hoja.getLastRowNum()/100;
            int x = 1;
            int cont=0;
             int contAutoresWoS=-1;
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
                            if(doc==Documento.scopus){
                                switch (celda.getStringCellValue()) {
                                    case ("EID")://codigo 
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
                            }
                            else{
                              //  System.out.println(IndiceColumna);
                               // System.out.println(celda.getStringCellValue());
                                switch (celda.getStringCellValue()) {
                                 case ("UT (Unique WOS ID)")://codigo 
                                     columnaCodigo=IndiceColumna;
                                     break;
                                 case "Article Title"://Titulo 
                                     columnaTitulo=IndiceColumna;
                                     break;
                                 case "Addresses": 
                                     columnaAuthorsWithAff=IndiceColumna;
                                     break;
                                 default:
                                     System.out.println("Error columna no identificada");
                                     //Si no lo encuentra puede ser que los nombres estan en otra fila,reinicio las filas
                                     
                                     break;
                             } 
                            }
                        }else{
                            //contenido de la fila
                            
                            //Saca el codigo y lo gaurda temporalmente
                            if(IndiceColumna==columnaCodigo){
                                
                               Codigo=celda.getStringCellValue();
                             //  System.out.println("Codigo: "+Codigo);
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
                                //Si se trata de Wos aqui se le dara el mismo formato que Scopus
                               
                                if(doc==Documento.WoS){
                                     
                                     AuthorsWithAff=DarFormato(AuthorsWithAff);
                                }
                                //Aqui se va a analizar y separar la columan de Authors with affiliations
                                //primero voy a remplazar los ; por , para procesar toda la informacion por igual 
                               // System.out.println(AuthorsWithAff);
                                String[] AuthorsWithAffDiv1 = AuthorsWithAff.split("; ");
                                
                                for (int i = 0; i < AuthorsWithAffDiv1.length; i++) {
                                    contAutoresWoS++;
                                   // voy a separar la informacion por las comas
                                    String[] AuthorsWithAffDiv2 = AuthorsWithAffDiv1[i].split(", ");
                                    //Se identifica si la informacion se trata del TEC
                                    
                                    Utec=AnalisisUTec(AuthorsWithAffDiv2);
                                    //System.out.println(Arrays.toString(AuthorsWithAffDiv2));
                                    for (int j = 0; j < AuthorsWithAffDiv2.length; j++) {
                                        
                                        String AuthorsWithAffDivInfo=AuthorsWithAffDiv2[j];
                                        
                                        if(doc==Documento.scopus){
                                            //Analizar Autor Comprueba si de verdad es un autor y ademas lo une con las iniciales
                                           
                                            if(AuthorsWithAffDiv2.length>=2){
                                                 //System.out.println(AuthorsWithAffDiv2[0]+","+AuthorsWithAffDiv2[1]);
                                                if(!"No encontrado".equals(AnalisisAutorScopus(AuthorsWithAffDiv2[0],AuthorsWithAffDiv2[1]))){
                                                    Autor=AnalisisAutorScopus(AuthorsWithAffDiv2[0],AuthorsWithAffDiv2[1]);
                                                }else{//en caso de que no lo encuentre lo busca en toda la linea 
                                                    if(!"No encontrado".equals(buscaAutorScopus(AuthorsWithAffDiv2))){
                                                        Autor=buscaAutorScopus(AuthorsWithAffDiv2);
                                                    }
                                                    else{
                                                       // si del todo no lo encuentra  lo mandaria a excepciones 
                                                       Autor="No encontrado";
                                                       ListaColumna[0]=Codigo;
                                                       ListaColumna[1]=ContFilas+1;
                                                       ListaColumna[2]="Autor";
                                                       ListaColumna[3]="AutoresTEC";
                                                       modelo.addRow(ListaColumna);
                                                       ResolverConflictos(AuthorsWithAffDiv1[i]);
                                                       // System.out.println("Este no lo encuentra");
                                                        //System.out.println(AuthorsWithAffDiv1[i]);
                                                    }


                                                }
                                            }
                                        }
                                         //Si no es Scopus es WoS
                                        else{
//                                            while (contAutoresWoS<AutoresWoS.size()) {                                                
//                                                Autor=AutoresWoS.get(contAutoresWoS);
//                                                contAutoresWoS++;
//                                            }
                                            //System.out.println();
                                            Autor=AutoresWoS.get(contAutoresWoS);
                                           // System.out.println(contAutoresWoS);
                                        }
                                         //si la informacion es del TEC
                                        if(Utec){
                                            
                                            //Escuela
                                            String resultadoEscuela=buscaEscuela(AuthorsWithAffDiv2);
                                            if(!"No encontrado".equals(resultadoEscuela)){
                                                Escuela=resultadoEscuela;
                                               // System.out.println(Escuela);
                                            }
                                            else{
                                                   //Estos a excepciones 
                                                   Escuela="No encontrado";
                                                   ListaColumna[0]=Codigo;
                                                   ListaColumna[1]=ContFilas+1;
                                                   ListaColumna[2]="Escuela o Unidad";
                                                   ListaColumna[3]="AutoresTEC";
                                                   modelo.addRow(ListaColumna);
                                                   ResolverConflictos(AuthorsWithAffDiv1[i]);
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
                                                ResolverConflictos(AuthorsWithAffDiv1[i]);
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
                                            //necesario para evitar repetidos
                                            break;
                                           // System.out.println(ContFilas);
                                        }else{//Autores externos
                                            
                                            // System.out.println(AuthorsWithAffDiv1[i]);
                                            
                                            
                                            //Analizar Pais
                                            Pais=BuscaPais(AuthorsWithAffDiv2);
                                            //Buscar Universidad
                                            Universidad=BuscarU(AuthorsWithAffDiv2);
                                            System.out.println(Universidad);
                                            break;
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
    public void ResolverConflictos(String infoCompleta){
           // System.out.println(sismos.get(v));
            Conflictos.add(infoCompleta);
            JButton boton =new JButton("Resolver");
            //System.out.println(modelo.getRowCount()-1+","+modelo.findColumn("Resolver"));
            modelo.setValueAt(boton, modelo.getRowCount()-1, modelo.findColumn("Resolver"));
    } 
    public boolean GuardarExcelTEC(File archivo) throws IOException{
        File fileC = new File (archivo.getAbsolutePath(),"AutoresTEC.xlsx");
        try ( // System.out.println(fileC.getAbsolutePath());
                FileOutputStream fileout = new FileOutputStream(fileC.getAbsolutePath())) {
                book2.write(fileout);
                return true; 
                
        }
    }
    String buscaAutorScopus( String[] InfoFila){
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
    String AnalisisAutorScopus( String Autor,String Iniciales){
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
    boolean AnalisisUTec(String[] InfoLinea1){
        String [] InfoLinea=InfoLinea1.clone();
   
        for (int i = 0; i < InfoLinea.length; i++) {
            //Convierte toda la info en minusculas
            InfoLinea[i]=InfoLinea[i].toLowerCase();
            //Si el nombre contiene Instituto y Costa Rica es TEC
            if((InfoLinea[i].matches("(.*)instituto(.*)"))&&(InfoLinea[i].matches("(.*)costa rica(.*)"))){
                return true;
            }
            if((InfoLinea[i].matches("(.*)tecnologico(.*)"))&&(InfoLinea[i].matches("(.*)costa rica(.*)"))){
                 return true;
            }
            if((InfoLinea[i].matches("(.*)tecnológico(.*)"))&&(InfoLinea[i].matches("(.*)costa rica(.*)"))){
                 return true;
            }
            if((InfoLinea[i].matches("(.*)institute(.*)"))&&(InfoLinea[i].matches("(.*)costa rica(.*)"))){
                 return true;
            }
            if((InfoLinea[i].matches("(.*)institute(.*)"))&&(InfoLinea[i].matches("(.*)costa rican(.*)"))){
                 return true;
            }
            if((InfoLinea[i].matches("(.*)technology(.*)"))&&(InfoLinea[i].matches("(.*)costa rica(.*)"))){
                 return true;
            }
            //Casos WoS
            if((InfoLinea[i].matches("(.*)ins(.*)"))&&(InfoLinea[i].matches("(.*)costa rica(.*)"))){
                return true;
            }
            if((InfoLinea[i].matches("(.*)inst(.*)"))&&(InfoLinea[i].matches("(.*)costa rica(.*)"))){
                return true;
            }
            if((InfoLinea[i].matches("(.*)tecnol(.*)"))&&(InfoLinea[i].matches("(.*)costa rica(.*)"))){
                return true;
            }
        }
        return false; 
     
    }
  
    String BuscaPais(String[] InfoLinea) throws MalformedURLException, IOException{
        for (int i = 0; i < InfoLinea.length; i++) {
            String pais=InfoLinea[i];
            pais=pais.toLowerCase();
            
            //asegurar que la primer letra del pais sea mayuscula
            pais=pais.replaceFirst(String.valueOf(pais.charAt(0)),String.valueOf(pais.charAt(0)).toUpperCase());
            //Si el pais se conforma de mas letras
            if(pais.contains(" ")){
                String[] separaciones=pais.split(" ");
                pais="";
                for (int j = 0; j < separaciones.length; j++) {
                    if(!"".equals(separaciones[j])){
                        String str =separaciones[j].replaceAll("\\)", "");
                        String str2 =str.replaceAll("\\(", "");
                        pais+=separaciones[j].replaceFirst(String.valueOf(str2.charAt(0)),String.valueOf(str2.charAt(0)).toUpperCase());
                        if(j!=separaciones.length-1)
                            pais+=" ";
                    }
                }
                
               
            }
            if(Paises.getPaises().contains(pais)){
                return pais;
            } 
        }
        
        return "No encontrado";
         
    }
    String BuscarU(String[] InfoLinea) {
        for (int i = 0; i < InfoLinea.length; i++) {
            String Uni=InfoLinea[i];
            Uni=Uni.toLowerCase();
            //Universadades nacionales
            for (int j = 0; j < Universidades.getUNacional().length; j++) {
                Uni=Uni.replaceAll("\\)", "");
                Uni=Uni.replaceAll("\\(", "");
                //Paraque no pnga en la Universidad el pais
                if(InfoLinea[i] == null ? Pais != null : !InfoLinea[i].equals(Pais)){
                    if(Universidades.getUNacional()[j].matches("(.*)"+Uni+"(.*)")&&(Uni.length()>4)){
                        return InfoLinea[i];
                    }
                        
                }
            }
             //Universadades Internacionales
            for (int j = 0; j < Universidades.getUInternacional().length; j++) {
                //Paraque no pnga en la Universidad el pais
                if(InfoLinea[i] == null ? Pais != null : !InfoLinea[i].equals(Pais)){
                    
                    if(Universidades.getUInternacional()[j].matches("(.*)"+Uni+"(.*)")&&(Uni.length()>34)){
                        return InfoLinea[i];
                    }
                        
                }
                
            }
        }
        return "No encontrado";
    }
    
    String buscaEscuela( String[] InfoFila){
      
        for (int i = 0; i < InfoFila.length; i++) {
            
            //Si solo son siglas
             if(InfoFila[i].equals(InfoFila[i].toUpperCase())&&(!InfoFila[i].matches("(.*)[.]"))){
                 return InfoFila[i];
             }
             //Convierte toda la info en minusculas 
             String InfoFilaMinus=InfoFila[i].toLowerCase();
             //frases claves
             for (int j = 0; j < Unidades.getFrasesClave().size(); j++) {
                 for (int k = 0; k < Unidades.getFrasesClave().get(j).length; k++) {
                    if(InfoFilaMinus.matches("(.*)"+Unidades.getFrasesClave().get(j)[k]+"(.*)")){
                        return Unidades.getFrasesClave().get(j)[0];
                    }
                     
                 }
               
                
            }
            
            
             //la infomarcion separa por los espacios
            String[] Info=InfoFilaMinus.split(" ");
            //palabras clave
            for (int j = 0; j < Info.length; j++) {
                for (int k = 0; k < Unidades.getPalabrasClave().size(); k++) {
                    for (int l = 0; l < Unidades.getPalabrasClave().get(k).length; l++) {
                       if(Unidades.getPalabrasClave().get(k)[l].equals(Info[j])){
                           //m.out.println(Unidades.getPalabrasClave().get(k)[0]);
                           return Unidades.getPalabrasClave().get(k)[0];
                       }
                        
                    }
                }
                
            }
            
             //\\buscar siglas entre parentesis
             int Par1=InfoFila[i].indexOf("(");
             if(Par1!=-1){
                 
                 int Par2=InfoFila[i].indexOf(")");
                 String siglas=InfoFila[i].substring(Par1+1, Par2);
                 //System.out.println(siglas);
                 //Este es muy especifico 
                 if("DOCINADE".equals(siglas)||"CIADEG-TEC".equals(siglas)||"CIB".equals(siglas)||"CIC".equals(siglas)||"CIF".equals(siglas)||"CIPA".equals(siglas)||"CIVCO".equals(siglas)
                    ||"CEQIATEC".equals(siglas)||"CIDASTH".equals(siglas)||"CIEMTEC".equals(siglas)||"CIGA".equals(siglas)||"GASEL".equals(siglas)){
                    // System.out.println("Reconocida");
                     return siglas;
                 }
                 
            }
            for (int j = 0; j < Info.length; j++) {
                //Si no encuentra la Unidad de investigacion pone la escula
                if(Info[j].matches("escuela")||(Info[j].matches("area"))||(Info[j].matches("unidad"))||(Info[j].matches("centro"))){
                    return InfoFila[i];
                }
                if(Info[j].matches("carrera")||(Info[j].matches("laboratorio"))){
                    return InfoFila[i];
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
    //Si se trata de Wos aqui se le dara el mismo formato que Scopus,saca los autores de los [] 
    public String DarFormato(String InfoLinea){
        String formato="";
        //Dividide la informacion entre los parentesis [ 
       // AutoresWoS.clear();
        String [] Div1=InfoLinea.split("\\[");
       // String[] A="[Pino-Gomez, Macario] Inst Tecnol Costa Rica, Sch Envirom Engn, Ctr Invest & Protecc Ambiental CIPA, Cartago, Costa Rica; [Soto-Cordoba, Silvia M.; Gaviria-Montoya, Lilliana] Inst Tecnol Costa Rica, Sch Chem, Ctr Invest & Protecc Ambiental CIPA, Apartado 159-7050, Cartago, Costa Rica".split("\\[");
        for (int i = 0; i < Div1.length; i++) {
              int cerrar=Div1[i].indexOf("]");
              if(cerrar!=-1){
                String Autores=Div1[i].substring(0, cerrar);
                String Info=Div1[i].substring(cerrar+1);
                Info=Info.replaceAll(";", "");
                //Procesamiento de los autores
                String[] autor=Autores.split("; ");
                  for (int j = 0; j < autor.length; j++) {
                    //guardar el Autor
                    //System.out.println(autor[j]);
                    AutoresWoS.add(autor[j]);
                    //solo se necesita la info
                    formato=formato.concat(Info);
                  //  if(j!=autor.length-1)
                    
                    formato  = formato+"; ";
                  }
               //   
              }    
        }
        
        
        formato = formato.replaceAll(" ; ", "; ");
        //System.out.println(formato);
       // System.out.println("Tamaño:"+formato.length());
        //System.out.println("cantidad au:"+AutoresWoS.size());
        return formato;
    }

    public static ArrayList<String> getConflictos() {
        return Conflictos;
    }

    public static XSSFWorkbook getBook2() {
        return book2;
    }

    public static DefaultTableModel getModelo() {
        return modelo;
    }
    
    
    
}

