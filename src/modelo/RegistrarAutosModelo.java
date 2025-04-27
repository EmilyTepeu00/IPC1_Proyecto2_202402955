package modelo;

import java.io.*;
import javax.swing.ImageIcon;

public class RegistrarAutosModelo implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String ARCHIVO_DATOS = "registro_autos.dat";
    private static final String CARPETA_IMAGENES = "imagenes_autos";
    private static final SerializadorModelo serializador = new SerializadorModelo();
    private static RegistrarAutosModelo instancia;
    
    private Auto[] autosRegistrados;
    private int capacidad;
    private int cantidadAutos;

    private RegistrarAutosModelo() {
        this.capacidad = 100; //CAPACIDAD INICIAL
        this.autosRegistrados = new Auto[capacidad];
        this.cantidadAutos = 0;
        crearCarpetasSiNoExisten();
    }

    public static RegistrarAutosModelo getInstance() {
        if (instancia == null) {
            instancia = cargarDatos();
            if (instancia == null) {
                instancia = new RegistrarAutosModelo();
            }
        }
        return instancia;
    }

    public static void guardarDatos() {
        serializador.guardarDatos(ARCHIVO_DATOS, getInstance());
    }

    private static RegistrarAutosModelo cargarDatos() {
        RegistrarAutosModelo modelo = (RegistrarAutosModelo) serializador.cargarDatos(ARCHIVO_DATOS);
        if (modelo != null) {
            modelo.crearCarpetasSiNoExisten();
        }
        return modelo;
    }

    private void crearCarpetasSiNoExisten() {
        File carpetaImagenes = new File(CARPETA_IMAGENES);
        if (!carpetaImagenes.exists()) {
            carpetaImagenes.mkdirs();
        }
    }

    public boolean guardarAuto(String usuario, String placa, String marca, String modelo, ImageIcon imagen) {
        //ELIMINAR AUTO EXISTENTE CON MISMA PLACA
        eliminarAuto(usuario, placa);
        
        //GUARDAR IMAGEN
        String rutaImagen = guardarImagen(usuario, placa, imagen);
        
        //CREAR Y AGREGAR EL AUTO NUEVO
        Auto nuevoAuto = new Auto(usuario, placa, marca, modelo, rutaImagen);
        
        if (cantidadAutos >= capacidad) {
            capacidad *= 2;
            Auto[] nuevoArray = new Auto[capacidad];
            for (int i = 0; i < cantidadAutos; i++) {
                nuevoArray[i] = autosRegistrados[i];
            }
            autosRegistrados = nuevoArray;
        }
        
        autosRegistrados[cantidadAutos] = nuevoAuto;
        cantidadAutos++;
        
        guardarDatos();
        return true;
    }

    private String guardarImagen(String usuario, String placa, ImageIcon imagen) {
        if (imagen != null) {
            try {
                String nombreImagen = CARPETA_IMAGENES + File.separator + usuario + "_" + placa + ".jpg";
                File outputFile = new File(nombreImagen);
                
                java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(
                    imagen.getIconWidth(),
                    imagen.getIconHeight(),
                    java.awt.image.BufferedImage.TYPE_INT_RGB);
                java.awt.Graphics g = bi.createGraphics();
                imagen.paintIcon(null, g, 0, 0);
                g.dispose();
                javax.imageio.ImageIO.write(bi, "jpg", outputFile);
                
                return nombreImagen;
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    public String[] buscarAuto(String usuario, String placa) {
        for (int i = 0; i < cantidadAutos; i++) {
            Auto auto = autosRegistrados[i];
            if (auto.getUsuario().equals(usuario) && auto.getPlaca().equals(placa)) {
                String[] datos = new String[4];
                datos[0] = auto.getPlaca();
                datos[1] = auto.getMarca();
                datos[2] = auto.getModelo();
                datos[3] = auto.getRutaImagen();
                return datos;
            }
        }
        return null;
    }

    public static boolean eliminarAuto(String usuario, String placa) {
        RegistrarAutosModelo modelo = getInstance();
        
        for (int i = 0; i < modelo.cantidadAutos; i++) {
            Auto auto = modelo.autosRegistrados[i];
            if (auto.getUsuario().equals(usuario) && auto.getPlaca().equals(placa)) {
                //ELIMINAR IMAGEN ASOCIADA
                if (auto.getRutaImagen() != null) {
                    new File(auto.getRutaImagen()).delete();
                }
                
                //MOVER ELEMENTOS RESTANTES
                for (int j = i; j < modelo.cantidadAutos - 1; j++) {
                    modelo.autosRegistrados[j] = modelo.autosRegistrados[j + 1];
                }
                modelo.cantidadAutos--;
                modelo.autosRegistrados[modelo.cantidadAutos] = null;
                
                guardarDatos();
                return true;
            }
        }
        return false;
    }

    public Auto[] getAutosPorUsuario(String usuario) {
        Auto[] temp = new Auto[cantidadAutos];
        int count = 0;
        
        for (int i = 0; i < cantidadAutos; i++) {
            if (autosRegistrados[i].getUsuario().equals(usuario)) {
                temp[count++] = autosRegistrados[i];
            }
        }
        
        if (count == 0) {
            return null;
        }
        
        Auto[] resultado = new Auto[count];
        for (int i = 0; i < count; i++) {
            resultado[i] = temp[i];
        }
        return resultado;
    }

    public static class Auto implements Serializable {
        private static final long serialVersionUID = 1L;
        private String usuario;
        private String placa;
        private String marca;
        private String modelo;
        private String rutaImagen;

        public Auto(String usuario, String placa, String marca, String modelo, String rutaImagen) {
            this.usuario = usuario;
            this.placa = placa;
            this.marca = marca;
            this.modelo = modelo;
            this.rutaImagen = rutaImagen;
        }

        public String getUsuario() { return usuario; }
        public String getPlaca() { return placa; }
        public String getMarca() { return marca; }
        public String getModelo() { return modelo; }
        public String getRutaImagen() { return rutaImagen; }
    }
    
    static {
        getInstance();
    }
}