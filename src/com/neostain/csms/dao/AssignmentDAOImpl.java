package com.neostain.csms.dao;

import com.neostain.csms.model.Assignment;
import com.neostain.csms.util.SQLQueries;
import com.neostain.csms.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AssignmentDAOImpl implements AssignmentDAO {
    private static final Logger LOGGER = Logger.getLogger(AssignmentDAOImpl.class.getName());
    private final Connection conn;

    public AssignmentDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Assignment findById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[FIND_BY_ID] Assignment ID is empty");
            return null;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ASSIGNMENT_FIND_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAssignment(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_ID] Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Assignment> findByEmployeeId(String employeeId) {
        List<Assignment> assignments = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(employeeId)) {
            LOGGER.warning("[FIND_BY_EMPLOYEE_ID] Employee ID is empty");
            return assignments;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ASSIGNMENT_FIND_BY_EMPLOYEE_ID)) {
            ps.setString(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapResultSetToAssignment(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_EMPLOYEE_ID] Error: " + e.getMessage());
        }
        return assignments;
    }

    @Override
    public List<Assignment> findByStoreId(String storeId) {
        List<Assignment> assignments = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(storeId)) {
            LOGGER.warning("[FIND_BY_STORE_ID] Store ID is empty");
            return assignments;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ASSIGNMENT_FIND_BY_STORE_ID)) {
            ps.setString(1, storeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapResultSetToAssignment(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_STORE_ID] Error: " + e.getMessage());
        }
        return assignments;
    }

    @Override
    public List<Assignment> findByRange(String start, String end) {
        List<Assignment> assignments = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(start) || StringUtils.isNullOrEmpty(end)) {
            LOGGER.warning("[FIND_BY_RANGE] Start or end time is empty");
            return assignments;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ASSIGNMENT_FIND_BY_RANGE)) {
            ps.setString(1, start);
            ps.setString(2, end);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapResultSetToAssignment(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_BY_RANGE] Error: " + e.getMessage());
        }
        return assignments;
    }

    @Override
    public List<Assignment> findAll() {
        List<Assignment> assignments = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ASSIGNMENT_FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapResultSetToAssignment(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[FIND_ALL] Error: " + e.getMessage());
        }
        return assignments;
    }

    @Override
    public boolean create(Assignment assignment) {
        if (assignment == null) {
            LOGGER.warning("[CREATE] Assignment is null");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ASSIGNMENT_CREATE)) {
            ps.setString(1, assignment.getEmployeeId());
            ps.setString(2, assignment.getStoreId());
            ps.setTimestamp(3, assignment.getStartTime());
            ps.setTimestamp(4, assignment.getEndTime());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[CREATE] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateStartTime(String id, String startTime) {
        if (StringUtils.isNullOrEmpty(id) || StringUtils.isNullOrEmpty(startTime)) {
            LOGGER.warning("[UPDATE_START_TIME] Assignment ID or start time is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ASSIGNMENT_UPDATE_START_TIME)) {
            ps.setString(1, startTime);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_START_TIME] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEndTime(String id, String endTime) {
        if (StringUtils.isNullOrEmpty(id) || StringUtils.isNullOrEmpty(endTime)) {
            LOGGER.warning("[UPDATE_END_TIME] Assignment ID or end time is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ASSIGNMENT_UPDATE_END_TIME)) {
            ps.setString(1, endTime);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[UPDATE_END_TIME] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            LOGGER.warning("[DELETE] Assignment ID is empty");
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(SQLQueries.ASSIGNMENT_DELETE)) {
            ps.setString(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.severe("[DELETE] Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Assignment> search(String assignmentId, String employeeId, String storeId, String from, String to) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ASSIGNMENT WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (!StringUtils.isNullOrEmpty(assignmentId)) {
            sql.append(" AND ID=?");
            params.add(assignmentId);
        }
        if (!StringUtils.isNullOrEmpty(employeeId)) {
            sql.append(" AND EMPLOYEE_ID=?");
            params.add(employeeId);
        }
        if (!StringUtils.isNullOrEmpty(storeId)) {
            sql.append(" AND STORE_ID=?");
            params.add(storeId);
        }
        if (!StringUtils.isNullOrEmpty(from)) {
            sql.append(" AND START_TIME >= TO_TIMESTAMP(?, 'YYYY-MM-DD')");
            params.add(from);
        }
        if (!StringUtils.isNullOrEmpty(to)) {
            sql.append(" AND END_TIME <= TO_TIMESTAMP(?, 'YYYY-MM-DD')");
            params.add(to);
        }
        sql.append(" ORDER BY START_TIME DESC");

        List<Assignment> assignments = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapResultSetToAssignment(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("[SEARCH] Error: " + e.getMessage());
        }
        return assignments;
    }

    private Assignment mapResultSetToAssignment(ResultSet rs) throws SQLException {
        return new Assignment(
                rs.getString("ID"),
                rs.getString("EMPLOYEE_ID"),
                rs.getString("STORE_ID"),
                rs.getTimestamp("START_TIME"),
                rs.getTimestamp("END_TIME")
        );
    }
}
