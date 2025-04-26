package modelo;

import modelo.ClientesAutosModelo.Cliente;
import modelo.ServiciosModelo.Servicio;


public class ProgresoModelo {
    private String[][] vehiculosEnProceso;
    private int contadorVehiculos;
    private VerAutosModelo verAutosModelo;
    
    public ProgresoModelo() {
        this.vehiculosEnProceso = new String[100][4]; // placa, marca, modelo, servicio
        this.contadorVehiculos = 0;
        this.verAutosModelo = new VerAutosModelo();
    }
    
    public String[][] obtenerVehiculosCliente(String usuario) {
        //OBTENER DE ARCHIVOS SI LOS HAY
        String[][] autos = verAutosModelo.obtenerAutosUsuario(usuario);
        
        //SI NO HAY, SE BUSCA EN LOS DAROS DEL CLIENTE
        if (autos == null || autos.length == 0) {
            autos = obtenerAutosDesdeCliente(usuario);
        }
        
        return autos;
    }
    
    private String[][] obtenerAutosDesdeCliente(String usuario) {
        Cliente cliente = ClientesAutosModelo.buscarClientePorUsuario(usuario);
        if (cliente != null && cliente.getAutomovil() != null && !cliente.getAutomovil().isEmpty()) {
            String[] datosAuto = cliente.getAutomovil().split(",");
            if (datosAuto.length >= 4) {
                String[][] resultado = new String[1][4];
                resultado[0][0] = datosAuto[0].trim(); // Placa
                resultado[0][1] = datosAuto[1].trim(); // Marca
                resultado[0][2] = datosAuto[2].trim(); // Modelo
                resultado[0][3] = datosAuto[3].trim(); // Imagen
                return resultado;
            }
        }
        return null;
    }
    
    public Servicio[] obtenerTodosServicios() {
        return ServiciosModelo.obtenerTodosServicios();
    }
    
    public boolean agregarVehiculoProceso(String placa, String marca, String modelo, String servicio) {
        if (contadorVehiculos >= vehiculosEnProceso.length) {
            return false;
        }
        
        vehiculosEnProceso[contadorVehiculos][0] = placa;
        vehiculosEnProceso[contadorVehiculos][1] = marca;
        vehiculosEnProceso[contadorVehiculos][2] = modelo;
        vehiculosEnProceso[contadorVehiculos][3] = servicio;
        contadorVehiculos++;
        return true;
    }
    
    public String[][] getVehiculosEnProceso() {
        String[][] resultado = new String[contadorVehiculos][4];
        System.arraycopy(vehiculosEnProceso, 0, resultado, 0, contadorVehiculos);
        return resultado;
    }
    
    public boolean verificarCompatibilidad(String marcaVehiculo, String modeloVehiculo, String nombreServicio) {
        Servicio[] servicios = ServiciosModelo.obtenerTodosServicios();
        for (Servicio servicio : servicios) {
            if (servicio != null && servicio.getNombre().equalsIgnoreCase(nombreServicio)) {
                return servicio.getMarca().equalsIgnoreCase(marcaVehiculo) && 
                       servicio.getModelo().equalsIgnoreCase(modeloVehiculo);
            }
        }
        return false;
    }
}