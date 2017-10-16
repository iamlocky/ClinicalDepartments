package cn.lockyluo.clinicaldepartments.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.ImageColumns;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * ClassName: FileUtils
 * 
 * @Description: 文件工具类
 * @author kesar
 * @date 2015-11-12
 */
public class FileUtils
{
	/**
	 * 
	 * @Description: uri转FilePath
	 * @param @param context
	 * @param @param uri
	 * @param @return
	 * @return String
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static String Uri2FilePath(Context context, Uri uri)
	{
		if (null == uri)
		{
			return null;
		}
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
		{
			data = uri.getPath();
		}
		else if (ContentResolver.SCHEME_FILE.equals(scheme))
		{
			data = uri.getPath();
		}
		else if (ContentResolver.SCHEME_CONTENT.equals(scheme))
		{
			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { ImageColumns.DATA }, null, null, null);
			if (null != cursor)
			{
				if (cursor.moveToFirst())
				{
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1)
					{
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	/**
	 * 
	 * @Description: Uri转File
	 * @param @param context
	 * @param @param uri
	 * @param @return
	 * @return File
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static File Uri2File(Context context, Uri uri)
	{
		String path = Uri2FilePath(context, uri);
		return path == null ? null : new File(path);
	}

	/**
	 * 
	 * @Description: filePath转uri
	 * @param @param filePath
	 * @param @return
	 * @return Uri
	 * @throws
	 * @author kesar
	 * @date 2015-12-1
	 */
	public static Uri File2Uri(String filePath)
	{
		return File2Uri(new File(filePath));
	}

	/**
	 * 
	 * @Description: file转uri
	 * @param @param file
	 * @param @return
	 * @return Uri
	 * @throws
	 * @author kesar
	 * @date 2015-12-1
	 */
	public static Uri File2Uri(File file)
	{
		return Uri.fromFile(file);
	}

	/**
	 * 获取文件夹对象
	 * 
	 * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
	 */
	public static File getSaveFolder(String folderName)
	{
		File file = new File(getSDCardPath() + File.separator + folderName
				+ File.separator);
		file.mkdirs();
		return file;
	}

	public static String getSDCardPath()
	{
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	// 复制文件
	public static void copyFile(File sourceFile,File targetFile)
			throws IOException{
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff=new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff=new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len =inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();

		//关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}
	// 复制文件夹
	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile=file[i];
				// 目标文件
				File targetFile=new
						File(new File(targetDir).getAbsolutePath()
						+File.separator+file[i].getName());
				copyFile(sourceFile,targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1=sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2=targetDir + "/"+ file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}

	public static void deleteAllFiles(File root) {
		File files[] = root.listFiles();
		if (files != null)
			for (File f : files) {
				if (f.isDirectory()) { // 判断是否为文件夹
					deleteAllFiles(f);
					try {
						f.delete();
					} catch (Exception e) {
					}
				} else {
					if (f.exists()) { // 判断是否存在
						deleteAllFiles(f);
						try {
							f.delete();
						} catch (Exception e) {
						}
					}
				}
			}
	}
}
