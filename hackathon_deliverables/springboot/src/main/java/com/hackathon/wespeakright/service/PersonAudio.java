package com.hackathon.wespeakright.service;

import java.io.Serializable;

public class PersonAudio implements Serializable{
    
    private String firstName;
    private String lastName;
    private String preferName;

    private String audioBase64;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPreferName() {
        return preferName;
    }

    public void setPreferName(String preferName) {
        this.preferName = preferName;
    }

    public String getAudioBase64() {
        return audioBase64;
    }

    public void setAudioBase64(String audioBase64) {
        this.audioBase64 = audioBase64;
    }

    
    @Override
    public String toString() {
        return "PersonAudio [audioBase64=" + audioBase64 + ", firstName=" + firstName + ", lastName=" + lastName
                + ", preferName=" + preferName + "]";
    }

    
    
}
