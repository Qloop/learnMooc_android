package com.upc.learnmooc.utils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 从数据流中读取为String
 * Created by Explorer on 2016/1/17.
 */
public class StreamUtils {

	public static String readFromStream(InputStream in) throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] buffer = new byte[1024];

		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		in.close();
		out.close();

		return out.toString();
	}

}
