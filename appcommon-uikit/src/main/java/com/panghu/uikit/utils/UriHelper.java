package com.panghu.uikit.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import android.webkit.MimeTypeMap;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class UriHelper {
    private static final String TAG = "UriHelper";
    private static final String PRIMARY = "primary";
    private static final String IMAGE = "image";
    private static final String VIDEO = "video";
    private static final String AUDIO = "audio";
    private static final String DOWNLOAD_PROVIDER_AUTHORITY = "com.android.providers.downloads.documents";
    private static final String MEDIA_PROVIDER_AUTHORITY = "com.android.providers.media.documents";
    private static final String EXTERNAL_PROVIDER_AUTHORITY = "com.android.externalstorage.documents";
    private static final Uri ALL_DOWNLOADS_CONTENT_URI = Uri.parse("content://downloads/all_downloads");

    public static String getMimeTypeByPath(String url) {
        String extension = getFileExtension(url);
        return getMimeTypeByExt(extension);
    }

    public static String getMimeTypeByExt(String ext) {
        if (ext != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return null;
    }

    private static String getFileExtension(String url) {
        return MimeTypeMap.getFileExtensionFromUrl(url);
    }

    public static boolean isImageFile(String mimeType) {
        return mimeType != null && mimeType.indexOf(IMAGE) == 0;
    }

    public static void getImageSize(String filePath, Point point) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        point.y = options.outHeight;
        point.x = options.outWidth;
    }

    public static void getImageSize(Context context, Uri uri, Point point) throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try (InputStream fileInputStream = context.getContentResolver().openInputStream(uri)) {
            BitmapFactory.decodeStream(fileInputStream, null, options);
        } catch (Throwable th) {
            Log.e(TAG, "get image size error", th);
            throw new FileNotFoundException(th.toString());
        }

        point.y = options.outHeight;
        point.x = options.outWidth;
    }

    public static String getPath(final Context context, final Uri uri) {
        //final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if (PRIMARY.equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                if (split.length > 0) {
                    try {
                        long id = Long.valueOf(split[0]);
                        final Uri contentUri = ContentUris.withAppendedId(ALL_DOWNLOADS_CONTENT_URI, id);
                        return getDataColumn(context, contentUri, null, null);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Error in querying uri: " + uri, e);
                    }
                }
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if (IMAGE.equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if (VIDEO.equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if (AUDIO.equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = BaseColumns._ID + "=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
        }
        // MediaStore (and general)
        else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        final String column = MediaStore.MediaColumns.DATA;
        final String[] projection = {column};
        try (Cursor cursor =
                context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        } catch (Throwable th) {
            Log.e(TAG, "get data column error", th);
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return EXTERNAL_PROVIDER_AUTHORITY.equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return DOWNLOAD_PROVIDER_AUTHORITY.equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return MEDIA_PROVIDER_AUTHORITY.equals(uri.getAuthority());
    }

    @NonNull
    public static String getFilename(@NonNull String url) {
        if (!url.contains("/")) {
            return "";
        } else {
            String[] paths = url.split("/");
            return paths[paths.length - 1];
        }
    }
}
