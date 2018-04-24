package cn.dtvalley.chilopod.master.service;


import cn.dtvalley.chilopod.core.instance.ClientRequest;
import cn.dtvalley.chilopod.core.instance.InstanceInfo;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collection;

public interface MasterService {
    void register(ClientRequest instanceInfo);

    Collection<InstanceInfo.Instance> list();

    void dispense() throws IOException, ServletException;

}
