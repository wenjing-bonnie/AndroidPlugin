package com.wj.manifest.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import com.wj.manifest.SystemPrint

/**
 * Created by wenjing.liu on 2021/9/17 in J1.
 * 读取配置文件(含有对每个版本的信息描述)中的versionCode与versionName
 * 然后在加上360的自动打包流程
 * 用来复写下Extension的定义 以及@InputFile
 *
 *  调用 clean 以后第一次编译的过程，这个就是全量编译，
 *  之后修改了代码或者资源文件，再次编译，就是增量编译。
 * @author wenjing.liu
 */
class SetLastVersionInfoTask extends DefaultTask {
    protected static final String TAG = "SetLastVersionInfoTask"

    @TaskAction
    void doTaskAction() {
        SystemPrint.outPrintln("Running ...")
    }


/**  IncrementalTask  //全量的时候执行操作
 @Override
  protected void doFullTaskAction() throws Exception {}//增量的时候执行操作
 @Override
  protected void doIncrementalTaskAction(@NotNull Map<File, ? extends FileStatus> changedInputs) throws Exception {super.doIncrementalTaskAction(changedInputs)}

 @Override
  Property<AnalyticsService>            getAnalyticsService() {return null}

 @Override
  WorkerExecutor getWorkerExecutor() {return null}

 @Override
  protected boolean getIncremental() {//return true 支持增量; false:不支持增量
  //MergeResources：增量任务过程和全量其实差异不大，只不过是在获取 resourceSets 的时候，使用的是修改后的文件
  return super.getIncremental()} */
}