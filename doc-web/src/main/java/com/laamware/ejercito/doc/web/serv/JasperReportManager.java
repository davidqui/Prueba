package com.laamware.ejercito.doc.web.serv;

import java.io.File;
import java.util.Date;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JasperReportManager extends SimpleCache<JasperReport> {

	@Value("${docweb.archivos.jasper}")
	String templateRoot;

	public JasperReport get(String template) throws JRException {
		String path = FilenameUtils.concat(templateRoot,
				String.format("%s.jrxml", template));
		File file = new File(path);
		if (!file.exists()) {
			throw new JRException("Archivo no existe: " + path);
		}
		Date lastMod = new Date(file.lastModified());
		JasperReport report = super.get(path, lastMod);
		if (report == null) {
			report = JasperCompileManager.compileReport(path);
			super.put(path, report, lastMod);
		}
		return report;
	}

}
