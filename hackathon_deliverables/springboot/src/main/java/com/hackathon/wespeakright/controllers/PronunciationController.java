package com.hackathon.wespeakright.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.hackathon.wespeakright.entity.Person;
import com.hackathon.wespeakright.service.BlobStorageService;
import com.hackathon.wespeakright.service.PersonAudio;
import com.hackathon.wespeakright.service.PronunciationService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;


@CrossOrigin
@RestController
@RequestMapping("/api/textToSpeech")
public class PronunciationController  {

    @Autowired
    public BlobStorageService blobStorageService;

    @Autowired
    public PronunciationService pronunciationService;

    @GetMapping("/speed/{name}")
    public ResponseEntity<StreamingResponseBody> pronunceNameWithSpeed(@PathVariable("name") String name,
    @RequestParam(required=false) Integer speed) throws FileNotFoundException {

        System.out.println("Pronunce name with Speed"+ name);
    
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/wav"))
                .body(pronunciationService.getAudio(name, speed));
    }

    @GetMapping("/{name}")
    public ResponseEntity<StreamingResponseBody> pronunceName(@PathVariable("name") String name) throws FileNotFoundException {

        System.out.println("Pronunce name "+ name);
    
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/wav"))
                .body(pronunciationService.getAudio(name, 150));
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

        System.out.println("Save Person POST "+ requestBody);
        return pronunciationService.savePostData(requestBody);

    }

    @PostMapping(value="/recordname")
    public Integer saveData(@RequestParam String firstName, 
                            @RequestParam String lastName, 
                            @RequestParam(required=false) String preferName, 
                            @RequestParam(required=false) MultipartFile file) throws IOException {
        PersonAudio person = new PersonAudio();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setPreferName(preferName);

        System.out.println("Save Person POST form data" );
        return pronunciationService.saveData(person, file.getBytes());

    }

    @GetMapping(value="/list/allPersons", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Person> getAllPersons() {
        System.out.println("Get All Persons");
        //List<Person> all = pronunciationService.getAllPersons();
        return pronunciationService.getAllPersons();
    }

}