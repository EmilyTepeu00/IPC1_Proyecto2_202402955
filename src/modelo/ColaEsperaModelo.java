package modelo;

public class ColaEsperaModelo {
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
    
    public boolean estaVacia() {
        return fin < frente;
    }
    
    public boolean estaLlena() {
        return fin == capacidad - 1;
    }
    
    public void encolar(String placa, String marca, String modelo, String servicio, String tipoCliente) {
        if (estaLlena()) return;
        
        fin++;
        cola[fin][0] = placa;
        cola[fin][1] = marca;
        cola[fin][2] = modelo;
        cola[fin][3] = servicio;
        cola[fin][4] = tipoCliente;
    }
    
    public void encolarConPrioridad(String placa, String marca, String modelo, String servicio, String tipoCliente) {
        if (tipoCliente.equals("ORO")) {
            if (estaLlena()) {
                for (int i = fin; i >= frente; i--) {
                    cola[i+1] = cola[i];
                }
                fin++;
            }
            cola[frente][0] = placa;
            cola[frente][1] = marca;
            cola[frente][2] = modelo;
            cola[frente][3] = servicio;
            cola[frente][4] = tipoCliente;
        } else {
            encolar(placa, marca, modelo, servicio, tipoCliente);
        }
    }
    
    public String[] desencolar() {
        if (estaVacia()) return null;
        
        String[] vehiculo = cola[frente];
        frente++;
        return vehiculo;
    }
    
    public int cantidadEnCola() {
        return fin - frente + 1;
    }
}