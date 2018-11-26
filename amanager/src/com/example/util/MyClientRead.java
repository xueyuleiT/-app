package com.example.util;

import java.io.InputStream;
import java.util.Map;


public class MyClientRead {

	public Map<String, String> readStream(final InputStream ins) {

		try {
			if(ins == null)
				return null;
			Map<String, String> getStream = MessageUtil.parseXml(ins);
			return getStream;
		} catch (Exception e) {
			return null;
		}
	}
}
