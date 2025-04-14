package modelo;

public class InicioModelo {
    private RegistroModelo registroModelo;
    
    //CREDENCIALES POR DEFECTO
    public static final String ADMIN_USER = "adminipc1f";
    public static final String ADMIN_PASS = "adminipc1f";
    public static final String MECANICO_USER = "mecanicoipc1f";
    public static final String MECANICO_PASS = "mecanicoipc1f";

    public InicioModelo(RegistroModelo registroModelo) {
        this.registroModelo = registroModelo;
    }

    public String verificarTipoUsuario(String usuario, String contrasena) {
        //VERIFICAR SI ES ADMINISTRADOR
        if (ADMIN_USER.equals(usuario) && ADMIN_PASS.equals(contrasena)) {
            return "ADMIN";
        }
        
        //VERIFICAR SI ES MECANICO
        if (MECANICO_USER.equals(usuario) && MECANICO_PASS.equals(contrasena)) {
            return "ADMIN"; 
        }
        
        //VERIFICAR SI ES CLIENTE
        if (registroModelo.verificarCredenciales(usuario, contrasena)) {
            return "CLIENTE";
        }
        
        return "ERROR";
    }
}