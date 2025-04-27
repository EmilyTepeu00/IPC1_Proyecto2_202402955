package controlador;

import modelo.SerializadorModelo;

public class SerializacionControlador {
    private static final SerializadorModelo serializador = new SerializadorModelo();
    
    public SerializacionControlador() {
    }
    
    public static void guardarTodosDatos() {
        //GUARDAR MODELOS PARA PERSISTENCIA
        modelo.RepuestosModelo.guardarDatos();
        modelo.ServiciosModelo.guardarDatos();
        modelo.ClientesAutosModelo.guardarDatos();
        modelo.ProgresoModelo.guardarDatos();
        modelo.RegistrarAutosModelo.guardarDatos();
        modelo.ColaEsperaModelo.guardarDatos();
    }
    
    public static void cargarTodosDatos() {
    }
    
    public static void realizarBackup() {
    }
}