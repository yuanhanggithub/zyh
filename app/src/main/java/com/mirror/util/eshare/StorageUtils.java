package com.mirror.util.eshare;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class StorageUtils {

	private static final String TAG = "eshare";
	private static final String save_file = "/sdcard/storage.txt";

	static boolean StartsWith(String[] array, String text) {
		for (String item : array) {
			if (text.startsWith(item))
				return true;
		}

		return false;
	}

	public static boolean close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
				return true;
			} catch (IOException e) {
				return false;
			}
		} else {
			return false;
		}
	}

	private static boolean filterFields(String fieldStr) {
		boolean ret = false;

		String Fields[] = { "DISPLAY", "FINGERPRINT", "HOST", "MANUFACTURER",
				"MODEL", "PRODUCT", "USER", "BRAND" };
		if (fieldStr != null) {
			for (String name : Fields) {
				if (name.equals(fieldStr)) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	public static String getDisplayDetails(Context context) {
		try {
			final WindowManager windowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			final Display display = windowManager.getDefaultDisplay();
			final DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);
			final StringBuilder result = new StringBuilder();

			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					if (filterFields(field.getName())) {
						result.append(field.getName() + ":")
								.append(field.get(null)).append('\n');
					}
					// System.out.println("Build.class fields -- " +
					// field.getName() + ":" + field.get(null));
				} catch (Exception e) {

				}
			}

			fields = Build.VERSION.class.getDeclaredFields();
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					if (filterFields(field.getName())) {
						result.append("VERSION.").append(field.getName() + ":")
								.append(field.get(null)).append('\n');
					}
					// System.out.println("Build.VERSION.class fields -- " +
					// field.getName() + ":" + field.get(null));
				} catch (Exception e) {

				}
			}

			return result.toString();

		} catch (Exception e) {
			return "DisplayDetails:null";
		}
	}

	public static ArrayList<File> getStorageDirectories(Context context) {

		if (false) {
			myLog.d("file read begin ");
			File[] externalStorageFiles = ContextCompat.getExternalFilesDirs(
					context, null);

			for (File file : externalStorageFiles) {
				String path = file.getAbsolutePath();
				path = path.replaceAll(
						"/Android/data/" + context.getPackageName() + "/files",
						"");
				myLog.d("path: " + path);
			}
			return null;
		}
		File file = new File(save_file);
		if (file.exists())
			file.delete();

		String detail = getDisplayDetails(context);
		myLog.writeToFile(save_file, ("\n" + detail + "\n").getBytes());
		BufferedReader bufReader = null;
		ArrayList<File> list = new ArrayList<File>();
		String externaSd = Environment.getExternalStorageDirectory().getPath();
		list.add(new File(externaSd));
		myLog.writeToFile(save_file, "\n++++++++++++>begin\n".getBytes());
		myLog.writeToFile(save_file, externaSd.getBytes());
		myLog.writeToFile(save_file, "\n++++++++++++>over\n".getBytes());
		List<String> typeWL = Arrays.asList("vfat", "exfat", "sdcardfs",
				"fuse", "ntfs", "fat32", "ext3", "ext4", "esdfs");
		List<String> typeBL = Arrays.asList("tmpfs");
		String[] mountWL = { "/mnt", "/Removable" };
		String[] mountBL = { "/mnt/secure", "/mnt/shell", "/mnt/asec",
				"/mnt/obb", "/mnt/runtime", "/mnt/media_rw",
				"/storage/emulated" };
		String[] deviceWL = { "/dev/block/vold", "/dev/fuse",
				"/mnt/media_rw/extSdCard" };

		try {
			bufReader = new BufferedReader(new FileReader("/proc/mounts"));
			String line;
			while ((line = bufReader.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(line, " ");
				String device = tokens.nextToken();
				String mountpoint = tokens.nextToken();
				String type = tokens.nextToken();
				myLog.writeToFile(save_file, (line + "\r\n").getBytes());
				if (externaSd.equals(mountpoint))
					continue;
				if (list.contains(mountpoint) || typeBL.contains(type)
						|| StartsWith(mountBL, mountpoint))
					continue;

				if (StartsWith(deviceWL, device)
						&& (typeWL.contains(type) || StartsWith(mountWL,
								mountpoint))) {
					myLog.writeToFile(save_file,
							"\n++++++++++++>begin\n".getBytes());
					myLog.writeToFile(save_file, mountpoint.getBytes());
					myLog.writeToFile(save_file,
							"\n++++++++++++>over\n".getBytes());
					list.add(new File(mountpoint));
				}
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			close(bufReader);
		}
		return list;
	}

}
