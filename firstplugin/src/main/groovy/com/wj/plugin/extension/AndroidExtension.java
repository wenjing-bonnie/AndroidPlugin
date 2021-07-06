package com.wj.plugin.extension;


import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;

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
    private NamedDomainObjectContainer<BuildTypes> buildTypes;

    public AndroidExtension(Project project) {
        buildTypes = project.container(BuildTypes.class);
    }

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
    public void defaultConfig(Action<DefaultConfig> action) {
        action.execute(defaultConfig);
    }

    //    /**
//     * @param config 在build.gradle文件
//     *               defaultConfig {
//     *               applicationId = "1.0.0"
//     *               minSdkVersion = "3.0.0"
//     *               }
//     */
//    public void setDefaultConfig(Closure config) {
//        ConfigureUtil.configure(config, defaultConfig);
//    }
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    public void buildTypes(Action<NamedDomainObjectContainer<BuildTypes>> action) {
        action.execute(buildTypes);
    }

    public NamedDomainObjectContainer<BuildTypes> getBuildTypes() {
        return buildTypes;
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

    /**
     * 必须为static类，否则会抛出"Class AndroidExtension.BuildTypes is a non-static inner class."
     */
    static class BuildTypes {
        private boolean signingConfig;
        //必须含有name属性，否则会抛出"'com.wj.plugin.extension.AndroidExtension$BuildTypes@130b3f7d' because it does not have a 'name' property"
        private String name;

        BuildTypes(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setSigningConfig(boolean config) {
            this.signingConfig = config;
        }

        public boolean getSigningConfig() {
            return this.signingConfig;
        }
    }
}
