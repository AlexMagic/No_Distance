/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.project.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;

public class ImageUtils {
	
	//环信的图片接口
	public static String getImagePath(String remoteUrl)
	{
		String imageName= remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1, remoteUrl.length());
		String path =PathUtil.getInstance().getImagePath()+"/"+ imageName;
        EMLog.d("msg", "image path:" + path);
        return path;
		
	}
	
	//环信的图片接口
	public static String getThumbnailImagePath(String imagePath) {
		String path = imagePath.substring(0, imagePath.lastIndexOf("/") + 1);
		path += "th" + imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
		EMLog.d("msg", "original image path:" + imagePath);
		EMLog.d("msg", "thum image path:" + path);
		return path;
	}
	
	//阿里云的图片接口
//	public static get
	
	
	public final static String IMG_PATH = Environment.getExternalStorageDirectory() + "/myApp_img/";
	
	public static void saveBitmap(Bitmap bm, String picName) {
		Log.e("", "保存图片");
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(IMG_PATH, picName + ".JPEG"); 
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Log.e("", "已经保存");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(IMG_PATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(IMG_PATH + fileName);
		file.isFile();
		return file.exists();
	}
	
	public static void delFile(String fileName){
		File file = new File(IMG_PATH + fileName);
		if(file.isFile()){
			file.delete();
        }
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(IMG_PATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}
}
