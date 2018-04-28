package cn.dtvalley.chilopod.slave;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Instance {
    private String ip;
    private LocalDateTime registerTime;
    private LocalDateTime updateTime;
    private List<String> masterIp;//注册master ip
}
