package modelo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;

public class RegistrarAutosModelo {
    private static final String CARPETA_DATOS = "datos_autos";
    private static final String CARPETA_IMAGENES = "imagenes_autos";
    
    public RegistrarAutosModelo() {
        crearCarpetasSiNoExisten();
    }
    
    private void crearCarpetasSiNoExisten() {
        File carpetaDatos = new File(CARPETA_DATOS);
        File carpetaImagenes = new File(CARPETA_IMAGENES);
        
        if (!carpetaDatos.exists()) {
            carpetaDatos.mkdir();
        }
        
        if (!carpetaImagenes.exists()) {
            carpetaImagenes.mkdir();
        }
    }
    
    public boolean guardarAuto(String usuario, String placa, String marca, String modelo, ImageIcon imagen) {
        //GUARDAR DATOS DEL AUTO
        String nombreArchivo = CARPETA_DATOS + File.separator + usuario + "_" + placa + ".txt";
        
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write("Usuario: " + usuario + "\n");
            writer.write("Placa: " + placa + "\n");
            writer.write("Marca: " + marca + "\n");
            writer.write("Modelo: " + modelo + "\n");
            
            //PARA GUARDAR LA IMAGEN 
            if (imagen != null) {
                String nombreImagen = CARPETA_IMAGENES + File.separator + usuario + "_" + placa + ".jpg";
                File archivoImagen = new File(nombreImagen);
                
                //SI YA EXISTE UNA IMAGEN, SE SOBRESCRIBE
                if (archivoImagen.exists()) {
                    archivoImagen.delete();
                }
                
                //GUARDAR LA REFERENCIA A LA IMAGEN
                writer.write("Imagen: " + nombreImagen + "\n");
                
                //GUARDAR IMAGEN
                File outputFile = new File(nombreImagen);
                java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(
                    imagen.getIconWidth(),
                    imagen.getIconHeight(),
                    java.awt.image.BufferedImage.TYPE_INT_RGB);
                java.awt.Graphics g = bi.createGraphics();
                imagen.paintIcon(null, g, 0, 0);
                g.dispose();
                javax.imageio.ImageIO.write(bi, "jpg", outputFile);
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("ERROR AL GUARDAR EL AUTO: " + e.getMessage());
            return false;
        }
    }
    
    public String[] buscarAuto(String usuario, String placa) {
        String nombreArchivo = CARPETA_DATOS + File.separator + usuario + "_" + placa + ".txt";
        File archivo = new File(nombreArchivo);
        
        if (!archivo.exists()) {
            return null;
        }
        
        try {
            java.util.List<String> lineas = Files.readAllLines(archivo.toPath());
            String[] datos = new String[5]; // usuario, placa, marca, modelo, rutaImagen
            
            for (String linea : lineas) {
                if (linea.startsWith("Usuario: ")) {
                    datos[0] = linea.substring("Usuario: ".length());
                } else if (linea.startsWith("Placa: ")) {
                    datos[1] = linea.substring("Placa: ".length());
                } else if (linea.startsWith("Marca: ")) {
                    datos[2] = linea.substring("Marca: ".length());
                } else if (linea.startsWith("Modelo: ")) {
                    datos[3] = linea.substring("Modelo: ".length());
                } else if (linea.startsWith("Imagen: ")) {
                    datos[4] = linea.substring("Imagen: ".length());
                }
            }
            
            return datos;
        } catch (IOException e) {
            System.err.println("EROR AL LEER EL AUTO: " + e.getMessage());
            return null;
        }
    }
}