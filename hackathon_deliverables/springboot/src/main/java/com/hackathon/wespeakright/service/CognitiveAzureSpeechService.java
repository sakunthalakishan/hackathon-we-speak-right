package com.hackathon.wespeakright.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.microsoft.cognitiveservices.speech.CancellationReason;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;

@Service
public class CognitiveAzureSpeechService {

    private static final String defaultVoice = "en-US-JennyNeural";
    private static final String defaultSpeed = "default";
    List<String> acceptedSpeeds = Arrays.asList("x-slow", "slow", "medium", "fast","x-fast");
    
    private static final String SSML_PATTERN = "<speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xml:lang='en-US'><voice name='%s'><prosody rate='%s'>%s</prosody></voice></speak>";
 
    SpeechConfig config;

    public CognitiveAzureSpeechService(@Value("${texttospeech.service.subscriptionkey}") String subscriptionKey,
                                    @Value("${texttospeech.service.serviceregion}") String serviceRegion){
        config = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion);
        System.out.println("SpeechConfig Initialiazed");
    }

    public StreamingResponseBody convertTextToSpeech(String text, String country, String speed) throws InterruptedException, ExecutionException {
       
        StreamingResponseBody streamout = null;

        System.out.println("country= " + country );
        System.out.println("speed= "+ speed);

        SpeechSynthesizer synthesizer = new SpeechSynthesizer(config,null);
        System.out.println("SpeechSynthesizer Created");
        
        String ssml = String.format(SSML_PATTERN, getVoice(country), getSpeed(speed), text);

        System.out.println(ssml);

        SpeechSynthesisResult speechSynthesisResult = synthesizer.SpeakSsmlAsync(ssml).get();

        if(speechSynthesisResult.getReason() == ResultReason.SynthesizingAudioCompleted) {
            byte[] result = speechSynthesisResult.getAudioData();
            System.out.println("Speech Synthesized for text "+ text + " byte size="+ result.length);
            
            streamout = out -> {
                out.write(result);
                out.close();
            };
            
        }
        else if (speechSynthesisResult.getReason() == ResultReason.Canceled) {
            SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails.fromResult(speechSynthesisResult);
            System.out.println("CANCELED: Reason=" + cancellation.getReason());

            if (cancellation.getReason() == CancellationReason.Error) {
                System.out.println("CANCELED: ErrorCode=" + cancellation.getErrorCode());
                System.out.println("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
                System.out.println("CANCELED: Did you update the subscription info?");
            }
        }

        speechSynthesisResult.close();
        synthesizer.close();

        return streamout;

    }

    public String getVoice(String voice) {
        String result = defaultVoice;
        if(voice != null) {
            String s = voice.toUpperCase();

            switch(s) {
                case("GB"): result= "en-GB-RyanNeural"; break;
                case("IN"): result= "en-IN-NeerjaNeural"; break;
                case("HK"): result= "en-HK-SamNeural"; break;
                case("CA"): result= "en-CA-ClaraNeural"; break;
                case("AU"): result= "en-AU-NatashaNeural"; break;
                case("IE"): result= "en-IE-EmilyNeural"; break;
                case("NZ"): result= "en-NZ-MollyNeural"; break;
                case("SG"): result= "en-SG-WayneNeural"; break;    
                case("NG"): result= "en-NG-EzinneNeural "; break;
                case("KE"): result= "en-KE-ChilembaNeural"; break; 
                default: result=defaultVoice; break;
            }
        }
        return result;
    }

    public String getSpeed(String speed) {
        if(speed != null) {
            String r = speed.toLowerCase();
            if(acceptedSpeeds.contains(r)) return r;
        }
        return defaultSpeed;
    }

}
