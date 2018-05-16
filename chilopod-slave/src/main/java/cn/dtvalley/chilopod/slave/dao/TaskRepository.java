package cn.dtvalley.chilopod.slave.dao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskDomain, String> {
}
