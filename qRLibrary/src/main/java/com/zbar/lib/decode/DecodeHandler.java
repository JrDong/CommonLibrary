package com.zbar.lib.decode;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.qrlibrary.R;
import com.zbar.lib.CaptureActivity;
import com.zbar.lib.ZbarManager;
import com.zbar.lib.bitmap.PlanarYUVLuminanceSource;


 public class DecodeHandler extends Handler {

	CaptureActivity activity = null;

	public DecodeHandler(CaptureActivity activity) {
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message message) {
		if (message.what == R.id.decode) {
			decode((byte[]) message.obj, message.arg1, message.arg2);
		} else if (message.what == R.id.quit) {
			Looper.myLooper().quit();
		}
	}

	private void decode(byte[] data, int width, int height) {
		byte[] rotatedData = new byte[data.length];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++)
				rotatedData[x * height + height - y - 1] = data[x + y * width];
		}
		int tmp = width;// Here we are swapping, that's the difference to #11
		width = height;
		height = tmp;

		ZbarManager manager = new ZbarManager();
		String result = manager.decode(rotatedData, width, height, true, activity.getX(), activity.getY(), activity.getCropWidth(),
				activity.getCropHeight());

		if (result != null) {
			if (activity.isNeedCapture()) {
				// 生成bitmap
				PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(rotatedData, width, height, activity.getX(), activity.getY(),
						activity.getCropWidth(), activity.getCropHeight(), false);
				int[] pixels = source.renderThumbnail();
				int w = source.getThumbnailWidth();
				int h = source.getThumbnailHeight();
				Bitmap bitmap = Bitmap.createBitmap(pixels, 0, w, w, h, Bitmap.Config.ARGB_8888);
				try {
					String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Qrcode/";
					File root = new File(rootPath);
					if (!root.exists()) {
						root.mkdirs();
					}
					File f = new File(rootPath + "Qrcode.jpg");
					if (f.exists()) {
						f.delete();
					}
					f.createNewFile();

					FileOutputStream out = new FileOutputStream(f);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
					out.flush();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (null != activity.getHandler()) {
				Message msg = new Message();
				msg.obj = result;
				msg.what = R.id.decode_succeeded;
				activity.getHandler().sendMessage(msg);
			}
		} else {
			if (null != activity.getHandler()) {
				activity.getHandler().sendEmptyMessage(R.id.decode_failed);
			}
		}
	}

}
