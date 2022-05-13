package com.hackathon.wespeakright.service;


import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import com.hackathon.wespeakright.utils.Utilities;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import com.sun.speech.freetts.audio.SingleFileAudioPlayer;
import javax.sound.sampled.AudioFileFormat;

@Service
public class TextToSpeechService {


    public StreamingResponseBody standardPronunciation(String name, int speed) throws FileNotFoundException {
        System.out.println("Standard Pronunciation");

        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        System.out.println(VoiceManager.getInstance().getVoices().length);

        Voice voice = VoiceManager.getInstance().getVoice("kevin");

        if (voice == null) return null;
        int rate = 150;

        voice.allocate();
        if(speed > 0) rate = speed;
        
        voice.setRate(rate);
        voice.setPitch(150);
        voice.setVolume(10);

        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        String tempdirectory = System.getProperty("java.io.tmpdir") +File.separator;

        System.out.println(tempdirectory);

        String trimmedname = name.replaceAll(" ", "");

        String fileName = tempdirectory+ trimmedname +timeInMillis;

        SingleFileAudioPlayer player = new SingleFileAudioPlayer(fileName, AudioFileFormat.Type.WAVE);

        voice.setAudioPlayer(player);
        
        voice.speak(name);

        voice.deallocate();
        player.close();
 
        return getStream(fileName+".wav");

    }

    public StreamingResponseBody getStream(String fullFileName) throws FileNotFoundException {
        System.out.println("Steam file "+ fullFileName);
        File file = new File(fullFileName);
        System.out.println("File exists = " + file.exists());
        InputStream is = new FileInputStream(file);

        StreamingResponseBody stream = Utilities.convert(is);

        System.out.println("delete of file " + fullFileName + " is " + file.delete());

        return stream;

    }

}
