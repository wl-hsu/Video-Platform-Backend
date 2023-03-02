package com.video.platform.service.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * MD5 Encryption
 *
 */
public class MD5Util {

	public static String sign(String content, String salt, String charset) {
		content = content + salt;
		return DigestUtils.md5Hex(getContentBytes(content, charset));
	}

	public static boolean verify(String content, String sign, String salt, String charset) {
		content = content + salt;
		String mysign = DigestUtils.md5Hex(getContentBytes(content, charset));
		return mysign.equals(sign);
	}

	private static byte[] getContentBytes(String content, String charset) {
		if (!"".equals(charset)) {
			try {
				return content.getBytes(charset);
			} catch (UnsupportedEncodingException var3) {
				throw new RuntimeException("An error occurred during the MD5 signature process, the specified encoding set is wrong");
			}
		} else {
			return content.getBytes();
		}
	}

	// Get the md5 encrypted string of the file
	public static String getFileMD5(MultipartFile file) throws Exception {
		InputStream fis = file.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int byteRead;
		while((byteRead = fis.read(buffer)) > 0){
			baos.write(buffer, 0, byteRead);
		}
		fis.close();
		return DigestUtils.md5Hex(baos.toByteArray());
	}

}