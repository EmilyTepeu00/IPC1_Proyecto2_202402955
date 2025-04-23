package modelo;

public class ClientesAutosModelo {
    private static final int MAX_CLIENTES = 100;
    private static Cliente[] clientes = new Cliente[MAX_CLIENTES];
    private static int contadorClientes = 0;
    private static int siguienteId = 1001;

    public static class Cliente {
        private int id;
        private String nombreCompleto;
        private String usuario;
        private String contraseña;
        private String tipoCliente;
        private String[] automoviles;

        public Cliente(int id, String nombreCompleto, String usuario, String contraseña, String tipoCliente, String[] automoviles) {
            this.id = id;
            this.nombreCompleto = nombreCompleto;
            this.usuario = usuario;
            this.contraseña = contraseña;
            this.tipoCliente = tipoCliente;
            this.automoviles = automoviles;
        }

        // Getters y Setters
        public int getId() { return id; }
        public String getNombreCompleto() { return nombreCompleto; }
        public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        public String getContraseña() { return contraseña; }
        public void setContraseña(String contraseña) { this.contraseña = contraseña; }
        public String getTipoCliente() { return tipoCliente; }
        public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
        public String[] getAutomoviles() { return automoviles; }
        public void setAutomoviles(String[] automoviles) { this.automoviles = automoviles; }
    }

    //PARA GESTIONAR CLIENTES
    public static int agregarCliente(String nombreCompleto, String usuario, String contraseña, String tipoCliente, String[] automoviles) {
        if (contadorClientes >= MAX_CLIENTES) return -1;
        
        int id = siguienteId++;
        clientes[contadorClientes++] = new Cliente(id, nombreCompleto, usuario, contraseña, tipoCliente, automoviles);
        return id;
    }

    public static Cliente buscarCliente(int id) {
        for (int i = 0; i < contadorClientes; i++) {
            if (clientes[i].getId() == id) {
                return clientes[i];
            }
        }
        return null;
    }

    public static boolean modificarCliente(int id, String nombreCompleto, String usuario, String contraseña, String tipoCliente, String[] automoviles) {
        Cliente cliente = buscarCliente(id);
        if (cliente == null) return false;
        
        cliente.setNombreCompleto(nombreCompleto);
        cliente.setUsuario(usuario);
        cliente.setContraseña(contraseña);
        cliente.setTipoCliente(tipoCliente);
        cliente.setAutomoviles(automoviles);
        return true;
    }

    public static boolean eliminarCliente(int id) {
        for (int i = 0; i < contadorClientes; i++) {
            if (clientes[i].getId() == id) {
                for (int j = i; j < contadorClientes - 1; j++) {
                    clientes[j] = clientes[j + 1];
                }
                contadorClientes--;
                return true;
            }
        }
        return false;
    }

    public static Cliente[] obtenerTodosClientes() {
        Cliente[] resultado = new Cliente[contadorClientes];
        System.arraycopy(clientes, 0, resultado, 0, contadorClientes);
        return resultado;
    }
    
    public static int getSiguienteId() {
        return siguienteId;
    }
    
    public static String[] parseAutomoviles(String automovilesStr) {
        return automovilesStr.split(";");
    }
}