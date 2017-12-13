package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.ReporteDependenciaDTO;
import com.laamware.ejercito.doc.web.dto.ReporteDependenciaTrdDTO;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.serv.JasperService;
import com.laamware.ejercito.doc.web.serv.ReporteService;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.io.IOUtils;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author egonzalezm
 */
@Controller
@RequestMapping(ReporteDependenciaController.PATH)
public class ReporteDependenciaController {

    static final String PATH = "/reporteDependencia";

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JasperService jasperService;
    
    @Autowired
    private DataSource dataSource;
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public String init(
            Model model, Principal principal,
            HttpServletResponse resp,
            HttpServletRequest req,
            RedirectAttributes redirect) {
        return "reporte-dependencia";
    }

    @RequestMapping(value = "/consultar", method = RequestMethod.GET)
    public String consultar(
            Model model, Principal principal,
            HttpServletResponse resp,
            HttpServletRequest req,
            RedirectAttributes redirect,
            @RequestParam(value = "fechaInicial", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicialValor,
            @RequestParam(value = "fechaFinal", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFinalValor) {

        

        if (fechaInicialValor != null) {
            model.addAttribute("fechaInicial", dateFormat.format(fechaInicialValor));
        }

        if (fechaFinalValor != null) {
            model.addAttribute("fechaFinal", dateFormat.format(fechaFinalValor));
        }

        final String login = principal.getName();
        Usuario usuario = usuarioRepository.findByLoginAndActivo(login, Boolean.TRUE);

        if (fechaInicialValor == null || fechaFinalValor == null) {
            model.addAttribute(AppConstants.FLASH_ERROR, "La fecha inicial o final no deben estar vacias");
            return "reporte-dependencia";
        }

        List<ReporteDependenciaDTO> dependencias = reporteService.obtenerReporteDependencia(usuario.getDependencia().getId(), fechaInicialValor, fechaFinalValor);
        model.addAttribute("dependencias", dependencias.size());

        List<ReporteDependenciaTrdDTO> dependenciasTrd = reporteService.obtenerReporteDependenciaXtrd(Integer.MIN_VALUE, fechaInicialValor, fechaFinalValor);
        model.addAttribute("dependenciasTrd", dependenciasTrd.size());

        return "reporte-dependencia";
    }

    @RequestMapping(value = "/barraDependencia", method = RequestMethod.GET)
    public void buildBarChartReporteDependencia(
            Principal principal,
            HttpServletResponse resp,
            @RequestParam(value = "fechaInicial", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicialValor,
            @RequestParam(value = "fechaFinal", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFinalValor) throws IOException {

        final String login = principal.getName();
        Usuario usuario = usuarioRepository.findByLoginAndActivo(login, Boolean.TRUE);

        final DefaultCategoryDataset categoryDataset = reporteService.generaDatasetReporteDependencia(usuario, fechaInicialValor, fechaFinalValor);
        final JFreeChart barChart = reporteService.generaGraficaReporteDependencia(usuario, fechaInicialValor, fechaFinalValor, categoryDataset);

        writeChartAsPNGImage(barChart, 800, 700, resp);
    }

    @RequestMapping(value = "/barraDependenciaTrd", method = RequestMethod.GET)
    public void buildBarChartDependenciaTrd(
            Principal principal,
            HttpServletResponse resp,
            @RequestParam(value = "fechaInicial", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicialValor,
            @RequestParam(value = "fechaFinal", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFinalValor) throws IOException {

        final String login = principal.getName();
        Usuario usuario = usuarioRepository.findByLoginAndActivo(login, Boolean.TRUE);

        final DefaultCategoryDataset categoryDataset = reporteService.generaDatasetReporteDependenciaTrd(usuario, fechaInicialValor, fechaFinalValor);
        final JFreeChart barChart = reporteService.generaGraficaReporteDependenciaTrd(usuario, fechaInicialValor, fechaFinalValor, categoryDataset);
        writeChartAsPNGImage(barChart, 800, 700, resp);
    }

    @RequestMapping(value = "generarReporte", method = RequestMethod.GET)
    public void generarReporte(
            Model model, Principal principal,
            HttpServletResponse resp,
            HttpServletRequest req,
            RedirectAttributes redirect,
            @RequestParam(value = "fechaInicial", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicialValor,
            @RequestParam(value = "fechaFinal", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFinalValor) {

        Map<String, Object> params = new HashMap<>();
        byte[] reporteGenerado = null;
        String nombreRporte = "Blank_Letter";

        try {
            if (fechaInicialValor != null) {
                model.addAttribute("fechaInicial", dateFormat.format(fechaInicialValor));
            }

            if (fechaFinalValor != null) {
                model.addAttribute("fechaFinal", dateFormat.format(fechaFinalValor));
            }
            
            final String login = principal.getName();
            Usuario usuario = usuarioRepository.findByLoginAndActivo(login, Boolean.TRUE);
            final DefaultCategoryDataset categoryDataset1 = reporteService.generaDatasetReporteDependencia(usuario, fechaInicialValor, fechaFinalValor);
            final JFreeChart barChart1 = reporteService.generaGraficaReporteDependencia(usuario, fechaInicialValor, fechaFinalValor, categoryDataset1);
            params.put("dependencia", barChart1.createBufferedImage(520, 700,500, 400, null));
            
            final DefaultCategoryDataset categoryDataset2 = reporteService.generaDatasetReporteDependenciaTrd(usuario, fechaInicialValor, fechaFinalValor);
            final JFreeChart barChart2 = reporteService.generaGraficaReporteDependenciaTrd(usuario, fechaInicialValor, fechaFinalValor, categoryDataset2);
            params.put("dependenciaTrd", barChart2.createBufferedImage(520, 700,500, 400, null));
            
            reporteGenerado = jasperService.pdf(nombreRporte, params, null, dataSource.getConnection());
        } catch (Exception e) {
            model.addAttribute(AppConstants.FLASH_ERROR, "Error general el reporte: " + e.getMessage());
            e.printStackTrace();
        }

        if (reporteGenerado != null) {
            try {
                resp.setContentLength((int) reporteGenerado.length);
                resp.setContentType("application/pdf");

                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", "SalidaReporte.pdf");
                resp.setHeader(headerKey, headerValue);

                // Write response
                ServletOutputStream outStream = resp.getOutputStream();
                IOUtils.write(reporteGenerado, outStream);
            } catch (Exception e) {
                model.addAttribute(AppConstants.FLASH_ERROR, "Error general enviar reporte al navegador: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void writeChartAsPNGImage(final JFreeChart chart, final int width, final int height, HttpServletResponse response) throws IOException {
        final BufferedImage bufferedImage = chart.createBufferedImage(width, height, 500, 400, null);
        
//        BufferedImage after = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//        AffineTransform at = new AffineTransform();
//        at.scale(2.0, 2.0);
//        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
//        after = scaleOp.filter(bufferedImage, after);
        
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        ChartUtilities.writeBufferedImageAsPNG(response.getOutputStream(), bufferedImage);
    }
}
