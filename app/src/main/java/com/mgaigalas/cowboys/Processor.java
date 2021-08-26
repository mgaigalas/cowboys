package com.mgaigalas.cowboys;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgaigalas.cowboys.model.Cowboy;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Marius Gaigalas
 */
@Slf4j
public class Processor {
    final ObjectMapper mapper;

    public Processor() {
        this.mapper = new ObjectMapper();
    }


    public void process() {
        final var inputJson = "[{\"name\":\"John\",\"health\":10,\"damage\":1},{\"name\":\"Bill\",\"health\":8,\"damage\":2},{\"name\":\"Sam\",\"health\":10,\"damage\":1},{\"name\":\"Peter\",\"health\":5,\"damage\":3},{\"name\":\"Philip\",\"health\":15,\"damage\":1}]";
        try {
            final CopyOnWriteArrayList<Cowboy> cowboyList = mapper.readValue(inputJson, new TypeReference<>() {
            });

            log.info("cowboyList: {}", cowboyList);
            final var game = new Game();
            game.start(cowboyList);
        } catch (JsonProcessingException e) {
            log.error("error: {}", e.getMessage(), e);
        }
    }
}
