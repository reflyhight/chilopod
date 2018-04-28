package cn.dtvalley.chilopod.core.instance;

import lombok.Data;

@Data
public class TaskStartParam {
    private String ip;
    private String port;
    private String taskName;
    private String type;
}
