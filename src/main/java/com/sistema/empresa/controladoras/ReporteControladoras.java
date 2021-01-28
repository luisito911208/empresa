package com.sistema.empresa.controladoras;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.sistema.empresa.modelos.MaterialRecibido;
import com.sistema.empresa.serviciosinterface.IContratoServicios;
import com.sistema.empresa.serviciosinterface.IMaterialRecibidoServicios;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reporte")
public class ReporteControladoras {

    @Autowired
    private IMaterialRecibidoServicios materialRecibidoServicios;

    @Autowired
    private IContratoServicios contratoServicios;

    private static final String RUTACONCENTRADO = "/Users/luisito/IdeaProjects/empresa/src/main/resources/reports/concentrado.pdf";
    private static final String IMAGEN = "/Users/luisito/IdeaProjects/empresa/src/main/resources/static/img/logo.jpg";


    @GetMapping("/avancefisico")
    public String modalAvanceFisico(Model model) {
        return "reportes/avancefisico";
    }

    @GetMapping("/generaravancefisico")
    public String generarFisico(Model model) {
        return "redirect:/";
    }

    @GetMapping("/concentrado")
    public String modalConcentrado(Model model) {
        return "reportes/concentrado";
    }

    @GetMapping("/generarconcentrado")
    public String generarConcentrado(@RequestParam("materialContratado")String material, @RequestParam("mes")String mes,
                                     @RequestParam("anno")String anno, Model model, HttpServletResponse response) throws IOException {

        SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy");

        PdfWriter pdfWriter = new PdfWriter(RUTACONCENTRADO);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        PageSize pageSize = PageSize.A4;
        Document document = new Document(pdfDocument, pageSize);
        PdfFont font = PdfFontFactory.createFont("/System/Library/Fonts/Supplemental/Arial.ttf", PdfEncodings.IDENTITY_H, true);

        MaterialRecibido materialRecibido = materialRecibidoServicios.buscarPorMaterial(material);

        //Datos proveedor
        document.add(new Paragraph("Teléfono: "+ materialRecibido.getMaterialContratado().getContrato().getProveedor().getTelefono()
                +"\n"+"Correo electrónico: "+ materialRecibido.getMaterialContratado().getContrato().getProveedor().getEmail()
                +"\n"+"Dirección: "+ materialRecibido.getMaterialContratado().getContrato().getProveedor().getDireccion()).setFont(font).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));

        //Imagen
        Image logo = new Image(ImageDataFactory.create(IMAGEN)).scaleAbsolute(100, 100).setFixedPosition(35,720);
        document.add(logo);

        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        //Encabezado
        document.add(new Paragraph(materialRecibido.getMaterialContratado().getContrato().getProyecto().toUpperCase()).setFont(font).setFontSize(12).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("CONTRATO: "+materialRecibido.getMaterialContratado().getContrato().getNumeroContrato().toUpperCase()).setFont(font).setFontSize(12).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("PERIODO DE CONTRATO: ").setFont(font).setFontSize(12).setTextAlignment(TextAlignment.CENTER));



        document.add(new Paragraph(""));
        document.add(new Paragraph(""));

        Table table = new Table(4);
        table.addCell(new Cell(2, 1).add("TOTAL DE CANTIDADES"+"\n"+"SUMINISTRADAS").setFontColor(DeviceGray.WHITE)).setTextAlignment(TextAlignment.CENTER);
        table.addCell("DESCRIPCION").setFontColor(DeviceGray.WHITE);
        table.addCell("CANTIDAD").setFontColor(DeviceGray.WHITE);
        table.addCell("UNIDAD").setFontColor(DeviceGray.WHITE);
        table.addCell(materialRecibido.getMaterialContratado().getNombreMaterial().toUpperCase()).setFontColor(DeviceGray.WHITE);
        table.addCell(String.valueOf(totalMaterial(material,mes,anno))).setFontColor(DeviceGray.WHITE);
        table.addCell(materialRecibido.getMaterialContratado().getUnidadMedida().toUpperCase()).setFontColor(DeviceGray.WHITE);
        table.setFontSize(10);
        table.setBackgroundColor(Color.GRAY);
        table.setTextAlignment(TextAlignment.CENTER);
        document.add(table);

        document.add(new Paragraph(""));
        document.add(new Paragraph(""));

        Table table2 = new Table(5);
        table2.addCell("FECHA").setFontColor(DeviceGray.WHITE);
        table2.addCell("FOLIO").setFontColor(DeviceGray.WHITE);
        table2.addCell("DESCRIPCION").setFontColor(DeviceGray.WHITE);
        table2.addCell("CANTIDAD").setFontColor(DeviceGray.WHITE);
        table2.addCell("UNIDAD").setFontColor(DeviceGray.WHITE);
        table2.setFontSize(10);
        table2.setBackgroundColor(Color.GRAY);
        table2.setTextAlignment(TextAlignment.CENTER);
        document.add(table2);

        Table table3 = new Table(5);
        table3.setFontSize(10);
        table3.setTextAlignment(TextAlignment.CENTER);
        String unidad = "";
        for (MaterialRecibido recibido:materialRecibidoServicios.buscarTodas()) {
            if(recibido.getMaterialContratado().getNombreMaterial().equalsIgnoreCase(material) &&
                    recibido.getFechaEntrega().getMonth() == mes(mes) && recibido.getFechaEntrega().toString().substring(0,4).equalsIgnoreCase(anno)){

                table3.addCell(formatoSalida.format(recibido.getFechaEntrega()));
                table3.addCell(recibido.getNumeroDeFolio());
                table3.addCell(recibido.getMaterialContratado().getNombreMaterial());
                table3.addCell(String.valueOf(recibido.getCantidad()));
                table3.addCell(recibido.getMaterialContratado().getUnidadMedida().toUpperCase());
                unidad = recibido.getMaterialContratado().getUnidadMedida();

            }

        }
        document.add(table3);


        Table table4 = new Table(new float[]{3,5,3});
        table4.setFontSize(10);
        table4.addCell("TOTAL");
        table4.addCell(String.valueOf(totalMaterial(material,mes,anno))).setFontColor(DeviceGray.WHITE);
        table4.addCell(unidad.toUpperCase());
        table4.setFontSize(10);
        table4.setBackgroundColor(Color.GRAY);
        table4.setTextAlignment(TextAlignment.CENTER);
        table4.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        document.add(table4);

        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));

        //Firmas
        Table table5 = new Table(2);
        table5.addCell("_________________________________").setBorder(Border.NO_BORDER);
        table5.addCell("_________________________________");
        table5.addCell("ING. ANDRES SANTIAGO LEON VIDAL");
        table5.addCell("C. EDUARDO DANIEL HERNANDEZ SOLORZANO");
        table5.addCell("_________________________________").setBorder(Border.NO_BORDER);
        table5.addCell("_________________________________");
        table5.addCell("C. OSIEL BASTO COHUO");
        table5.addCell("C. JOSE LUIS CASTRO GARIBAY");
        table5.setTextAlignment(TextAlignment.CENTER);
        document.add(table5);


        document.close();



        try {
            String fileName = RUTACONCENTRADO;
            File fileToDownload = new File(fileName);
            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+"concentrado.pdf");
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }

        return "redirect:/";
    }

    public int mes(String m) {
        int mes = 0;
        switch (m) {
            case "Enero":
                mes = 0;
                break;
            case "Febrero":
                mes = 1;
                break;
            case "Marzo":
                mes = 2;
                break;
            case "Abril":
                mes = 3;
                break;
            case "Mayo":
                mes = 4;
                break;
            case "Junio":
                mes = 5;
                break;
            case "Julio":
                mes = 6;
                break;
            case "Agosto":
                mes = 7;
                break;
            case "Septiembre":
                mes =8;
                break;
            case "Octubre":
                mes = 9 ;
                break;
            case "Noviembre":
                mes = 10;
                break;
            case "Diciembre":
                mes = 11 ;
                break;
        }

        return mes;
    }

    public double totalMaterial(String material,String mes,String anno){
        double total  = 0;
        for (MaterialRecibido recibido:materialRecibidoServicios.buscarTodas()) {
            if(recibido.getMaterialContratado().getNombreMaterial().equalsIgnoreCase(material) && recibido.getFechaEntrega().getMonth() == mes(mes) &&
                    recibido.getFechaEntrega().toString().substring(0,4).equalsIgnoreCase(anno)){
                total += recibido.getCantidad();
            }
        }
        return total;
    }

    public List<String> meses(){
        List<String> lista = new ArrayList<>();
        for (MaterialRecibido material:materialRecibidoServicios.buscarTodas()) {
            switch (material.getFechaEntrega().getMonth()) {
                case 0:
                    String mes = "Enero";
                    lista.add(mes);
                    break;
                case 1:
                    String mes1 = "Febrero";
                    lista.add(mes1);
                    break;
                case 2:
                    String mes2 = "Marzo";
                    lista.add(mes2);
                    break;
                case 3:
                    String mes3 = "Abril";
                    lista.add(mes3);
                    break;
                case 4:
                    String mes4 = "Mayo";
                    lista.add(mes4);
                    break;
                case 5:
                    String mes5= "Junio";
                    lista.add(mes5);
                    break;
                case 6:
                    String mes6 = "Julio";
                    lista.add(mes6);
                    break;
                case 7:
                    String mes7 = "Agosto";
                    lista.add(mes7);
                    break;
                case 8:
                    String mes8 = "Septiembre";
                    lista.add(mes8);
                    break;
                case 9:
                    String mes9 = "Octubre";
                    lista.add(mes9);
                    break;
                case 10:
                    String mes10 = "Noviembre";
                    lista.add(mes10);
                    break;
                case 11:
                    String mes11 = "Diciembre";
                    lista.add(mes11);
                    break;
            }
        }

        lista = lista.stream().distinct().collect(Collectors.toList());
        return lista;
    }

    public List<String> anno(){
        List<String> lista = new ArrayList<>();
        for (MaterialRecibido material:materialRecibidoServicios.buscarTodas()) {
            String anno = material.getFechaEntrega().toString().substring(0,4);
            lista.add(anno);

        }
        lista = lista.stream().distinct().collect(Collectors.toList());
        return lista;
    }

    public List<String> material(){
        List<String> lista = new ArrayList<>();
        for (MaterialRecibido material:materialRecibidoServicios.buscarTodas()) {
            lista.add(material.getMaterialContratado().getNombreMaterial());

        }
        lista = lista.stream().distinct().collect(Collectors.toList());
        return lista;
    }
    @ModelAttribute
    public void cargarListas(Model model){
        model.addAttribute("anno",anno());
        model.addAttribute("meses",meses());
        model.addAttribute("materiales",material());
        model.addAttribute("contratos",contratoServicios.buscarTodas());



    }




}
