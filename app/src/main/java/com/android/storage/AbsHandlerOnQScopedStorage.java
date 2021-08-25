package com.android.storage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenjing.liu on 2021/8/24 .
 * <p>
 * 适配每个多媒体文件夹的Uri和File的获取
 * <p>
 * 难点在于怎么找到该文件的Uri,因为看到的文件的名字并不一定是保存到数据库的displayName
 *
 * @author wenjing.liu
 */
public abstract class AbsHandlerOnQScopedStorage implements IDifferenceTypeOnQScopedStorage {

    /**
     * 当{@link Environment#getExternalStorageState()}是{@link Environment#MEDIA_MOUNTED}的时候返回external content uri
     *
     * @return
     */
    public abstract Uri getExternalContentUri();

    /**
     * 当{@link Environment#getExternalStorageState()}不是{@link Environment#MEDIA_MOUNTED}的时候返回internal content uri
     *
     * @return
     */
    public abstract Uri getInternalContentUri();

    /**
     * The type of storage directory to return.Should be one of
     * {@link Environment#DIRECTORY_MUSIC}, {@link Environment#DIRECTORY_PODCASTS},
     * {@link Environment#DIRECTORY_RINGTONES}, {@link Environment#DIRECTORY_ALARMS},
     * {@link Environment#DIRECTORY_NOTIFICATIONS}, {@link Environment#DIRECTORY_PICTURES},
     * {@link Environment#DIRECTORY_MOVIES}, {@link Environment#DIRECTORY_DOWNLOADS},
     * {@link Environment#DIRECTORY_DCIM}, or {@link Environment#DIRECTORY_DOCUMENTS}. May not be null.
     */
    public abstract String getExternalStoragePublicDirectoryType();

    /**
     * 适配Android Q及以上将文件写入到对应的文件夹
     *
     * @param context
     * @param fileName
     * @param content
     */
    @Override
    public void writeAndAppend(Context context, String fileName, byte[] content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeAndAppendAboveQWithoutWritePermission(context, fileName, content);
        } else {
            writeAndAppendByFile(fileName, content);
        }
    }

    @Override
    public Object read(Context context, String fileName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return readAboveQWithoutReadPermission(context, fileName);
        }
        return null;
    }

//    private Object readByFile(String fileName) {
//        File srcFile = getSrcFile(fileName);
//        return readInputStream(new FileInputStream(srcFile));
//    }


    /**
     * 适配AndroidQ及以上版本进行读文件
     *
     * @param context
     * @param fileName
     * @return
     */
    private Object readAboveQWithoutReadPermission(Context context, String fileName) {
        InputStream is = null;
        AndroidQFileInfo info = getUriByDisplayName(context, fileName);
        if (info == null) {
            return null;
        }
        Uri uri = info.uri;
        if (uri == null) {
            return null;
        }
        try {
            is = context.getContentResolver().openInputStream(uri);
            if (is == null) {
                return null;
            }
            //文本类型
            if (info.mineType.startsWith("text")) {
                return readForTextAboveQWithoutReadPermission(is);
            } else if (info.mineType.startsWith("image")) {
                return readForImageAboveQWithoutReadPermission(is);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
            }
        }

        return null;
    }

    /**
     * 读取文本类型的文档内容
     *
     * @param is
     * @return
     */
    private String readForTextAboveQWithoutReadPermission(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer buffer = new StringBuffer();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (IOException e) {

        }
        return null;
    }

    /**
     * 读取图片文件
     *
     * @param is
     * @return
     */
    private Bitmap readForImageAboveQWithoutReadPermission(InputStream is) {
        return BitmapFactory.decodeStream(is);
    }


    /**
     * 低于Android Q以下往Download文件中写文件
     * 在AndroidQ中也可以使用这种方式,但是如果关闭了存储权限,无法写成功, 要申请写权限
     *
     * @param fileName
     * @param content
     */
    private synchronized void writeAndAppendByFile(String fileName, byte[] content) {
        try {
            File srcFile = getSrcFile(fileName);
            writeOutputStreamOfUtf8(new FileOutputStream(srcFile, true), content);
        } catch (FileNotFoundException e) {
        }
    }

    /**
     * Android Q及以上往Download文件写文件
     * 无需写权限就可以实现写文件
     *
     * @param context
     * @param content
     * @param fileName
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private synchronized void writeAndAppendAboveQWithoutWritePermission(Context context, String fileName, byte[] content) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = getUriByDisplayName(context, fileName).uri;
        try {
            writeOutputStreamOfUtf8(resolver.openOutputStream(uri, "wa"), content);
        } catch (FileNotFoundException e) {
        } catch (RecoverableSecurityException e) {
            //修改其他应用创建的多媒体文件会抛出该异常
            handlerRecoverableSecurityException(context, e);
        }
    }

    /**
     * 弹框提示用户是否允许修改或删除此文件.
     * 用户操作的结果,将通过onActivityResult回调返回到APP.如果用户允许,APP将获得该Uri 的修改权限,直到设备下一次重启.
     *
     * @param context
     * @param e
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private void handlerRecoverableSecurityException(Context context, RecoverableSecurityException e) {
        try {
            ((Activity) context).startIntentSenderForResult(e.getUserAction().getActionIntent().getIntentSender(), 100, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException sendIntentException) {
            sendIntentException.printStackTrace();
        }
    }

    /**
     * 往OutputStream写入内容
     *
     * @param fos
     * @param content
     */
    private synchronized void writeOutputStreamOfUtf8(OutputStream fos, byte[] content) {
        try {
            fos.write(content);
        } catch (Exception e) {
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 从InputStream中读取文件内容
     *
     * @param is
     * @return
     */
    private synchronized Object readInputStream(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer buffer = new StringBuffer();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (IOException e) {

        }
        return null;
    }

    /**
     * 根据fileName获取对应的多媒体文件的File
     *
     * @param fileName
     * @return
     */
    private File getSrcFile(String fileName) {
        return new File(Environment.getExternalStoragePublicDirectory(getExternalStoragePublicDirectoryType()), fileName);
    }

    /**
     * 根据外部存储的状态返回对应的Uri
     *
     * @return
     */
    private Uri getContentUriByStorageState() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return getExternalContentUri();
        }
        return getInternalContentUri();
    }

    /**
     * 根据fileName获取AndroidQ及以上的Uri
     *
     * @param context
     * @param fileName
     * @return
     */
    @SuppressLint("Range")
    private AndroidQFileInfo getUriByDisplayName(Context context, String fileName) {
        Uri queryUri = null;
        AndroidQFileInfo fileInfo = new AndroidQFileInfo();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return null;
        }
        Uri uri = getContentUriByStorageState();

        String selection = MediaStore.MediaColumns.DISPLAY_NAME + "=?";
        String[] args = new String[]{fileName};
        String[] projection = new String[]{MediaStore.MediaColumns._ID, MediaStore.MediaColumns.MIME_TYPE};
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, args, null);
        if (cursor == null) {
            return null;
        }
        if (cursor.getCount() == 0) {
            //之前没有保存过该文件,则执行插入操作
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            //TODO 类型暂时不去设置,目前测试下来发现系统会自动设置类型
            queryUri = context.getContentResolver().insert(uri, values);
            fileInfo.uri = queryUri;
            return fileInfo;
        }
        //返回第一条数据的uri
        if (cursor.moveToFirst()) {
            queryUri = ContentUris.withAppendedId(uri, cursor.getLong(0));
            fileInfo.uri = queryUri;
            fileInfo.mineType = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
            return fileInfo;
        }

        return fileInfo;
    }

    public List<AndroidQFileInfo> getAllUris(Context context) {
        List<AndroidQFileInfo> infos = new ArrayList<>();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return null;
        }
        Uri uri = getContentUriByStorageState();

        String[] projection = new String[]{MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.RELATIVE_PATH};
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        while (cursor.moveToNext()) {
            AndroidQFileInfo fileInfo = new AndroidQFileInfo();
//            fileInfo.uri = ContentUris.withAppendedId(uri, cursor.getLong(0));
//            fileInfo.displayName = cursor.getString(1);
//            fileInfo.mineType = cursor.getString(2);
            infos.add(fileInfo);
            //Log.d(" displayName = " + fileInfo.displayName + " , " + fileInfo.mineType + " " + cursor.getString(3));
        }

        return infos;
    }

    /**
     * 暂时不用每个子类分别去设置mineType,还是借用系统保存文件的时候设置的mineType
     */
    protected class AndroidQFileInfo {
        protected String mineType;
        protected Uri uri;
        protected String displayName;
    }
}
