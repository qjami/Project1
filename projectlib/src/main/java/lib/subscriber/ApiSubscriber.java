package lib.subscriber;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;

import java.net.UnknownHostException;

import lib.mvp.IModel;
import io.reactivex.subscribers.ResourceSubscriber;


/**
 * Created by li.qing
 * on 2016/12/26.
 */

public abstract class ApiSubscriber<T extends IModel> extends ResourceSubscriber<T> {


    @Override
    public void onError(Throwable e) {
        NetError error = null;
        if (e != null) {
            if (!(e instanceof NetError)) {
                if (e instanceof UnknownHostException) {
                    error = new NetError(e, NetError.NoConnectError);
                } else if (e instanceof JSONException
                        || e instanceof JsonParseException
                        || e instanceof JsonSyntaxException) {
                    error = new NetError(e, NetError.ParseError);
                } else {
                    error = new NetError(e, NetError.OtherError);
                }
            } else {
                error = (NetError) e;
            }
            onFail(error);
        }

    }

    protected abstract void onFail(NetError error);

    @Override
    public void onComplete() {

    }


}
