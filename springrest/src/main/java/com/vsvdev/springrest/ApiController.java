package com.vsvdev.springrest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ElasticSearchService elasticSearchService;

@Autowired
    public ApiController(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    @GetMapping("/count")
    public long count() throws IOException {
        return elasticSearchService.getCount();
    }


    @PutMapping("/articles")
    public String addArticle(@RequestParam("title") String title, @RequestParam("text") String text) throws Exception {
        String id = UUID.randomUUID().toString();
        elasticSearchService.updateArticle(id, title, text);
        return id;
    }

    @GetMapping("/search")
    public ResponseEntity<List<ElasticSearchService.Article>> search(@RequestParam("query") String query) throws Exception {
       List<ElasticSearchService.Article> articleList= new ArrayList<>();
               if (elasticSearchService.search(query).size()!=0){
                   articleList =elasticSearchService.search(query);
                   return new ResponseEntity<>(articleList, HttpStatus.OK);
        }

    return new ResponseEntity<>(articleList, HttpStatus.OK);
    }

}
