package com.wj.plugin.extension;


import org.gradle.api.Action;
import org.gradle.util.ConfigureUtil;

import groovy.lang.Closure;

/**
 * Created by wenjing.liu on 2021/6/25 in J1.
 * <p>
 * 仿android{}中的多层闭包的Extension
 *
 * @author wenjing.liu
 */
public class AndroidExtension {
    public static final String TAG = "androidExtension";
    private String compileSdkVersion;
    private String buildToolsVersion;
    private DefaultConfig defaultConfig = new DefaultConfig();

    public void setCompileSdkVersion(String version) {
        this.compileSdkVersion = version;
    }

    public String getCompileSdkVersion() {
        return this.compileSdkVersion;
    }

    public void setBuildToolsVersion(String version) {
        this.buildToolsVersion = version;
    }

    public String getBuildToolsVersion() {
        return this.buildToolsVersion;
    }

    /**
     * @param action 在build.gradle文件
     *               defaultConfig{
     *               it.applicationId = "1.0.0"
     *               }
     */
//    public void setDefaultConfig(Action<DefaultConfig> action) {
//        action.execute(defaultConfig);
//    }

    /**
     * @param config 在build.gradle文件
     *               defaultConfig {
     *               applicationId = "1.0.0"
     *               minSdkVersion = "3.0.0"
     *               }
     */
    public void setDefaultConfig(Closure config) {
        ConfigureUtil.configure(config, defaultConfig);
    }

    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    class DefaultConfig {
        private String applicationId;
        private String minSdkVersion;

        public void setApplicationId(String id) {
            this.applicationId = id;
        }

        public String getApplicationId() {
            return this.applicationId;
        }

        public void setMinSdkVersion(String sdk) {
            this.minSdkVersion = sdk;
        }

        public String getMinSdkVersion() {
            return this.minSdkVersion;
        }
    }
}
