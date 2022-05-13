package com.hackathon.wespeakright.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.hackathon.wespeakright.entity.Person;
import com.hackathon.wespeakright.service.BlobStorageService;
import com.hackathon.wespeakright.service.PersonAudio;
import com.hackathon.wespeakright.service.PronunciationService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;


@CrossOrigin
@RestController
@RequestMapping("/api/textToSpeech")
public class PronunciationController  {
/*
    @Value("${wespeakaudio.storage.connectionstring}")
    String connectionStr;

    @Value("${wespeakaudio.storage.containername}")
    String containerName;
    */

    @Autowired
    public BlobStorageService blobStorageService;

    @Autowired
    public PronunciationService pronunciationService;



/*
    @Value("${spring.cloud.azure.db.url}")
    private String azureDbUrl;
*/

    

/*
    private final String containerName;
    private final ResourceLoader resourceLoader;
    //private final AzureStorageBlobProtocolResolver azureStorageBlobProtocolResolver;
    

    public PronunciationController(@Value("${spring.cloud.azure.storage.blob.container-name}") String containerName,
                          ResourceLoader resourceLoader) {
                          //AzureStorageBlobProtocolResolver patternResolver) {
        this.containerName = containerName;
        this.resourceLoader = resourceLoader;
        //this.azureStorageBlobProtocolResolver = patternResolver;
    } */

   /*
    @GetMapping("/store/{name}")
    public String streamName(@PathVariable("name") String name) {

        BlobContainerClient container = new BlobContainerClientBuilder()
        .connectionString(connectionStr)
        .containerName(containerName)
        .buildClient();

        
        ByteArrayInputStream bis = new ByteArrayInputStream(name.getBytes());

        BlobClient blob = container.getBlobClient(name);
        blob.upload(bis,bis.available(),false);
        //blob.upload(name.getBytes());

        System.out.println("blob URL "+blob.getBlobUrl());
        System.out.println("upload successful for "+ name);
        return name;
    }
    */

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


/*
    @GetMapping("/read/{name}")
    public String streamReadName(@PathVariable("name") String name) {

        BlobContainerClient container = new BlobContainerClientBuilder()
        .connectionString(connectionStr)
        .containerName(containerName)
        .buildClient();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        //new BlobClientBuilder().buildClient().
        BlobClient c = container.getBlobClient(name);
        c.download(bos);

        System.out.println("read bytes "+ bos.size());
        System.out.println("read name "+ name);
        return name;
    }
    */



    @GetMapping("/upload/{name}")
    public String uploadName(@PathVariable("name") String name) throws IOException {

        System.out.println("Service Store file "+ name);
        return blobStorageService.uploadAudioBlob(name.getBytes(), name);

    }

    @GetMapping("/download/{name}")
    public String downloadName(@PathVariable("name") String name) throws IOException {

        System.out.println("Service download file "+ name);
        blobStorageService.downloadAudioBlob(name);
        return name;

    }

    @GetMapping("/delete/{name}")
    public String deleteName(@PathVariable("name") String name) throws IOException {

        System.out.println("Service delete file  "+ name);
        boolean result = blobStorageService.deleteAudioBlob(name);
        System.out.println("Delete is "+ result);
        return name;

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
        List<Person> all = pronunciationService.getAllPersons();
        return pronunciationService.getAllPersons();
    }

}