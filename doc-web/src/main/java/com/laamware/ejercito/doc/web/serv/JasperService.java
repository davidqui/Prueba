package com.laamware.ejercito.doc.web.serv;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.AppConstants;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class JasperService {

	@Autowired
	JasperReportManager jrManager;

	@Autowired
	OFS ofs;
	/**
	 * 
	 * @param template
	 * @param data
	 * @param beans
	 * @param dataSource
	 * @return
	 * @throws JRException
	 */
	public byte[] pdf(String template, Map<String, Object> data,Collection<?> beans, Connection connection) throws JRException {

		JasperPrint print = print(template, data, beans, connection);

		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(print));

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(stream));

		exporter.exportReport();

		return stream.toByteArray();
	}

	/**
	 * 
	 * @param template
	 * @param data
	 * @param beans
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	public String savePdf(String template, Map<String, Object> data,Collection<?> beans, Connection connection) throws Exception {
		byte[] bytes = pdf(template, data, beans, connection);
		return ofs.save(bytes, AppConstants.MIME_TYPE_PDF);
	}

	public JasperPrint print(String template, Map<String, Object> data,Collection<?> beans, Connection connection) throws JRException {
		JasperReport report = jrManager.get(template);
		
		JasperPrint print = null;
		if( template != null && data != null && beans != null ){
			
			print = JasperFillManager.fillReport(report, data,new JRBeanCollectionDataSource(beans));
			
		}else if( template != null && data != null && connection != null ){
			
			print = JasperFillManager.fillReport(report, data, connection );
			
		} 
		return print;
	}	

}
