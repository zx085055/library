package com.tgfc.library.repository;

import com.tgfc.library.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query("SELECT r from Schedule r where r.id=?1 ")
    Schedule getById(int id);

    /**
     * 恢復全部暫停job時變更狀態
     */
    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.status='1' where r.status='2'")
    int resumeAll();

    /**
     * 暫停全部job時變更狀態
     */
    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.status='2' where r.status='1'")
    int pauseAll();

    /**
     * 暫停job時變更狀態
     */
    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.status='2' where r.status='1' and r.id=?1")
    int pauseJob(int id);

    /**
     * 恢復暫停job時變更狀態
     */
    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.status='1' where r.status='2' and r.id=?1")
    int resumeJob(int id);

    /**
     * 設定Group
     */
    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.group=?1 where r.id=?2")
    int setGroup(String group, int id);

    /**
     * 設置Job上次執行狀態紀錄
     */
    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.lastExecute=?2 where r.id=?1")
    int setLastExecute(int id, String lastExecute);

    /**
     * 建立時初始化
     */
    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.lastExecute=?2 , r.group=?3  where r.id=?1")
    int setInitStatus(int id, String lastExecute, String group);

    /**
     * 設置狀態
     */
    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.status=?2 where r.id=?1")
    int setStatus(int id, String Status);

    /**
     * 查詢上次執行狀態
     */
    @Query("SELECT r.lastExecute from Schedule r where r.id=?1 ")
    String getLastExecute(int id);

    /**
     * 設置Id
     */
    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.id=?2 where r.id=?1")
    int setId(int oleId, int newId);


}
