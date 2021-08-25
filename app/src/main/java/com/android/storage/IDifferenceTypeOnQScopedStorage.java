package com.android.storage;

import android.content.Context;

/**
 * Created by wenjing.liu on 2021/8/24 .
 *
 * @author wenjing.liu
 */
public interface IDifferenceTypeOnQScopedStorage {

    void writeAndAppend(Context context, String fileName, byte[] content);

    Object read(Context context,String fileName);
}
