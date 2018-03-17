package cn.dtvalley.chilopod.master.service;


import cn.dtvalley.chilopod.core.instance.ClientRequest;

public interface MasterService {
    void register(ClientRequest instanceInfo);
}
