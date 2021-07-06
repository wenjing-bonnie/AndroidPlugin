package com.wj.plugin.transform;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;

import java.util.Set;

/**
 * Created by wenjing.liu on 2021/7/6 in J1.
 * 学习有class到dex的Transform过程
 *
 * @author wenjing.liu
 */
public class HotTransform extends Transform {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return null;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return null;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }
}
