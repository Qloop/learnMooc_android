package com.upc.learnmooc.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.upc.learnmooc.R;
import com.upc.learnmooc.utils.ToastUtils;
import com.upc.learnmooc.utils.UserInfoCacheUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 修改头像
 * Created by Explorer on 2016/4/15.
 */
public class ChangeAvatarActivity extends Activity {

	private static int CAMERA_REQUEST_CODE = 1;
	private static int GALLERY_REQUEST_CODE = 2;
	private static int CROP_REQUEST_CODE = 3;

	private TextView tvFromGallery;
	private TextView tvTakePhoto;
	private File img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avatar_revise_activity);

		initViews();
	}

	private void initViews() {
		tvTakePhoto = (TextView) findViewById(R.id.tv_take_photo);
		tvFromGallery = (TextView) findViewById(R.id.tv_chose_gallery);

		//拍照
		tvTakePhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAMERA_REQUEST_CODE);
			}
		});
		//从相册选
		tvFromGallery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, GALLERY_REQUEST_CODE);
			}
		});
	}


	private Uri saveBitmap(Bitmap bm) {
		File tmpDir;
		if (hasSD()) {
			tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.upc.avatar");
		} else {
			tmpDir = new File(Environment.getDataDirectory() + "/com.upc.avatar");
		}

		if (!tmpDir.exists()) {
			tmpDir.mkdir();
		}
		img = new File(tmpDir.getAbsolutePath() + "avater.png");
		try {
			FileOutputStream fos = new FileOutputStream(img);
			bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			return Uri.fromFile(img);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Uri convertUri(Uri uri) {
		InputStream is = null;
		try {
			is = getContentResolver().openInputStream(uri);
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			is.close();
			return saveBitmap(bitmap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void startImageZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST_CODE) {
			if (data == null) {
				return;
			} else {
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap bm = extras.getParcelable("data");
					Uri uri = saveBitmap(bm);
					startImageZoom(uri);
				}
			}
		} else if (requestCode == GALLERY_REQUEST_CODE) {
			if (data == null) {
				return;
			}
			Uri uri;
			uri = data.getData();
			Uri fileUri = convertUri(uri);
			startImageZoom(fileUri);
		} else if (requestCode == CROP_REQUEST_CODE) {
			if (data == null) {
				return;
			}
			Bundle extras = data.getExtras();
			if (extras == null) {
				return;
			}
			Bitmap bm = extras.getParcelable("data");
			finish();
//			sendImage(img);
		}
	}

	private void sendImage(File img) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configTimeout(5000);
		RequestParams params = new RequestParams();
		params.addBodyParameter(img.getPath().replace("/", ""), img);
		String mUrl = null;
		httpUtils.send(HttpRequest.HttpMethod.POST, mUrl, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				ToastUtils.showToastLong(ChangeAvatarActivity.this,"修改成功 ^_^");
				//上传成功后 更新本地缓存
				UserInfoCacheUtils.setString(ChangeAvatarActivity.this,"avatar",responseInfo.result);
				finish();
			}

			@Override
			public void onFailure(HttpException e, String s) {
				e.printStackTrace();
				finish();
			}
		});
	}

	/**
	 * 是否有SD卡
	 */
	private boolean hasSD() {
		//如果有SD卡 则下载到SD卡中
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;

		} else {
			//如果没有SD卡
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

}
