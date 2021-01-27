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
import com.itextpdf.layout.property.TextAlignment;
import com.sistema.empresa.modelos.SolicitudMaterial;
import com.sistema.empresa.serviciosinterface.IMaterialContratadoServicios;
import com.sistema.empresa.serviciosinterface.ISolicitudServicios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/solicitud")
public class SolicitudMaterialesControladora {

    @Autowired
    private JavaMailSender javaMailSender;

    private SimpleMailMessage simpleMailMessage;

    private static final Logger LOGGER = LoggerFactory.getLogger(SolicitudMaterialesControladora.class);

    @Autowired
    private IMaterialContratadoServicios materialServicios;

    @Autowired
    private ISolicitudServicios solicitudServicios;

    private static final String RUTA = "/Users/luisito/IdeaProjects/empresa/src/main/resources/reports/solicitud.pdf";
    private static final String IMAGEN = "/Users/luisito/IdeaProjects/empresa/src/main/resources/static/img/logo.jpg";

    @GetMapping("/lista")
    public String mostrarLista(){
        return "solicitudmateriales/listarsolicitud";
    }

    @GetMapping("/agregar")
    public ModelAndView modal(ModelAndView modelAndView){
        modelAndView.addObject("solicitud", new SolicitudMaterial());
        modelAndView.setViewName("solicitudmateriales/solicitud");
        return modelAndView;
    }

    @PostMapping("/guardar")
    public String guardar(SolicitudMaterial solicitudMaterial) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date fecha = sdf.parse(sdf.format(new Date()));
        solicitudMaterial.setFechaSolicitud(fecha);
        solicitudServicios.guardar(solicitudMaterial);
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String modificar(@PathVariable("id") int id, Model model){
        SolicitudMaterial solicitudMaterial = solicitudServicios.buscarPorId(id);
        model.addAttribute("solicitud",solicitudMaterial);
        return "solicitudmateriales/solicitud";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") int id,Model model){
        solicitudServicios.eliminar(id);
        System.out.println("Elemento Eliminado");
        return "redirect:/";
    }

    @GetMapping("/vistaeliminar/{id}")
    public String vistaEliminar(@PathVariable("id") int id, Model model){
        SolicitudMaterial solicitudMaterial = solicitudServicios.buscarPorId(id);
        model.addAttribute("solicitud",solicitudMaterial);
        return "solicitudmateriales/eliminarsolicitud";
    }

    @GetMapping("/error/{id}")
    public String errorInternet(@PathVariable("id") int id, Model model){
        SolicitudMaterial solicitudMaterial = solicitudServicios.buscarPorId(id);
        model.addAttribute("solicitud",solicitudMaterial);
        return "solicitudmateriales/problemainternet";
    }

    @GetMapping("/enviarSolicitud/{id}")
    public String enviarSolicitud(@PathVariable("id") int id, Model model){
        SolicitudMaterial solicitudMaterial = solicitudServicios.buscarPorId(id);
        model.addAttribute("solicitud",solicitudMaterial);
        return "solicitudmateriales/enviarsolicitud";
    }

    @GetMapping("/enviar/{id}")
    public String enviar(@PathVariable("id") int id) throws IOException, MessagingException {
        SolicitudMaterial solicitudMaterial = solicitudServicios.buscarPorId(id);
        //PDF
        PdfWriter pdfWriter = new PdfWriter(RUTA);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument, PageSize.LETTER);
        PdfFont font = PdfFontFactory.createFont("/System/Library/Fonts/Supplemental/Arial.ttf", PdfEncodings.IDENTITY_H, true);
        PdfFont fontBold = PdfFontFactory.createFont("/System/Library/Fonts/Supplemental/Arial Bold.ttf", PdfEncodings.IDENTITY_H, true);

        //Encabezado
        document.add(new Paragraph("Solicitud de materiales").setFont(font).setFontSize(25).setTextAlignment(TextAlignment.CENTER));
        //Imagen
        Image logo = new Image(ImageDataFactory.create(IMAGEN)).scaleAbsolute(100, 100).setFixedPosition(35,620);
        document.add(logo);
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
        document.add(new Paragraph("Nombre y apellidos: Luis Alberto Chailloux"));
        document.add(new Paragraph("Proyecto: "+solicitudMaterial.getMaterialContratado().getContrato().getProyecto()));
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        document.add(new Paragraph("Fecha de solictud: "+formateador.format(solicitudMaterial.getFechaSolicitud())));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph("Detalle de la solicitud")).setFont(fontBold).setFontSize(15).setTextAlignment(TextAlignment.LEFT);
        document.add(new Paragraph(solicitudMaterial.getMaterialContratado().getNombreMaterial()+": "+String.valueOf(solicitudMaterial.getCantidad())+solicitudMaterial.getMaterialContratado().getUnidadMedida()).setFont(font).setFontSize(12).setTextAlignment(TextAlignment.LEFT));


        document.close();
        //---------------------------------
        //----------------Correo

        MimeMessage message = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("sistemaempresa2@gmail.com");
            helper.setTo(solicitudMaterial.getMaterialContratado().getContrato().getProveedor().getEmail());
            helper.setSubject("Solicitud Del Material "+solicitudMaterial.getMaterialContratado().getNombreMaterial());
            helper.setText("");
            FileSystemResource file = new FileSystemResource(RUTA);
            helper.addAttachment(file.getFilename(), file);

        }catch (MessagingException e) {
            throw new MailParseException(e);
        }
        javaMailSender.send(message);

        //-----------------------------------
        return "redirect:/";
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
    //    Lista
    @ModelAttribute
    public void cargarListas(Model model){
        model.addAttribute("materiales",materialServicios.buscarTodas());
        model.addAttribute("solicitudes",solicitudServicios.buscarTodas());

    }
}
