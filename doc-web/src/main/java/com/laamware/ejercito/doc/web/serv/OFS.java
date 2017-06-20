package com.laamware.ejercito.doc.web.serv;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aspose.words.HeaderFooter;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.HorizontalAlignment;
import com.aspose.words.Paragraph;
import com.aspose.words.RelativeHorizontalPosition;
import com.aspose.words.RelativeVerticalPosition;
import com.aspose.words.Section;
import com.aspose.words.Shape;
import com.aspose.words.ShapeType;
import com.aspose.words.VerticalAlignment;
import com.aspose.words.WrapType;
import com.laamware.ejercito.doc.web.entity.OFSStage;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.OFSStageRepository;
import com.laamware.ejercito.doc.web.util.GeneralUtils;

@Service
public class OFS {

	public static final int DIR_LEVELS = 4;
	
	public static final long MAX_TIME = 7258136400000L;

	private static final String TYPE_APPLICATION_PDF = "application/pdf";

	@Autowired
	OFSStageRepository digiR;

	@Value("${docweb.ofs.root}")
	public void setRoot(String root) {
		this.root = root;
		File rootFile = new File(root);
		if (!rootFile.exists()) {
			try {
				FileUtils.forceMkdir(rootFile);
			} catch (IOException e) {
				throw new RuntimeException("Directorio de OFS no se puede crear: " + root);
			}
		}
	}

	public String getRoot() {
		return root;
	}

	private String root;

	public String save(byte[] bytes, String contentType) throws IOException {
		String id = null;
		File file = null;
		do {
			id = GeneralUtils.newId();
			file = new File(getPath(id));
		} while (file.exists());

		FileUtils.writeByteArrayToFile(file, bytes);
		convertToPDF(file, contentType);
		file.setLastModified(getRandomTime());

		file = new File(getPath(id) + ".cnt");
		FileUtils.write(file, "application/pdf");
		file.setLastModified(getRandomTime());

		makeThumbnail(getPath(id));

		return id;
	}

	public String saveAsIs(byte[] bytes, String contentType) throws IOException {
		String id = null;
		File file = null;
		do {
			id = GeneralUtils.newId();
			file = new File(getPath(id));
		} while (file.exists());

		FileUtils.writeByteArrayToFile(file, bytes);
		file.setLastModified(getRandomTime());

		if (contentType != null && contentType.toLowerCase().startsWith("image/")) {
			String extesion = contentType.split("/")[1];
			file = new File(getPath(id) + "." + extesion);
			FileUtils.writeByteArrayToFile(file, bytes);
			file.setLastModified(getRandomTime());
		} else if (contentType != null && contentType
				.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
			file = new File(getPath(id));
			FileUtils.writeByteArrayToFile(file, bytes);
			file.setLastModified(getRandomTime());
		}

		file = new File(getPath(id) + ".cnt");
		FileUtils.write(file, contentType);
		file.setLastModified(getRandomTime());

		return id;
	}

	private void convertToPDF(File file, String contentType) {

		if (TYPE_APPLICATION_PDF.equals(contentType)) {
			return;
		}

		Process p;
		try {

			File tmp = new File(file.getAbsolutePath() + ".tmp");

			StringBuilder cmd = new StringBuilder("convert ");
			cmd.append(file.getAbsolutePath()).append(" ");
			cmd.append("pdf:").append(tmp);

			p = Runtime.getRuntime().exec(cmd.toString());
			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			StringBuilder result = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				result.append(line + "\n");
			}
			System.out.println(result.toString());

			file.delete();
			tmp.renameTo(file);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void makeThumbnail(String path) {
		String tmb = path + ".tmb";
		Process p;
		try {

			StringBuilder cmd = new StringBuilder("convert -auto-orient -thumbnail 250x250 -unsharp 0x.5 ");
			cmd.append(path).append("[0] ");
			cmd.append("gif:").append(tmb);

			p = Runtime.getRuntime().exec(cmd.toString());
			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			StringBuilder result = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				result.append(line + "\n");
			}
			System.out.println(result.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public byte[] readThumbnail(String id) throws IOException {
		String path = getPath(id);
		File file = new File(path + ".tmb");
		if (!file.exists()) {
			throw new FileNotFoundException("No se encuentra el archivo: " + path + ".tmb");
		}
		byte[] bytes = FileUtils.readFileToByteArray(file);

		return bytes;
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public OFSEntry readPDFAspose(File file) throws Exception {

		if (!file.exists()) {
			throw new FileNotFoundException("No se encuentra el archivo: " + file);
		}

		// PRIMERO QUITAMOS
		/*
		 * PdfReader reader = new PdfReader( file.getPath() ); PdfDictionary
		 * dict = reader.getPageN(1); PdfObject object =
		 * dict.getDirectObject(PdfName.CONTENTS); if (object instanceof
		 * PRStream) { PRStream stream = (PRStream)object; byte[] data =
		 * PdfReader.getStreamBytes(stream);
		 * 
		 * stream.setData(new String(data, "ISO-8859-2").
		 * replace("(Evaluation Only. Created with Aspose.Words. C)-1(opyri)1(gh)-1(t 2003-2016 Aspose Pty Ltd.)"
		 * , "").getBytes("ISO-8859-2")); }
		 * 
		 * 
		 * File fTempSalidaPDF = File.createTempFile("_sigdi_final_temp_",
		 * ".pdf");
		 * 
		 * PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
		 * fTempSalidaPDF )); stamper.close(); reader.close();
		 * 
		 * byte[] bytes = FileUtils.readFileToByteArray( fTempSalidaPDF );
		 */

		byte[] bytes = FileUtils.readFileToByteArray(file);

		OFSEntry entry = new OFSEntry(TYPE_APPLICATION_PDF, bytes);

		/*
		 * try { fTempSalidaPDF.delete(); } catch (Exception e) {
		 * e.printStackTrace(); try { fTempSalidaPDF.deleteOnExit(); } catch
		 * (Exception e2) { e2.printStackTrace(); } }
		 */

		return entry;
	}

	public OFSEntry read(String id) throws IOException {
		String path = getPath(id);
		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException("No se encuentra el archivo: " + path);
		}
		byte[] bytes = FileUtils.readFileToByteArray(file);

		file = new File(path + ".cnt");
		String contentType = FileUtils.readFileToString(file);

		OFSEntry entry = new OFSEntry(contentType, bytes);
		return entry;
	}

	private long getRandomTime() {
		Random rnd = new Random();
		long time = rnd.nextLong();
		if (time < 0)
			time *= -1;
		return time % MAX_TIME;
	}

	public String getPath(String id) {
		StringBuilder b = new StringBuilder(root);
		for (int i = 0; i < DIR_LEVELS; i++) {
			b.append("/").append(id.charAt(i));
		}
		b.append("/").append(id);
		return b.toString();
	}

	private OFSStage newStage(String refId, Usuario usuario, Integer tipo) {
		OFSStage stage = new OFSStage();
		stage.setId(GeneralUtils.newId());
		stage.setRef(refId);
		stage.setUsuario(usuario);
		stage.setTipo(tipo);
		digiR.saveAndFlush(stage);
		return stage;
	}

	public Map<String, Object> save(byte[] bytes, String contentType, String stageId, String refId, Usuario usuario)
			throws IOException {

		HashMap<String, Object> response = new HashMap<String, Object>();

		OFSStage stage = null;
		if (StringUtils.isBlank(stageId)) {
			stage = newStage(refId, usuario, OFSStage.TIPO_SCANNER);
		} else {
			stage = digiR.findOne(stageId);
		}

		String fileId = save(bytes, contentType);

		if (StringUtils.isBlank(stage.getPartes())) {
			stage.setPartes(fileId);
		} else {
			stage.setPartes(stage.getPartes() + ":" + fileId);
		}
		digiR.save(stage);

		response.put("stageId", stage.getId());
		response.put("new", fileId);
		response.put("ids", stage.getPartes().split(":"));

		return response;
	}

	public OFSStage stage(String refId, Integer usuId) {
		List<OFSStage> stages = digiR.findByRefAndUsuarioId(refId, usuId);
		if (stages.size() > 0) {
			return stages.get(0);
		}
		return null;
	}

	public String pdf(String refId, Integer usuarioId) throws IOException {
		OFSStage stage = stage(refId, usuarioId);

		String[] files = stage.getPartes().split(":");
		StringBuilder cmd = new StringBuilder("convert ");
		for (String x : files) {
			cmd.append(" png:").append(getPath(x));
		}
		String pdfId = GeneralUtils.newId();
		String pdfPath = getPath(pdfId);
		try {
			FileUtils.forceMkdir(new File(pdfPath).getParentFile());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		cmd.append(" pdf:").append(getPath(pdfId));

		Process p;
		try {
			p = Runtime.getRuntime().exec(cmd.toString());
			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			StringBuilder result = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				result.append(line + "\n");
			}
			System.out.println(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		File file = new File(pdfPath);
		file.setLastModified(getRandomTime());

		file = new File(pdfPath + ".cnt");
		FileUtils.write(file, "application/pdf");
		file.setLastModified(getRandomTime());

		makeThumbnail(pdfPath);

		digiR.delete(stage);

		return pdfId;
	}

	public String html2pdf(String html) throws IOException {
		String id = save(html.getBytes(), "application/pdf");
		String path = getPath(id);
		String tmpHtml = path + ".html";
		String tmpPdf = path + ".pdf";
		File def = new File(path);
		File htmlFile = new File(tmpHtml);
		def.renameTo(htmlFile);
		File pdfFile = new File(tmpPdf);

		try {
			// String[] args = new String[] { "wkhtmltopdf", "--encoding",
			// "'utf-8'", "-T", "10mm", "-B", "20mm", "-L", "20mm", "-R",
			// "20mm", "-s", "Letter", tmpHtml, tmpPdf };
			String[] args = new String[] { "wkhtmltopdf", "--encoding", "'utf-8'", "-T", "0mm", "-B", "0mm", "-L",
					"20mm", "-R", "20mm", "-s", "Letter", tmpHtml, tmpPdf };
			Process p = new ProcessBuilder(args).start();

			InputStream is = p.getErrorStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;

			System.out.printf("Output of running %s is:", Arrays.toString(args));

			while ((line = br.readLine()) != null) {
				System.out.println(line);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (pdfFile.exists()) {
			htmlFile.delete();
			pdfFile.renameTo(def);
		}

		return id;
	}

	/**
	 * Inserts a watermark into a document.
	 *
	 * @param doc
	 *            The input document.
	 * @param watermarkText
	 *            Text of the watermark.
	 */
	public void insertWatermarkText(com.aspose.words.Document doc, String watermarkText) throws Exception {
		// Create a watermark shape. This will be a WordArt shape.
		// You are free to try other shape types as watermarks.
		Shape watermark = new Shape(doc, ShapeType.TEXT_PLAIN_TEXT);

		// Set up the text of the watermark.
		watermark.getTextPath().setText(watermarkText);
		watermark.getTextPath().setFontFamily("Arial");
		watermark.setWidth(500);
		watermark.setHeight(100);
		// Text will be directed from the bottom-left to the top-right corner.
		watermark.setRotation(-40);
		// Remove the following two lines if you need a solid black text.
		watermark.getFill().setColor(Color.LIGHT_GRAY); // Try LightGray to get
														// more Word-style
														// watermark
		watermark.setStrokeColor(Color.LIGHT_GRAY); // Try LightGray to get more
													// Word-style watermark

		// Place the watermark in the page center.
		watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
		watermark.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
		watermark.setWrapType(WrapType.NONE);
		watermark.setVerticalAlignment(VerticalAlignment.CENTER);
		watermark.setHorizontalAlignment(HorizontalAlignment.CENTER);

		/*
		 * 2017-06-20 jgarcia@controltechcg.com Issue #3 (SICDI-Controltech)
		 * hotfix-3.
		 */
		// Create a new paragraph and append the watermark to this paragraph.
		// Paragraph watermarkPara = new Paragraph(doc);
		// watermarkPara.appendChild(watermark);

		// Insert the watermark into all headers of each document section.
		for (Section sect : doc.getSections()) {
			// There could be up to three different headers in each section,
			// since we want
			/*
			 * 2017-06-20 jgarcia@controltechcg.com Issue #3 (SICDI-Controltech)
			 * hotfix-3.
			 */
			insertWatermarkIntoHeader(watermark, sect, HeaderFooterType.HEADER_PRIMARY);
			insertWatermarkIntoHeader(watermark, sect, HeaderFooterType.HEADER_FIRST);
			insertWatermarkIntoHeader(watermark, sect, HeaderFooterType.HEADER_EVEN);
		}
	}

	/**
	 * Inserta la marca de agua dentro del header del documento.
	 * 
	 * @param watermark
	 * @param sect
	 * @param headerType
	 * @throws Exception
	 */
	/*
	 * 2017-06-20 jgarcia@controltechcg.com Issue #3 (SICDI-Controltech)
	 * hotfix-3. Modificación de la adición de la marca de agua con respecto al
	 * encabezado del documento. Tomado de:
	 * https://www.aspose.com/community/forums/permalink/832037/832037/
	 * showthread.aspx
	 */
	private static void insertWatermarkIntoHeader(Shape watermark, Section sect, int headerType) throws Exception {
		HeaderFooter header = sect.getHeadersFooters().getByHeaderFooterType(headerType);

		if (header != null) {
			Paragraph watermarkPara = header.getFirstParagraph();
			watermarkPara.appendChild(watermark.deepClone(true));
		}
	}
}
