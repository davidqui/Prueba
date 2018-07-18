package com.laamware.ejercito.doc.web.serv;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para almacenar cache de objetos de recurrencia petición y minima
 * mutación
 *
 * @author Samuel Delgado Muñoz
 * @since 1.8
 * @version 11/07/2018 Issue #179 (SICDI-Controltech) feature-179
 */
@Service
public class CacheService {
    
    
    private Map<String, Object> cache;
    
    
    public void setKeyCache(String key, Object object){
        initMap();
        cache.putIfAbsent(key, object);
    }
    
    public void deleteKeyCache(String key){
        initMap();
    for(Iterator<Map.Entry<String, Object>> it = cache.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> entry = it.next();
            System.out.println(entry.getKey()+" -- "+key+" = "+entry.getKey().contains(key));
            if (entry.getKey().contains(key)) {
                it.remove();
            }
        }
    }
    
    public Object getKeyCache(String key){
        initMap();
        return cache.get(key);
    }
    
    public void initMap(){
        if (cache == null) {
            cache = new HashMap<>();
        }
    }
}
