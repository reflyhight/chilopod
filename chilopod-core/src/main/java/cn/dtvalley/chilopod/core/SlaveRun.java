package cn.dtvalley.chilopod.core;

public interface SlaveRun {
    default boolean init() {
        return true;
    }

    void run();

    default void destroy() {
    }
}
