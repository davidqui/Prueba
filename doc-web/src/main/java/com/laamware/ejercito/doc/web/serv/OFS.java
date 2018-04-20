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
import java.io.FilenameFilter;
import java.util.Objects;

@Service
public class OFS {

    public static final int DIR_LEVELS = 4;

    public static final long MAX_TIME = 7258136400000L;

    private static final String TYPE_APPLICATION_PDF = "application/pdf";

    @Autowired
    OFSStageRepository digiR;

    /**
     * Home de la aplicación convert.
     */
    /*
     * 2017-09-14 jgarcia@controltechcg.com hotfix-windows:
     * Home de la aplicación convert para que pueda ser ejecutado en 
     * sistemas Windows.
     */
    @Value("${co.mil.imi.sicdi.ofs.convert.home}")
    private String convertHome;

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

        Process process;
        try {
            File tmp = new File(file.getAbsolutePath() + ".tmp");

            /*
             * 2017-09-14 jgarcia@controltechcg.com hotfix-windows:
             * Home de la aplicación convert para que pueda ser ejecutado en 
             * sistemas Windows.
             */
            StringBuilder cmd = new StringBuilder(Objects.toString(convertHome, ""))
                    .append("convert ");
            cmd.append(file.getAbsolutePath()).append(" ");
            cmd.append("pdf:").append(tmp);

            process = Runtime.getRuntime().exec(cmd.toString());
            int exitValue = process.waitFor();

            final String cmdExit = getCommandExit(process, exitValue);
            System.out.println(cmdExit);

            file.delete();
            tmp.renameTo(file);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Presenta la salida de la aplicación, según el valor de salida de la
     * ejecución del comando.
     *
     * @param process Proceso ejecutado.
     * @param exitValue Valos de salida de ejecución.
     * @return Salida de la aplciación.
     * @throws IOException
     */
    /*
     * 2017-09-14 jgarcia@controltechcg.com hotfix-windows:
     * Home de la aplicación convert para que pueda ser ejecutado en 
     * sistemas Windows.
     */
    private String getCommandExit(final Process process, final int exitValue) throws IOException {
        final InputStream inputStream;
        if (exitValue == 0) {
            inputStream = process.getInputStream();
        } else {
            inputStream = process.getErrorStream();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            return result.toString();
        }
    }

    private void makeThumbnail(String path) {
        String tmb = path + ".tmb";
        Process p;
        try {
            /*
             * 2017-09-14 jgarcia@controltechcg.com hotfix-windows:
             * Home de la aplicación convert para que pueda ser ejecutado en 
             * sistemas Windows.
             */
            StringBuilder cmd = new StringBuilder(Objects.toString(convertHome, ""))
                    .append("convert -auto-orient -thumbnail 250x250 -unsharp 0x.5 ");
            cmd.append(path).append("[0] ");
            cmd.append("gif:").append(tmb);
            System.err.println("EJECUTANDO CMD= " + cmd.toString());
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

        byte[] bytes = FileUtils.readFileToByteArray(file);

        OFSEntry entry = new OFSEntry(TYPE_APPLICATION_PDF, bytes);

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
        if (time < 0) {
            time *= -1;
        }
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

    public String getPathDirectory(String id) {
        StringBuilder b = new StringBuilder(root);
        for (int i = 0; i < DIR_LEVELS; i++) {
            b.append("/").append(id.charAt(i));
        }
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
        /*
         * 2017-09-14 jgarcia@controltechcg.com hotfix-windows:
         * Home de la aplicación convert para que pueda ser ejecutado en sistemas 
         * Windows.
         */
        StringBuilder cmd = new StringBuilder(Objects.toString(convertHome, ""))
                .append("convert ");
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
            String[] args = new String[]{"wkhtmltopdf", "--encoding", "'utf-8'", "-T", "0mm", "-B", "0mm", "-L",
                "20mm", "-R", "20mm", "-s", "Letter", tmpHtml, tmpPdf};
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
     * @param doc The input document.
     * @param watermarkText Text of the watermark.
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

    /**
     * Copia un archivo OFS (Datos y tipo de contenido) en otra ruta y destino,
     * según el UUID indicado para el resultado.
     *
     * @param originUUID UUID de los archivos de origen.
     * @param resultUUID UUID de los archivos resultado de la copia.
     * @throws OFSException En caso que se presente alguna violación a las
     * reglas de negocio, o algún problema de E/S.
     */
    /*
     * 2018-04-17 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156: Método creado para el proceso de clonación de documentos
     * para el proceso de envío multidestino.
     */
    void copy(final String originUUID, final String resultUUID) throws OFSException {
        final String originPath = getPathDirectory(originUUID);
        final File originDirectory = new File(originPath);
        if (!originDirectory.exists()) {
            throw new OFSException("Directorio origen no existe en OFS: " + originPath);
        }

        final File[] originFiles = originDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(originUUID);
            }
        });

        if (originFiles == null || originFiles.length == 0) {
            throw new OFSException("Archivos no existe en OFS: " + originUUID);
        }

        final String resultPath = getPathDirectory(resultUUID);
        final File resultDirectory = new File(resultPath);

        if (!resultDirectory.exists()) {
            try {
                FileUtils.forceMkdir(resultDirectory);
            } catch (IOException ex) {
                throw new OFSException("Error al crear directorio: " + resultPath, ex);
            }
        }

        for (final File originFile : originFiles) {
            try {
                final String originFilename = originFile.getName();
                final String resultFilename = originFilename.replaceAll(originUUID, resultUUID);

                final File resultFile = new File(resultPath + File.separator + resultFilename);
                FileUtils.copyFile(originFile, resultFile);
                resultFile.setLastModified(getRandomTime());
            } catch (IOException ex) {
                throw new OFSException("Error clonando archivo: " + originUUID + "-" + resultUUID, ex);
            }
        }
    }

    /**
     * Elimina un archivo OFS (Datos y tipo de contenido),según el UUID indicado
     * para el resultado.
     *
     * @param deleteUUID UUID de los archivos a borrar.
     * @throws OFSException En caso que se presente alguna violación a las
     * reglas de negocio, o algún problema de E/S.
     */
    /*
     * 2018-04-19 edison.gonzalez@controltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156: Método creado para el proceso de clonación de documentos
     * para el proceso de envío multidestino.
     */
    void delete(final String deleteUUID) throws OFSException {
        final String deletePath = getPathDirectory(deleteUUID);
        final File deleteDirectory = new File(deletePath);
        if (!deleteDirectory.exists()) {
            throw new OFSException("Directorio de archivo a borrar no existe en OFS: " + deletePath);
        }

        final File[] deleteFiles = deleteDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(deleteUUID);
            }
        });

        if (deleteFiles == null || deleteFiles.length == 0) {
            throw new OFSException("Archivos no existe en OFS: " + deleteUUID);
        }

        for (final File deleteFile : deleteFiles) {
            FileUtils.deleteQuietly(deleteFile);
        }
    }
}
