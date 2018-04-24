package cn.dtvalley.chilopod.slave.register;

import cn.dtvalley.chilopod.core.common.utils.NetUtil;
import cn.dtvalley.chilopod.core.instance.ClientRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.*;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class RegisterListener implements ApplicationListener<ApplicationStartedEvent> {

    private final RegisterConfiguration registerConfiguration;
    @Resource
    private RestTemplate restTemplate;


    @Autowired
    public RegisterListener(RegisterConfiguration registerConfiguration) {
        this.registerConfiguration = registerConfiguration;
    }


    @Override
    public void onApplicationEvent(@Nullable ApplicationStartedEvent event) {
        try {
            String ip = NetUtil.getIpAddr();
            List<String> serverIps = registerConfiguration.getIp();
            log.info(ip);
            serverIps.forEach(it -> {
                try {
                    URI url = new URI("http://" + it + "/register");
                    ClientRequest request = new ClientRequest();
                    request.setIp(ip);
//                    ResponseEntity responseEntity = restTemplate.postForEntity(url, request, String.class);
//                    if (responseEntity.getStatusCode() == HttpStatus.OK)
                        return;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            });
        } catch (UnknownHostException e) {
            log.error("无法获取本地ip", e);
        }
    }
}
