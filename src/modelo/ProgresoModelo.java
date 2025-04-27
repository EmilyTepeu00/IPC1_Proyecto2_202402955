package modelo;

import modelo.ClientesAutosModelo.Cliente;
import modelo.ServiciosModelo.Servicio;
import java.io.*;

public class ProgresoModelo {
    private String[][] vehiculosEnProceso;
    private int contadorVehiculos;
    private VerAutosModelo verAutosModelo;
    private ColaEsperaModelo colaEspera;
    private int serviciosCompletados;
    
    public ProgresoModelo() {
        this.vehiculosEnProceso = new String[100][5];
        this.contadorVehiculos = 0;
        this.verAutosModelo = new VerAutosModelo();
        this.colaEspera = new ColaEsperaModelo(100);
        this.serviciosCompletados = 0;
    }
    
    public String[][] obtenerVehiculosCliente(String usuario) {
        String[][] autos = verAutosModelo.obtenerAutosUsuario(usuario);
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
                resultado[0][0] = datosAuto[0].trim();
                resultado[0][1] = datosAuto[1].trim();
                resultado[0][2] = datosAuto[2].trim();
                resultado[0][3] = datosAuto[3].trim();
                return resultado;
            }
        }
        return null;
    }
    
    public Servicio[] obtenerTodosServicios() {
        return ServiciosModelo.obtenerTodosServicios();
    }
    
    public boolean agregarVehiculoProceso(String placa, String marca, String modelo, String servicio, String usuario) {
        if (contadorVehiculos >= vehiculosEnProceso.length) {
            return false;
        }
        
        String tipoCliente = obtenerTipoCliente(usuario);
        vehiculosEnProceso[contadorVehiculos][0] = placa;
        vehiculosEnProceso[contadorVehiculos][1] = marca;
        vehiculosEnProceso[contadorVehiculos][2] = modelo;
        vehiculosEnProceso[contadorVehiculos][3] = servicio;
        vehiculosEnProceso[contadorVehiculos][4] = tipoCliente;
        
        colaEspera.encolarConPrioridad(placa, marca, modelo, servicio, tipoCliente);
        contadorVehiculos++;
        return true;
    }
    
    private String obtenerTipoCliente(String usuario) {
        Cliente cliente = ClientesAutosModelo.buscarClientePorUsuario(usuario);
        return cliente != null ? cliente.getTipoCliente() : "NORMAL";
    }
    
    public void servicioCompletado(String usuario) {
        serviciosCompletados++;
        if (serviciosCompletados >= 4) {
            actualizarClienteAOro(usuario);
            serviciosCompletados = 0;
        }
    }
    
    private void actualizarClienteAOro(String usuario) {
        String carpetaClientes = "datos_clientes";
        File archivoCliente = new File(carpetaClientes, usuario + ".txt");
        File archivoTemp = new File(carpetaClientes, usuario + "_temp.txt");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoCliente));
             BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTemp))) {
            
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("TipoCliente: ")) {
                    writer.write("TipoCliente: ORO\n");
                } else {
                    writer.write(linea + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR AL ACTUALIZAR EL CLIENTE A ORO: " + e.getMessage());
            return;
        }
        
        if (archivoCliente.delete()) {
            archivoTemp.renameTo(archivoCliente);
        }
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
    
    public String[][] getVehiculosEnProceso() {
        String[][] resultado = new String[contadorVehiculos][5];
        System.arraycopy(vehiculosEnProceso, 0, resultado, 0, contadorVehiculos);
        return resultado;
    }
}