package cn.dtvalley.chilopod.master;

import cn.dtvalley.chilopod.core.ChilopodContext;
import cn.dtvalley.chilopod.core.Environment;
import cn.dtvalley.chilopod.core.instance.InstanceInfo;
import org.springframework.stereotype.Component;

@Component
public class MasterChilopodContext implements ChilopodContext {

    private InstanceInfo instanceInfo;//服务注册信息

    @Override
    public Environment getEnvironment() {
        return Environment.MASTER;
    }
}
