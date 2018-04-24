package cn.dtvalley.chilopod.master.service.impl;

import cn.dtvalley.chilopod.core.instance.ClientRequest;
import cn.dtvalley.chilopod.core.instance.InstanceInfo;
import cn.dtvalley.chilopod.master.service.MasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Service
@Slf4j
public class MasterServiceImpl implements MasterService {

    @Autowired
    private RestTemplate restTemplate;

    private InstanceInfo instanceInfo = InstanceInfo.instanceInfo;

    @Override
    public void register(ClientRequest clientRequest) {
        log.info("客户端注册");
        instanceInfo.addInstance(clientRequest.getIp());
    }

    @Override
    public Collection<InstanceInfo.Instance> list() {
        Map<String, InstanceInfo.Instance> map = instanceInfo.getInstance();
        return map.values();
    }

    @Override
    public void dispense() throws IOException, ServletException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add(request.getPart("file").getSubmittedFileName() , new InputStreamResource(request.getPart("file").getInputStream()));
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(form, headers);
        restTemplate.postForEntity("http://localhost:8080/slave/jar", entity, String.class);
    }

}
