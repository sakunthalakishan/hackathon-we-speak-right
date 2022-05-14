package com.hackathon.wespeakright.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.hackathon.wespeakright.entity.Person;
import com.hackathon.wespeakright.repository.PersonRepository;
import com.hackathon.wespeakright.utils.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Service
public class PronunciationService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    TextToSpeechService textToSpeechService;

    @Autowired
    CognitiveAzureSpeechService cognitiveSpeechService;

    @Autowired
    public BlobStorageService blobStorageService;

    public List<Person> getAllPersons() {
        return personRepository.findAll(Sort.by("id").descending());
    }

    private Integer saveRecord(PersonAudio personAudio, String storageFileName) {
        Person p = new Person();
        
        p.setFirstName(personAudio.getFirstName());
        p.setLastName(personAudio.getLastName());
        p.setPreferName(personAudio.getPreferName());
        p.setAudioFileLocation(storageFileName);

        Person person = personRepository.save(p);
        
        Integer result = person.getId();

        System.out.println("Details saved for " + result);
        return result;

    }

    public StreamingResponseBody getAudio(String name, int speed) throws FileNotFoundException{

        int id =0;

        String pronunceName = null;

        try{
            id = Integer.parseInt(name);
        } catch(NumberFormatException ne) {
            System.out.println("Not ID, use Standard Pronunciation");
            pronunceName = name;
        }

        if(id != 0) {
            Person p = personRepository.getById(id);
            String fileLocation = p.getAudioFileLocation();
            if(fileLocation != null) {
                return getStreamFromStorage(fileLocation);
            }

            if(p.getPreferName() != null && p.getPreferName().trim().length() != 0) pronunceName = p.getPreferName();
            else pronunceName = p.getFirstName();
        }

        if(pronunceName != null) {
            return textToSpeechService.standardPronunciation(pronunceName,speed);
        } else {
            System.out.println("No name to pronunce");
            throw new RuntimeException();
        }

    }

    public StreamingResponseBody getAudio(String name, String country, String speed) throws InterruptedException, ExecutionException, FileNotFoundException {

        int id =0;

        String pronunceName = null;

        try{
            id = Integer.parseInt(name);
        } catch(NumberFormatException ne) {
            System.out.println("Not ID, use Standard Pronunciation");
            pronunceName = name;
        }

        if(id != 0) {
            Person p = personRepository.getById(id);
            String fileLocation = p.getAudioFileLocation();
            if(fileLocation != null) {
                return getStreamFromStorage(fileLocation);
            }

            if(p.getPreferName() != null && p.getPreferName().trim().length() != 0) pronunceName = p.getPreferName();
            else pronunceName = p.getFirstName();
        }

        if(pronunceName != null) {
            return cognitiveSpeechService.convertTextToSpeech(pronunceName, country, speed);
        } else {
            System.out.println("No name to pronunce");
            throw new RuntimeException();
        }

    }   

    public StreamingResponseBody getStreamFromStorage(String fileName) throws FileNotFoundException {

        byte[] audioData = blobStorageService.downloadAudioBlob(fileName);

        InputStream is = new ByteArrayInputStream(audioData);
        
        StreamingResponseBody stream = Utilities.convert(is);
        
        System.out.println("before returning stream from storage");
        
        return stream;

    }

    public void deleteData(Integer id) {
        Person p = personRepository.getById(id);
        boolean proceed = false;
        if(p != null) {
            proceed = true;
            if(p.getAudioFileLocation() != null) {
                proceed = blobStorageService.deleteAudioBlob(p.getAudioFileLocation() );
            }
        }
        if(proceed) personRepository.deleteById(id);
    }
    
    public Integer savePostData(PersonAudio personAudio) throws IOException {

        String encodedString = personAudio.getAudioBase64();
        
        String audioFileLocation = null;

        if(encodedString != null && encodedString.trim().length() != 0) {
            byte[] audioContent = Base64.getMimeDecoder().decode(encodedString);
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            String fileName = personAudio.getFirstName()+personAudio.getLastName()+timeInMillis+".wav";
            audioFileLocation = blobStorageService.uploadAudioBlob(audioContent, fileName);
        }

        return saveRecord(personAudio, audioFileLocation);
    }

    public Integer saveData(PersonAudio personAudio, byte[] audioContent) throws IOException {

        String audioFileLocation = null;

        if(audioContent != null) {
            System.out.println("AudioContent not null");
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            String fileName = personAudio.getFirstName()+personAudio.getLastName()+timeInMillis+".wav";
            System.out.println("filename before storage upload "+ fileName);
            audioFileLocation = blobStorageService.uploadAudioBlob(audioContent, fileName);
            System.out.println("audio saved at " + audioFileLocation);
        }
        
        return saveRecord(personAudio, audioFileLocation);
    }


}
