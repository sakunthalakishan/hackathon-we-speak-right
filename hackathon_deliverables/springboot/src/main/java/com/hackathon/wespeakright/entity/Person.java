package com.hackathon.wespeakright.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;
    private String preferName;

    //@JsonIgnore
    private String audioFileLocation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getAudioFileLocation() {
        return audioFileLocation;
    }

    public void setAudioFileLocation(String audioFileLocation) {
        this.audioFileLocation = audioFileLocation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((audioFileLocation == null) ? 0 : audioFileLocation.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((preferName == null) ? 0 : preferName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (audioFileLocation == null) {
            if (other.audioFileLocation != null)
                return false;
        } else if (!audioFileLocation.equals(other.audioFileLocation))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (preferName == null) {
            if (other.preferName != null)
                return false;
        } else if (!preferName.equals(other.preferName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Person [audioFileLocation=" + audioFileLocation + ", firstName=" + firstName + ", id=" + id
                + ", lastName=" + lastName + ", preferName=" + preferName + "]";
    }

    

}
