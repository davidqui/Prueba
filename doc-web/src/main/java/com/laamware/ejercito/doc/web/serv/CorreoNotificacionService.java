package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.ctrl.DocumentoController;
import com.laamware.ejercito.doc.web.dto.EmailDTO;
import com.laamware.ejercito.doc.web.entity.Notificacion;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Servicio encarga de enviar las notificaciones de correo eléctronico
 *
 * @author samuel.delgado@controtechcg.com
 * @since 1.8
 * @version 06/07/2018
 */
@Service
public class CorreoNotificacionService {

    @Value("${com.mil.imi.sicdi.mail.username}")
    private String senderMail;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private NotificacionService notificacionService;

    private static final int TEMPLATE_HEADER = 60;
    private static final int TEMPLATE_FOOTER = 70;

    @Value("${docweb.images.root}")
    private String pathImages;

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DocumentoController.class);

    /**
     * Envia un correo de una notificación
     *
     * @param mensaje
     * @throws javax.mail.MessagingException
     */
    public void enviarNotificacion(EmailDTO mensaje) throws MessagingException {
        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        if (mensaje.getCopiaDestino() != null && !mensaje.getCopiaDestino().isEmpty()) {
            List<String> ccDestino = mensaje.getCopiaDestino();
            String[] array = new String[ccDestino.size()];
            String[] copias = mensaje.getCopiaDestino().toArray(array);
            System.out.println("ARRAYS DE CC " + Arrays.toString(copias) + copias.length);
//            message.setBcc(copias);
        }

        message.setSubject(mensaje.getAsunto());

        message.setFrom(senderMail);

        if (mensaje.getDestino() != null) {
            message.setTo(mensaje.getDestino());
        }

        //se agrega el cuerpo del mensaje
        String bodyHtml = "";

        List<Notificacion> header = notificacionService.fingByTypoNotificacionId(TEMPLATE_HEADER);
        List<Notificacion> footer = notificacionService.fingByTypoNotificacionId(TEMPLATE_FOOTER);

        if (header != null && !header.isEmpty()) {
            bodyHtml += header.get(0).getTemplate();
        }
        if (mensaje.getCuerpo() != null) {
            bodyHtml += mensaje.getCuerpo();
        }
        if (footer != null && !footer.isEmpty()) {
            bodyHtml += footer.get(0).getTemplate();
        }

        MimeMultipart multipart = new MimeMultipart("related");
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(bodyHtml, "text/html; charset=utf-8");
        multipart.addBodyPart(messageBodyPart);
        File folder = new File(pathImages);
        File[] listOfFiles = folder.listFiles();

        Pattern pattern = Pattern.compile("\"cid:(.*?)\"");
        Matcher machs = pattern.matcher(bodyHtml);
        try {
            while (machs.find()) {
                String img = machs.group(1);
                for (File listOfFile : listOfFiles) {
                    String nameFile = listOfFile.getName().split("\\.")[0];
                    if (nameFile.equals(img)) {
                        String absolutePath = listOfFile.getAbsolutePath();
                        InputStream imageStream = new FileInputStream(absolutePath);
                        DataSource fds = new ByteArrayDataSource(IOUtils.toByteArray(imageStream), "image/jpg");
                        messageBodyPart = new MimeBodyPart();
                        messageBodyPart.setDataHandler(new DataHandler(fds));
                        messageBodyPart.setHeader("Content-ID", "<" + img + ">");
                        multipart.addBodyPart(messageBodyPart);
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mimeMessage.setContent(multipart, "UTF-8");

        this.javaMailSender.send(mimeMessage);
        LOG.info("Envió de correo satisfactorio");
    }
}
