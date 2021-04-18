package com.vsvdev.es_link_raiting;

import co.elastic.apm.attach.ElasticApmAttacher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EsLinkRaitingApplication {

    public static void main(String[] args) {
        ElasticApmAttacher.attach();
        SpringApplication.run(EsLinkRaitingApplication.class, args);
    }

}
