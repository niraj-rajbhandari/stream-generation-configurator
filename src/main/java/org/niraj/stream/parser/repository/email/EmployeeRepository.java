package org.niraj.stream.parser.repository.email;

import org.niraj.stream.parser.domain.email.pojo.Employee;
import org.niraj.stream.parser.domain.email.pojo.RecipientEmployee;
import org.niraj.stream.parser.repository.EmailDBConnection;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EmployeeRepository {

    private static Logger LOGGER = Logger.getLogger(EmployeeRepository.class.getName());
    private Connection connection;

    public EmployeeRepository() throws SQLException, FileNotFoundException {
        connection = EmailDBConnection.getConnection();
    }

    public Employee getEmployeeByEmail(String email) throws SQLException {

        String query = "SELECT e.* FROM employeelist e " +
                "WHERE e.Email_id = ? OR e.Email2=? OR e.Email3 = ? OR e.Email4 = ? LIMIT 1";

//        LOGGER.info(query + " -- email: " + email);
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        statement.setString(2, email);
        statement.setString(3, email);
        statement.setString(4, email);

        ResultSet result = statement.executeQuery();
        Employee employee = null;
        if (result.next()) {
            employee = new Employee();
            employee.setId(result.getInt(1));
            employee.setFirstName(result.getString(2));
            employee.setLastName(result.getString(3));
            employee.setEmailId(result.getString(4));
            employee.setEmail2(result.getString(5));
            employee.setEmail3(result.getString(6));
            employee.setEmail4(result.getString(7));
            employee.setStatus(result.getString(9));
        }

        return employee;
    }

    public List<RecipientEmployee> getRecipientEmployeeByMessageId(int mid) throws SQLException {
        String query = "SELECT ri.rtype as method, e.* FROM employeelist e, message m " +
                "JOIN recipientinfo ri ON m.mid= ri.mid " +
                "WHERE ri.mid = ? AND (ri.rvalue = e.email_id OR ri.rvalue = e.email2 OR ri.rvalue = e.email3 OR ri.rvalue = e.email4)";
//        LOGGER.info(query + "===== mid: " + mid);
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setInt(1, mid);

        ResultSet result = statement.executeQuery();

        List<RecipientEmployee> employees = new ArrayList<>();

        while (result.next()) {
            RecipientEmployee employee = new RecipientEmployee();
            employee.setReceptionMethod(result.getString(1));
            employee.setId(result.getInt(2));
            employee.setFirstName(result.getString(3));
            employee.setLastName(result.getString(4));
            employee.setEmailId(result.getString(5));
            employee.setEmail2(result.getString(6));
            employee.setEmail3(result.getString(7));
            employee.setEmail4(result.getString(8));
            employee.setStatus(result.getString(10));

            employees.add(employee);
        }

        return employees;
    }
}
