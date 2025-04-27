package modelo;

import java.io.Serializable;

public class ProgresoModelo implements Serializable {
    private String[][] vehiculosEnProceso;
    private int contadorVehiculos;
    private int serviciosCompletados;
    private static final String ARCHIVO_DATOS = "progreso.dat";
    private static final SerializadorModelo serializador = new SerializadorModelo();
    private static ProgresoModelo instancia;
    private static final long serialVersionUID = 1L;
    
    //INSTANCIA DE MODELOS
    private VerAutosModelo verAutosModelo = new VerAutosModelo();
    private ColaEsperaModelo colaEsperaModelo = new ColaEsperaModelo(100);

    private ProgresoModelo() {
        this.vehiculosEnProceso = new String[100][5];
        this.contadorVehiculos = 0;
        this.serviciosCompletados = 0;
    }

    public static ProgresoModelo getInstance() {
        if (instancia == null) {
            instancia = cargarDatos();
            if (instancia == null) {
                instancia = new ProgresoModelo();
            }
        }
        return instancia;
    }

    public static void guardarDatos() {
        serializador.guardarDatos(ARCHIVO_DATOS, getInstance());
    }

    private static ProgresoModelo cargarDatos() {
        return (ProgresoModelo) serializador.cargarDatos(ARCHIVO_DATOS);
    }

    public String[][] obtenerVehiculosCliente(String usuario) {
        String[][] autos = verAutosModelo.obtenerAutosUsuario(usuario);
        if (autos == null || autos.length == 0) {
            autos = obtenerAutosDesdeCliente(usuario);
        }
        return autos;
    }
    
    private String[][] obtenerAutosDesdeCliente(String usuario) {
        ClientesAutosModelo.Cliente cliente = ClientesAutosModelo.buscarClientePorUsuario(usuario);
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
    
    public ServiciosModelo.Servicio[] obtenerTodosServicios() {
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
        colaEsperaModelo.encolarConPrioridad(placa, marca, modelo, servicio, tipoCliente);
        contadorVehiculos++;
        guardarDatos();
        return true;
    }
    
    private String obtenerTipoCliente(String usuario) {
        ClientesAutosModelo.Cliente cliente = ClientesAutosModelo.buscarClientePorUsuario(usuario);
        return cliente != null ? cliente.getTipoCliente() : "NORMAL";
    }
    
    public void servicioCompletado(String usuario) {
        serviciosCompletados++;
        if (serviciosCompletados >= 4) {
            actualizarClienteAOro(usuario);
            serviciosCompletados = 0;
        }
        guardarDatos();
    }
    
    private void actualizarClienteAOro(String usuario) {
        ClientesAutosModelo.Cliente cliente = ClientesAutosModelo.buscarClientePorUsuario(usuario);
        if (cliente != null) {
            cliente.setTipoCliente("ORO");
            ClientesAutosModelo.guardarDatos();
        }
    }
    
    public boolean verificarCompatibilidad(String marcaVehiculo, String modeloVehiculo, String nombreServicio) {
        ServiciosModelo.Servicio[] servicios = ServiciosModelo.obtenerTodosServicios();
        for (ServiciosModelo.Servicio servicio : servicios) {
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