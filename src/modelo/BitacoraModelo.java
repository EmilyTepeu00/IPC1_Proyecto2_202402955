package modelo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BitacoraModelo {
    private static final int MAX_REGISTROS = 1000;
    private static final int MAX_LISTENERS = 10;
    private static RegistroBitacora[] registros = new RegistroBitacora[MAX_REGISTROS];
    private static BitacoraListener[] listeners = new BitacoraListener[MAX_LISTENERS];
    private static int contadorRegistros = 0;
    private static int contadorListeners = 0;
    
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
        if (contadorListeners >= MAX_LISTENERS) {
            BitacoraListener[] nuevosListeners = new BitacoraListener[MAX_LISTENERS * 2];
            for (int i = 0; i < contadorListeners; i++) {
                nuevosListeners[i] = listeners[i];
            }
            listeners = nuevosListeners;
        }
        listeners[contadorListeners++] = listener;
    }
    
    public static void removeListener(BitacoraListener listener) {
        for (int i = 0; i < contadorListeners; i++) {
            if (listeners[i] == listener) {
                //MOVER ELEMENTOS RESTANTES
                for (int j = i; j < contadorListeners - 1; j++) {
                    listeners[j] = listeners[j + 1];
                }
                contadorListeners--;
                listeners[contadorListeners] = null;
                break;
            }
        }
    }
    
    public static void registrarEvento(String usuario, String accion, String resultado, String detalles) {
        RegistroBitacora nuevo = new RegistroBitacora(usuario, accion, resultado, detalles);
        
        //MANEJO DE REGISTROS
        if (contadorRegistros < MAX_REGISTROS) {
            registros[contadorRegistros++] = nuevo;
        } else {
            for (int i = 0; i < MAX_REGISTROS - 1; i++) {
                registros[i] = registros[i + 1];
            }
            registros[MAX_REGISTROS - 1] = nuevo;
        }
        
        //NOTIFICAR A LOS LISTENERS
        for (int i = 0; i < contadorListeners; i++) {
            listeners[i].onNuevoRegistro(nuevo);
        }
    }
    
    public static RegistroBitacora[] obtenerRegistros() {
        RegistroBitacora[] registrosActuales = new RegistroBitacora[contadorRegistros];
        for (int i = 0; i < contadorRegistros; i++) {
            registrosActuales[i] = registros[i];
        }
        return registrosActuales;
    }
}