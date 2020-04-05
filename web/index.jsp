<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.yuriy.entity.Student" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.OracleDaoConnection" %>
<%--
  Created by IntelliJ IDEA.
  User: olbe0615
  Date: 22.03.2020
  Time: 22:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ajax Project</title>
</head>
<body>
<%
    String name = request.getParameter("val");
    List<Student> studentList = OracleDaoConnection.getInstance().selectAllStudents(name);
    if (name == null || name.trim().equals("")) { %>
<p>Please enter name!</p>
<%
} else {
    try {
        boolean flag = false;
        Student currentStudent = null;
        for (Student student : studentList) {
            if (student.getName().equals(name)) {
                currentStudent = student;
                flag = true;
            }
        }
        if (!flag) {
%>
<p>No Record Found!</p>
<% } else { %>
<table border='1' cellpadding='2' width='100%'>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Salary</th>
    </tr>
    <%for (Student student : studentList) {%>
    <tr>
        <td><%=student.getId()%>
        </td>
        <td><%=student.getName()%>
        </td>
        <td><%=student.getSalary()%>
        </td>
    </tr>
    <% } %>
</table>
<% }
} catch (Exception e) { %>
<p>Some problems ...</p>
<% }
} %>
</body>
</html>
