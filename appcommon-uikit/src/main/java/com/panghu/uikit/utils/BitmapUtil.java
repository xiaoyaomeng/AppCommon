package com.panghu.uikit.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSIllegalArgumentException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

public class BitmapUtil {

    private final static String TAG = "BitmapUtil";
    private static final double MEGA_BYTES = 1024 * 1024.0;
    private static final double REQ_RESOLUTION = 1024 * 1024.0;

    public static Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Throwable throwable) {
            bitmap = null;
            Log.e(TAG, "Error in createBitmap");
        }
        return bitmap;
    }

    public static Bitmap croppedBitmap2Circle(Bitmap bitmap, int targetSize) {

        Bitmap output = Bitmap.createBitmap(targetSize,
                targetSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final Rect dstRect = new Rect(0, 0, targetSize, targetSize);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(targetSize / 2, targetSize / 2, targetSize / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, dstRect, paint);

        return output;
    }

    /**
     * Get the max size of bitmap allowed to be rendered on the device.<br>
     * http://stackoverflow.com/questions/7428996/hw-accelerated-activity-how-to-get-opengl-texture-size-limit.
     */
    public static int getMaxTextureSize() {
        // Safe minimum default size
        final int IMAGE_MAX_BITMAP_DIMENSION = 2048;

        try {
            // Get EGL Display
            EGL10 egl = (EGL10) EGLContext.getEGL();
            EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

            // Initialise
            int[] version = new int[2];
            egl.eglInitialize(display, version);

            // Query total number of configurations
            int[] totalConfigurations = new int[1];
            egl.eglGetConfigs(display, null, 0, totalConfigurations);

            // Query actual list configurations
            EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
            egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

            int[] textureSize = new int[1];
            int maximumTextureSize = 0;

            // Iterate through all the configurations to located the maximum texture size
            for (int i = 0; i < totalConfigurations[0]; i++) {
                // Only need to check for width since opengl textures are always squared
                egl.eglGetConfigAttrib(display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize);

                // Keep track of the maximum texture size
                if (maximumTextureSize < textureSize[0]) {
                    maximumTextureSize = textureSize[0];
                }
            }

            // Release
            egl.eglTerminate(display);

            // Return largest texture size found, or default
            return Math.max(maximumTextureSize, IMAGE_MAX_BITMAP_DIMENSION);
        } catch (Exception e) {
            return IMAGE_MAX_BITMAP_DIMENSION;
        }
    }

    private static int getQuality(double originSize, double maxSize) {
        if (originSize < maxSize) {
            return 100;
        }
        double quality = Math.round(originSize / maxSize);
        if (quality > 50) {
            return 10;
        } else if (quality > 25) {
            return 20;
        } else if (quality > 15) {
            return 50;
        } else if (quality > 10) {
            return 60;
        } else if (quality > 6) {
            return 70;
        } else if (quality > 3) {
            return 80;
        } else {
            return 90;
        }
    }

    private static InputStream getInputStreamFromUri(Context context,  Uri sourceUri) throws Exception{
        return context.getContentResolver().openInputStream(sourceUri);
    }

    public static long compressBitmapToDestPath(Context context,  Uri sourceUri, String toPath, long originSize, long maxSize) throws Exception {
        Log.v(TAG, "originSize : " + (originSize / MEGA_BYTES) + "MB maxSize : " + (maxSize / MEGA_BYTES) + "MB");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream inputStream = getInputStreamFromUri(context, sourceUri);
        BitmapFactory.decodeStream(inputStream,null, options);
        inputStream.close();
        options.inSampleSize = calculateInSampleSize(options);
        options.inJustDecodeBounds = false;
        FileDescriptor fileDescriptor = context.getContentResolver()
                .openFileDescriptor(sourceUri,"r")
                .getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor,null, options);
        int degree = getPicRotate(getInputStreamFromUri(context, sourceUri));
        if (degree != 0) {
            Matrix m = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            m.setRotate(degree);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // Store the bitmap into output stream(no compress)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        // Compress by loop
        long size = os.toByteArray().length;
        int quality = getQuality(size / MEGA_BYTES, maxSize / MEGA_BYTES);
        int compressTimes = 1;
        while (size > maxSize) {
            // Clean up os
            os.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            size = os.toByteArray().length;
            compressTimes++;
            if (size / maxSize > 2) {
                quality -= 20;
            } else {
                quality -= 10;
            }
            if (quality < 0) {
                quality = 0;
            }
        }
        Log.v(TAG, "compress times : " + compressTimes + " finalSize : " + (size / MEGA_BYTES) + "MB quality :" + quality);
        File file = new File(toPath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!file.exists() && file.createNewFile()) {
            // Generate compressed image file
            FileOutputStream fos = new FileOutputStream(toPath);
            fos.write(os.toByteArray());
            fos.flush();
            fos.close();
        }
        bitmap.recycle();
        return size;
    }

    private static int getPicRotate(InputStream inputStream) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(inputStream);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException ignored) {
            }
        }
        return degree;
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options) {
        // Raw height and width of image
        int inSampleSize = 1;
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        long outResolution = outHeight * outWidth;
        if (outResolution > REQ_RESOLUTION) {
            inSampleSize = (int) Math.sqrt(outResolution / REQ_RESOLUTION);
        }
        return inSampleSize;
    }

    public static @Nullable Bitmap getLauncherIcon(@NonNull Context context, int resId) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            Drawable drawable = context.getDrawable(resId);
            if (drawable != null) {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
            }
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        }

        return bitmap;
    }

    public @NonNull static Bitmap blur(Context context, Bitmap source, int radius, float scale){
        if (radius <= 0 || radius > 25) {
            Log.w(TAG, "Radius out of range (0 < r <= 25).");
            if (radius <=0) {
                return source;
            } else {
                radius = 25;
            }
        }
        int width = Math.round(source.getWidth() * scale);
        int height = Math.round(source.getHeight() * scale);

        Bitmap inputBmp = Bitmap.createScaledBitmap(source, width, height, false);
        RenderScript renderScript = RenderScript.create(context);

        // Allocate memory for Renderscript to work with
        final Allocation input = Allocation.createFromBitmap(renderScript, inputBmp);
        final Allocation output = Allocation.createTyped(renderScript, input.getType());

        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur scriptIntrinsicBlur =
                ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        scriptIntrinsicBlur.setInput(input);

         // Set the blur radius
        scriptIntrinsicBlur.setRadius(radius);

        // Start the ScriptIntrinisicBlur
        scriptIntrinsicBlur.forEach(output);

        // Copy the output to the blurred bitmap
        output.copyTo(inputBmp);

        renderScript.destroy();
        return inputBmp;
    }

    /**
     * Creates a bitmap from the supplied view.
     * @param src The view to get the bitmap.
     * @param scale The scale for the bitmap.
     * @return The bitmap from the supplied drawable.
     */
    public @NonNull static Bitmap createBitmapFromView(@NonNull View src, float scale) {
        Bitmap bitmap = Bitmap.createBitmap(
                (int) (src.getWidth() * scale),
                (int) (src.getHeight() * scale),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(bitmap);
        Matrix matrix = new Matrix();
        matrix.preScale(scale, scale);
        canvas.setMatrix(matrix);
        src.draw(canvas);

        return bitmap;
    }

    public static int convertDpToPixels(float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, Resources.getSystem().getDisplayMetrics()));
    }
}
