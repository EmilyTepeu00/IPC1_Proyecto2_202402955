package controlador;

import modelo.ColaEsperaModelo;
import modelo.BitacoraModelo;
import modelo.RegistroModelo;

public class ColaEsperaControlador {
    private ColaEsperaModelo modelo;
    private String usuarioActual;
    
    public ColaEsperaControlador() {
        this.modelo = ColaEsperaModelo.getInstance(100); //CAPACIDAD
        this.usuarioActual = RegistroModelo.getInstance().getUsuarioActual();
    }
    
    public boolean agregarVehiculo(String placa, String marca, String modelo, String servicio, String tipoCliente) {
        this.modelo.encolarConPrioridad(placa, marca, modelo, servicio, tipoCliente);
        BitacoraModelo.registrarEvento(usuarioActual, "Agregar a Cola", "Éxito", "Vehículo agregado a cola: " + placa);
        return true;
    }
    
    public String[] atenderSiguiente() {
        String[] vehiculo = modelo.desencolar();
        if (vehiculo != null) {
            BitacoraModelo.registrarEvento(usuarioActual, "Atender Vehículo", "Éxito", 
                "Vehículo atendido: " + vehiculo[0]);
        }
        return vehiculo;
    }
    
    public String[][] obtenerColaCompleta() {
        return modelo.obtenerTodosEnCola();
    }
    
    public int obtenerCantidadEnCola() {
        return modelo.cantidadEnCola();
    }
    
    public String[] verProximo() {
        return modelo.verPrimero();
    }
}