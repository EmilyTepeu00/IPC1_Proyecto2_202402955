package modelo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class BitacoraModelo {
    private static final int MAX_REGISTROS = 1000;
    private static RegistroBitacora[] registros = new RegistroBitacora[MAX_REGISTROS];
    private static int contadorRegistros = 0;
    private static List<BitacoraListener> listeners = new ArrayList<>();
    
    public interface BitacoraListener {
        void onNuevoRegistro(RegistroBitacora registro);
    }
    
    public static class RegistroBitacora {
        private String marcaTemporal;
        private String usuario;
        private String accion;
        private String resultado;
        private String detalles;
        
        public RegistroBitacora(String usuario, String accion, String resultado, String detalles) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            this.marcaTemporal = sdf.format(new Date());
            this.usuario = usuario;
            this.accion = accion;
            this.resultado = resultado;
            this.detalles = detalles;
        }
        
        public String getMarcaTemporal() { return marcaTemporal; }
        public String getUsuario() { return usuario; }
        public String getAccion() { return accion; }
        public String getResultado() { return resultado; }
        public String getDetalles() { return detalles; }
    }
    
    public static void addListener(BitacoraListener listener) {
        listeners.add(listener);
    }
    
    public static void registrarEvento(String usuario, String accion, String resultado, String detalles) {
        RegistroBitacora nuevo = new RegistroBitacora(usuario, accion, resultado, detalles);
        
        if (contadorRegistros < MAX_REGISTROS) {
            registros[contadorRegistros++] = nuevo;
        } else {
            for (int i = 0; i < MAX_REGISTROS - 1; i++) {
                registros[i] = registros[i + 1];
            }
            registros[MAX_REGISTROS - 1] = nuevo;
        }
        
        //NOTIFICAR A LOS LISTENERS
        for (BitacoraListener listener : listeners) {
            listener.onNuevoRegistro(nuevo);
        }
    }
    
    public static RegistroBitacora[] obtenerRegistros() {
        RegistroBitacora[] registrosActuales = new RegistroBitacora[contadorRegistros];
        System.arraycopy(registros, 0, registrosActuales, 0, contadorRegistros);
        return registrosActuales;
    }
}