package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.ReporteDependenciaDTO;
import com.laamware.ejercito.doc.web.dto.ReporteDependenciaTrdDTO;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.ReporteRepository;
import com.laamware.ejercito.doc.web.util.DateUtil;
import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author egonzalezm
 */
@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public List<ReporteDependenciaDTO> obtenerReporteDependencia(Integer dependencia, Date fechaInicial, Date fechaFinal) {
        DateUtil.setTime(fechaInicial, DateUtil.SetTimeType.START_TIME);
        DateUtil.setTime(fechaFinal, DateUtil.SetTimeType.END_TIME);

        List<Object[]> resultados = reporteRepository.findReporteDependencia(dependencia, fechaInicial, fechaFinal);
        List<ReporteDependenciaDTO> list = new ArrayList<>();
        int total = 0;
        for (Object[] resultado : resultados) {
            ReporteDependenciaDTO dTO = new ReporteDependenciaDTO((String) resultado[0], (String) resultado[1], ((BigDecimal) resultado[2]).intValue());
            list.add(dTO);
            total = total + dTO.getCantidad();
        }

        if (total > 0) {
            ReporteDependenciaDTO dTO = new ReporteDependenciaDTO("TOTAL", "TOTAL", total);
            list.add(dTO);
        }

        return list;
    }

    public List<ReporteDependenciaTrdDTO> obtenerReporteDependenciaXtrd(Integer dependencia, Date fechaInicial, Date fechaFinal) {
        DateUtil.setTime(fechaInicial, DateUtil.SetTimeType.START_TIME);
        DateUtil.setTime(fechaFinal, DateUtil.SetTimeType.END_TIME);

        List<Object[]> resultados = reporteRepository.findReporteDependenciaXtrd(dependencia, fechaInicial, fechaFinal);
        List<ReporteDependenciaTrdDTO> list = new ArrayList<>();
        for (Object[] resultado : resultados) {
            ReporteDependenciaTrdDTO dTO = new ReporteDependenciaTrdDTO((String) resultado[0], ((BigDecimal) resultado[1]).intValue());
            list.add(dTO);
        }
        return list;
    }

    public DefaultCategoryDataset generaDatasetReporteDependencia(Usuario usuario, Date fechaInicialValor, Date fechaFinalValor) {
        final DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        List<ReporteDependenciaDTO> dependencias = obtenerReporteDependencia(usuario.getDependencia().getId(), fechaInicialValor, fechaFinalValor);

        for (ReporteDependenciaDTO dTO : dependencias) {
            if (dTO.getSiglaDependencia() == null) {
                categoryDataset.setValue(dTO.getCantidad(), "DOC_GENERADOS", dTO.getNombreDependencia());
            } else {
                categoryDataset.setValue(dTO.getCantidad(), "DOC_GENERADOS", dTO.getSiglaDependencia());
            }
        }
        return categoryDataset;
    }

    public DefaultCategoryDataset generaDatasetReporteDependenciaTrd(Usuario usuario, Date fechaInicialValor, Date fechaFinalValor) {
        final DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        List<ReporteDependenciaTrdDTO> dependencias = obtenerReporteDependenciaXtrd(usuario.getDependencia().getId(), fechaInicialValor, fechaFinalValor);

        for (ReporteDependenciaTrdDTO dTO : dependencias) {
            categoryDataset.setValue(dTO.getCantidad(), "DOC_GENERADOS", dTO.getTrdNombre());
        }
        return categoryDataset;
    }

    public JFreeChart generaGraficaReporteDependencia(Usuario usuario, Date fechaInicialValor, Date fechaFinalValor, DefaultCategoryDataset categoryDataset) {
        final String title = usuario.getDependencia().getNombre() + "\n" + dateFormat.format(fechaInicialValor) + " al " + dateFormat.format(fechaFinalValor);
        final String categoryAxisLabel = null;
        final String valueAxisLabel = null;
        final boolean legend = true;
        final boolean tooltips = true;
        final boolean urls = false;

        final JFreeChart barChart = ChartFactory.createBarChart3D(title, categoryAxisLabel, valueAxisLabel, categoryDataset, PlotOrientation.VERTICAL, legend, tooltips, urls);

        //Clase que controla la representacion visual
        final CategoryPlot categoryPlot = (CategoryPlot) barChart.getPlot();
        //Cambia el color de fondo de la grafica
        categoryPlot.setBackgroundPaint(Color.white);
        //Pinta las Lineas punteadas del eje y
        categoryPlot.setRangeGridlinesVisible(true);
        //Color las Lineas punteadas del eje y
        categoryPlot.setRangeGridlinePaint(Color.gray);
        //Pinta las Lineas punteadas del eje x
        categoryPlot.setDomainGridlinesVisible(false);

        //Retorna el render de la trama
        BarRenderer categoryItemRenderer = (BarRenderer) barChart.getCategoryPlot().getRenderer();
        //Cambia el color de la barra
        Color color = new Color(255, 165, 0);
        categoryItemRenderer.setSeriesPaint(0, color);
        //Ancho de la barra
        categoryItemRenderer.setMaximumBarWidth(.07);

        //Cambio del label informativo en la barra
//        categoryItemRenderer.setSeriesItemLabelGenerator(0, new StandardCategoryItemLabelGenerator());
//        //Ordena que se visualizan los labels
//        categoryItemRenderer.setSeriesItemLabelsVisible(0, true);
//        //Crea la clase que posiciona el label en la barra
//        final ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.TOP_CENTER);
//        categoryItemRenderer.setSeriesPositiveItemLabelPosition(0, position);
//        categoryItemRenderer.setSeriesItemLabelPaint(0, Color.white);
//        categoryItemRenderer.setSeriesItemLabelFont(0, new Font("Serif", Font.BOLD, 12));
        //Otra forma
        categoryItemRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        categoryItemRenderer.setBaseItemLabelsVisible(true);
        ItemLabelPosition outside12 = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER);
        categoryItemRenderer.setBasePositiveItemLabelPosition(outside12);
        categoryItemRenderer.setBaseItemLabelPaint(Color.black);
//        categoryItemRenderer.setBaseItemLabelFont(new Font("Serif", Font.BOLD, 14));

        //Devuelve el eje del dominio o del eje X
        CategoryAxis domainAxis = categoryPlot.getDomainAxis();
        //Cambia la posición de los labels del eje x
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        return barChart;
    }

    public JFreeChart generaGraficaReporteDependenciaTrd(Usuario usuario, Date fechaInicialValor, Date fechaFinalValor, DefaultCategoryDataset categoryDataset) {
        final String title = "DOCUMENTACION GENERADA POR TRD " + usuario.getDependencia().getNombre();
        final String categoryAxisLabel = null;
        final String valueAxisLabel = null;
        final boolean legend = false;
        final boolean tooltips = true;
        final boolean urls = false;

        final JFreeChart barChart = ChartFactory.createBarChart3D(title, categoryAxisLabel, valueAxisLabel, categoryDataset, PlotOrientation.HORIZONTAL, legend, tooltips, urls);

        //Clase que controla la representacion visual
        final CategoryPlot categoryPlot = (CategoryPlot) barChart.getPlot();
        //Cambia el color de fondo de la grafica
        categoryPlot.setBackgroundPaint(Color.white);
        //Pinta las Lineas punteadas del eje y
        categoryPlot.setRangeGridlinesVisible(true);
        //Color las Lineas punteadas del eje y
        categoryPlot.setRangeGridlinePaint(Color.gray);
        //Pinta las Lineas punteadas del eje x
        categoryPlot.setDomainGridlinesVisible(false);

        //Retorna el render de la trama
        BarRenderer categoryItemRenderer = (BarRenderer) barChart.getCategoryPlot().getRenderer();
        //Cambia el color de la barra
        Color color = new Color(173, 216, 230);
        categoryItemRenderer.setSeriesPaint(0, color);
        //Ancho de la barra
        categoryItemRenderer.setMaximumBarWidth(.07);

        //Cambio del label informativo en la barra
//        categoryItemRenderer.setSeriesItemLabelGenerator(0, new StandardCategoryItemLabelGenerator());
//        //Ordena que se visualizan los labels
//        categoryItemRenderer.setSeriesItemLabelsVisible(0, true);
//        //Crea la clase que posiciona el label en la barra
//        final ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.TOP_CENTER);
//        categoryItemRenderer.setSeriesPositiveItemLabelPosition(0, position);
//        categoryItemRenderer.setSeriesItemLabelPaint(0, Color.white);
//        categoryItemRenderer.setSeriesItemLabelFont(0, new Font("Serif", Font.BOLD, 12));
        //Otra forma
        categoryItemRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        categoryItemRenderer.setBaseItemLabelsVisible(true);
        ItemLabelPosition outside12 = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE3, TextAnchor.BASELINE_CENTER);
        categoryItemRenderer.setBasePositiveItemLabelPosition(outside12);
        categoryItemRenderer.setBaseItemLabelPaint(Color.black);
        System.err.println("getColumnCount= " + categoryDataset.getColumnCount());
        if (categoryDataset.getColumnCount() > 15) {
            categoryItemRenderer.setBaseItemLabelFont(new Font("Serif", Font.LAYOUT_LEFT_TO_RIGHT, 6));
        } else {
            categoryItemRenderer.setBaseItemLabelFont(new Font("Serif", Font.LAYOUT_LEFT_TO_RIGHT, 12));
        }

        //Devuelve el eje del dominio o del eje X
        CategoryAxis domainAxis = categoryPlot.getDomainAxis();
        //Cambia la posición de los labels del eje x

        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
        categoryPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        if (categoryDataset.getColumnCount() > 15) {
            categoryPlot.getDomainAxis().setTickLabelFont(new Font("Serif", Font.LAYOUT_LEFT_TO_RIGHT, 4));
        } else {
            categoryPlot.getDomainAxis().setTickLabelFont(new Font("Serif", Font.LAYOUT_LEFT_TO_RIGHT, 12));
        }

        return barChart;
    }
}
