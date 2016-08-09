package com.zbar.lib;


public class ZbarManager {

	static {
		System.loadLibrary("zbar");
	}
	public native String decodeRaw(byte[] data, int width, int height);
	
	public native String decode(byte[] data, int width, int height, boolean isCrop, int x, int y, int cwidth, int cheight);
}
