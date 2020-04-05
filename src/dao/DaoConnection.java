package dao;

import com.yuriy.entity.Student;

import java.util.List;

public interface DaoConnection {
    void connect();
    void disconnect();
    List<Student> selectAllStudents(String nameOfStudent);
}
