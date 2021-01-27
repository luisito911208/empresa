package com.sistema.empresa.controladoras;


import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.sistema.empresa.modelos.Contrato;
import com.sistema.empresa.modelos.ControlPresupuesto;
import com.sistema.empresa.modelos.MaterialContratado;
import com.sistema.empresa.modelos.MaterialRecibido;
import com.sistema.empresa.serviciosinterface.IContratoServicios;
import com.sistema.empresa.serviciosinterface.IMaterialContratadoServicios;
import com.sistema.empresa.serviciosinterface.IMaterialRecibidoServicios;
import com.sistema.empresa.util.MaterialMesCantidad;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/controlpresupuesto")
public class ControlPresupuestoControladora {

    @Autowired
    private IContratoServicios contratoProveedorServicios;

    @Autowired
    private IMaterialRecibidoServicios materialesUsadosServicios;

    @Autowired
    private IMaterialContratadoServicios materialServicios;


    LinkedList<MaterialMesCantidad> lista = new LinkedList<>();


    private static final String RUTA = "/Users/luisito/IdeaProjects/empresa/src/main/resources/reports/reporte.pdf";
    private static final String IMAGEN = "/Users/luisito/IdeaProjects/empresa/src/main/resources/static/img/logo.jpg";

    @GetMapping("/lista")
    public String mostrarLista(Model model){
        List<Contrato> contratos = contratoProveedorServicios.buscarTodas();
        LinkedList<ControlPresupuesto> listaControlPresupuesto = new LinkedList<>();

        for (Contrato contrato :contratos) {
            double montoUtilizado = 0;
            ControlPresupuesto controlPresupuesto = new ControlPresupuesto();
            controlPresupuesto.setContrato(contrato);
            controlPresupuesto.setMontoinicio(contrato.getMontoAsignado());

            for (MaterialContratado materialContratado : contrato.getMateriales()) {
                montoUtilizado += materialContratado.getTotal();
            }
            controlPresupuesto.setMontoUtilizado(montoUtilizado);
            controlPresupuesto.setMontoFinal(contrato.getMontoAsignado() - controlPresupuesto.getMontoUtilizado());
            listaControlPresupuesto.add(controlPresupuesto);
        }

        model.addAttribute("controlpresupuesto",listaControlPresupuesto);
        return "controlpresupuesto/listarcontrolpresupuesto";
    }
    @GetMapping("/reporte/{id}")
    public String crearPDF(@PathVariable("id") int id,HttpServletResponse response) throws IOException {
        Contrato contrato = contratoProveedorServicios.buscarPorId(id);
        pdf(contrato);

        try {
            String fileName=RUTA;
            File fileToDownload = new File(fileName);
            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+"Reporte.pdf");
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }

        return "redirect:/";
    }

    public void pdf(Contrato contrato) throws IOException {
        PdfWriter pdfWriter = new PdfWriter(RUTA);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument, PageSize.A4);
        PdfFont font = PdfFontFactory.createFont("/System/Library/Fonts/Supplemental/Arial.ttf", PdfEncodings.IDENTITY_H, true);
        //Encabezado
        document.add(new Paragraph("Control del presupuesto").setFont(font).setFontSize(25).setTextAlignment(TextAlignment.CENTER));

        //Imagen
        Image logo = new Image(ImageDataFactory.create(IMAGEN)).scaleAbsolute(100, 100).setFixedPosition(35,660);
        document.add(logo);
        //Datos proveedor
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormat decimales = new DecimalFormat("$ ###,###,###.## ");
        double totalMateriales = 0;
//        setBackgroundColor(ColorConstants.GRAY)
        document.add(new Paragraph("Proveedor: "+ contrato.getProveedor().getNombre()
                +"\n"+"Teléfono: "+ contrato.getProveedor().getTelefono()
                +"\n"+"Correo electrónico: "+ contrato.getProveedor().getEmail()
                +"\n"+"Dirección: "+ contrato.getProveedor().getDireccion()
                +"\n"+"Monto asignado: "+ decimales.format(contrato.getMontoAsignado())).setFont(font).setFontSize(12).setTextAlignment(TextAlignment.RIGHT));

        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        //Nombre del proyecto
        document.add(new Paragraph(contrato.getProyecto()).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(""));
        document.add(new Paragraph("Materiales Contratados").setFontSize(12).setTextAlignment(TextAlignment.LEFT));

        Table table = new Table(7);
        table.addCell("Material");
        table.addCell("Unidad de medida");
        table.addCell("Cantidad");
        table.addCell("Precio unitario sin IVA");
        table.addCell("Importe sin IVA");
        table.addCell("IVA");
        table.addCell("Total (IVA Incluido)");

        for (MaterialContratado materialContratado : contrato.getMateriales()) {
            table.addCell(materialContratado.getNombreMaterial());
            table.addCell(materialContratado.getUnidadMedida());
            table.addCell(String.valueOf(materialContratado.getCantidad()));
            table.addCell(decimales.format(materialContratado.getPrecio()));
            table.addCell(decimales.format(materialContratado.getSubtotal()));
            table.addCell(decimales.format(materialContratado.getSubtotaliva()));
            table.addCell(decimales.format(materialContratado.getTotal()));
            totalMateriales += materialContratado.getTotal();
        }

        table.setFontSize(10);
        table.setTextAlignment(TextAlignment.CENTER);
        document.add(table);
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        //---------------------------Segunda Tabla----------------------------------------------------------------------------------------------------

        document.add(new Paragraph("Materiales utilizados").setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        Table table3 = new Table(6);
        table3.addCell("Mes");
        table3.addCell("Material");
        table3.addCell("Cantidad");
        table3.addCell("Unidad de medida");
        table3.addCell("Precio unitario sin IVA");
        table3.addCell("Total utilizado (IVA Incluido)");
        listaOrganizada(contrato);
        Collections.sort(lista);
        double costoTotal = 0;
        for (MaterialMesCantidad material:lista){
            if(material.getMes() == 0){
                table3.addCell("Enero");
                table3.addCell(material.getMaterial());
                table3.addCell(String.valueOf(material.getCantidad()));
                table3.addCell(material.getUnidadmedida());
                table3.addCell(decimales.format(material.getPreciounitario()));
                double precioiva = (material.getCantidad() * material.getPreciounitario())*0.16;
                double preciototal = (material.getCantidad() * material.getPreciounitario()) + precioiva;
                table3.addCell(decimales.format(preciototal));
                costoTotal += preciototal;
            }
            else{
                if(material.getMes() == 1){
                    table3.addCell("Febrero");
                    table3.addCell(material.getMaterial());
                    table3.addCell(String.valueOf(material.getCantidad()));
                    table3.addCell(material.getUnidadmedida());
                    table3.addCell(decimales.format(material.getPreciounitario()));
                    double precioiva = (material.getCantidad() * material.getPreciounitario())*0.16;
                    double preciototal = (material.getCantidad() * material.getPreciounitario()) + precioiva;
                    table3.addCell(decimales.format(preciototal));
                    costoTotal += preciototal;
                }
                else{
                    if(material.getMes() == 2){
                        table3.addCell("Marzo");
                        table3.addCell(material.getMaterial());
                        table3.addCell(String.valueOf(material.getCantidad()));
                        table3.addCell(material.getUnidadmedida());
                        table3.addCell(decimales.format(material.getPreciounitario()));
                        double precioiva = (material.getCantidad() * material.getPreciounitario())*0.16;
                        double preciototal = (material.getCantidad() * material.getPreciounitario()) + precioiva;
                        table3.addCell(decimales.format(preciototal));
                        costoTotal += preciototal;
                    }
                    else{
                        if(material.getMes() == 3) {
                            table3.addCell("Abril");
                            table3.addCell(material.getMaterial());
                            table3.addCell(String.valueOf(material.getCantidad()));
                            table3.addCell(material.getUnidadmedida());
                            table3.addCell(decimales.format(material.getPreciounitario()));
                            double precioiva = (material.getCantidad() * material.getPreciounitario()) * 0.16;
                            double preciototal = (material.getCantidad() * material.getPreciounitario()) + precioiva;
                            table3.addCell(decimales.format(preciototal));
                            costoTotal += preciototal;
                        }
                        else {
                            if(material.getMes() == 4){
                                table3.addCell("Mayo");
                                table3.addCell(material.getMaterial());
                                table3.addCell(String.valueOf(material.getCantidad()));
                                table3.addCell(material.getUnidadmedida());
                                table3.addCell(decimales.format(material.getPreciounitario()));
                                double precioiva = (material.getCantidad() * material.getPreciounitario())*0.16;
                                double preciototal = (material.getCantidad() * material.getPreciounitario()) + precioiva;
                                table3.addCell(decimales.format(preciototal));
                                costoTotal += preciototal;
                            }
                            else {
                                if(material.getMes() == 5){
                                    table3.addCell("Junio");
                                    table3.addCell(material.getMaterial());
                                    table3.addCell(String.valueOf(material.getCantidad()));
                                    table3.addCell(material.getUnidadmedida());
                                    table3.addCell(decimales.format(material.getPreciounitario()));
                                    double precioiva = (material.getCantidad() * material.getPreciounitario())*0.16;
                                    double preciototal = (material.getCantidad() * material.getPreciounitario()) + precioiva;
                                    table3.addCell(decimales.format(preciototal));
                                    costoTotal += preciototal;
                                }
                                else {
                                    if(material.getMes() == 6){
                                        table3.addCell("Julio");
                                        table3.addCell(material.getMaterial());
                                        table3.addCell(String.valueOf(material.getCantidad()));
                                        table3.addCell(material.getUnidadmedida());
                                        table3.addCell(decimales.format(material.getPreciounitario()));
                                        double precioiva = (material.getCantidad() * material.getPreciounitario())*0.16;
                                        double preciototal = (material.getCantidad() * material.getPreciounitario()) + precioiva;
                                        table3.addCell(decimales.format(preciototal));
                                        costoTotal += preciototal;
                                    }
                                    else {
                                        if(material.getMes() == 7){
                                            table3.addCell("Agosto");
                                            table3.addCell(material.getMaterial());
                                            table3.addCell(String.valueOf(material.getCantidad()));
                                            table3.addCell(material.getUnidadmedida());
                                            table3.addCell(decimales.format(material.getPreciounitario()));
                                            double precioiva = (material.getCantidad() * material.getPreciounitario())*0.16;
                                            double preciototal = (material.getCantidad() * material.getPreciounitario()) + precioiva;
                                            table3.addCell(decimales.format(preciototal));
                                            costoTotal += preciototal;
                                        }
                                        else {
                                            if(material.getMes() == 8){
                                                table3.addCell("Septiembre");
                                                table3.addCell(material.getMaterial());
                                                table3.addCell(String.valueOf(material.getCantidad()));
                                                table3.addCell(material.getUnidadmedida());
                                                table3.addCell(decimales.format(material.getPreciounitario()));
                                                double precioiva = (material.getCantidad() * material.getPreciounitario())*0.16;
                                                double preciototal = (material.getCantidad() * material.getPreciounitario()) + precioiva;
                                                table3.addCell(decimales.format(preciototal));
                                                costoTotal += preciototal;
                                            }
                                            else {
                                                if(material.getMes() == 9){
                                                    table3.addCell("Octubre");
                                                    table3.addCell(material.getMaterial());
                                                    table3.addCell(String.valueOf(material.getCantidad()));
                                                    table3.addCell(material.getUnidadmedida());
                                                    table3.addCell(decimales.format(material.getPreciounitario()));
                                                    double precioiva = (material.getCantidad() * material.getPreciounitario())*0.16;
                                                    double preciototal = (material.getCantidad() * material.getPreciounitario()) + precioiva;
                                                    table3.addCell(decimales.format(preciototal));
                                                    costoTotal += preciototal;
                                                }
                                                else {
                                                    if(material.getMes() == 10){
                                                        table3.addCell("Noviembre");
                                                        table3.addCell(material.getMaterial());
                                                        table3.addCell(String.valueOf(material.getCantidad()));
                                                        table3.addCell(material.getUnidadmedida());
                                                        table3.addCell(decimales.format(material.getPreciounitario()));
                                                        double precioiva = (material.getCantidad() * material.getPreciounitario())*0.16;
                                                        double preciototal = (material.getCantidad() * material.getPreciounitario()) + precioiva;
                                                        table3.addCell(decimales.format(preciototal));
                                                        costoTotal += preciototal;
                                                    }
                                                    else {
                                                        if(material.getMes() == 11){
                                                            table3.addCell("Diciembre");
                                                            table3.addCell(material.getMaterial());
                                                            table3.addCell(String.valueOf(material.getCantidad()));
                                                            table3.addCell(material.getUnidadmedida());
                                                            table3.addCell(decimales.format(material.getPreciounitario()));
                                                            double precioiva = (material.getCantidad() * material.getPreciounitario())*0.16;
                                                            double preciototal = (material.getCantidad() * material.getPreciounitario()) + precioiva;
                                                            table3.addCell(decimales.format(preciototal));
                                                            costoTotal += preciototal;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }


        table3.setFontSize(10);
        table3.setTextAlignment(TextAlignment.CENTER);
        document.add(table3);

//---------------------------------------------------------------------------------------------------------------------------------------------------------
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
        Table table2 = new Table(2);

        table2.addCell("Monto total materiales utilizados)").setHorizontalAlignment(HorizontalAlignment.RIGHT);
        table2.addCell(decimales.format(costoTotal)).setHorizontalAlignment(HorizontalAlignment.RIGHT);

        table2.addCell("Precio total de materiales (IVA incluido) ").setHorizontalAlignment(HorizontalAlignment.RIGHT);
        table2.addCell(decimales.format(totalMateriales)).setHorizontalAlignment(HorizontalAlignment.RIGHT);

        table2.addCell("Costo final").setHorizontalAlignment(HorizontalAlignment.RIGHT);
        table2.addCell(decimales.format(contrato.getMontoAsignado() - totalMateriales)).setHorizontalAlignment(HorizontalAlignment.RIGHT);

        document.add(new Paragraph(""));
        table2.setFontSize(10);
        table2.setTextAlignment(TextAlignment.CENTER);
        document.add(table2);
        document.close();
    }

    public void listaOrganizada(Contrato contrato){
        double cantidad = 0;
        for (MaterialContratado materialContratado :materialServicios.buscarTodas()) {
            if (materialContratado.getContrato().equals(contrato)){
                for (MaterialRecibido materialRecibido : materialContratado.getMaterialRecibidos()){
                    if(materialRecibido.getFechaEntrega().getMonth() == 0){
                        if(!existeMaterial(materialContratado.getNombreMaterial(),0)){
                            MaterialMesCantidad materialMesCantidad = new MaterialMesCantidad();
                            materialMesCantidad.setCantidad(materialRecibido.getCantidad());
                            materialMesCantidad.setMes(0);
                            materialMesCantidad.setMaterial(materialContratado.getNombreMaterial());
                            materialMesCantidad.setUnidadmedida(materialContratado.getUnidadMedida());
                            materialMesCantidad.setPreciounitario(materialContratado.getPrecio());
                            lista.add(materialMesCantidad);
                        }
                        else{
                            if(existeMaterial(materialContratado.getNombreMaterial(),0)) {
                                boolean ok = false;
                                Iterator<MaterialMesCantidad> it = lista.iterator();
                                while (it.hasNext() && !ok) {
                                    MaterialMesCantidad m = it.next();
                                    if (m.getMaterial().equalsIgnoreCase(materialContratado.getNombreMaterial()) && m.getMes() == 0) {
                                        m.setCantidad(m.getCantidad() + materialRecibido.getCantidad());
                                        ok = true;
                                    }
                                }
                            }
                        }
                    }
                    if(materialRecibido.getFechaEntrega().getMonth() == 1){
                        if(!existeMaterial(materialContratado.getNombreMaterial(),1)){
                            MaterialMesCantidad materialMesCantidad = new MaterialMesCantidad();
                            materialMesCantidad.setCantidad(materialRecibido.getCantidad());
                            materialMesCantidad.setMes(1);
                            materialMesCantidad.setMaterial(materialContratado.getNombreMaterial());
                            materialMesCantidad.setUnidadmedida(materialContratado.getUnidadMedida());
                            materialMesCantidad.setPreciounitario(materialContratado.getPrecio());

                            lista.add(materialMesCantidad);
                        }
                        else {
                            if (existeMaterial(materialContratado.getNombreMaterial(), 1)) {
                                boolean ok = false;
                                Iterator<MaterialMesCantidad> it = lista.iterator();
                                while (it.hasNext() && !ok) {
                                    MaterialMesCantidad m = it.next();
                                    if (m.getMaterial().equalsIgnoreCase(materialContratado.getNombreMaterial()) && m.getMes()==1) {
                                        m.setCantidad(m.getCantidad() + materialRecibido.getCantidad());
                                        ok = true;
                                    }
                                }
                            }
                        }
                    }
                    if(materialRecibido.getFechaEntrega().getMonth() == 2){
                        if(!existeMaterial(materialContratado.getNombreMaterial(),2)){
                            MaterialMesCantidad materialMesCantidad = new MaterialMesCantidad();
                            materialMesCantidad.setCantidad(materialRecibido.getCantidad());
                            materialMesCantidad.setMes(2);
                            materialMesCantidad.setMaterial(materialContratado.getNombreMaterial());
                            materialMesCantidad.setUnidadmedida(materialContratado.getUnidadMedida());
                            materialMesCantidad.setPreciounitario(materialContratado.getPrecio());

                            lista.add(materialMesCantidad);
                        }
                        else {
                            if (existeMaterial(materialContratado.getNombreMaterial(), 2)) {
                                boolean ok = false;
                                Iterator<MaterialMesCantidad> it = lista.iterator();
                                while (it.hasNext() && !ok) {
                                    MaterialMesCantidad m = it.next();
                                    if (m.getMaterial().equalsIgnoreCase(materialContratado.getNombreMaterial()) && m.getMes() == 2) {
                                        m.setCantidad(m.getCantidad() + materialRecibido.getCantidad());
                                        ok = true;
                                    }
                                }
                            }
                        }
                    }
                    if(materialRecibido.getFechaEntrega().getMonth() == 3){
                        if(!existeMaterial(materialContratado.getNombreMaterial(),3)){
                            MaterialMesCantidad materialMesCantidad = new MaterialMesCantidad();
                            materialMesCantidad.setCantidad(materialRecibido.getCantidad());
                            materialMesCantidad.setMes(3);
                            materialMesCantidad.setMaterial(materialContratado.getNombreMaterial());
                            materialMesCantidad.setUnidadmedida(materialContratado.getUnidadMedida());
                            materialMesCantidad.setPreciounitario(materialContratado.getPrecio());

                            lista.add(materialMesCantidad);
                        }
                        else {
                            if (existeMaterial(materialContratado.getNombreMaterial(), 3)) {
                                boolean ok = false;
                                Iterator<MaterialMesCantidad> it = lista.iterator();
                                while (it.hasNext() && !ok) {
                                    MaterialMesCantidad m = it.next();
                                    if (m.getMaterial().equalsIgnoreCase(materialContratado.getNombreMaterial()) && m.getMes()==3) {
                                        m.setCantidad(m.getCantidad() + materialRecibido.getCantidad());
                                        ok = true;
                                    }
                                }
                            }
                        }
                    }
                    if(materialRecibido.getFechaEntrega().getMonth() == 4){
                        if(!existeMaterial(materialContratado.getNombreMaterial(),4)){
                            MaterialMesCantidad materialMesCantidad = new MaterialMesCantidad();
                            materialMesCantidad.setCantidad(materialRecibido.getCantidad());
                            materialMesCantidad.setMes(4);
                            materialMesCantidad.setMaterial(materialContratado.getNombreMaterial());
                            materialMesCantidad.setUnidadmedida(materialContratado.getUnidadMedida());
                            materialMesCantidad.setPreciounitario(materialContratado.getPrecio());

                            lista.add(materialMesCantidad);
                        }
                        else {
                            if (existeMaterial(materialContratado.getNombreMaterial(), 4)) {
                                boolean ok = false;
                                Iterator<MaterialMesCantidad> it = lista.iterator();
                                while (it.hasNext() && !ok) {
                                    MaterialMesCantidad m = it.next();
                                    if (m.getMaterial().equalsIgnoreCase(materialContratado.getNombreMaterial()) && m.getMes()==4) {
                                        m.setCantidad(m.getCantidad() + materialRecibido.getCantidad());
                                        ok = true;
                                    }
                                }
                            }
                        }
                    }
                    if(materialRecibido.getFechaEntrega().getMonth() == 5){
                        if(!existeMaterial(materialContratado.getNombreMaterial(),5)){
                            MaterialMesCantidad materialMesCantidad = new MaterialMesCantidad();
                            materialMesCantidad.setCantidad(materialRecibido.getCantidad());
                            materialMesCantidad.setMes(5);
                            materialMesCantidad.setMaterial(materialContratado.getNombreMaterial());
                            materialMesCantidad.setUnidadmedida(materialContratado.getUnidadMedida());
                            materialMesCantidad.setPreciounitario(materialContratado.getPrecio());

                            lista.add(materialMesCantidad);
                        }
                        else {
                            if (existeMaterial(materialContratado.getNombreMaterial(), 5)) {
                                boolean ok = false;
                                Iterator<MaterialMesCantidad> it = lista.iterator();
                                while (it.hasNext() && !ok) {
                                    MaterialMesCantidad m = it.next();
                                    if (m.getMaterial().equalsIgnoreCase(materialContratado.getNombreMaterial()) && m.getMes()==5) {
                                        m.setCantidad(m.getCantidad() + materialRecibido.getCantidad());
                                        ok = true;
                                    }
                                }
                            }
                        }
                    }
                    if(materialRecibido.getFechaEntrega().getMonth() == 6){
                        if(!existeMaterial(materialContratado.getNombreMaterial(),6)){
                            MaterialMesCantidad materialMesCantidad = new MaterialMesCantidad();
                            materialMesCantidad.setCantidad(materialRecibido.getCantidad());
                            materialMesCantidad.setMes(6);
                            materialMesCantidad.setMaterial(materialContratado.getNombreMaterial());
                            materialMesCantidad.setUnidadmedida(materialContratado.getUnidadMedida());
                            materialMesCantidad.setPreciounitario(materialContratado.getPrecio());

                            lista.add(materialMesCantidad);
                        }
                        else {
                            if (existeMaterial(materialContratado.getNombreMaterial(), 6)) {
                                boolean ok = false;
                                Iterator<MaterialMesCantidad> it = lista.iterator();
                                while (it.hasNext() && !ok) {
                                    MaterialMesCantidad m = it.next();
                                    if (m.getMaterial().equalsIgnoreCase(materialContratado.getNombreMaterial()) && m.getMes()==6) {
                                        m.setCantidad(m.getCantidad() + materialRecibido.getCantidad());
                                        ok = true;
                                    }
                                }
                            }
                        }
                    }
                    if(materialRecibido.getFechaEntrega().getMonth() == 7){
                        if(!existeMaterial(materialContratado.getNombreMaterial(),7)){
                            MaterialMesCantidad materialMesCantidad = new MaterialMesCantidad();
                            materialMesCantidad.setCantidad(materialRecibido.getCantidad());
                            materialMesCantidad.setMes(7);
                            materialMesCantidad.setMaterial(materialContratado.getNombreMaterial());
                            materialMesCantidad.setUnidadmedida(materialContratado.getUnidadMedida());
                            materialMesCantidad.setPreciounitario(materialContratado.getPrecio());

                            lista.add(materialMesCantidad);
                        }
                        else {
                            if (existeMaterial(materialContratado.getNombreMaterial(), 7)) {
                                boolean ok = false;
                                Iterator<MaterialMesCantidad> it = lista.iterator();
                                while (it.hasNext() && !ok) {
                                    MaterialMesCantidad m = it.next();
                                    if (m.getMaterial().equalsIgnoreCase(materialContratado.getNombreMaterial()) && m.getMes()==7) {
                                        m.setCantidad(m.getCantidad() + materialRecibido.getCantidad());
                                        ok = true;
                                    }
                                }
                            }
                        }
                    }
                    if(materialRecibido.getFechaEntrega().getMonth() == 8){
                        if(!existeMaterial(materialContratado.getNombreMaterial(),8)){
                            MaterialMesCantidad materialMesCantidad = new MaterialMesCantidad();
                            materialMesCantidad.setCantidad(materialRecibido.getCantidad());
                            materialMesCantidad.setMes(8);
                            materialMesCantidad.setMaterial(materialContratado.getNombreMaterial());
                            materialMesCantidad.setUnidadmedida(materialContratado.getUnidadMedida());
                            materialMesCantidad.setPreciounitario(materialContratado.getPrecio());

                            lista.add(materialMesCantidad);
                        }
                        else {
                            if (existeMaterial(materialContratado.getNombreMaterial(), 8)) {
                                boolean ok = false;
                                Iterator<MaterialMesCantidad> it = lista.iterator();
                                while (it.hasNext() && !ok) {
                                    MaterialMesCantidad m = it.next();
                                    if (m.getMaterial().equalsIgnoreCase(materialContratado.getNombreMaterial()) && m.getMes()==8) {
                                        m.setCantidad(m.getCantidad() + materialRecibido.getCantidad());
                                        ok = true;
                                    }
                                }
                            }
                        }
                    }
                    if(materialRecibido.getFechaEntrega().getMonth() == 9){
                        if(!existeMaterial(materialContratado.getNombreMaterial(),9)){
                            MaterialMesCantidad materialMesCantidad = new MaterialMesCantidad();
                            materialMesCantidad.setCantidad(materialRecibido.getCantidad());
                            materialMesCantidad.setMes(9);
                            materialMesCantidad.setMaterial(materialContratado.getNombreMaterial());
                            materialMesCantidad.setUnidadmedida(materialContratado.getUnidadMedida());
                            materialMesCantidad.setPreciounitario(materialContratado.getPrecio());

                            lista.add(materialMesCantidad);
                        }
                        else {
                            if (existeMaterial(materialContratado.getNombreMaterial(), 9)) {
                                boolean ok = false;
                                Iterator<MaterialMesCantidad> it = lista.iterator();
                                while (it.hasNext() && !ok) {
                                    MaterialMesCantidad m = it.next();
                                    if (m.getMaterial().equalsIgnoreCase(materialContratado.getNombreMaterial()) && m.getMes()==9) {
                                        m.setCantidad(m.getCantidad() + materialRecibido.getCantidad());
                                        ok = true;
                                    }
                                }
                            }
                        }
                    }
                    if(materialRecibido.getFechaEntrega().getMonth() == 10){
                        if(!existeMaterial(materialContratado.getNombreMaterial(),10)){
                            MaterialMesCantidad materialMesCantidad = new MaterialMesCantidad();
                            materialMesCantidad.setCantidad(materialRecibido.getCantidad());
                            materialMesCantidad.setMes(10);
                            materialMesCantidad.setMaterial(materialContratado.getNombreMaterial());
                            materialMesCantidad.setUnidadmedida(materialContratado.getUnidadMedida());
                            materialMesCantidad.setPreciounitario(materialContratado.getPrecio());

                            lista.add(materialMesCantidad);
                        }
                        else {
                            if (existeMaterial(materialContratado.getNombreMaterial(), 10)) {
                                boolean ok = false;
                                Iterator<MaterialMesCantidad> it = lista.iterator();
                                while (it.hasNext() && !ok) {
                                    MaterialMesCantidad m = it.next();
                                    if (m.getMaterial().equalsIgnoreCase(materialContratado.getNombreMaterial()) && m.getMes()==10) {
                                        m.setCantidad(m.getCantidad() + materialRecibido.getCantidad());
                                        ok = true;
                                    }
                                }
                            }
                        }
                    }
                    if(materialRecibido.getFechaEntrega().getMonth() == 11){
                        if(!existeMaterial(materialContratado.getNombreMaterial(),11)){
                            MaterialMesCantidad materialMesCantidad = new MaterialMesCantidad();
                            materialMesCantidad.setCantidad(materialRecibido.getCantidad());
                            materialMesCantidad.setMes(11);
                            materialMesCantidad.setMaterial(materialContratado.getNombreMaterial());
                            materialMesCantidad.setUnidadmedida(materialContratado.getUnidadMedida());
                            materialMesCantidad.setPreciounitario(materialContratado.getPrecio());

                            lista.add(materialMesCantidad);
                        }
                        else {
                            if (existeMaterial(materialContratado.getNombreMaterial(), 11)) {
                                boolean ok = false;
                                Iterator<MaterialMesCantidad> it = lista.iterator();
                                while (it.hasNext() && !ok) {
                                    MaterialMesCantidad m = it.next();
                                    if (m.getMaterial().equalsIgnoreCase(materialContratado.getNombreMaterial()) && m.getMes()==11) {
                                        m.setCantidad(m.getCantidad() + materialRecibido.getCantidad());
                                        ok = true;
                                    }
                                }
                            }
                        }
                    }

                }
            }

        }

    }

    public double cantidadUsadoPorMes(String material,int mes){
        double sum = 0;
        for (MaterialRecibido materialRecibido :materialesUsadosServicios.buscarTodas()) {
            if (mes == materialRecibido.getFechaEntrega().getMonth() && material.equalsIgnoreCase(materialRecibido.getMaterialContratado().getNombreMaterial())){
                sum += materialRecibido.getCantidad();
            }
        }
        return sum;
    }

    public boolean existeMaterial(String material, int mes){
        boolean ok = false;
        Iterator<MaterialMesCantidad> it = lista.iterator();
        while (it.hasNext() && !ok){
            MaterialMesCantidad m = it.next();
            if(m.getMaterial().equalsIgnoreCase(material) && m.getMes() == mes){
                ok = true;
            }
        }
        return ok;
    }

}
