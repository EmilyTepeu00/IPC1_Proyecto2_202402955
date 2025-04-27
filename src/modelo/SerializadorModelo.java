package modelo;

import java.io.*;

public class SerializadorModelo {
    private static final String DIRECTORIO_DATOS = "datos_serializados/";
    
    static {
        new File(DIRECTORIO_DATOS).mkdirs();
    }
    
    //GUARDAR UN OBJETO EN UN ARCHIVO BINARIO
    public boolean guardarDatos(String nombreArchivo, Object objeto) {
        String rutaCompleta = DIRECTORIO_DATOS + nombreArchivo;
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(rutaCompleta))) {
            oos.writeObject(objeto);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    //CARGAR UN OBJETO DESDE UN ARCHIVO BINARIO
    public Object cargarDatos(String nombreArchivo) {
        String rutaCompleta = DIRECTORIO_DATOS + nombreArchivo;
        File archivo = new File(rutaCompleta);
        if (!archivo.exists()) return null;
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(rutaCompleta))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
    
    //VERIFICAR SI EXISTE UN ARCHIVO DE DATOS
    public boolean existeArchivo(String nombreArchivo) {
        return new File(DIRECTORIO_DATOS + nombreArchivo).exists();
    }
}