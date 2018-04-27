package cn.dtvalley.chilopod.core.instance;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class ClientRequest {
    private String ip;
    private int port;
    private String name;
    private String status;
    private LocalDateTime startTime;
    private Collection<SlaveTask> tasks;
}
