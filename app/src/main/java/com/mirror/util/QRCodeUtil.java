package com.mirror.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mirror.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class QRCodeUtil {

    /**
     * 生成二维码Bitmap
     *
     * @param content  内容
     * @param logoBm   二维码中心的Logo图标（可以为null）
     * @param filePath 用于存储二维码图片的文件路径
     * @return 生成二维码及保存文件是否成功
     */
    public static boolean createQRImage(String content, Bitmap logoBm, String filePath) {
        int widthPix = 300;
        int heightPix = 300;
        FileOutputStream fos = null;
        try {
            if (content == null || "".equals(content)) {
                return false;
            }
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }
            File path = new File(filePath);
            if (!path.exists()) {
                path.createNewFile();
            }
            fos = new FileOutputStream(filePath);
            boolean cr = (bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos));
            return cr;
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        // 获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        // logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }

    //生成二维码图片
    public static void createErCode(final String device, final String path) {
        try {
            String downDir = path.substring(0, path.lastIndexOf("/")).trim();
            Log.i("downDir===", downDir);
            File file = new File(downDir);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                Log.i("create==downDir===", mkdirs + "");
            }
        } catch (Exception e) {
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean qrImage = createQRImage(device, null, path);
                    Log.i("create ER ", "" + qrImage);
                } catch (Exception e) {
                }
            }
        }).start();
    }

    //生成二维码图片
    public static void createErCode(final String device, final String path, final ErCodeListener listener) {
        try {
            FileUtil.creatDirPathNoExists();
            File fileDie = new File(path);
            if (fileDie.exists()) {
                fileDie.delete();
            }
            fileDie.createNewFile();
            String downDir = path.substring(0, path.lastIndexOf("/")).trim();
            Log.i("downDir===", downDir);
            File file = new File(downDir);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                Log.i("create==downDir===", mkdirs + "");
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean qrImage = createQRImage(device, null, path);
                    if (qrImage) {
                        listener.createSuccess(path);
                    } else {
                        listener.createFailed("创建二维码失败");
                    }
                    Log.i("create ER ", "" + qrImage);
                }
            }).start();
        } catch (Exception e) {
            listener.createFailed("创建二维码失败");
        }
    }

    public interface ErCodeListener {
        void createSuccess(String path);

        void createFailed(String error);
    }

}
