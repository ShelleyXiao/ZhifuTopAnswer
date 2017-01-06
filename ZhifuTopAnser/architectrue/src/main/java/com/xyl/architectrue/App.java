package com.xyl.architectrue;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.xyl.architectrue.utils.LogUtils;
import com.xyl.architectrue.utils.Utils;

/**
 * User: ShaudXiao
 * Date: 2017-01-03
 * Time: 15:32
 * Company: zx
 * Description:
 * FIXME
 */


public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
        Utils.init(getApplicationContext());
        LogUtils.init(true, false, 'e', "Zhifu");
    }
}
