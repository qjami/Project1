package cn.app.model;

/**
 * Created by Q
 * on 2017/8/14.
 * 要使用Realm 可以实现RealmModel接口，或者直接继承于 RealmObject
 */

public class LoginBean extends BaseBean{
    private LoginData result;

    public LoginData getResult() {
        return result;
    }

    public void setResult(LoginData result) {
        this.result = result;
    }

}
