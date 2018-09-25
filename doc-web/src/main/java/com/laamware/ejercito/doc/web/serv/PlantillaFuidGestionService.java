package com.laamware.ejercito.doc.web.serv;

import com.aspose.words.Cell;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.ParagraphAlignment;
import com.aspose.words.Row;
import com.aspose.words.Run;
import com.aspose.words.Table;
import com.laamware.ejercito.doc.web.dto.KeysValuesAsposeDocxDTO;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.ExpDocumento;
import com.laamware.ejercito.doc.web.entity.PlantillaFuidGestion;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivoDetalle;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.PlantillaFuidGestionRepository;
import com.laamware.ejercito.doc.web.repo.TransferenciaArchivoDetalleRepository;
import com.laamware.ejercito.doc.web.util.Global;
import com.laamware.ejercito.doc.web.util.NumeroVersionComparator;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio para plantilla de FUID.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since Sep 11, 2018
 * @version 1.0.0 (feature-gogs-4).
 */
@Service
public class PlantillaFuidGestionService {

    private static final Logger LOG = Logger.getLogger(PlantillaFuidGestionService.class.getName());

    /**
     * Repositorio de plantillas.
     */
    @Autowired
    private PlantillaFuidGestionRepository plantillaRepository;

    @Autowired
    private DependenciaRepository dependenciaRepository;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private ExpDocumentoService expDocumentoService;

    @Autowired
    private TransferenciaArchivoDetalleRepository detalleRepository;

    @Autowired
    private TransferenciaArchivoService transferenciaArchivoService;

    /**
     * Servicio de OFS.
     */
    @Autowired
    private OFS ofs;

    /**
     * Obtiene la plantilla activa en el sistema.
     *
     * @return Plantilla activa.
     */
    public PlantillaFuidGestion findPlantillaActiva() {
        return plantillaRepository.findByActivoTrue();
    }

    /**
     * Registra un archivo como nueva plantilla activa de Fuid. En caso que
     * exista una plantilla activa anterior, la coloca como inactiva.
     *
     * @param file Archivo multipart cargado por el usuario.
     * @param usuario Usuario.
     * @return Nuevo registro de plantilla de transferencia de archivo.
     * @throws IOException En caso que exista un error durante el almacenamiento
     * en el OFS.
     */
    @Transactional
    public PlantillaFuidGestion registrarNuevaPlantilla(final MultipartFile file,
            final Usuario usuario) throws IOException {
        final PlantillaFuidGestion actualPlantilla = findPlantillaActiva();
        final String codigoOFS = ofs.saveAsIs(file.getBytes(), file.getContentType());

        if (actualPlantilla != null) {
            actualPlantilla.setActivo(false);
            plantillaRepository.saveAndFlush(actualPlantilla);
        }

        final Date ahora = new Date(System.currentTimeMillis());
        final String firmaMD5 = DigestUtils.md5Hex(file.getInputStream());

        final PlantillaFuidGestion nuevaPlantilla = new PlantillaFuidGestion(true, file.getOriginalFilename(), (int) file.getSize(), firmaMD5, codigoOFS, usuario, ahora);
        plantillaRepository.saveAndFlush(nuevaPlantilla);

        return nuevaPlantilla;
    }

    public void crearDocumentoFuid(TransferenciaArchivo transferenciaArchivo,List<TransferenciaArchivoDetalle> detalles) throws Exception {

        if (detalles.size() > 0) {
            final PlantillaFuidGestion plantilla = plantillaRepository.findByActivoTrue();
            final String plantillaPath = ofs.getPath(plantilla.getCodigoOfs());
            final Document asposeDocument = new Document(plantillaPath);

            final Table table = (Table) asposeDocument.getChild(NodeType.TABLE, 2, true);

            if (table != null) {
                if (!detalles.isEmpty()) {
                    int indice = 1;
                    for (TransferenciaArchivoDetalle detalle : detalles) {
                        final String[] cellValues = buildCellValuesDocumento(detalle, indice);
                        Row row = new Row(asposeDocument);
                        fillTableRow(asposeDocument, row, cellValues);
                        table.appendChild(row);
                        indice++;
                    }
                }
            }

            final License asposeLicense = new License();
            final Resource resource = new ClassPathResource("Aspose.Words.lic");
            asposeLicense.setLicense(resource.getInputStream());
            final KeysValuesAsposeDocxDTO asposeMap = crearMapaAspose(transferenciaArchivo);
            asposeDocument.getMailMerge().execute(asposeMap.getNombres(), asposeMap.getValues());

            final File tmpFile = File.createTempFile("_sigdi_temp_", ".pdf");
            asposeDocument.save(tmpFile.getPath());

            final OFSEntry ofsEntry = ofs.readPDFAspose(tmpFile);
            final String codigoActaOFS = ofs.save(ofsEntry.getContent(), AppConstants.MIME_TYPE_PDF);

            transferenciaArchivo.setFuid(codigoActaOFS);
            transferenciaArchivoService.actualizarTransferenciaArchivo(transferenciaArchivo);

            try {
                tmpFile.delete();
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, null, ex);
                try {
                    tmpFile.deleteOnExit();
                } catch (Exception ex1) {
                    LOG.log(Level.SEVERE, null, ex1);
                }
            }
        }
    }

    /**
     * Crea el mapa de campos/valor para el reemplazo de wildcards sobre la
     * plantilla para la generación del mapa a través de la API de Aspose.
     *
     * @param transferenciaArchivo Transferencia de archivo.
     * @return Mapa de campos/valor.
     */
    private KeysValuesAsposeDocxDTO crearMapaAspose(final TransferenciaArchivo transferenciaArchivo) {
        final SimpleDateFormat documentDateFormatter = new SimpleDateFormat(Global.DATE_FORMAT, Global.COLOMBIA);
        final KeysValuesAsposeDocxDTO map = new KeysValuesAsposeDocxDTO();

        Integer unidadIdPadre = dependenciaRepository.findUnidadID(transferenciaArchivo.getOrigenDependencia().getId());
        Dependencia unidadPadre = dependenciaRepository.findOne(unidadIdPadre);
        map.put("NOMBRE_UNIDAD_PADRE", unidadPadre.getNombre());

        if (transferenciaArchivo.getOrigenDependencia().getPadre() != null) {
            Dependencia dependenciaPadre = dependenciaRepository.findOne(transferenciaArchivo.getOrigenDependencia().getPadre());
            map.put("NOMBRE_DEPENDENCIA_PADRE_ORIGEN", dependenciaPadre.getNombre());
        } else {
            map.put("NOMBRE_DEPENDENCIA_PADRE_ORIGEN", transferenciaArchivo.getOrigenDependencia().getNombre());
        }

        map.put("NOMBRE_DEPENDENCIA_ORIGEN", transferenciaArchivo.getOrigenDependencia().getNombre());

        map.put("JUSTIFICACION", transferenciaArchivo.getJustificacion());

        Calendar cal = Calendar.getInstance();
        cal.setTime(transferenciaArchivo.getFechaCreacion());

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        map.put("ANIO_TRAN", year);
        map.put("MES_TRAN", month);
        map.put("DIA_TRAN", day);

        if (transferenciaArchivo.getDocId() != null) {
            map.put("NUM_TRAN", transferenciaArchivo.getDocId().getRadicado());
        }

        Integer numFoliosTotal = transferenciaArchivoService.obetenerNumeroFoliosPorTransferenciaArchivo(transferenciaArchivo);
        map.put("NUM_FOLIOS_TOTAL", numFoliosTotal);

        final String usuarioEmisor = transferenciaArchivo.getOrigenGrado().getId() + ". " + transferenciaArchivo.getOrigenUsuario().getNombre();
        map.put("NOMBRE_USUARIO_EMISOR", usuarioEmisor);
        map.put("CARGO_USUARIO_EMISOR", transferenciaArchivo.getUsuOrigenCargo().getCarNombre());
        map.put("DEPENDENCIA_ORIGEN_CIUDAD", transferenciaArchivo.getOrigenDependencia().getCiudad());

        final String usuarioDestinatario = transferenciaArchivo.getDestinoGrado().getId() + ". " + transferenciaArchivo.getDestinoUsuario().getNombre();
        map.put("NOMBRE_USUARIO_RECEPTOR", usuarioDestinatario);
        map.put("CARGO_USUARIO_RECEPTOR", transferenciaArchivo.getUsuDestinoCargo().getCarNombre());
        map.put("DEPENDENCIA_DESTINO_CIUDAD", transferenciaArchivo.getDestinoDependencia().getCiudad());

        final String fechaTransferencia = documentDateFormatter.format(transferenciaArchivo.getFechaCreacion());
        map.put("FECHA_TRANSFERENCIA", fechaTransferencia);

        final File creadorFirma = getImagenFirma(transferenciaArchivo.getOrigenUsuario());
        if (creadorFirma != null) {
            try {
                map.put("img_firma_emisor", FileUtils.readFileToByteArray(creadorFirma));
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "img_firma_emisor", ex);
            }
        }

        final File destinoFirma = getImagenFirma(transferenciaArchivo.getDestinoUsuario());
        if (destinoFirma != null) {
            try {
                map.put("img_firma_receptor", FileUtils.readFileToByteArray(destinoFirma));
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "img_firma_receptor", ex);
            }
        }
        return map;
    }

    /**
     * Construye los valores para las celdas de una fila de la tabla de FUID.
     *
     * @param detalle Registro de detalle de transferencia.
     * @return Arreglo de valores del detalle de transferencia.
     */
    private String[] buildCellValuesDocumento(TransferenciaArchivoDetalle detalle, int indice) {
        final SimpleDateFormat documentDateFormatter = new SimpleDateFormat(Global.DATE_FORMAT, Global.COLOMBIA);
        final DocumentoDependencia documentoDependencia = detalle.getDocumentoDependencia();
        final Documento documento = documentoDependencia.getDocumento();

        final String numOrden = String.valueOf(indice);
        final String codigoDependendia = documento.getElabora().getDependencia().getCodigo();
        final String codigoTrd = documento.getTrd().getCodigo();
        final String codigo = codigoDependendia + " - " + codigoTrd;
        final String asunto = documento.getAsunto();

        String fecRadicado = documentDateFormatter.format(documento.getDocFecRadicado());
        final String numCaja    = "-----------";
        final String numCarpeta = "-----------";
        final String tomo       = "-----------";
        final String otro = "NAS-IMI";

        final int numFolios = documentoService.obtenerNumeroFolios(documento);
        final String numeroFolios = String.valueOf(numFolios);

        final String soporte;
        if(documento.getInstancia().getProceso().getId().equals(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS)){
            soporte = "DIGITALIZADO";
        }else{
            soporte = "ELECTRONICO";
        }
        
        final String freConsulta = "BAJA";
        final String notas;
        ExpDocumento expDocumento = expDocumentoService.findByDocumento(documento);
        if (expDocumento != null) {
            notas = expDocumento.getExpId().getExpNombre();
        } else {
            notas = "EXPEDIENTE ELECTRONICO";
        }
        return new String[]{numOrden, codigo, asunto, fecRadicado, fecRadicado, numCaja, numCarpeta, tomo, otro, numeroFolios, soporte, freConsulta, notas};
    }

    /**
     * Llena la fila de table.
     *
     * @param asposeDocument Documento ASPOSE.
     * @param row Fila.
     * @param cellValues Valores para las celdas de la fila.
     */
    private void fillTableRow(final Document asposeDocument, final Row row, final String... cellValues) {
        for (String cellValue : cellValues) {
            Paragraph paragraph = new Paragraph(asposeDocument);
            paragraph.getParagraphFormat().getStyle().getFont().setSize(8);
            paragraph.getParagraphFormat().getStyle().getFont().setName("Arial");
            paragraph.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
            paragraph.appendChild(new Run(asposeDocument, cellValue));

            Cell cell = new Cell(asposeDocument);
            cell.appendChild(paragraph);
            row.appendChild(cell);
        }
    }

    /**
     * Retorna el archivo de la firma del usuario
     *
     * @param usuario
     * @return Archivo de firma
     */
    private File getImagenFirma(final Usuario usuario) {
        final String imagenFirma = usuario.getImagenFirma();
        if (imagenFirma == null) {
            return null;
        }

        return new File(ofs.getPath(usuario.getImagenFirmaExtension()));
    }
}
