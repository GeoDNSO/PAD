package es.ucm.fdi.tieryourlikes.utilities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.R;

public class MediaManager {

    // https://stackoverflow.com/questions/56904485/how-to-save-an-image-in-android-q-using-mediastore
    @NonNull
    public static Uri saveBitmap(@NonNull final Context context, @NonNull final Bitmap bitmap,
                          @NonNull final Bitmap.CompressFormat format,
                          @NonNull final String mimeType,
                          @NonNull final String displayName) throws IOException {

        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);

        final ContentResolver resolver = context.getContentResolver();
        Uri uri = null;

        try {
            final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri = resolver.insert(contentUri, values);

            if (uri == null)
                throw new IOException("Failed to create new MediaStore record.");

            try (final OutputStream stream = resolver.openOutputStream(uri)) {
                if (stream == null)
                    throw new IOException("Failed to open output stream.");

                if (!bitmap.compress(format, 95, stream))
                    throw new IOException("Failed to save bitmap.");
            }

            return uri;
        }
        catch (IOException e) {

            if (uri != null) {
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(uri, null, null);
            }

            throw e;
        }
    }


    // https://stackoverflow.com/questions/60798804/store-image-via-android-media-store-in-new-folder
    public static void saveImage(Bitmap bitmap, Context context, String folderName) throws FileNotFoundException {
        if (Build.VERSION.SDK_INT >= 29) {
            ContentValues values = contentValues();
            values.put("relative_path", "Pictures/" + folderName);
            values.put("is_pending", true);
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                saveImageToStream(bitmap, context.getContentResolver().openOutputStream(uri));
                values.put("is_pending", false);
                context.getContentResolver().update(uri, values, (String)null, (String[])null);
            }
        } else {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + File.separator + folderName);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = System.currentTimeMillis() + ".png";
            File file = new File(directory, fileName);
            saveImageToStream(bitmap, (OutputStream)(new FileOutputStream(file)));
            if (file.getAbsolutePath() != null) {
                ContentValues values = contentValues();
                values.put("_data", file.getAbsolutePath());
                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }
        }

    }

    private static ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put("mime_type", "image/png");
        values.put("date_added", System.currentTimeMillis() / (long)1000);
        values.put("datetaken", System.currentTimeMillis());
        return values;
    }

    private static void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

    }


    public static void createImageChooser(Activity activity, boolean allowMultipleSelect){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultipleSelect); //allowMultipleSelect --> true más de una imagen a seleccionar
        intent.setAction(Intent.ACTION_GET_CONTENT);
        String msg = (allowMultipleSelect) ? activity.getString(R.string.select_images) : activity.getString(R.string.select_image);
        int requestCode = (allowMultipleSelect) ? AppConstants.INSERT_IMAGES_RC_IMAGES : AppConstants.INSERT_IMAGES_RC;
        activity.startActivityForResult(Intent.createChooser(intent, msg), requestCode);
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        Bitmap bitmapConvert = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapConvert.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }
}
