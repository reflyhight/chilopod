package cn.dtvalley.chilopod.master.service;

public interface URLService {
    String pop(String key);

    Long push(String key, String value);
}
