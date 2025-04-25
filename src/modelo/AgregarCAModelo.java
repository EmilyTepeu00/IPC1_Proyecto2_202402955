package modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AgregarCAModelo {
    private static final String CARPETA_CLIENTES = "datos_clientes";
    private static final String CARPETA_AUTOS = "datos_autos";
    
    public AgregarCAModelo() {
        // Crear carpetas si no existen
        new File(CARPETA_CLIENTES).mkdirs();
        new File(CARPETA_AUTOS).mkdirs();
    }
    
    public boolean agregarClienteAuto(String dpi, String nombre, String usuario, String contraseña, 
                                    String tipoCliente, String[] datosAuto) {
        try {
            // Verificar si el DPI ya existe
            File archivoExistente = new File(CARPETA_CLIENTES, usuario + ".txt");
            if (archivoExistente.exists()) {
                return false;
            }
            
            // Guardar información del cliente
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoExistente))) {
                writer.write("DPI: " + dpi + "\n");
                writer.write("Nombre: " + nombre + "\n");
                writer.write("Usuario: " + usuario + "\n");
                writer.write("Contraseña: " + contraseña + "\n");
                writer.write("TipoCliente: " + tipoCliente + "\n");
            }
            
            // Guardar información del auto
            if (datosAuto != null && datosAuto.length >= 4) {
                File archivoAuto = new File(CARPETA_AUTOS, usuario + "_" + datosAuto[0] + ".txt");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoAuto))) {
                    writer.write("Placa: " + datosAuto[0] + "\n");
                    writer.write("Marca: " + datosAuto[1] + "\n");
                    writer.write("Modelo: " + datosAuto[2] + "\n");
                    writer.write("Imagen: " + datosAuto[3] + "\n");
                }
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar cliente y auto: " + e.getMessage());
            return false;
        }
    }
    
    public static String[] parseAutomovil(String automovilStr) {
        return automovilStr.split(",");
    }
}