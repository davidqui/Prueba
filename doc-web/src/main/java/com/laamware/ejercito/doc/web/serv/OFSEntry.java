package com.laamware.ejercito.doc.web.serv;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

public class OFSEntry {

	private byte[] bytes;

	private byte[] content;

	private byte[] contentType;

	public OFSEntry(byte[] bytes) {
		this.bytes = bytes;
		contentType = Arrays.copyOfRange(bytes, 0, 128);
		content = Arrays.copyOfRange(bytes, 128, this.bytes.length);
	}

	public OFSEntry(String contentType, byte[] bytes) {
		this.contentType = new byte[128];
		Arrays.fill(this.contentType, (byte) 0);
		byte[] b = contentType.getBytes();
		for (int i = 0; i < b.length; i++) {
			this.contentType[i] = b[i];
		}
		this.content = bytes;
		this.bytes = new byte[128 + this.content.length];
		for (int i = 0; i < 128; i++) {
			this.bytes[i] = this.contentType[i];
		}
		for (int i = 0; i < this.content.length; i++) {
			this.bytes[128 + i] = this.content[i];
		}
	}

	public OFSEntry(File file) throws IOException {
		this.bytes = FileUtils.readFileToByteArray(file);
	}

	public String getContentType() {
		return (new String(this.contentType)).trim();
	}

	public byte[] getContent() {
		return this.content;
	}

	public byte[] getBytes() {
		return this.bytes;
	}

}
