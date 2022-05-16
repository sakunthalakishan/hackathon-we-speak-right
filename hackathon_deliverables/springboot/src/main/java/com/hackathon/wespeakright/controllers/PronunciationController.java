package com.hackathon.wespeakright.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.hackathon.wespeakright.entity.Person;
import com.hackathon.wespeakright.service.BlobStorageService;
import com.hackathon.wespeakright.service.PersonAudio;
import com.hackathon.wespeakright.service.PronunciationService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;


@CrossOrigin
@RestController
@RequestMapping("/api/textToSpeech")
public class PronunciationController  {

    @Autowired
    public BlobStorageService blobStorageService;

    @Autowired
    public PronunciationService pronunciationService;


    @GetMapping("/{name}")
    public ResponseEntity<StreamingResponseBody> pronunceNameWithSpeedWithCountry(
        @PathVariable("name") String name,
        @RequestParam(required=false) String country,
        @RequestParam(required=false) String speed) throws FileNotFoundException, InterruptedException, ExecutionException {

        System.out.println("Pronunce name with Speed and Country "+ name);
    
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/wav"))
                .body(pronunciationService.getAudio(name, country, speed));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteData(@PathVariable("id") Integer id) throws IOException {

        System.out.println("Service delete file  "+ id);
        pronunciationService.deleteData(id);
        return ResponseEntity.ok().body(id);

    }

    @PostMapping(value="/record", produces=MediaType.APPLICATION_JSON_VALUE, 
                                    consumes=MediaType.APPLICATION_JSON_VALUE)
    public Integer savePostData(@RequestBody PersonAudio requestBody) throws IOException {

        //System.out.println("Save Person POST "+ requestBody);
        return pronunciationService.savePostData(requestBody);

    }


    @GetMapping(value="/list/allPersons", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Person> getAllPersons() {
        System.out.println("Get All Persons");
        return pronunciationService.getAllPersons();
    }

}