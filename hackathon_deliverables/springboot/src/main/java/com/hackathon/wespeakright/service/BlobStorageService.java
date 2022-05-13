package com.hackathon.wespeakright.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BlobStorageService {

    
    @Value("${wespeakaudio.storage.connectionstring}")
    String connectionStr;

    @Value("${wespeakaudio.storage.containername}")
    String containerName;

 /*   BlobContainerClient container;

    public BlobStorageService( @Value("${wespeakaudio.storage.connectionstring}") String connectionStr,
    @Value("${wespeakaudio.storage.containername}") String containerName) {
        container = new BlobContainerClientBuilder()
        .connectionString(connectionStr)
        .containerName(containerName)
        .buildClient();
    }
*/
    public BlobContainerClient getBlobContainerClient() {
        return new BlobContainerClientBuilder()
        .connectionString(connectionStr)
        .containerName(containerName)
        .buildClient();
    }

    public String uploadAudioBlob(byte[] data, String fileName) throws IOException {

        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        
        BlobClient blob = getBlobContainerClient().getBlobClient(fileName);
        blob.upload(bis,bis.available(),false);
        
        bis.close();
        System.out.println("blob URL "+blob.getBlobUrl());
        System.out.println("upload successful for "+ fileName + " bytes = "+ data.length);

        return fileName;
    }

    public byte[] downloadAudioBlob(String fileName) {
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        BlobClient c = getBlobContainerClient().getBlobClient(fileName);
        
        c.download(bos);

        byte[] audioBlob = bos.toByteArray();

        System.out.println("read bytes "+ bos.size());
        System.out.println("download successful for " + fileName + " bytes = " + audioBlob.length);
        return audioBlob;
    }

    public boolean deleteAudioBlob(String fileName) {
        
        BlobClient c = getBlobContainerClient().getBlobClient(fileName);
               
        if( c.exists()) {            
            c.delete();
            System.out.println("delete successful for " + fileName);
        } else {
            System.out.println(fileName + " does not exist to delete");
        }
        return true;
    }
    
}
