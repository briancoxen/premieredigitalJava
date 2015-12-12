package com.premiere;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import premiereXML.XMLHandler;

@Controller
public class FileUploadCtrl {

    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @CrossOrigin(origins="http://52.25.116.171")
    @RequestMapping(value="/uploadXML", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File("/app/files/" + name)));
                stream.write(bytes);
                stream.close();
                
                XMLHandler xml = new XMLHandler(new File("/app/files/" + name));
                xml.parseXML();
                return xml.loadXMLData();
            } catch (Exception e) {
                return "{\"status\":\""+e+"\"}";
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
}

