package modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AgregarCAModelo {
    private static final String CARPETA_CLIENTES = "datos_clientes";
    private static final String CARPETA_AUTOS = "datos_autos";
    private static final int MAX_AUTOS = 100;
    
    public AgregarCAModelo() {
        // Crear carpetas si no existen
        new File(CARPETA_CLIENTES).mkdirs();
        new File(CARPETA_AUTOS).mkdirs();
    }
    
    public boolean agregarClienteAutos(String dpi, String nombre, String usuario, 
                                     String contraseña, String tipoCliente, String automovilesStr) {
        try {
            //VERIFICAR SI EL AUTO EXISTE
            File archivoExistente = new File(CARPETA_CLIENTES, usuario + ".txt");
            if (archivoExistente.exists()) {
                return false;
            }
            
            //GUARDAR INFORMACION DEL CLIENTE
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoExistente))) {
                writer.write("DPI: " + dpi + "\n");
                writer.write("Nombre: " + nombre + "\n");
                writer.write("Usuario: " + usuario + "\n");
                writer.write("Contraseña: " + contraseña + "\n");
                writer.write("TipoCliente: " + tipoCliente + "\n");
            }
            
            //PROCESAR MULTIPLES AUTOS
            String[] autos = automovilesStr.split(";");
            int autosRegistrados = 0;
            
            for (String autoStr : autos) {
                if (autosRegistrados >= MAX_AUTOS) break;
                
                String[] datosAuto = autoStr.trim().split(",");
                if (datosAuto.length == 4) { // placa,marca,modelo,imagen
                    File archivoAuto = new File(CARPETA_AUTOS, usuario + "_" + datosAuto[0] + ".txt");
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoAuto))) {
                        writer.write("Placa: " + datosAuto[0] + "\n");
                        writer.write("Marca: " + datosAuto[1] + "\n");
                        writer.write("Modelo: " + datosAuto[2] + "\n");
                        writer.write("Imagen: " + datosAuto[3] + "\n");
                        autosRegistrados++;
                    }
                }
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("ERROR AL GUARDAR EL CLIENTE Y AUTO: " + e.getMessage());
            return false;
        }
    }
    
    public static String[] parseAutomovil(String automovilStr) {
        return automovilStr.split(",");
    }
}