package com.xxl.job.service.handler;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.util.HTTPSClientUtil;

public class DemoGlueJobHandler extends IJobHandler {
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        //获取message对应的值
        String result = HTTPSClientUtil.insureResponsePost(param);

        return ReturnT.SUCCESS;

    }


}
