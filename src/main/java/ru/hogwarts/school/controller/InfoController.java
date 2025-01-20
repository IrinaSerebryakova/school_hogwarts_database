package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Stream;


@RestController
@RequestMapping("/info")
public class InfoController {

   private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Value("${server.port}")
    private String port;

    @GetMapping("/port")
    public ResponseEntity<String> getPort() {
        return ResponseEntity.ok(port);
    }

    @GetMapping("/sum")
    public void getSum(){
        logger.info("The method non-optimized 'getSum' was called");
        long start = System.currentTimeMillis();
          Optional<Integer> reduce = Stream
                        .iterate(1, a -> a + 1)
                        .limit(1_000_000)
                        .reduce(Integer::sum);
        long finish = System.currentTimeMillis();

        logger.info("The method optimized 'getSum' was called");
        long startOptimized = System.currentTimeMillis();

        Optional<Integer> reduceOptimized =
               Stream
                        .iterate(1, a -> a + 1)
                       .limit(1_000_000)
                       .parallel()
                       .reduce(Integer::sum);
        long finishOptimized = System.currentTimeMillis();
    }
}
