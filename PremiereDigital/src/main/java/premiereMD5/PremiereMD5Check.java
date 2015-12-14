package premiereMD5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PremiereMD5Check {
	private byte[] file;
	
	public PremiereMD5Check(byte[] file) {
		this.file = file;
	}
	 
	public String digest() throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] hash = md.digest(file); 
		
		StringBuilder sb = new StringBuilder(2*hash.length); 
			
		for(byte b : hash){ 
			sb.append(String.format("%02x", b&0xff)); 
		} 
			
		return sb.toString();
	 }
}
