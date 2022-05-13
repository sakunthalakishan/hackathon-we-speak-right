package com.hackathon.wespeakright.utils;

import java.io.InputStream;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public class Utilities {

    public static StreamingResponseBody convert(InputStream is){
        
        StreamingResponseBody stream = out -> {
            byte[] buf = new byte[8192];
            int c;

            while((c=is.read(buf,0,buf.length)) >0) {
                out.write(buf,0,c);
                out.flush();
            }
            
            out.close();
            is.close();
        };
        
        return stream;
    }
    
}
