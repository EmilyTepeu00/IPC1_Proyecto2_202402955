package controlador;

import modelo.ReportesModelo;
import vista.MenuAVista;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;



public class ReportesControlador {
    private MenuAVista vista;
    
    public ReportesControlador(MenuAVista vista) {
        this.vista = vista;
    }
    
    public void generarTodosReportes() {
        generarReporteClientes();
        generarReporteRepuestosUsados();
        generarReporteRepuestosCaros();
        generarReporteServiciosUsados();
        generarReporteAutosRepetidos();
    }
    
    private void generarReporteClientes() {
        try {
            String filename = ReportesModelo.getNextFileName("clientes");
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            
            //TITULO
            document.add(new Paragraph("Reporte de Clientes"));
            document.add(new Paragraph(" "));
            
            //TABLA DE CLIENTES NORMALES
            String[][] clientesNormales = ReportesModelo.obtenerClientes(false);
            document.add(new Paragraph("Clientes Normales:"));
            agregarTablaClientes(document, clientesNormales);
            
            //TABLA DE CLIENTES ORO
            String[][] clientesOro = ReportesModelo.obtenerClientes(true);
            document.add(new Paragraph("Clientes Oro:"));
            agregarTablaClientes(document, clientesOro);
            
            //GRAFICA DE PASTEL
            int[] conteo = ReportesModelo.contarClientesPorTipo();
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("Normales (" + conteo[0] + ")", conteo[0]);
            dataset.setValue("Oro (" + conteo[1] + ")", conteo[1]);
            
            JFreeChart chart = ChartFactory.createPieChart( "Distribución de Clientes", dataset, true, true, false);
            
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setSectionPaint("Normales (" + conteo[0] + ")", java.awt.Color.BLUE);
            plot.setSectionPaint("Oro (" + conteo[1] + ")", java.awt.Color.YELLOW);
            
            BufferedImage bufferedImage = chart.createBufferedImage(500, 300);
            Image image = Image.getInstance(bufferedImage, null);
            document.add(image);
            
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void agregarTablaClientes(Document document, String[][] clientes) throws DocumentException {
        if (clientes == null || clientes.length == 0) {
            document.add(new Paragraph("No hay clientes registrados"));
            return;
        }
        
        PdfPTable table = new PdfPTable(5);
        table.addCell(new PdfPCell(new Phrase("DPI")));
        table.addCell(new PdfPCell(new Phrase("Nombre")));
        table.addCell(new PdfPCell(new Phrase("Usuario")));
        table.addCell(new PdfPCell(new Phrase("Contraseña")));
        table.addCell(new PdfPCell(new Phrase("Tipo")));
        
        for (String[] cliente : clientes) {
            if (cliente != null) {
                for (String dato : cliente) {
                    table.addCell(new PdfPCell(new Phrase(dato != null ? dato : "")));
                }
            }
        }
        
        document.add(table);
        document.add(new Paragraph(" "));
    }
    
    private void generarReporteRepuestosUsados() {
        try {
            String filename = ReportesModelo.getNextFileName("repuestos_usados");
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
        
            document.add(new Paragraph("Top 10 Repuestos Más Usados"));
            document.add(new Paragraph(" "));
        
            String[][] repuestos = ReportesModelo.obtenerTopRepuestosUsados();
        
            if (repuestos == null || repuestos.length == 0) {
                document.add(new Paragraph("No hay repuestos usados registrados"));
            } else {
                PdfPTable table = new PdfPTable(5);
                table.addCell(new PdfPCell(new Phrase("ID")));
                table.addCell(new PdfPCell(new Phrase("Nombre")));
                table.addCell(new PdfPCell(new Phrase("Marca")));
                table.addCell(new PdfPCell(new Phrase("Modelo")));
                table.addCell(new PdfPCell(new Phrase("Veces usado")));
            
                for (String[] repuesto : repuestos) {
                    if (repuesto != null) {
                        for (String dato : repuesto) {
                            table.addCell(new PdfPCell(new Phrase(dato != null ? dato : "0")));
                        }
                    }
                }
            
                document.add(table);
            
                //GRAFICA DE BARRAS
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                for (String[] repuesto : repuestos) {
                    if (repuesto != null && repuesto.length >= 5) {
                        try {
                            double usos = Double.parseDouble(repuesto[4]);
                            dataset.addValue(usos, "Uso", repuesto[1]);
                        } catch (NumberFormatException e) {
                            dataset.addValue(0, "Uso", repuesto[1]);
                        }
                    }
                }
            
            if (dataset.getRowCount() > 0) {
                JFreeChart chart = ChartFactory.createBarChart("Top 10 Repuestos Más Usados", "Repuesto", "Veces usado", dataset);
                
                    
                }
            }
        
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void generarReporteRepuestosCaros() {
        try {
            String filename = ReportesModelo.getNextFileName("repuestos_caros");
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            
            document.add(new Paragraph("Top 10 Repuestos Más Caros"));
            document.add(new Paragraph(" "));
            
            String[][] repuestos = ReportesModelo.obtenerTopRepuestosCaros();
            PdfPTable table = new PdfPTable(5);
            table.addCell(new PdfPCell(new Phrase("ID")));
            table.addCell(new PdfPCell(new Phrase("Nombre")));
            table.addCell(new PdfPCell(new Phrase("Marca")));
            table.addCell(new PdfPCell(new Phrase("Modelo")));
            table.addCell(new PdfPCell(new Phrase("Precio")));
            
            for (String[] repuesto : repuestos) {
                for (String dato : repuesto) {
                    table.addCell(new PdfPCell(new Phrase(dato)));
                }
            }
            
            document.add(table);
            
            //GRAFICA DE BARRAS (SOLO NOMBRES)
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (String[] repuesto : repuestos) {
                dataset.addValue(Double.parseDouble(repuesto[4]), "Precio", repuesto[1]);
            }
            
            JFreeChart chart = ChartFactory.createBarChart("Top 10 Repuestos Más Caros", "Repuesto", "Precio", 
                dataset);
            
            BufferedImage bufferedImage = chart.createBufferedImage(500, 300);
            Image image = Image.getInstance(bufferedImage, null);
            document.add(image);
            
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void generarReporteServiciosUsados() {
        try {
            String filename = ReportesModelo.getNextFileName("servicios_usados");
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            
            document.add(new Paragraph("Top 10 Servicios Más Usados"));
            document.add(new Paragraph(" "));
            
            String[][] servicios = ReportesModelo.obtenerTopServiciosUsados();
            PdfPTable table = new PdfPTable(6);
            table.addCell(new PdfPCell(new Phrase("ID")));
            table.addCell(new PdfPCell(new Phrase("Nombre")));
            table.addCell(new PdfPCell(new Phrase("Marca")));
            table.addCell(new PdfPCell(new Phrase("Modelo")));
            table.addCell(new PdfPCell(new Phrase("Mano Obra")));
            table.addCell(new PdfPCell(new Phrase("Total")));
            
            for (String[] servicio : servicios) {
                for (String dato : servicio) {
                    table.addCell(new PdfPCell(new Phrase(dato)));
                }
            }
            
            document.add(table);
            
            //GRAFICA DE BARRAS
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (String[] servicio : servicios) {
                dataset.addValue(Double.parseDouble(servicio[5]), "Uso", servicio[1]);
            }
            
            JFreeChart chart = ChartFactory.createBarChart("Top 10 Servicios Más Usados", "Servicio", "Uso", dataset);
            
            BufferedImage bufferedImage = chart.createBufferedImage(500, 300);
            Image image = Image.getInstance(bufferedImage, null);
            document.add(image);
            
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void generarReporteAutosRepetidos() {
        try {
            String filename = ReportesModelo.getNextFileName("autos_repetidos");
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
        
            document.add(new Paragraph("Top 5 Autos Más Repetidos"));
            document.add(new Paragraph(" "));
        
            String[][] autos = ReportesModelo.obtenerAutosMasRepetidos();
            PdfPTable table = new PdfPTable(4);
            table.addCell(new PdfPCell(new Phrase("Cliente")));
            table.addCell(new PdfPCell(new Phrase("Placa")));
            table.addCell(new PdfPCell(new Phrase("Marca")));
            table.addCell(new PdfPCell(new Phrase("Modelo")));
        
            for (String[] auto : autos) {
                for (String dato : auto) {
                    table.addCell(new PdfPCell(new Phrase(dato)));
                }
            }
        
            document.add(table);
        
            //GRAFICA DE PASTEL (LOS 2 MAS REPETIDOS)
            if (autos.length >= 2) {
                DefaultPieDataset dataset = new DefaultPieDataset();
                String auto1 = autos[0][2] + " " + autos[0][3];
                String auto2 = autos[1][2] + " " + autos[1][3];
            
                //OBTENER CONTEO DE REPETICIONES
                Map<String, Integer> conteo = new HashMap<>();
                File carpetaAutos = new File(ReportesModelo.CARPETA_AUTOS);
                File[] archivosAutos = carpetaAutos.listFiles();
            
                for (File archivo : archivosAutos) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                        String linea;
                        String marca = "";
                        String modelo = "";
                    
                        while ((linea = reader.readLine()) != null) {
                            if (linea.startsWith("Marca: ")) {
                                marca = linea.substring(7).trim();
                            } else if (linea.startsWith("Modelo: ")) {
                                modelo = linea.substring(8).trim();
                            }
                        }
                    
                        String clave = marca + " " + modelo;
                        if (clave.equals(auto1) || clave.equals(auto2)) {
                            conteo.put(clave, conteo.getOrDefault(clave, 0) + 1);
                        }   
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            
                dataset.setValue(auto1 + " (" + conteo.getOrDefault(auto1, 0) + ")", conteo.getOrDefault(auto1, 0));
                dataset.setValue(auto2 + " (" + conteo.getOrDefault(auto2, 0) + ")", conteo.getOrDefault(auto2, 0));
            
                JFreeChart chart = ChartFactory.createPieChart( "Autos Más Repetidos", dataset, true, true, false);
            
                BufferedImage bufferedImage = chart.createBufferedImage(500, 300);
                Image image = Image.getInstance(bufferedImage, null);
                document.add(image);
            }
        
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}