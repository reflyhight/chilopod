package cn.dtvalley.chilopod.slave.dao;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Data
public class TaskDomain {

    @Id
    private String id;

    private String name;

    private String instanceName;

    private String ip;

    private String status;

    private String mainClass;

    private String path;
}
