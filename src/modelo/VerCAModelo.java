package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class VerCAModelo {
    private static final String CARPETA_CLIENTES = "datos_clientes";
    private static final String CARPETA_AUTOS = "datos_autos";
    
    public static String[][] obtenerDatosClientesYAutos() {
        File carpetaClientes = new File(CARPETA_CLIENTES);
        File[] archivosClientes = carpetaClientes.listFiles();
        int totalClientes = countClientes();
        String[][] datos = new String[totalClientes][6]; 
        
        if (archivosClientes != null) {
            int index = 0;
            for (File archivoCliente : archivosClientes) {
                try (BufferedReader reader = new BufferedReader(new FileReader(archivoCliente))) {
                    String linea;
                    String dpi = "";
                    String nombre = "";
                    String usuario = "";
                    String contraseña = "";
                    String tipoCliente = "";
                    StringBuilder automoviles = new StringBuilder();
                    
                    while ((linea = reader.readLine()) != null) {
                        if (linea.startsWith("DPI: ")) dpi = linea.substring(5);
                        else if (linea.startsWith("Nombre: ")) nombre = linea.substring(8);
                        else if (linea.startsWith("Usuario: ")) usuario = linea.substring(9);
                        else if (linea.startsWith("Contraseña: ")) contraseña = linea.substring(12);
                        else if (linea.startsWith("TipoCliente: ")) tipoCliente = linea.substring(13);
                    }
                    
                    // OBTENER AUTOS DEL CLIENTE
                    File carpetaAutos = new File(CARPETA_AUTOS);
                    final String usuarioFinal = usuario;
                    File[] archivosAutos = carpetaAutos.listFiles(new java.io.FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            return name.startsWith(usuarioFinal + "_");
                        }
                    });
                    
                    if (archivosAutos != null) {
                        for (File archivoAuto : archivosAutos) {
                            try (BufferedReader autoReader = new BufferedReader(new FileReader(archivoAuto))) {
                                String lineaAuto;
                                String placa = "";
                                String marca = "";
                                String modelo = "";
                                
                                while ((lineaAuto = autoReader.readLine()) != null) {
                                    if (lineaAuto.startsWith("Placa: ")) placa = lineaAuto.substring(7);
                                    else if (lineaAuto.startsWith("Marca: ")) marca = lineaAuto.substring(7);
                                    else if (lineaAuto.startsWith("Modelo: ")) modelo = lineaAuto.substring(8);
                                }
                                
                                if (automoviles.length() > 0) {
                                    automoviles.append("<br>");
                                }
                                automoviles.append(String.format(
                                    "Placa: %s, Marca: %s, Modelo: %s", 
                                    placa, marca, modelo
                                ));
                            }
                        }
                    }
                    
                    datos[index++] = new String[]{
                        dpi, 
                        nombre, 
                        usuario, 
                        contraseña, 
                        tipoCliente, 
                        "<html>" + automoviles.toString() + "</html>"
                    };
                } catch (IOException e) {
                    System.err.println("ERROR AL LEER EL ARCHIVO: " + e.getMessage());
                }
            }
            
            //METODO BURBUJA PARA ORDENAR CLIENTES
            ordenarPorDPIBurbuja(datos);
        }
        return datos;
    }
    
    //ORDENAMIENTO ASCENDENTE SEGUN EL DPI
    private static void ordenarPorDPIBurbuja(String[][] datos) {
        int n = datos.length;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (datos[j][0].compareTo(datos[j+1][0]) > 0) {
                    //INTERCAMBIAR FILAS COMPLETAS
                    String[] temp = datos[j];
                    datos[j] = datos[j+1];
                    datos[j+1] = temp;
                }
            }
        }
    }
    
    private static int countClientes() {
        File carpetaClientes = new File(CARPETA_CLIENTES);
        File[] archivos = carpetaClientes.listFiles();
        return archivos != null ? archivos.length : 0;
    }
    
    public static void actualizarTipoCliente(String usuario, String nuevoTipo) {
        File archivoCliente = new File(CARPETA_CLIENTES, usuario + ".txt");
        File tempFile = new File(CARPETA_CLIENTES, usuario + "_temp.txt");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoCliente));
             FileWriter writer = new FileWriter(tempFile)) {
            
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("TipoCliente: ")) {
                    writer.write("TipoCliente: " + nuevoTipo + "\n");
                } else {
                    writer.write(linea + "\n");
                }
            }
        } catch (IOException e) {
        }
        
        if (archivoCliente.delete()) {
            tempFile.renameTo(archivoCliente);
        }
    }
}