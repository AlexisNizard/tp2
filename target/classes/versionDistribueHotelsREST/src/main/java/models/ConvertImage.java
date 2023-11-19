package models;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ConvertImage {

	public byte[] ImageToString(String nomImage) throws IOException {
		byte[] fileContent = FileUtils.readFileToByteArray(new File("src/main/resources/static/"+nomImage+".jpg"));
		return fileContent;
	}
	
	public void StringToImage(byte[] codeImage,String nomImage) throws IOException {
		FileUtils.writeByteArrayToFile(new File("src/main/resources/static/"+nomImage+".jpg"), codeImage);
	}
	
}

