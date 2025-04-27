package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.ImageIcon;
import java.awt.Image;

public class VerAutosModelo {
    private static final String CARPETA_AUTOS = "datos_autos";
    
    public String[][] obtenerAutosUsuario(String usuario) {
        //OBTENER AUTOS DE ARCHIVOS
        File carpeta = new File(CARPETA_AUTOS);
        File[] archivos = carpeta.listFiles((dir, name) -> name.startsWith(usuario + "_"));
        
        //OBTENER AUTOS DEL MODELO DE REGISTRO
        RegistrarAutosModelo.Auto[] autosRegistrados = RegistrarAutosModelo.getInstance().getAutosPorUsuario(usuario);
        
        int totalArchivos = (archivos != null) ? archivos.length : 0;
        int totalRegistrados = (autosRegistrados != null) ? autosRegistrados.length : 0;
        int totalAutos = totalArchivos + totalRegistrados;
        
        if (totalAutos == 0) {
            return null;
        }
        
        String[][] autos = new String[totalAutos][4];
        int index = 0;
        
        //PROCESAR ARCHIVOS
        if (archivos != null) {
            for (File archivo : archivos) {
                try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        if (linea.startsWith("Placa: ")) {
                            autos[index][0] = linea.substring(7).trim();
                        } else if (linea.startsWith("Marca: ")) {
                            autos[index][1] = linea.substring(7).trim();
                        } else if (linea.startsWith("Modelo: ")) {
                            autos[index][2] = linea.substring(8).trim();
                        } else if (linea.startsWith("Imagen: ")) {
                            autos[index][3] = linea.substring(8).trim();
                        }
                    }
                    index++;
                } catch (IOException e) {
                    System.err.println("ERROR AL LEER EL ARCHIVO: " + archivo.getName());
                }
            }
        }
        
        //PROCESAR AUTOS REGISTRADOS
        if (autosRegistrados != null) {
            for (RegistrarAutosModelo.Auto auto : autosRegistrados) {
                autos[index][0] = auto.getPlaca();
                autos[index][1] = auto.getMarca();
                autos[index][2] = auto.getModelo();
                autos[index][3] = auto.getRutaImagen();
                index++;
            }
        }
        
        return autos;
    }
    
    public ImageIcon obtenerImagen(String rutaImagen, int ancho, int alto) {
        if (rutaImagen == null || rutaImagen.isEmpty()) {
            return null;
        }
        
        try {
            File archivoImagen = new File(rutaImagen);
            if (!archivoImagen.exists()) {
                return null;
            }
            
            ImageIcon icono = new ImageIcon(rutaImagen);
            Image imagen = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(imagen);
        } catch (Exception e) {
            System.err.println("ERROR AL CARGAR LA IMAGEN: " + e.getMessage());
            return null;
        }
    }
    
    public void shellSort(String[][] autos, boolean ascendente) {
        if (autos == null || autos.length == 0) return;
        
        int n = autos.length;
        for (int gap = n/2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                String[] temp = autos[i];
                String tempPlaca = temp[0];
                int j;
                
                for (j = i; j >= gap; j -= gap) {
                    String[] comparar = autos[j - gap];
                    String compararPlaca = comparar[0];
                    
                    boolean condicion = ascendente ? 
                        tempPlaca.compareTo(compararPlaca) < 0 : 
                        tempPlaca.compareTo(compararPlaca) > 0;
                    
                    if (condicion) {
                        autos[j] = autos[j - gap];
                    } else {
                        break;
                    }
                }
                autos[j] = temp;
            }
        }
    }
}