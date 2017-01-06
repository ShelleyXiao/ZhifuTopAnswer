package com.xyl.architectrue.okHttp.builder;

import com.xyl.architectrue.okHttp.OkHttpUtils;
import com.xyl.architectrue.okHttp.request.OtherRequest;
import com.xyl.architectrue.okHttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
