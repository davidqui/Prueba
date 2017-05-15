package com.laamware.ejercito.doc.web.docx4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.NullArgumentException;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.finders.RangeFinder;
import org.docx4j.jaxb.Context;
import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Br;
import org.docx4j.wml.CTBookmark;
import org.docx4j.wml.CTMarkupRange;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.SdtBlock;
import org.docx4j.wml.SdtPr;
import org.docx4j.wml.SdtRun;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

/**
 * 
 * @author Rafael Blanco
 *
 */
public class ReemplazarBookmarksConTexto {

	private static ReemplazarBookmarksConTexto reemplazarBookmarksConTexto;

	private static final boolean DELETE_BOOKMARK = true;

	private static final org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();

	private boolean startAgain;

	/**
	 * Metodo que devuelve una única instancia de la clase
	 * ReemplazarBookmarksConTexto
	 *
	 * @see ReemplazarBookmarksConTexto
	 * @return
	 */
	public static ReemplazarBookmarksConTexto getInstance() {

		if (reemplazarBookmarksConTexto == null) {
			reemplazarBookmarksConTexto = new ReemplazarBookmarksConTexto();
		}

		return reemplazarBookmarksConTexto;

	}

	private ReemplazarBookmarksConTexto() {

	}

	/**
	 * Metodo que procesa una plantilla para realizar reemplazos de los BookMark
	 * en archivos .docx
	 *
	 * @param bookMarkets
	 * @param listaTablas
	 * @param rutaPlantilla
	 * @param rutaSalidaArchivoFinal
	 * @throws java.lang.Exception
	 */
	public void procesar(Map<String, Object> bookMarkets,
			List<com.laamware.ejercito.doc.web.docx4j.TablaDTO> listaTablas, File archivoPlantilla, File archivoSalida,
			List<com.laamware.ejercito.doc.web.docx4j.TablaDTO> listaVinetas) throws Exception {

		if (bookMarkets == null) {

			throw new NullArgumentException("[keyValues]");

		}

		if (!archivoPlantilla.exists()) {

			throw new FileNotFoundException("[" + archivoPlantilla.getAbsolutePath() + "] NO EXISTE ");
		}

		if (archivoSalida.exists()) {

			archivoSalida.delete();
			// throw new RuntimeException("[" + archivoSalida.getAbsolutePath()
			// + "] EXISTE ");
		}

		// WordprocessingMLPackage wordMLPackage =
		// WordprocessingMLPackage.load(archivoPlantilla);
		WordprocessingMLPackage wordMLPackage = new WordprocessingMLPackage();
		wordMLPackage = WordprocessingMLPackage.load(archivoPlantilla);
		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

		org.docx4j.wml.Document wmlDocumentEl = (org.docx4j.wml.Document) documentPart.getJaxbElement();
		// Body body = wmlDocumentEl.getBody();

		/*
		 * SectPr sectPr = wordMLPackage.getDocumentModel().getSections().get( 0
		 * ).getSectPr(); List<org.docx4j.wml.CTRel> hdrFtrRefs =
		 * sectPr.getEGHdrFtrReferences();
		 * 
		 * for (CTRel rel : hdrFtrRefs) {
		 * 
		 * }
		 */

		/*
		 * List<?> texts =
		 * getAllElementFromObject(wordMLPackage.getMainDocumentPart(),
		 * Text.class); for(Object textObj : texts){ Text text = (Text) textObj;
		 * if(text.getValue().equalsIgnoreCase("${MY_PLACE_HOLDER}")){ File file
		 * = new File("C:\\image.jpeg"); P paragraphWithImage =
		 * addInlineImageToParagraph(createInlineImage(file));
		 * tc.getContent().remove(0);
		 * 
		 * tc.getContent().add(paragraphWithImage); } }
		 */

		Iterator<Map.Entry<String, Object>> itera = bookMarkets.entrySet().iterator();
		while (itera.hasNext()) {

			Map.Entry<String, Object> kv = itera.next();
			if (kv.getValue() == null) {
				continue;
			}
			// map.put(new DataFieldName(kv.getKey()),
			// kv.getValue().toString());
			findWord(wordMLPackage, kv.getKey(), kv.getValue());

		}

		// replaceBookmarkContents(body.getContent(), map);

		if (listaTablas != null) {

			for (TablaDTO unaTabla : listaTablas) {

				List<Map<String, String>> listaMapa = new ArrayList<Map<String, String>>();
				for (int columna = 0; columna < unaTabla.getListaFilasTabla().size(); columna++) {

					Map<String, String> mapaFila = new HashMap<String, String>();
					for (int cabecera = 0; cabecera < unaTabla.getListaCabeceraTabla().size(); cabecera++) {

						mapaFila.put(unaTabla.getListaCabeceraTabla().get(cabecera),
								unaTabla.getListaFilasTabla().get(columna).get(cabecera));

					}
					listaMapa.add(mapaFila);
				}
				replaceTable(unaTabla.getListaCabeceraTabla().get(0), listaMapa, wordMLPackage);
			}
		}

		if (listaVinetas != null) {
			for (TablaDTO unaTabla : listaVinetas) {
				List<Map<String, String>> listaMapa = new ArrayList<Map<String, String>>();
				for (int columna = 0; columna < unaTabla.getListaFilasTabla().size(); columna++) {
					Map<String, String> mapaFila = new HashMap<String, String>();
					for (int cabecera = 0; cabecera < unaTabla.getListaCabeceraTabla().size(); cabecera++) {
						mapaFila.put(unaTabla.getListaCabeceraTabla().get(cabecera),
								unaTabla.getListaFilasTabla().get(columna).get(cabecera));
					}
					listaMapa.add(mapaFila);
				}
				replaceVineta(unaTabla.getListaCabeceraTabla().get(0), listaMapa, wordMLPackage);

			}
		}

		// GUARDAMOS EL DOCXS
		File fileCNT = new File(archivoSalida.getAbsolutePath() + ".odt.cnt");
		FileUtils.write(fileCNT, "application/vnd.oasis.opendocument.text");
		// FileUtils.write(fileCNT, "application/pdf");

		File fDocx = new File(archivoSalida.getAbsolutePath() + ".docx");
		wordMLPackage.save(fDocx);
		System.out.println(fDocx);

		/*
		 * Docx4J.toHTML( wordMLPackage,
		 * "C:\\Users\\rafar\\Downloads\\Nueva carpeta (40)\\imageDirPath",
		 * "C:\\Users\\rafar\\Downloads\\Nueva carpeta (40)\\imageTargetUri",
		 * new FileOutputStream( new File( archivoSalida.getAbsolutePath() +
		 * ".html" )));
		 */
		/*
		 * try { convertOfficeToPDF( fDocx,
		 * "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
		 * ); } catch (Exception e) { Docx4J.toPDF(wordMLPackage, new
		 * FileOutputStream( archivoSalida )); }
		 */
		OfficeManager officeManager1 = new DefaultOfficeManagerConfiguration()
				.setOfficeHome("C:\\Program Files (x86)\\LibreOffice 5\\").buildOfficeManager();

		if (!officeManager1.isRunning()) {
			officeManager1.start();
		}

		File fODT = new File(archivoSalida.getAbsolutePath() + ".odt");

		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager1);
		converter.convert(fDocx, fODT);

		officeManager1.stop();

		/*
		 * File fPDT = new File(
		 * "C:\\Users\\rafar\\Downloads\\Nueva carpeta (40)\\Formato_Oficio_FINAl.pdf"
		 * );
		 * 
		 * OdfTextDocument document = OdfTextDocument.loadDocument( new
		 * FileInputStream(fODT)); OutputStream out = new FileOutputStream( fPDT
		 * );
		 * 
		 * PdfOptions options = PdfOptions.create();
		 * 
		 * PdfConverter.getInstance().convert( document, out, options );
		 */

		/*
		 * XHTMLOptions options2 = XHTMLOptions.create().URIResolver(new
		 * FileURIResolver(new File("word/media")));
		 * 
		 * OutputStream out2 = new ByteArrayOutputStream();
		 * 
		 * XHTMLConverter.getInstance().convert(document, out, options2); String
		 * html=out.toString(); System.out.println(html);
		 */

	}

	private void replaceVineta(String placeholderFirt, List<Map<String, String>> textToAdd,
			WordprocessingMLPackage template) throws Docx4JException, JAXBException {
		List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), P.class);
		// 1. BUSCAMOS LA TABLA
		P tempTable = getTemplateVinetas(tables, placeholderFirt);
		List<Object> rows = getAllElementFromObject(tempTable, R.class);
		if (rows.size() == 1) {
			R templateRow = (R) rows.get(0);
			for (Map<String, String> replacements : textToAdd) {
				addRowToVineta(tempTable, templateRow, replacements);
			}
			tempTable.getContent().remove(templateRow);
		}
	}

	private static void addRowToVineta(P spc, R templateRow, Map<String, String> replacements) {
		R workingRow = (R) XmlUtils.deepCopy(templateRow);
		List<?> textElements = getAllElementFromObject(workingRow, Text.class);
		for (Object object : textElements) {
			Text text = (Text) object;
			String replacementValue = (String) replacements.get(text.getValue());
			if (replacementValue != null) {
				text.setValue(replacementValue);
				Br br = factory.createBr();
				workingRow.getContent().add(br);
			}
			spc.getContent().add(workingRow);
		}
	}

	private P getTemplateVinetas(List<Object> tables, String templateKey) throws Docx4JException, JAXBException {
		for (Object tbl : tables) {
			List<?> textElements = getAllElementFromObject(tbl, Text.class);
			for (Object text : textElements) {
				Text textElement = (Text) text;
				if (textElement.getValue() != null && textElement.getValue().equals(templateKey))
					return (P) tbl;
			}
		}
		return null;
	}

	/**
	 *
	 * @param paragraphs
	 * @param data
	 * @throws Exception
	 */
	private void replaceBookmarkContents(List<Object> paragraphs, Map<DataFieldName, String> data) throws Exception {

		try {

			RangeFinder rt = new RangeFinder("CTBookmark", "CTMarkupRange");
			new TraversalUtil(paragraphs, rt);

			for (CTBookmark bookMarket : rt.getStarts()) {

				// SI EL NOMBRE DEL BOOKMARKET ES NULO
				// CONTINUAMOS CON EL SIGUIENTE REGISTRO
				if (bookMarket.getName() == null) {
					continue;
				}

				String valor = data.get(new DataFieldName(bookMarket.getName()));
				// SI EL VALOR ES NULO, CONTINUAMOS CON EL SIGUIENTE REGISTRO
				if (valor == null) {
					continue;
				}

				List<Object> listaObjetosReemplazar;

				if (bookMarket.getParent() instanceof P) {
					// CARGAR LISTA
					listaObjetosReemplazar = ((ContentAccessor) (bookMarket.getParent())).getContent();
				} else {
					// CONTINUAMOS CON EL SIGUIENTE REGISTRO
					continue;
				}

				int rangoInicio = -1;
				int rangoFin = -1;
				int i = 0;

				for (Object objetoReemplazo : listaObjetosReemplazar) {

					Object listEntry = XmlUtils.unwrap(objetoReemplazo);
					if (listEntry.equals(bookMarket)) {
						if (DELETE_BOOKMARK) {
							rangoInicio = i;
						} else {
							rangoInicio = i + 1;
						}
					} else if (listEntry instanceof CTMarkupRange) {

						if (((CTMarkupRange) listEntry).getId().equals(bookMarket.getId())) {
							if (DELETE_BOOKMARK) {
								rangoFin = i;
							} else {
								rangoFin = i - 1;
							}
							break;
						}
					}
					i++;
				}

				if (rangoInicio >= 0 && rangoFin > rangoInicio) {

					// BORRAMOS LOS BOOKBARKET
					for (int j = rangoFin; j >= rangoInicio; j--) {
						listaObjetosReemplazar.remove(j);
					}

					// ADICIONAMOS EL VALOR SOBRE EL BOOK QUE ES CARGADO POR
					// MEDIO DE LA LISTA
					org.docx4j.wml.R run = factory.createR();
					org.docx4j.wml.Text t = factory.createText();
					/*
					 * Br nl = factory.createBr();
					 * if(bookMarket.getName().startsWith("lista_aspectos")){
					 * String[] datos = valor.split("\\n"); for(String dato:
					 * datos){ run.getContent().add(nl);
					 * 
					 * run.getContent().add(t); t.setValue(dato); } }else{
					 * 
					 * }
					 */
					run.getContent().add(t);
					t.setValue(valor);
					listaObjetosReemplazar.add(rangoInicio, run);
				}
			}

		} catch (Exception e) {

			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "ERROR", e);
			throw e;

		}
	}

	/**
	 * 
	 * @param placeholders
	 * @param textToAdd
	 * @param template
	 * @throws Docx4JException
	 * @throws JAXBException
	 */
	private void replaceTable(String placeholderFirt, List<Map<String, String>> textToAdd,
			WordprocessingMLPackage template) throws Docx4JException, JAXBException {

		List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);

		// 1. BUSCAMOS LA TABLA
		Tbl tempTable = getTemplateTable(tables, placeholderFirt);
		List<Object> rows = getAllElementFromObject(tempTable, Tr.class);

		// PRIMERA FILA ES HEADER Y DESDE LA SEGUNDA FILA ES CONTENIDO
		if (rows.size() == 2) {

			// LA PRIMERA FILA, ES LA FILA TEMPLATE
			Tr templateRow = (Tr) rows.get(1);

			for (Map<String, String> replacements : textToAdd) {
				// ADICIONAMOS LAS FILAS NECESARIAS DE ACUERDO AL TEMPLATE
				addRowToTable(tempTable, templateRow, replacements);
			}

			// 4. ELIMINAMOS LA FILA TEMPLATE
			tempTable.getContent().remove(templateRow);
		}
	}

	/**
	 * 
	 * @param reviewtable
	 * @param templateRow
	 * @param replacements
	 */
	private static void addRowToTable(Tbl reviewtable, Tr templateRow, Map<String, String> replacements) {
		Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
		List<?> textElements = getAllElementFromObject(workingRow, Text.class);
		for (Object object : textElements) {
			Text text = (Text) object;
			String replacementValue = (String) replacements.get(text.getValue());
			if (replacementValue != null)
				text.setValue(replacementValue);
		}

		reviewtable.getContent().add(workingRow);
	}

	/**
	 * 
	 * @param tables
	 * @param templateKey
	 * @return
	 * @throws Docx4JException
	 * @throws JAXBException
	 */
	private Tbl getTemplateTable(List<Object> tables, String templateKey) throws Docx4JException, JAXBException {
		for (Object tbl : tables) {
			List<?> textElements = getAllElementFromObject(tbl, Text.class);
			for (Object text : textElements) {
				Text textElement = (Text) text;
				if (textElement.getValue() != null && textElement.getValue().equals(templateKey))
					return (Tbl) tbl;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param obj
	 * @param toSearch
	 * @return
	 */
	private static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
		List<Object> result = new ArrayList<Object>();
		if (obj instanceof JAXBElement)
			obj = ((JAXBElement<?>) obj).getValue();

		if (obj != null && obj.getClass().equals(toSearch))
			result.add(obj);
		else if (obj instanceof ContentAccessor) {
			List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child : children) {
				result.addAll(getAllElementFromObject(child, toSearch));
			}

		}
		return result;
	}

	/**
	 * 
	 * @param doc
	 * @param toFind
	 * @return
	 */
	private int findWord(WordprocessingMLPackage doc, String key, Object value) throws Exception {

		HashMap<ContentAccessor, List<Text>> caMap = new HashMap<ContentAccessor, List<Text>>();

		List<Object> bodyChildren = doc.getMainDocumentPart().getContent();

		for (Object child : bodyChildren) {
			if (child instanceof JAXBElement)
				child = ((JAXBElement<?>) child).getValue();

			if (child instanceof SdtBlock) {
				SdtBlock stdBlock = (SdtBlock) child;
				if (!checkIfInclude(stdBlock.getSdtPr())) {
					do {
						startAgain = false;
						for (Object o : stdBlock.getSdtContent().getContent()) {

							if (o instanceof JAXBElement)
								o = ((JAXBElement<?>) o).getValue();
							if (o instanceof SdtBlock) {
								stdBlock = (SdtBlock) o;
								startAgain = true;
								break;
							} else if (o instanceof ContentAccessor) {

								ContentAccessor caElement = (ContentAccessor) o;
								if (o instanceof P) {
									caMap.put(caElement, getAllTextfromContenAccessor(caElement, caMap));
								} else {
									getAllTextfromContenAccessor(caElement, caMap);
								}
							}
						}
					} while (startAgain);
				}
			} else if (child instanceof ContentAccessor) {

				ContentAccessor caElement = (ContentAccessor) child;
				if (child instanceof P) {
					caMap.put(caElement, getAllTextfromContenAccessor(caElement, caMap));
				} else {

					getAllTextfromContenAccessor(caElement, caMap);
				}
			}
		}

		// i've the map paragraph -- textList

		int wordOcc = 0;
		for (ContentAccessor ca : caMap.keySet()) {
			if (!caMap.get(ca).isEmpty()) {
				// StringBuilder builder = new StringBuilder();
				for (Text text : caMap.get(ca)) {
					if (text.getValue() != null && text.getValue().equalsIgnoreCase(key)) {
						if (key.startsWith("${_img_")) {

							((P) ca).getContent().remove(0);
							if (value instanceof Byte) {
								R paragraphWithImage = addInlineImageToParagraph(
										createInlineImage((byte[]) value, doc));
								((P) ca).getContent().add(paragraphWithImage);
							} else {
								text.setValue("");
							}

						} else {
							text.setValue(value.toString());
						}
					}
					// builder.append(text.getValue());
				}

				// int cantidaLocal = numOfOccourences(builder, key);
				// wordOcc += cantidaLocal;
			}
		}

		return wordOcc;
	}

	private String[][] tasks = { { "^t", "\t" }, { "^=", "\u2013" }, { "^+", "\u2014" }, { "^s", "\u00A0" },
			{ "^?", "." }, { "^#", "\\d" }, { "^$", "\\p{L}" } };

	private R addInlineImageToParagraph(Inline inline) {
		// Now add the in-line image to a paragraph
		// org.docx4j.wml.ObjectFactory factory = new
		// org.docx4j.wml.ObjectFactory();
		// P paragraph = factory.createP();
		R run = factory.createR();
		// paragraph.getContent().add(run);
		Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);
		return run;
	}

	/**
	 * 
	 * @param file
	 * @param wordMLPackage
	 * @return
	 * @throws Exception
	 */
	private Inline createInlineImage(byte[] bytes, WordprocessingMLPackage wordMLPackage) throws Exception {

		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

		int docPrId = 1;
		int cNvPrId = 2;

		return imagePart.createImageInline("Filename hint", "Alternative text", docPrId, cNvPrId, false);
	}

	private int numOfOccourences(StringBuilder builder, String toFind) {

		for (String[] replacement : tasks)
			toFind = toFind.replace(replacement[0], replacement[1]);

		Pattern p = Pattern.compile(toFind, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(builder.toString());

		int count = 0;
		while (m.find()) {
			count += 1;
		}
		return count;
	}

	/*
	 * check if it is a include object
	 * 
	 */
	private boolean checkIfInclude(SdtPr sdtPr) {
		for (Object child : sdtPr.getRPrOrAliasOrLock()) {
			if (child instanceof JAXBElement)
				child = ((JAXBElement<?>) child).getValue();

			if (child instanceof SdtPr.Alias) {
				SdtPr.Alias alias = (SdtPr.Alias) child;
				if (alias.getVal().contains(("Include :"))) {
					return true;
				} else
					return false;
			}
		}
		return false;
	}

	private List<Text> getAllTextfromContenAccessor(ContentAccessor ca, HashMap<ContentAccessor, List<Text>> caMap) {

		List<Text> textList = new ArrayList<Text>();
		List<Object> children = ca.getContent();
		for (Object child : children) {
			if (child instanceof JAXBElement)
				child = ((JAXBElement<?>) child).getValue();
			if (child instanceof Text) {
				Text text = (Text) child;
				textList.add(text);
			} else if (child instanceof R) {

				R run = (R) child;
				for (Object o : run.getContent()) {

					if (o instanceof JAXBElement)
						o = ((JAXBElement<?>) o).getValue();

					if (o instanceof R.Tab) {
						Text text = new Text();
						text.setValue("\t");

						textList.add(text);
					}
					if (o instanceof R.SoftHyphen) {
						Text text = new Text();
						text.setValue("\u00AD");
						textList.add(text);
					}

					if (o instanceof Text) {
						textList.add((Text) o);
					}
				}
			} else if (child instanceof ContentAccessor) {
				ContentAccessor caElement = (ContentAccessor) child;
				if (child instanceof P) {
					caMap.put(caElement, getAllTextfromContenAccessor(caElement, caMap));
				} else {
					getAllTextfromContenAccessor(caElement, caMap);
				}
			} else if (child instanceof SdtRun) {
				SdtRun sdtRun = (SdtRun) child;
				getAllTextFromSdtRun(sdtRun, textList, caMap);
			}
		}
		return textList;
	}

	public List<Text> getAllTextFromSdtRun(SdtRun sdtRun, List<Text> textList,
			HashMap<ContentAccessor, List<Text>> caMap) {

		if (!checkIfInclude(sdtRun.getSdtPr())) {
			for (Object o : sdtRun.getSdtContent().getContent()) {

				if (o instanceof JAXBElement)
					o = ((JAXBElement<?>) o).getValue();

				if (o instanceof R) {

					R run = (R) o;
					for (Object ob : run.getContent()) {
						if (ob instanceof JAXBElement)
							ob = ((JAXBElement<?>) ob).getValue();

						if (o instanceof R.Tab) {
							Text text = new Text();
							text.setValue("\t");
							textList.add(text);
						}
						if (o instanceof R.SoftHyphen) {
							Text text = new Text();
							text.setValue("\u00AD");
							textList.add(text);
						}
						if (ob instanceof Text) {
							textList.add((Text) ob);
						}
					}
				} else if (o instanceof ContentAccessor) {

					ContentAccessor caElement = (ContentAccessor) o;
					if (o instanceof P) {
						caMap.put(caElement, getAllTextfromContenAccessor(caElement, caMap));
					} else {
						textList.addAll(getAllTextfromContenAccessor(caElement, caMap));
					}
				}
			}
		}
		return textList;
	}

	/**
	 * 
	 * @param file
	 * @param contentType
	 */
	private void convertOfficeToPDF(File file, String contentType) throws Exception {

		try {

			File tmp = new File(file.getAbsolutePath() + ".pdf");

			StringBuilder cmd = new StringBuilder("unoconv ");
			cmd.append("\"" + file.getAbsolutePath() + "\"");

			Process p = Runtime.getRuntime().exec(cmd.toString());
			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			StringBuilder result = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				result.append(line + "\n");
			}
			System.out.println(result.toString());

			file.delete();
			tmp.renameTo(file);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

}
