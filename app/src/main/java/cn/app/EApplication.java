package cn.app;


import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import lib.EConf;
import lib.net.EHttp;
import lib.utils.Utils;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * 应用,主要用来做一下初始化的操作
 */

public class EApplication extends Application {
    private static Context mContext;
    private static Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        EHttp.setmContext(getmContext());
        initLogger();
        initRealm();
        Utils.init(this);
    }

    /**
     * 初始化Logger打印工具
     */
    private void initLogger(){
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    /**
     * 初始化并配置Realm
     */
    private void initRealm(){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(EConf.DB_NAME)//指定数据库的名称。如不指定默认名为default
                //.encryptionKey(getKey()) //指定数据库的密钥
                .schemaVersion(1) // 版本号
                //.modules(new MySchemaModule())
                //.migration(new MyMigration())//指定迁移操作的迁移类
                //.inMemory() //声明数据库只在内存中持久化
                .deleteRealmIfMigrationNeeded() //声明版本冲突时自动删除原数据库。
                .build();
        // Use the config
        realm = Realm.getInstance(config);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
        /*RealmInspectorModulesProvider.builder(this)
                .withFolder(getCacheDir())
                //.withEncryptionKey("encrypted.realm", key)
                .withMetaTables()
                .withDescendingOrder()
                .withLimit(1000)
                .databaseNamePattern(Pattern.compile(".+\\.realm"))
                .build();*/
    }
    /**
     * @return
     * 全局的上下文
     */
    public static Context getmContext() {
        return mContext;
    }

    public static Realm getRealm() {
        return realm;
    }


}
