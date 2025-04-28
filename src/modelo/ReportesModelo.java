package modelo;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class ReportesModelo {
    private static final String CARPETA_CLIENTES = "datos_clientes";
    public static final String CARPETA_AUTOS = "datos_autos";
    private static final String CARPETA_REPUESTOS = "datos_repuestos";
    private static final String CARPETA_SERVICIOS = "datos_servicios";
    
    private static int contadorClientes = 1;
    private static int contadorRepuestosUsados = 1;
    private static int contadorRepuestosCaros = 1;
    private static int contadorServiciosUsados = 1;
    private static int contadorAutosRepetidos = 1;
    
    public static String[][] obtenerClientes(boolean oro) {
        File carpeta = new File(CARPETA_CLIENTES);
        File[] archivos = carpeta.listFiles();
        int count = 0;
        
        //CONTAR CLIENTES DEL TIPO SOLICITADO
        for (File archivo : archivos) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    if (linea.startsWith("TipoCliente: ")) {
                        String tipo = linea.substring(13).trim();
                        if ((oro && tipo.equals("ORO")) || (!oro && tipo.equals("NORMAL"))) {
                            count++;
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        String[][] clientes = new String[count][5];
        int index = 0;
        
        for (File archivo : archivos) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                String[] datos = new String[5];
                
                while ((linea = reader.readLine()) != null) {
                    if (linea.startsWith("DPI: ")) datos[0] = linea.substring(5).trim();
                    else if (linea.startsWith("Nombre: ")) datos[1] = linea.substring(8).trim();
                    else if (linea.startsWith("Usuario: ")) datos[2] = linea.substring(9).trim();
                    else if (linea.startsWith("Contraseña: ")) datos[3] = linea.substring(12).trim();
                    else if (linea.startsWith("TipoCliente: ")) datos[4] = linea.substring(13).trim();
                }
                
                if ((oro && datos[4].equals("ORO")) || (!oro && datos[4].equals("NORMAL"))) {
                    clientes[index++] = datos;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return clientes;
    }
    
    public static int[] contarClientesPorTipo() {
        int normales = 0;
        int oro = 0;
        
        File carpeta = new File(CARPETA_CLIENTES);
        File[] archivos = carpeta.listFiles();
        
        for (File archivo : archivos) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    if (linea.startsWith("TipoCliente: ")) {
                        String tipo = linea.substring(13).trim();
                        if (tipo.equals("ORO")) oro++;
                        else normales++;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return new int[]{normales, oro};
    }
    
    public static String[][] obtenerTopRepuestosUsados() {
        RepuestosModelo.Repuesto[] repuestos = RepuestosModelo.obtenerRepuestosMasUsados();

        //FILTRAR SOLO REPUESTOS CON CONTADOR > 0
        int count = 0;
        for (RepuestosModelo.Repuesto rep : repuestos) {
            if (rep != null && rep.getContadorUsos() > 0) {
                count++;
            }
        }
    
        //TOMAR LOS PRIMEROS 10 O MENOS
        count = Math.min(10, count);
        String[][] resultado = new String[count][5];

        int index = 0;
        for (RepuestosModelo.Repuesto repuesto : repuestos) {
            if (repuesto != null && repuesto.getContadorUsos() > 0 && index < count) {
                resultado[index][0] = String.valueOf(repuesto.getId());
                resultado[index][1] = repuesto.getNombre();
                resultado[index][2] = repuesto.getMarca();
                resultado[index][3] = repuesto.getModelo();
                resultado[index][4] = String.valueOf(repuesto.getContadorUsos());
                index++;
            }
        }

        return resultado;
    }  
    
    public static String[][] obtenerTopRepuestosCaros() {
        //OBTENER REPUESTOS DEL MODELO
        RepuestosModelo.Repuesto[] repuestos = RepuestosModelo.obtenerTodosRepuestos();
        
        //ORDENAR PRECIO DESCENDENTEMENTE
        for (int i = 0; i < repuestos.length - 1; i++) {
            for (int j = 0; j < repuestos.length - i - 1; j++) {
                if (repuestos[j].getPrecio() < repuestos[j+1].getPrecio()) {
                    RepuestosModelo.Repuesto temp = repuestos[j];
                    repuestos[j] = repuestos[j+1];
                    repuestos[j+1] = temp;
                }
            }
        }
        
        //TOMAR LOS PRIMEROS 10 O MENOS
        int count = Math.min(10, repuestos.length);
        String[][] resultado = new String[count][5];
        
        for (int i = 0; i < count; i++) {
            resultado[i][0] = String.valueOf(repuestos[i].getId());
            resultado[i][1] = repuestos[i].getNombre();
            resultado[i][2] = repuestos[i].getMarca();
            resultado[i][3] = repuestos[i].getModelo();
            resultado[i][4] = String.valueOf(repuestos[i].getPrecio());
        }
        
        return resultado;
    }
    
    public static String[][] obtenerTopServiciosUsados() {
        ServiciosModelo.Servicio[] servicios = ServiciosModelo.obtenerTodosServicios();

        //FILTAR SOLO LOS SERVICIOS CON CONTADOR > 0
        int count = 0;
        for (ServiciosModelo.Servicio serv : servicios) {
            if (serv != null && serv.getContadorUsos() > 0) {
                count++;
            }
        }
    
        //ORDENAR POR CONTADOR DE USOS
        for (int i = 0; i < servicios.length - 1; i++) {
            for (int j = 0; j < servicios.length - i - 1; j++) {
                if (servicios[j] == null || 
                    (servicios[j+1] != null && 
                    servicios[j].getContadorUsos() < servicios[j+1].getContadorUsos())) {
                    ServiciosModelo.Servicio temp = servicios[j];
                    servicios[j] = servicios[j+1];
                    servicios[j+1] = temp;
                }
            }
        }

        //TOMAR LOS PRIMEROS 10 O MENOS
        count = Math.min(10, count);
        String[][] resultado = new String[count][6];

        int index = 0;
        for (ServiciosModelo.Servicio servicio : servicios) {
            if (servicio != null && servicio.getContadorUsos() > 0 && index < count) {
                resultado[index][0] = String.valueOf(servicio.getId());
                resultado[index][1] = servicio.getNombre();
                resultado[index][2] = servicio.getMarca();
                resultado[index][3] = servicio.getModelo();
                resultado[index][4] = String.valueOf(servicio.getPrecioTotal());
                resultado[index][5] = String.valueOf(servicio.getContadorUsos());
                index++;
            }
        }

        return resultado;
    }
    
    public static String[][] obtenerAutosMasRepetidos() {
        File carpetaAutos = new File(CARPETA_AUTOS);
        File[] archivosAutos = carpetaAutos.listFiles();
    
        if (archivosAutos == null || archivosAutos.length == 0) {
            return new String[0][4];
        }
    
        //CONTAR REPETICIONES DE CADA AUTO
        Map<String, Integer> contadorAutos = new HashMap<>();
        Map<String, String[]> infoAutos = new HashMap<>(); // placa, cliente
    
        for (File archivo : archivosAutos) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                String placa = "";
                String marca = "";
                String modelo = "";
                String usuario = archivo.getName().split("_")[0];
            
                while ((linea = reader.readLine()) != null) {
                    if (linea.startsWith("Placa: ")) {
                        placa = linea.substring(7).trim();
                    } else if (linea.startsWith("Marca: ")) {
                        marca = linea.substring(7).trim();
                    } else if (linea.startsWith("Modelo: ")) {
                        modelo = linea.substring(8).trim();
                    }
                }
            
                String clave = marca + "|" + modelo;
                contadorAutos.put(clave, contadorAutos.getOrDefault(clave, 0) + 1);
            
                //OBTENER NOMBRE DEL CLIENTE
                String nombreCliente = "Desconocido";
                File archivoCliente = new File(CARPETA_CLIENTES, usuario + ".txt");
                if (archivoCliente.exists()) {
                    try (BufferedReader readerCliente = new BufferedReader(new FileReader(archivoCliente))) {
                        String lineaCliente;
                        while ((lineaCliente = readerCliente.readLine()) != null) {
                            if (lineaCliente.startsWith("Nombre: ")) {
                                nombreCliente = lineaCliente.substring(8).trim();
                                break;
                            }
                        }
                    }
                }
            
                infoAutos.put(clave, new String[]{placa, nombreCliente});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        //ORDENAR POR CANTIDAD DE REPETICIONES
        String[][] autosOrdenados = new String[contadorAutos.size()][4];
        int index = 0;
        for (Map.Entry<String, Integer> entry : contadorAutos.entrySet()) {
            String[] partes = entry.getKey().split("\\|");
            String[] infoAuto = infoAutos.get(entry.getKey());
            autosOrdenados[index][0] = infoAuto[1]; // Nombre cliente
            autosOrdenados[index][1] = infoAuto[0]; // Placa
            autosOrdenados[index][2] = partes[0];   // Marca
            autosOrdenados[index][3] = partes[1];   // Modelo
            index++;
        }
    
        //ORDENAR DE MAYOR A MENOR
        for (int i = 0; i < autosOrdenados.length - 1; i++) {
            for (int j = 0; j < autosOrdenados.length - i - 1; j++) {
                String clave1 = autosOrdenados[j][2] + "|" + autosOrdenados[j][3];
                String clave2 = autosOrdenados[j+1][2] + "|" + autosOrdenados[j+1][3];
                if (contadorAutos.getOrDefault(clave1, 0) < contadorAutos.getOrDefault(clave2, 0)) {
                    String[] temp = autosOrdenados[j];
                    autosOrdenados[j] = autosOrdenados[j+1];
                    autosOrdenados[j+1] = temp;
                }
            }
        }
    
        //TOMAR LOS PRIMEROS 5 O MENOS
        int count = Math.min(5, autosOrdenados.length);
        String[][] resultado = new String[count][4];
        System.arraycopy(autosOrdenados, 0, resultado, 0, count);
    
        return resultado;
    }
    
    public static String getNextFileName(String tipo) {
        switch(tipo) {
            case "clientes":
                return "ReporteClientes" + (contadorClientes++) + ".pdf";
            case "repuestos_usados":
                return "ReporteRepuestosUsados" + (contadorRepuestosUsados++) + ".pdf";
            case "repuestos_caros":
                return "ReporteRepuestosCaros" + (contadorRepuestosCaros++) + ".pdf";
            case "servicios_usados":
                return "ReporteServiciosUsados" + (contadorServiciosUsados++) + ".pdf";
            case "autos_repetidos":
                return "ReporteAutosRepetidos" + (contadorAutosRepetidos++) + ".pdf";
            default:
                return "Reporte" + System.currentTimeMillis() + ".pdf";
        }
    }
}