package com.laamware.ejercito.doc.web.docx4j;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import net.sourceforge.jbarcodebean.JBarcodeBean;
import net.sourceforge.jbarcodebean.model.Interleaved25;

public class Test
{
  public static void mainm(String[] args)
    throws Exception
  {
	  JBarcodeBean barcode = new JBarcodeBean();

      // nuestro tipo de codigo de barra
      barcode.setCodeType(new Interleaved25());
      //barcode.setCodeType(new Code39());

      // nuestro valor a codificar y algunas configuraciones mas
      barcode.setCode("1234554321987654321123456789");
      barcode.setCheckDigit(true);

      BufferedImage bufferedImage = barcode.draw(new BufferedImage(200, 50, BufferedImage.TYPE_INT_RGB));

      // guardar en disco como png
      File fi = new File("C:\\Users\\rafar\\Downloads\\Nueva carpeta (40)\\codebar.png");
      ImageIO.write(bufferedImage, "png", fi);
      
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	  	ImageIO.write( bufferedImage, "jpg", baos );
	  	baos.flush();
	  	byte[] imageInByteBarCode = baos.toByteArray();
	  	baos.close();
	  	
	  
	  Map<String, Object> map = new HashMap<String, Object>();
      map.put("${_acceso_}", "RESTRINGIDO");
      map.put("${_desc_comando_caimi}", "COMANDO DE APOYO DE COMBATE DE INTELIGENCIA MILITAR");
      map.put("${_codigo_barra_}", "2016-2016-156-1-2");
      map.put("${_txt_radiograna_}", "RADIOGRAMA");
      map.put("${_txt_oficio_}", "OFICIO");
      map.put("${_de_sigla_}", "CAIMI");
      map.put("${_para_sigla_}", "CACIM");
      map.put("${_radicado_}", "2016-530");
      map.put("${_depende_siglas_}", "/MDN-CGFM-COEJC-SECEJ-JEMOP-CAIMI-C-6-18.1");
      map.put("${_u_elabora_grado_}", "GRADO_ELABORA");
      map.put("${_u_elabora_nombre_}", "NOMBRE_ELABORA");
      map.put("${_u_elabora_cargo_}", "CARGO_ELABORA");
      map.put("${_u_firma_grado_}", "GRADO_FIRMA");
      map.put("${_u_firma_nombre_}", "NOMBRE_FIRMA");
      map.put("${_u_firma_cargo_}", "CARGO_FIRMA");
      map.put("${_u_revisa_grado_}", "GRADO_REVISA");
      map.put("${_u_revisa_nombre_}", "NOMBRE_REVISA");
      map.put("${_u_revisa_cargo_}", "CARGO_REVISA");
      File file = new File("C:\\Users\\rafar\\OneDrive\\Imágenes\\firma_ejemplo.png");
      map.put("${_img_firma_}", FileUtils.readFileToByteArray(file));
      map.put("${_img_c_barra_}", imageInByteBarCode);
      
      ReemplazarBookmarksConTexto rbct = ReemplazarBookmarksConTexto.getInstance();
      //soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard
      
      /*List<TablaDTO> listaDeTablas = new ArrayList<TablaDTO>();        
      
      List<String> listaCabeceraTabla1 = new ArrayList<String>();
      listaCabeceraTabla1.add("_fila_columna_1_");
      listaCabeceraTabla1.add("_fila_columna_2_");
      listaCabeceraTabla1.add("_fila_columna_3_");
      
      List<List<String>> listaFilasTabla1 = new ArrayList<List<String>>();
      listaFilasTabla1.add( Arrays.asList("Valor f1 c1","Valor f1 c2","Valor f1 c3") );        
      listaFilasTabla1.add( Arrays.asList("1232312","Valor f1 c2","Valor f1 c3") );        
      listaFilasTabla1.add( Arrays.asList("555555 f1 c1","Valor f1 c2","V565656565") );        
      
                      
      listaDeTablas.add( new TablaDTO(listaCabeceraTabla1, listaFilasTabla1)); //TABLA 1       
      
      
      List<String> listaCabeceraTabla2 = new ArrayList<String>();
      listaCabeceraTabla2.add("_fila_columna_11_");
      listaCabeceraTabla2.add("_fila_columna_22_");        
      List<List<String>> listaFilasTabla2 = new ArrayList<List<String>>();
      listaFilasTabla2.add( Arrays.asList("Valor f1 c1","Valor f1 c2") );
      listaFilasTabla2.add( Arrays.asList("Valor f2 c1","Valor f2 c2") );
      listaFilasTabla2.add( Arrays.asList("Valor f3 c1","Valor f3 c2") );
      
      listaDeTablas.add( new TablaDTO(listaCabeceraTabla2, listaFilasTabla2));// TABLA 2
      
      
      List<String> listaCabeceraTabla3 = new ArrayList<String>();
      listaCabeceraTabla3.add("_fila_columna_111_");
      listaCabeceraTabla3.add("_fila_columna_222_");
      listaCabeceraTabla3.add("_fila_columna_333_");
      listaCabeceraTabla3.add("_fila_columna_444_");
      List<List<String>> listaFilasTabla3 = new ArrayList<List<String>>();
      listaFilasTabla3.add( Arrays.asList("Rafael 1","Valor f1 c2","Valor f1 c3","Valor 4") );
      listaFilasTabla3.add( Arrays.asList("Rafael 2","Valor f2 c2","Valor f2 c3","Valor 4") );
      listaFilasTabla3.add( Arrays.asList("Rafael 3","Valor f3 c2","Valor f3 c3","Valor 4") );
      listaFilasTabla3.add( Arrays.asList("Rafael 1","Valor f1 c2","Valor f1 c3","Valor 4") );
      listaFilasTabla3.add( Arrays.asList("Rafael 2","Valor f2 c2","Valor f2 c3","Valor 4") );
      listaFilasTabla3.add( Arrays.asList("Rafael 3","Valor f3 c2","Valor f3 c3","Valor 4") );
              
      listaDeTablas.add( new TablaDTO(listaCabeceraTabla3, listaFilasTabla3));// TABLA3 */
      
    
      rbct.procesar(map,
    		  null, 
    		  new File("C:\\Users\\rafar\\Downloads\\Formato_Radiograma (2).docx"), 
    		  new File("C:\\Users\\rafar\\Downloads\\Nueva carpeta (40)\\Formato_Radiograma (2)"), 
    		  null);
  }
}

