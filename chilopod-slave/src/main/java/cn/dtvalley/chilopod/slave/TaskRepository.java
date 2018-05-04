package cn.dtvalley.chilopod.slave;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskDomain , String> {
}
