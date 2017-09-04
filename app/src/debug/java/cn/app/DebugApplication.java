package cn.app;

import com.facebook.stetho.Stetho;

/**
 * Created by li.qing
 * on 2017/8/24.
 */

public class DebugApplication extends EApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        //在chrome上查看realm数据
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
