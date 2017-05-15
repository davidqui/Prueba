package com.laamware.ejercito.doc.web.serv;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class DocumentServiceImpl implements DocumentService {

	@Override
	public List<Map<String, Object>> search() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map;

		map = new HashMap<String, Object>();
		list.add(map);
		map.put("DOC_ID", 2134);
		map.put("DOC_FECHA_RADICACION", new Date());
		map.put("DOC_ASUNTO", "Derecho de petición PEPITO PÉREZ");
		map.put("DEST_NOMBRE", "Jefatura de análisis");
		map.put("EST_NOMBRE", "Devuelto");
		map.put("TIPO_RAD_NOMBRE", "Por ventanilla");

		map = new HashMap<String, Object>();
		list.add(map);
		map.put("DOC_ID", 2138);
		map.put("DOC_FECHA_RADICACION", new Date());
		map.put("DOC_ASUNTO", "Solicitud de revisión de expediente");
		map.put("DEST_NOMBRE", "Jefatura de inteligencia");
		map.put("EST_NOMBRE", "Solicitud de visto bueno");
		map.put("TIPO_RAD_NOMBRE", "Por ventanilla");

		map = new HashMap<String, Object>();
		list.add(map);
		map.put("DOC_ID", 2254);
		map.put("DOC_FECHA_RADICACION", new Date());
		map.put("DOC_ASUNTO", "Memorando mensual");
		map.put("DEST_NOMBRE", "Departamento de inteligencia");
		map.put("EST_NOMBRE", "Plazo");
		map.put("TIPO_RAD_NOMBRE", "Interno");
		return list;
	}

}
