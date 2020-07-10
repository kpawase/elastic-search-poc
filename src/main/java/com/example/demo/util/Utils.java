package com.example.demo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class Utils {

	public static boolean validateIndexName(String indexName) {
		if (!indexName.equals(indexName.toLowerCase()) || indexName.startsWith("_") || indexName.startsWith("-"))
			return false;

		return true;

	}
	
	public static File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

}
