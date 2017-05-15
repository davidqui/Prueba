package com.laamware.ejercito.doc.web.serv;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DriveService {

	private static final String CONNECTION_FILE = ".connection";
	
	@Value("${docweb.drives.root}")
	String root;

	public boolean checkConnection(String ip, String username) {
		File file = new File(getConnectionFile(username));
		if(file.exists() == false) {
			return false;
		}
		
		String connectionIp = null;
		try {
			connectionIp = FileUtils.readFileToString(file);
			connectionIp = connectionIp.trim();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		if(ip.contains(connectionIp)) {
			return true;
		} else {
			return false;
		}
	}

	private String getConnectionFile(String username) {
		String path =  getDriveFile(username);
		path = FilenameUtils.concat(path, CONNECTION_FILE);
		return path;
	}

	private String getDriveFile(String username) {
		String path =  FilenameUtils.concat(root, username);
		return path;
	}
	
	public File[] files(String username) {
		File drive = new File(getDriveFile(username));
		if(drive.exists()) {
			return drive.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return !name.startsWith(".");
				}
			});
		} else {
			return null;
		}
	}

	public byte[] read(String filename, String username) {
		String filenameFull = FilenameUtils.concat(getDriveFile(username), filename);
		try {
			return FileUtils.readFileToByteArray(new File(filenameFull));
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	public void delete(String filename, String username) {
		String filenameFull = FilenameUtils.concat(getDriveFile(username), filename);
		File file = new File(filenameFull);
		file.delete();
	}
	
	
	
}
