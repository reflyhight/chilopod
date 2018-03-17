package cn.dtvalley.chilopod.master.service.impl;

import cn.dtvalley.chilopod.core.instance.ClientRequest;
import cn.dtvalley.chilopod.core.instance.InstanceInfo;
import cn.dtvalley.chilopod.master.service.MasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MasterServiceImpl implements MasterService {

    private InstanceInfo instanceInfo = InstanceInfo.getInstance();

    @Override
    public void register(ClientRequest clientRequest) {
        log.info("客户端注册");
        instanceInfo.addInstance(clientRequest.getName(), clientRequest.getIp());
    }
}
