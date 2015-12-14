package com.premiere;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import DB.PremiereDBConn;
import premiereMD5.PremiereMD5Check;
import premiereXML.XMLHandler;

@Controller
public class FileUploadCtrl {

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public @ResponseBody String provideUploadInfo() {
		return "You can upload a file by posting to this same URL.";
	}

	@CrossOrigin(origins = "http://52.25.116.171")
	@RequestMapping(value = "/uploadXML", method = RequestMethod.POST)
	public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				String name = "file.xml";
				byte[] bytes = file.getBytes();
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File("/app/files/" + name)));
				stream.write(bytes);
				stream.close();

				XMLHandler xml = new XMLHandler(new File("/app/files/" + name));
				xml.parseXML();
				return xml.loadXMLData();
			} catch (Exception e) {
				return "{\"status\":\"" + e + "\"}";
			}
		} else {
			return "You failed to upload because the file was empty.";
		}
	}

	@CrossOrigin(origins = "http://52.25.116.171")
	@RequestMapping(value = "/uploadMedia", method = RequestMethod.POST)
	public @ResponseBody String handleMediaUpload(@RequestParam("id") String id,
			@RequestParam("checksum") String checksum,
			@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				String name = "media.txt";
				String filePath = "/var/www/html/static/files/" + name;
				String urlPath = "static/files/" + name;
				
				byte[] bytes = file.getBytes();
			
				PremiereMD5Check md5 = new PremiereMD5Check(bytes);
				
				if(md5.digest().contains(checksum)) {
					BufferedOutputStream stream = new BufferedOutputStream(
							new FileOutputStream(new File(filePath)));
					stream.write(bytes);
					stream.close();
					
					String update = String.format("update movies set File='%s' where id=%s", urlPath, id);
				
					PremiereDBConn mySQL = new PremiereDBConn();
					Connection myDB = (Connection) mySQL.getDB();
					Statement stmt = (Statement) myDB.createStatement();
					stmt.executeUpdate(update);
					
					return "{\"status\":\"0\"}";
				} else {
					return "{\"status\":\"1\",\"error\":\"" + md5.digest() + "\"}";
				}

			} catch (Exception e) {
				return "{\"status\":\"" + e + "\"}";
			}
		} else {
			return "You failed to upload because the file was empty.";
		}
	}
}
