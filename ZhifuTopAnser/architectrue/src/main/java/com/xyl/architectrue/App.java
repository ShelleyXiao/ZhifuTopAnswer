package com.xyl.architectrue;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

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
    }
}
