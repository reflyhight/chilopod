package cn.dtvalley.chilopod.core.instance;

import cn.dtvalley.chilopod.core.SlaveRun;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SlaveTask {
    private String name;
    private long startTime;
    private long createTime;
    private Status status;
    private String path;
    @JsonIgnore
    private SlaveRun runObject;

    public enum Status {
        SLEEP, RUNNING, ERROR
    }
}
