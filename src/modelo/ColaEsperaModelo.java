package modelo;

import java.io.Serializable;

public class ColaEsperaModelo implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String ARCHIVO_DATOS = "cola_espera.dat";
    private static final SerializadorModelo serializador = new SerializadorModelo();
    private static ColaEsperaModelo instancia;

    private String[][] cola;
    private int frente;
    private int fin;
    private int capacidad;

    public ColaEsperaModelo(int capacidad) {
        this.capacidad = capacidad;
        this.cola = new String[capacidad][5]; // placa, marca, modelo, servicio, tipoCliente
        this.frente = 0;
        this.fin = -1;
    }

    public static ColaEsperaModelo getInstance(int capacidad) {
        if (instancia == null) {
            instancia = cargarDatos();
            if (instancia == null || instancia.capacidad != capacidad) {
                instancia = new ColaEsperaModelo(capacidad);
            }
        }
        return instancia;
    }

    public static void guardarDatos() {
        if (instancia != null) {
            serializador.guardarDatos(ARCHIVO_DATOS, instancia);
        }
    }

    private static ColaEsperaModelo cargarDatos() {
        return (ColaEsperaModelo) serializador.cargarDatos(ARCHIVO_DATOS);
    }

    public boolean estaVacia() {
        return fin < frente;
    }

    public boolean estaLlena() {
        return fin == capacidad - 1;
    }

    public void encolar(String placa, String marca, String modelo, String servicio, String tipoCliente) {
        if (estaLlena()) {
            return;
        }
        
        fin++;
        cola[fin][0] = placa;
        cola[fin][1] = marca;
        cola[fin][2] = modelo;
        cola[fin][3] = servicio;
        cola[fin][4] = tipoCliente;
        guardarDatos();
    }

    public void encolarConPrioridad(String placa, String marca, String modelo, String servicio, String tipoCliente) {
        if (tipoCliente.equals("ORO")) {
            if (estaLlena()) {
                //CORRER ELEMENTOS HACIA ATRAS
                for (int i = fin; i >= frente; i--) {
                    if (i + 1 < capacidad) {
                        cola[i + 1] = cola[i].clone();
                    }
                }
                fin++;
            }
            
            //INSERTAR AL FRENTE
            cola[frente][0] = placa;
            cola[frente][1] = marca;
            cola[frente][2] = modelo;
            cola[frente][3] = servicio;
            cola[frente][4] = tipoCliente;
            
            if (fin < frente) {
                fin = frente;
            }
        } else {
            encolar(placa, marca, modelo, servicio, tipoCliente);
        }
        guardarDatos();
    }

    //COLA VACIA
    public String[] desencolar() {
        if (estaVacia()) {
            return null;
        }
        
        String[] vehiculo = cola[frente].clone();
        frente++;
        
        //RESETEAR COLA SI ESTA VACIA
        if (frente > fin) {
            frente = 0;
            fin = -1;
        }
        
        guardarDatos();
        return vehiculo;
    }

    public String[] verPrimero() {
        if (estaVacia()) {
            return null;
        }
        return cola[frente].clone();
    }

    public int cantidadEnCola() {
        if (estaVacia()) {
            return 0;
        }
        return fin - frente + 1;
    }

    public String[][] obtenerTodosEnCola() {
        if (estaVacia()) {
            return new String[0][5];
        }
        
        String[][] resultado = new String[cantidadEnCola()][5];
        for (int i = 0; i < resultado.length; i++) {
            resultado[i] = cola[frente + i].clone();
        }
        return resultado;
    }
    
    static {
        getInstance(100);
    }
}