package com.teleclub.telesms.util;

import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ResourceUtil {
	public static String RES_DIRECTORY = Environment.getExternalStorageDirectory() + "/RajaTalk/";

	public static String getFilePath(String fileName) {
		String tempDirPath = RES_DIRECTORY;
		String tempFileName = fileName;

		File tempDir = new File(tempDirPath);
		if (!tempDir.exists())
			tempDir.mkdirs();
		File tempFile = new File(tempDirPath + tempFileName);
		if (!tempFile.exists())
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		return tempDirPath + tempFileName;
	}

	public static void SetLoginDataToFile(String phonenumber, String password) {
		File file = new File(ResourceUtil.getFilePath("Login.txt"));
		if (file.exists()) {
			try {
				file.delete();
				file.createNewFile();
			} catch (IOException e) {}
		}
		try {
			FileOutputStream fOut = new FileOutputStream(file, true);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			String data = phonenumber + "---" + password;
			myOutWriter.append(data);
			myOutWriter.close();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String[] GetLoginDataFromFile() {
		String[] strLoginData = new String[2];
		File file = new File(ResourceUtil.getFilePath("Login.txt"));
		StringBuilder text = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				text.append(line);
			}
			br.close();
			String data = text.toString();
			if (!TextUtils.isEmpty(data)) {
				String[] login = data.split("---");
				if (login.length > 0) {
					strLoginData[0] = login[0];
					strLoginData[1] = login[1];
				}
			}
		}
		catch (IOException e) {}
		return strLoginData;
	}
}
