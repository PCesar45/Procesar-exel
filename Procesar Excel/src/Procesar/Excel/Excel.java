/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Procesar.Excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JTable;

import java.io.*;
import java.net.MalformedURLException;
import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

/**
 * En esta clase se procesa el excel fuente se le saca la informacion solicitada  y se crea el Excel de salida y se guarda
 * @author Pablo
 * 
 */
public class Excel {
   
    private static XSSFWorkbook book;
    
    private static XSSFWorkbook book2;
    File ArchivoEntrada;
    private int columnaCodigo=-1;
    private int columnaTitulo=-1;
    private int columnaAutoresConAfi=-1;

    //esto va a cambiar segun la fila ,por ciclo
    private String Codigo;
    private String Titulo;
    //la de AutoresConAfi hay que dividirla en mas partes
    private String AutoresConAfi;
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
    //boolean HojaEncontrada=false;
    //hoja del excel donde estan las columnas Código Título Autores con afiliación
    int numHoja=0;
    Documento doc;
    Sheet hoja;
    Sheet hojaGuardarTEC ;
    Sheet hojaGuardarInter;
    //Hoja de sin Univerdades no reconocidas
    Sheet hojaNoUni;
    
    public Excel() {
        book2=new XSSFWorkbook(); 
        AutoresWoS=new ArrayList<>();
        Conflictos=new ArrayList<>();

        modelo=new DefaultTableModel();
        
    }
    public String Importar(File archivo, JTable tablaExcep,Documento doc){
        ArchivoEntrada=archivo;
        this.doc=doc;
        String mensaje="Error en la Importacion";
        tabla=tablaExcep;
        tabla.setModel(modelo);
        tabla.setDefaultRenderer(Object.class, new Render());
        
        modelo.addColumn("Codigo");
        modelo.addColumn("Fila");
        modelo.addColumn("Tipo");
        modelo.addColumn("Hoja del Excel");
        modelo.addColumn("Resolver");
        
          //Para guardar el nuevo exel
        //----------------
        hojaGuardarTEC = book2.createSheet("AutoresTEC");
        //nombres de las columnas del excel nuevo
        TitulosColTEC(hojaGuardarTEC);
        hojaGuardarInter= book2.createSheet("Autores Externos");
        TitulosColExter(hojaGuardarInter);
        //Hoja de sin Univerdades no reconocidas
        hojaNoUni= book2.createSheet("Univesidad no reconocida");
        TitulosColExter(hojaNoUni);
        //para la informacion de la Universidad no identicada
        Cell celda5=hojaNoUni.getRow(0).createCell(5);
        celda5.setCellValue("Informacion Completa");
        try {
            book=new XSSFWorkbook(new FileInputStream(ArchivoEntrada));
            ProcesarExcel(); 
        } catch (IOException | EncryptedDocumentException e){}
        return mensaje;
    }
    public void ProcesarExcel() throws FileNotFoundException, IOException{
       
        //hoja del excel donde estan las columnas Código Título Autores con afiliación
        hoja=book.getSheetAt(numHoja);
        Iterator FilaIterator=hoja.rowIterator();
        float Porciento=(float)100/(hoja.getLastRowNum());
        int contAutoresWoS=-1;
        int NumCol=0;
        //Para que cuando entre en el ciclo la primer fila sea el IndiceFila=0
        int IndiceFila=-1;
        int ContFilasTEC=1;
        int ContFilasExtern=1;
        int ContFilasNoUni=1;
        //VA SER VERDADERO SI EXISTEN FILAS POR RECORRER
        try{
            while (FilaIterator.hasNext()) {                
                //INDICE FILA AUMENTA 1 POR CADA RECORRIDO
                IndiceFila++;
                Row fila=(Row)FilaIterator.next();
                //ASIGNAMOS
                Object[]ListaColumna=new Object[6];
               //el indice columna vuelve a 0 en cada cambio de fila
                int IndiceColumna=-1;

                //si es la primer fila localizamos en que columna va a estar el codigo ,titulo y Autores con afiliación por medio del nombre de las columnas
                if (IndiceFila==0){
                    //numero de columnas y identificar las columnas
                    NumCol=IdenNomDeCol_Y_numCol(fila);
                }else{
                while (IndiceColumna!=NumCol-1) {       
                    //INDICE COLUMNA AUMENTA 1 POR CADA RECORRIDO
                    IndiceColumna++;
                    Cell celda=(Cell)fila.getCell(IndiceColumna);  
                    if(celda!=null){
                        //contenido de la fila
                        //Saca el codigo y lo guarda temporalmente esto cambia en cada fila
                        if(IndiceColumna==columnaCodigo){
                            Codigo=celda.getStringCellValue();
                        }
                        //Saca el titulo y lo guarda temporalmente
                        if(IndiceColumna==columnaTitulo){
                            Titulo=celda.getStringCellValue();
                        }
                        //Saca toda la informacion de Authors with affiliations y la guarda en una variable para posteriomente procesarla
                        if(IndiceColumna==columnaAutoresConAfi){
                            AutoresConAfi=celda.getStringCellValue();
                            //Si se trata de Wos aqui se le dara el mismo formato que Scopus
                            if(doc==Documento.WoS){
                                AutoresConAfi=DarFormato(AutoresConAfi);
                            }
                            //Aqui se va a analizar y separar la columan de Autores con afiliacion
                            //s voy a separar la informacion por ; y lo guardo en un arraylist
                            String[] AutoresConAfiDiv1 = AutoresConAfi.split("; ");   
                            for (int i = 0; i < AutoresConAfiDiv1.length; i++) {
                                contAutoresWoS++;
                                // voy a separar la informacion por comas y lo guardo en un arraylist
                                String[] AutoresConAfiDiv2 = AutoresConAfiDiv1[i].split(", ");
                                //Se identifica si la informacion se trata del TEC
                                Utec=AnalisisUTec(AutoresConAfiDiv2);
                                for (int j = 0; j < AutoresConAfiDiv2.length; j++) {
                                    //si es un Excel scopus
                                    if(doc==Documento.scopus){
                                        //Analizar Autor Comprueba si de verdad es un autor y ademas lo une con las iniciales
                                        if(AutoresConAfiDiv2.length>=2){
                                            if(!"No encontrado".equals(AnalisisAutorScopus(AutoresConAfiDiv2[0],AutoresConAfiDiv2[1]))){
                                                Autor=AnalisisAutorScopus(AutoresConAfiDiv2[0],AutoresConAfiDiv2[1]);
                                            }else{//en caso de que no lo encuentre lo busca en toda la linea 
                                                if(!"No encontrado".equals(buscaAutorScopus(AutoresConAfiDiv2))){
                                                    Autor=buscaAutorScopus(AutoresConAfiDiv2);
                                                }
                                                else{
                                                    // si del todo no lo encuentra  lo mandaria a excepciones 
                                                    Autor="No encontrado";
                                                    if(Utec){
                                                        añadirConflicto(ListaColumna,ContFilasTEC+1,"Autor","AutoresTEC",AutoresConAfiDiv1[i]);
                                                    }
                                                    else{
                                                        añadirConflicto(ListaColumna,ContFilasExtern+1,"Autor","Autores Externos",AutoresConAfiDiv1[i]);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    //Si no es Scopus es WoS
                                    else{
                                        //Guarda el autor del excel WoS o ESCI
                                        Autor=AutoresWoS.get(contAutoresWoS);
                                    }
                                    //si la informacion es del TEC saca la escuela, campus ,universidad y país
                                    if(Utec){
                                        //Escuela
                                        String resultadoEscuela=buscaEscuela(AutoresConAfiDiv2);
                                        if(!"No encontrado".equals(resultadoEscuela)){
                                            Escuela=resultadoEscuela;
                                        }
                                        else{
                                            //Estos a excepciones
                                            Escuela="No encontrado";
                                            añadirConflicto(ListaColumna,ContFilasTEC+1,"Escuela o Unidad","AutoresTEC",AutoresConAfiDiv1[i]);
                                        }
                                        //Buscar el campus
                                        String resultadoCampus=buscaCampus(AutoresConAfiDiv2);
                                        if(!"No encontrado".equals(resultadoCampus)){
                                            Campus=resultadoCampus;
                                        }
                                        else{
                                            Campus="No encontrado";
                                            añadirConflicto(ListaColumna,ContFilasTEC+1,"Campus","AutoresTEC",AutoresConAfiDiv1[i]);
                                        }
                                        //Universidad y Pais autores TEC son fijos
                                        Universidad="Instituto Tecnologico de Costa Rica";
                                        Pais="Costa Rica";
                                        GuardarFilaAuTEC(hojaGuardarTEC,ContFilasTEC);
                                        ContFilasTEC++;
                                        //necesario para evitar repetidos
                                        break;
                                    }else{//Autores externos
                                        //Analizar Pais
                                        Pais=BuscaPais(AutoresConAfiDiv2);
                                        if("No encontrado".equals(Pais)){
                                            añadirConflicto(ListaColumna,ContFilasExtern+1,"País","Autores Externos",AutoresConAfiDiv1[i]);
                                        }
                                        //Buscar Universidad
                                        Universidad=BuscarU(AutoresConAfiDiv2);
                                        if("No encontrado".equals(Universidad)){
                                            añadirConflicto(ListaColumna,ContFilasNoUni+1,"Universidad","Univesidad no reconocida",AutoresConAfiDiv1[i]);
                                            GuardarFilaAuExtern(hojaNoUni,ContFilasNoUni);
                                            Cell celda05=hojaNoUni.getRow(ContFilasNoUni).createCell(5);
                                            celda05.setCellValue(AutoresConAfiDiv1[i]);
                                            ContFilasNoUni++;
                                        }
                                        //Si no encuentra la universidad manda esa informacion a una hoja por aparte (Universidad no reconocida)
                                        else{
                                            GuardarFilaAuExtern(hojaGuardarInter,ContFilasExtern);
                                            ContFilasExtern++;
                                        }
                                        break;
                                    }   
                                }
                            }
                        }
                    }
               
                }
            }

            //Barra de progreso
            Cargando.getjProgressBar1().setValue((int) (Porciento*(IndiceFila+1)));
            Cargando.getjProgressBar1().setString(String.valueOf((int) (Porciento*(IndiceFila+1)))+"%");
        }
    }catch (IndexOutOfBoundsException | PatternSyntaxException |NullPointerException e) {
        JOptionPane.showMessageDialog(null, "Error,Asegurese de seleccionar correctamente el tipo de archivo y el archivo correcto","Error",JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
        
    }
    public String QuitaTildes(String palabra){
        palabra=palabra.replaceAll("á", "a");
        palabra=palabra.replaceAll("é", "e");
        palabra=palabra.replaceAll("í", "i");
        palabra=palabra.replaceAll("ó", "o");
        palabra=palabra.replaceAll("ú", "u");
        return palabra;
    }
    public int IdenNomDeCol_Y_numCol(Row fila) throws IOException{
        Iterator ColumnaIterator=fila.cellIterator();
        int numeroDeColumnas=0;
       
        while (ColumnaIterator.hasNext()) {
            Cell celda=(Cell)ColumnaIterator.next();
            
            
            try {
                //Quita las tildes y converte todo a miniscula 
                String infoCelda=QuitaTildes(celda.getStringCellValue().toLowerCase());
                //Quitar los espacios al final
            infoCelda=infoCelda.trim();
            switch (infoCelda) {
                //Estandar
                case("codigo"):
                    columnaCodigo=numeroDeColumnas;
                    break;
                case "titulo":
                    columnaTitulo=numeroDeColumnas;
                    break;
                case "autores con afiliacion":
                    columnaAutoresConAfi=numeroDeColumnas;
                    break;
                default:                                    
                    break;
            }
            numeroDeColumnas++;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al procesar el Excel ,cerciorese de que esta abriendo el archivo correcto","Error al procesar el Excel",JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            
        }
       //Si no encuentra alguna columna ,pone un mensaje de error con indicaciones  y se sale 
        if(columnaCodigo==-1||columnaTitulo==-1||columnaAutoresConAfi==-1){
            //Para que busque  en las otras hojas del Excel 
            if(numHoja<(book.getNumberOfSheets()-1)){
                numHoja++;
                ProcesarExcel();
                return 0;
            }
            else{
                JOptionPane.showMessageDialog(null, "Nombres de las columnas del archivo fuente deben ser repectivamente:\n\nCódigo|Título|Autores con afiliación\n\nTambien es necesario que los nombres de las columnas deben estar en la primer fila del Excel\nPor favor corrija los nombres de las columnas y vuelva a abrir el archivo\n\n \t ¡Recuerde guardar el Excel despues de realizar los cambios!\n\n ","Nombres de las columnas del archivo fuente incorrectas",JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
       return numeroDeColumnas;
    }
    //Guarda una nueva fila en la hoja de AutoresTEC
    public void GuardarFilaAuTEC(Sheet hoja,int fila){
        //Si alguno llega en null quierie decir que hubo un error probablemente en el orden de las columnas
        if(Codigo==null||Titulo==null||Autor==null||Escuela==null||Campus==null||Universidad==null||Pais==null){
            JOptionPane.showMessageDialog(null, "El orden de las columnas del archivo fuente deben estar en el siguente orden:\n\nCódigo|Título|Autores con afiliación\n\nPor favor corrija el orden de las columnas y vuelva a abrir el archivo","El orden de las columnas",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        Row filaNueva=hoja.createRow(fila);
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
    }
    //Guarda una nueva fila en la hoja de Autores Externos
    public void GuardarFilaAuExtern(Sheet hoja,int fila){
        Row filaNueva=hoja.createRow(fila);
        Cell celda00=filaNueva.createCell(0);
        celda00.setCellValue(Codigo);
        Cell celda01=filaNueva.createCell(1);
        celda01.setCellValue(Titulo);
        Cell celda02=filaNueva.createCell(2);
        celda02.setCellValue(Autor);
        Cell celda03=filaNueva.createCell(3);
        celda03.setCellValue(Universidad);
        Cell celda04=filaNueva.createCell(4);
        celda04.setCellValue(Pais);
    }
    //Filtra que no vaya filas duplicadas a la Jtable de conflictos
    public boolean DuplicadoTabla(DefaultTableModel model,Object[]ListaColumna){
        if(model.getRowCount()==0){
            return false;
        }
        boolean duplicado=true;
        for (int i = 0; i <model.getRowCount(); i++) {
             duplicado=true;
             for (int j = 0; j < 4; j++) {
                if(!(ListaColumna[j].toString().equals(model.getValueAt(i, j).toString()))){
                    duplicado=false;
                    break;
                }
            }
            if(duplicado!=false){
                return true;
            }
            
        }
    return duplicado;
    }
    //Añade filas a la tabla de conflictos
    public void añadirConflicto(Object[]ListaColumna,int Fila,String Tipo,String HojaExcel,String Info){
        
        ListaColumna[0]=Codigo;
        ListaColumna[1]=Fila;
        ListaColumna[2]=Tipo;
        ListaColumna[3]=HojaExcel;
        JButton boton =new JButton("Resolver");
        boton.setName(Info);
         ListaColumna[4]=boton;
      //Eliminar duplicados
       if(DuplicadoTabla(modelo, ListaColumna)!=true){
            modelo.addRow(ListaColumna);
            Conflictos.add(Info);
       }

    }
    //Nombres de las columnas de la hoja de AutoresTEC
    public void TitulosColTEC(Sheet hoja){
        Row fila1=hoja.createRow(0);
        Cell celda0=fila1.createCell(0);
        celda0.setCellValue("Código");
        Cell celda1=fila1.createCell(1);
        celda1.setCellValue("Título");
        Cell celda2=fila1.createCell(2);
        celda2.setCellValue("Autor");
        Cell celda3=fila1.createCell(3);
        celda3.setCellValue("Unidad de investigación");
        Cell celda4=fila1.createCell(4);
        celda4.setCellValue("Campus");
        Cell celda5=fila1.createCell(5);
        celda5.setCellValue("Universidad");
        Cell celda6=fila1.createCell(6);
        celda6.setCellValue("País");
    }
    //Nombres de las columnas de la hoja de Autores Externos
    public void TitulosColExter(Sheet hoja){
        Row fila1=hoja.createRow(0);
        Cell celda0=fila1.createCell(0);
        celda0.setCellValue("Código");
        Cell celda1=fila1.createCell(1);
        celda1.setCellValue("Título");
        Cell celda2=fila1.createCell(2);
        celda2.setCellValue("Autor");
        Cell celda3=fila1.createCell(3);
        celda3.setCellValue("Universidad");
        Cell celda4=fila1.createCell(4);
        celda4.setCellValue("País");
    }
    //Añade los botones de resolver conflictos (Estos botones son solo de decoracion ,no tienen funcionlidad, la accion de abrir la ventana
    //de resolver conflicto se hace por medio de un oyente del mouse jTable1MouseClicked dentro de la tabla de la clase Conflictos 
    public void ResolverConflictos(String InfoLinea){
            JButton boton =new JButton("Resolver");
            boton.setName(InfoLinea);
            modelo.setValueAt(boton, modelo.getRowCount()-1, modelo.findColumn("Resolver"));
    } 
    //Guarda el excel de salida
    public boolean GuardarExcel(File archivo) throws IOException{
        File fileC = new File (archivo.getAbsolutePath(),"AutoresTEC y Autores Externos ("+ArchivoEntrada.getName()+").xlsx");
        try ( 
                FileOutputStream fileout = new FileOutputStream(fileC.getAbsolutePath())) {
                book2.write(fileout);
                return true; 
                
        }
    }
    //Busca el autor en el formato en que viene  Scopus en toda la informacion
    public String buscaAutorScopus( String[] InfoFila){
         String autor;
         //Siempre despues del nombre viene una Inicial,identificamos esa inicial para encontrar el nombre
         for (int i = 0; i < InfoFila.length; i++) {
            //Las iniciales siempre estan en mayuscula y Las iniciales terminan con un punto
            if((InfoFila[i].matches("[A-Z].*")||(InfoFila[i].matches("[Á-Ú].*")))&&((InfoFila[i].matches("(.*)[.]")))){
                 //retornamos el nombre con la(s) iniciale(s)
                 //Antes de la inicial esta el nombre por eso i-1
                 autor=InfoFila[i-1]+" ".concat(InfoFila[i]);
                 return autor;
            }   
         }
        return "No encontrado";
    }
    //Busca el autor en el formato en que viene  Scopus antes de unas iniciales 
    public String AnalisisAutorScopus( String Autor,String Iniciales){
         String autor;
         //Siempre despues del nombre viene una Inicial,identificamos esa inicial para encontrar el nombre
        //Las iniciales siempre estan en mayuscula y Las iniciales terminan con un punto
        if((Iniciales.matches("[A-Z].*")||(Iniciales.matches("[Á-Ú].*")))&&((Iniciales.matches("(.*)[.]")))){
             //retornamos el nombre con la(s) iniciale(s)
             autor=Autor+" ".concat(Iniciales);
             return autor;
        }
        return "No encontrado";
    } 
    //comprueba si la informacio se trata del TEC
    public boolean AnalisisUTec(String[] InfoLinea1){
        String [] InfoLinea=InfoLinea1.clone();
   
        for (int i = 0; i < InfoLinea.length; i++) {
            //Convierte toda la info en minusculas
            InfoLinea[i]=InfoLinea[i].toLowerCase();
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
            if((InfoLinea[i].matches("(.*)tecnol(.*)"))&&(InfoLinea[i].matches("(.*)costa rica(.*)"))){
                return true;
            }
            if((InfoLinea[i].matches("(.*)tecno(.*)"))&&(InfoLinea[i].matches("(.*)costa rica(.*)"))){
                return true;
            }
            if((InfoLinea[i].matches("(.*)tech(.*)"))&&(InfoLinea[i].matches("(.*)costa rica(.*)"))){
                return true;
            }
        }
        return false; 
    }
    //Busca el nombre de un pais dentro de un arraylist de String
    public String BuscaPais(String[] InfoLinea) throws MalformedURLException, IOException,PatternSyntaxException{
        for (int i = 0; i < InfoLinea.length; i++) {
            String pais=InfoLinea[i];
            //Copia para identificar siglas
            pais=pais.toLowerCase();
            //asegurar que la primer letra del pais sea mayuscula
            pais=pais.replaceFirst(String.valueOf(pais.charAt(0)),String.valueOf(pais.charAt(0)).toUpperCase());
            //Quitar los espacios al final
            pais=pais.trim();
            //Si el pais se conforma de mas palabras
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
            //si esta dentro de la lista de paises 
            if(Paises.getPaises().contains(pais)){
                return pais;
            }    
        }
        //Si no lo encuentra por el nombre lo intenta encontrar por la abreviatutura del pais
        for (int i = 0; i < InfoLinea.length; i++) {
            String[] palab=InfoLinea[i].split(" ");
            for (int j = 0; j < palab.length; j++) {
                //si Todas las letras  son mayus
                if(palab[j].equals(palab[j].toUpperCase())){
                    for (int k = 0; k < Paises.getCodPaises().length; k++) {
                        if(palab[j].matches(Paises.getCodPaises()[k])){
                                return Paises.getCodPaises()[k];
                        }   
                    }
                } 
            }
        }
        return "No encontrado"; 
    }
    //Busca el nombre de una universidad dentro de un arraylist de String
    public String BuscarU(String[] InfoLinea) {
        for (int i = 0; i < InfoLinea.length; i++) {
            String infoMinus=InfoLinea[i].toLowerCase();
            infoMinus=infoMinus.trim();
            if(infoMinus.charAt(0)==' '){
                infoMinus=infoMinus.replaceFirst(" ", "");
            }
            //Si el String contiene esas letras consecutiva (universidad contiene univ en el nombre ,entonces lo reconoce
            if(infoMinus.matches("(.*)univ(.*)")||infoMinus.matches("(.*)inst(.*)")||infoMinus.matches("(.*)cent(.*)")
                ||infoMinus.matches("(.*)nacional(.*)")||infoMinus.matches("(.*)national(.*)")||infoMinus.matches("(.*)ctr(.*)")||infoMinus.matches("(.*)coll(.*)")){
                return InfoLinea[i];
            }
            //Universadades nacionales
            for (int j = 0; j < Universidades.getUNacional().length; j++) {
                //Paraque no pnga en la Universidad el pais
                if(InfoLinea[i] == null ? Pais != null : !InfoLinea[i].equals(Pais)){                  
                    String str=infoMinus.replaceAll("\\)", "");
                    str=str.replaceAll("\\(", "");
                    if(Universidades.getUNacional()[j].toLowerCase().matches(str)&&(!str.matches("(.*)[.]"))){
                        return InfoLinea[i];
                    }
                          
                }
            }
             //Universadades Internacionales
            for (int j = 0; j < Universidades.getUInternacional().length; j++) {
                //Paraque no pnga en la Universidad el pais
                if(InfoLinea[i] == null ? Pais != null : !InfoLinea[i].equals(Pais)){
                    String str=infoMinus.replaceAll("\\)", "");
                    str=str.replaceAll("\\(", "");
                    if(Universidades.getUInternacional()[j].toLowerCase().matches(str)&&(!str.matches("(.*)[.]"))){
                        return InfoLinea[i];
                    }   
                }
                
            }
        }
        return "No encontrado";
    }
    //Busca el nombre de una Unidad de investigacion o escuela dentro de un arraylist de String
    public String buscaEscuela( String[] InfoFila){
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
                //Este es muy especifico 
                if("DOCINADE".equals(siglas)||"CIADEG-TEC".equals(siglas)||"CIB".equals(siglas)||"CIC".equals(siglas)||"CIF".equals(siglas)||"CIPA".equals(siglas)||"CIVCO".equals(siglas)
                   ||"CEQIATEC".equals(siglas)||"CIDASTH".equals(siglas)||"CIEMTEC".equals(siglas)||"CIGA".equals(siglas)||"GASEL".equals(siglas)){
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
    //Busca el nombre de un campus dentro de un arraylist de String por el nombre de la provincia ,execepto San Carlos y Alajuela
    String buscaCampus( String[] InfoFila){
        if("CIDASTH - CENTRO DE INVESTIGACIÓN Y DESARROLLO EN AGRICULTURA SOSTENIBLE PARA EL TRÓPICO HÚMEDO".equals(Escuela)){
            return "3 - CAMPUS TECNOLOGICO LOCAL SAN CARLOS";   
        }
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
                if("DOCINADE".equals(Escuela)){
                    return "3 - CAMPUS TECNOLOGICO LOCAL SAN CARLOS";   
                }
                if("ECE - ESCUELA DE CIENCIAS NATURALES Y EXACTAS".equals(Escuela)){
                    return "3 - CAMPUS TECNOLOGICO LOCAL SAN CARLOS";   
                }
                if("EIC - ESCUELA DE IDIOMAS Y CIENCIAS SOCIALES".equals(Escuela)){
                    return "3 - CAMPUS TECNOLOGICO LOCAL SAN CARLOS";   
                }
                if("CIADEGTEC - CENTRO DE INVESTIGACIÓN EN ADMINISTRACIÓN".equals(Escuela)){
                    return "3 - CAMPUS TECNOLOGICO LOCAL SAN CARLOS";   
                }
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
                    AutoresWoS.add(autor[j]);
                    //solo se necesita la info
                    formato=formato.concat(Info);
                    formato  = formato+"; ";
                  } 
              }    
        } 
        formato = formato.replaceAll(" ; ", "; ");
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

