package dao;

import com.yuriy.entity.Student;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class OracleDaoConnection implements DaoConnection {

    private static OracleDaoConnection oracleDaoConnection;
    private Context context;
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement statement;
    //private Statement statement;

    public static OracleDaoConnection getInstance() {
        if (oracleDaoConnection != null) {
            return oracleDaoConnection;
        }
        return new OracleDaoConnection();
    }

    //CONNECT
    @Override
    public void connect() {
        try {
            Hashtable hashtable = new Hashtable();
            hashtable.put(Context.INITIAL_CONTEXT_FACTORY,
                    "weblogic.jndi.WLInitialContextFactory");
            hashtable.put(Context.PROVIDER_URL, "t3://localhost:7001"); //jdbc:oracle:thin:@localhost:1521:STUDENTS
            context = new InitialContext(hashtable);
            DataSource dataSource = (DataSource) context.lookup("my2");
            connection = dataSource.getConnection();
            System.out.println("Connect is ok");
        } catch (SQLException | NamingException e) {
            System.out.println("CONNECTION ERROR");
            e.printStackTrace();
        }
    }


    //DISCONNECT
    @Override
    public void disconnect() {
        try {
            connection.close();
            resultSet.close();
            statement.close();
            context.close();
            if (connection.isClosed()) {
                System.out.println("closed");
            }
        } catch (SQLException | NamingException e) {
            System.out.println("DISCONNECTION ERROR");
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> selectAllStudents(String nameOfStudent) {
        connect();
        List<Student> students = new ArrayList<>();
        try {
            char c = nameOfStudent.charAt(0);
            exceptionSolver(c + "%", students);
        } catch (StringIndexOutOfBoundsException ex) {
            exceptionSolver(nameOfStudent, students);
        }
        disconnect();
        return students;
    }

    private Student parseStudent(ResultSet resultSet) {
        Student student = null;
        try {
            int id = resultSet.getInt("STUDENT_ID");
            String name = resultSet.getString("STUDENT_NAME");
            float salary = resultSet.getFloat("STUDENT_SALARY");
            student = new Student(id, name, salary);
        } catch (SQLException e) {
            System.out.println("SQL EXEPTION");
            e.printStackTrace();
        }
        return student;
    }

    private void exceptionSolver(String nameOfStudent, List<Student> students) {
        try {
            statement = connection.prepareStatement("SELECT * FROM STUDENTS WHERE STUDENT_NAME LIKE ?");
            statement.setString(1, nameOfStudent);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                students.add(parseStudent(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION");
            e.printStackTrace();
        }
    }
}
