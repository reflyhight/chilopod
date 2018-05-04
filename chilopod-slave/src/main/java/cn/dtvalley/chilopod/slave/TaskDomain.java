package cn.dtvalley.chilopod.slave;

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

    private String ip;

//    private String
}
