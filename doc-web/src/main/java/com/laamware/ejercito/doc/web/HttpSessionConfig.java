package com.laamware.ejercito.doc.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
/*
 * 2017-02-08 jgarcia@controltechcg.com Comentar la
 * anotación @EnableRedisHttpSession si no se va a trabajar con REDIS.
 */
public class HttpSessionConfig {

}