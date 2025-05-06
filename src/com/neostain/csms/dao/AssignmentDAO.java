package com.neostain.csms.dao;

import com.neostain.csms.model.Assignment;

import java.util.List;

public interface AssignmentDAO {
    Assignment findById(String id);

    List<Assignment> findByEmployeeId(String employeeId);

    List<Assignment> findByStoreId(String storeId);

    List<Assignment> findByRange(String start, String end);

    List<Assignment> findAll();

    boolean create(Assignment assignment);

    boolean updateStartTime(String id, String startTime);

    boolean updateEndTime(String id, String endTime);

    boolean delete(String id);
}
