package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.NotificacionxUsuarioDTO;
import com.laamware.ejercito.doc.web.entity.NotificacionXUsuario;
import com.laamware.ejercito.doc.web.entity.TipoNotificacion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.NotificacionxUsuarioRepository;
import com.laamware.ejercito.doc.web.repo.TipoNotificacionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link NotificacionXUsuario}.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since 1.8
 * @version 25/07/2018 Issue #182 (SICDI-Controltech) feature-182.
 */
@Service
public class NotificacionxUsuarioService {

    @Autowired
    private NotificacionxUsuarioRepository notificacionxUsuarioRepository;

    public List<NotificacionxUsuarioDTO> retornaNotificacionesXUsuario(Integer usuId) {
        return notificacionxUsuarioRepository.findNotificacionesXusuario(usuId);
    }

    public Integer cambiarEstado(Integer usuId, Integer tnfId) {
        NotificacionXUsuario notificacionXUsuario = notificacionxUsuarioRepository.findByUsuIdAndTnfId(usuId, tnfId);
        if (notificacionXUsuario == null) {
            notificacionXUsuario = new NotificacionXUsuario();
            notificacionXUsuario.setUsuId(new Usuario(usuId));
            notificacionXUsuario.setTnfId(new TipoNotificacion(tnfId));
            notificacionxUsuarioRepository.saveAndFlush(notificacionXUsuario);
            return 0;
        } else {
            notificacionxUsuarioRepository.delete(notificacionXUsuario);
            return 1;
        }
    }
    
    public int findCountByUsuIdAndTnfId(Integer usuId, Integer tnfId){
        return notificacionxUsuarioRepository.findCountByUsuIdAndTnfId(usuId, tnfId);
    }
}
