package com.zbar.lib;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrlibrary.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;
import com.zbar.lib.decode.RGBLuminanceSource;

import java.io.IOException;
import java.util.Hashtable;

public class CaptureActivity extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.50f;
	private boolean vibrate;
	private int x = 0;
	private int y = 0;
	private int cropWidth = 0;
	private int cropHeight = 0;
	private RelativeLayout mContainer = null;
	private RelativeLayout mCropLayout = null;
	private boolean isNeedCapture = false;
	private static final int REQUEST_CODE = 92;
	
	public boolean isNeedCapture() {
		return isNeedCapture;
	
	}

	public void setNeedCapture(boolean isNeedCapture) {
		this.isNeedCapture = isNeedCapture;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qr_scan);
		Button button_back = (Button) findViewById(R.id.button_back);
		TextView button_function = (TextView) findViewById(R.id.button_function);
		button_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CaptureActivity.this.finish();
			}
		});
		button_function.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CaptureActivity.this, SelectPictureActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
		
		
		// 初始化 CameraManager
		CameraManager.init(getApplication());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);

		ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
		TranslateAnimation mAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
		mAnimation.setDuration(1500);
		mAnimation.setRepeatCount(-1);
		mAnimation.setRepeatMode(Animation.REVERSE);
		mAnimation.setInterpolator(new LinearInterpolator());
		mQrLineView.setAnimation(mAnimation);
	}

	boolean flag = true;

	protected void light() {
		if (flag == true) {
			flag = false;
			// 开闪光灯
			CameraManager.get().openLight();
		} else {
			flag = true;
			// 关闪光灯
			CameraManager.get().offLight();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	public void handleDecode(final String result) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		AlertDialog.Builder builder=new AlertDialog.Builder(CaptureActivity.this);
		builder.setMessage(result);
		final String message=result;
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				Intent resultIntent = new Intent();
				resultIntent.putExtra("result", message);
				setResult(RESULT_OK, resultIntent);
				finish();
			}
		});
		builder.setNegativeButton("再扫一次", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				// 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
				 handler.sendEmptyMessage(R.id.restart_preview);
			}
		});
		builder.create().show();


	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);

			Point point = CameraManager.get().getCameraResolution();
			int width = point.y;
			int height = point.x;

			int x = mCropLayout.getLeft() * width / mContainer.getWidth();
			int y = mCropLayout.getTop() * height / mContainer.getHeight();

			int cropWidth = mCropLayout.getWidth() * width / mContainer.getWidth();
			int cropHeight = mCropLayout.getHeight() * height / mContainer.getHeight();

			setX(x);
			setY(y);
			setCropWidth(cropWidth);
			setCropHeight(cropHeight);
			// 设置是否需要截图
			setNeedCapture(true);
			

		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(CaptureActivity.this);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public Handler getHandler() {
		return handler;
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){  
        case REQUEST_CODE:  
        	if(resultCode==RESULT_OK){
        		String photo_path =  data.getExtras().getString("intent_selected_picture");
        		Result result = scanningImage(photo_path);
        		if (result != null) {
        			//Toast.makeText(this, "result："+result, Toast.LENGTH_SHORT).show();
        			handleDecode(result.getText());
//	        		if (null != getHandler()) {
//	    				Message msg = new Message();
//	    				msg.obj = result.getText();
//	    				msg.what = R.id.decode_succeeded;
//	    				getHandler().sendMessage(msg);
//	    			}
        		} else {
        			Toast.makeText(this, "无法识别", Toast.LENGTH_SHORT).show(); 
	    			if (null != getHandler()) {
	    				getHandler().sendEmptyMessage(R.id.decode_failed);
	    			}
        		}
        	}
            break;  
        }  
	};
//	public String scanningImage(String path) {
//		if(TextUtils.isEmpty(path)){
//			return null; 
//		}
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
//		options.inJustDecodeBounds = false;
//		
//		int sampleSize = (int) (options.outHeight / (float) 200);
//		if (sampleSize <= 0)
//			sampleSize = 1;
//		options.inSampleSize = sampleSize;
//		scanBitmap = BitmapFactory.decodeFile(path, options);
//		
//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		scanBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//		byte[] byteArray = stream.toByteArray();
//		
//		int width = options.outWidth;
//		int height = options.outHeight;
//		scanBitmap.recycle();
//		scanBitmap = null;
//		ZbarManager manager = new ZbarManager();
//		return manager.decode(byteArray, width, height, true, getX(), getY(), getCropWidth(), getCropHeight());
//		
//	}
	
	public Result scanningImage(String path) {
		if(TextUtils.isEmpty(path)){
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); 

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; 
		Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; 
		int sampleSize = (int) (options.outHeight / (float) 200);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);

		}  catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}