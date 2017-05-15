
package com.laamware.ejercito.doc.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.laamware.ejercito.doc.web.repo.DocumentoRepository;

@Component
public class ProcesosAutomaticosCron {

	@Autowired
	DocumentoRepository documentRepository;
	
	
	/*@Scheduled(cron = "${job.all_day_1205am}")
	public void limpiarBandejaDocumentoEntrada(){
		System.out.println("LLego");
		//documentRepository.
		
	}*/
	
}
