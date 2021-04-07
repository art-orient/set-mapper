package com.epam.rd.autocode;

import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class SetMapperImpl implements SetMapper<Set<Employee>> {

    @Override
    public Set<Employee> mapSet(ResultSet resultSet) {
        Set<Employee> employees = new HashSet<>();
        try {
            while (resultSet.next()) {
                Employee employee = getEmployee(resultSet);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    private Employee getEmployee(ResultSet resultSet) throws SQLException {
        BigInteger id = BigInteger.valueOf(resultSet.getInt(1));
        FullName fullName = new FullName(resultSet.getString(2), resultSet.getString(3),
                resultSet.getString(4));
        Position position = Position.valueOf(resultSet.getString("position"));
        LocalDate hired = LocalDate.parse(resultSet.getDate("hiredate").toString());
        BigDecimal salary = resultSet.getBigDecimal("salary");
        Employee manager = getManager(resultSet, resultSet.getInt("manager"));
        return new Employee(id, fullName, position, hired, salary, manager);
    }

    private Employee getManager(ResultSet resultSet, int id) throws SQLException {
        Employee manager = null;
        int currentRow = resultSet.getRow();
        resultSet.beforeFirst();
        while (resultSet.next()) {
            if (resultSet.getInt(1) == id) {
                manager = getEmployee(resultSet);
                break;
            }
        }
        resultSet.absolute(currentRow);
        return manager;
    }
}
