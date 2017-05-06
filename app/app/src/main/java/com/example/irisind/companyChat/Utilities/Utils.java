package com.example.irisind.companyChat.Utilities;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by irisind on 22/2/17.
 */

public class Utils {
    private static final String TAG = "HelperClass";
    private static final String ISO8601DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss.SS'Z'";

    public static Date ISODate_to_JavaDateObject(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO8601DATEFORMAT, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static boolean isValidText(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return true;
        }
    }

    public static long DownloadWithDownloadManager(Activity activity, String downloadFileUrl, String fileName) {
        DownloadManager downloadManager;
        long myDownloadReference;
        BroadcastReceiver receiverDownloadComplete;
        BroadcastReceiver receiverNotificationClicked;

        downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(downloadFileUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        String PATH = Environment.getExternalStorageDirectory() + "/Downloads/Beatmap/";
        File folder = new File(PATH);
        if (!folder.exists()) {
            folder.mkdir();//If there is no folder it will be created.
        }
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        String targetFileName = name + extension;
        request.setDestinationUri(Uri.parse(PATH + targetFileName));
        request.setDescription(targetFileName).setTitle("Attachment Download");
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(true);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        myDownloadReference = downloadManager.enqueue(request);
        return myDownloadReference;

    }

    public static class DownloadFileToSdcard extends AsyncTask<String, Integer, Long> {

        private final ProgressDialog mProgressDialog;
        Activity activity;
        String strFolderName, fileName;
        int id = 1;
        private NotificationManager mNotifyManager;
        private NotificationCompat.Builder mBuilder;
        private String extension, name;
        private Uri fileUri;


        public DownloadFileToSdcard(String originalname, String strFolderName, Activity activity) {
            this.strFolderName = strFolderName;
            this.fileName = originalname;
            this.activity = activity;
            mProgressDialog = new ProgressDialog(activity);
        }

        public void scanMediaForChanges(String path, MediaScannerConnection.OnScanCompletedListener listener) {
            MediaScannerConnection.scanFile(activity, new String[]{path}, null, listener);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Downloading");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();


            mNotifyManager =
                    (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(activity);
         /*   mBuilder.setContentTitle("Attatchment Download")
                    .setContentText("Download in progress")
                    .setSmallIcon(android.R.drawable.stat_sys_download);*/
        }

        @Override
        protected Long doInBackground(String... aurl) {
            int count;
            try {
                URL url = new URL((String) aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                extension = fileName.substring(fileName.lastIndexOf("."));
                name = fileName.substring(0, fileName.lastIndexOf("."));

                String targetFileName = name + extension;//Change name and subname
                int lenghtOfFile = conexion.getContentLength();
                String PATH = Environment.getExternalStorageDirectory() + "/Pictures/" + strFolderName + "/";
                File folder = new File(PATH);
                if (!folder.exists()) {
                    folder.mkdir();//If there is no folder it will be created.
                }
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(PATH + targetFileName);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }
                onPostExecute(PATH + targetFileName);
                output.flush();
                output.close();
                input.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            mProgressDialog.setProgress(progress[0]);
            if (mProgressDialog.getProgress() == mProgressDialog.getMax()) {
                mProgressDialog.dismiss();
                Toast.makeText(activity, "Downloaded to sdcard/Pictures/" + strFolderName, Toast.LENGTH_SHORT).show();
            }

            /*mBuilder.setProgress(100, progress[0], false);
            mNotifyManager.notify(id, mBuilder.build());*/

        }

        protected void onPostExecute(final String result) {

            fileUri = Uri.parse(result);

            scanMediaForChanges(result, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    Log.e(TAG,"scan completed uri = "+uri);
                    mBuilder.setContentText("Downloaded to sdcard/Pictures/" + strFolderName).setContentTitle("Attachment Download")
                            // Removes the progress bar
                            .setProgress(0, 0, false).setSmallIcon(android.R.drawable.stat_sys_download_done);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String ext = MimeTypeMap.getFileExtensionFromUrl(result);
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
                    Log.e(TAG, "mimetype = " + mimeType);
                    intent.setDataAndTypeAndNormalize(uri, mimeType);
                    PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    mBuilder.addAction(android.R.drawable.stat_sys_download_done, "Open File", contentIntent);
                    final Notification notification = mBuilder.build();
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    mNotifyManager.notify(id, notification);
                }
            });


        }
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }


    public static String getSelectedImagePath(Activity activity,Intent data){
        String selectedImagePath = null;
        try {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};


            CursorLoader cursorLoader = new CursorLoader(activity, selectedImage, filePathColumn, null, null,
                    null);
            // Get the cursor
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                selectedImagePath = cursor.getString(column_index);
            } else {
                selectedImagePath = null;
            }
            if (selectedImagePath == null) {
                //2:OI FILE Manager --- call method: uri.getPath()
                selectedImagePath = selectedImage.getPath();
            }
            cursor.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return selectedImagePath;
    }


}
