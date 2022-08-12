package SQL;

import java.sql.*;

public class Database {

    public static String searchTable(int x) throws Exception {

        String result = new String();
        String query = "SELECT * FROM student WHERE id =" + x;

        Connection myConnection = null;
        try {
            myConnection = getConnection();
            Statement myStatement = myConnection.createStatement();
            ResultSet myResult = myStatement.executeQuery(query);

            while (myResult.next()) {
                result = myResult.getString("name") + "->" + myResult.getString("major");
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        }

        myConnection.close();
        System.out.println("Search complete");

        return result;
    }

    public static String fetch() throws Exception {

        String result = new String();
        String query = "SELECT * FROM student";

        Connection myConnection = null;

        try {
            myConnection = getConnection();
            Statement stmt = myConnection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {

                result += resultSet.getString("id") + " " + resultSet.getString("name") + " " + resultSet.getString("major") + "^^";
                System.out.println(result);
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        myConnection.close();

        return result;
    }

    public static void insertStudent(Integer id, String name, String surname, String major, Double gpa) throws Exception {

        try {
            Connection myConnection = getConnection();
            PreparedStatement preparedStatement = myConnection.prepareStatement("INSERT INTO student (id , name , surname , major , gpa , is_admin) VALUES('" + id + "','" + name + "','" + surname + "','" + major + "','" + gpa + "','" + 0 + "')");
            preparedStatement.executeUpdate();
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            System.out.println("Added to table");
        }

    }

    public static void insertAdmin(Integer id, String name, String surname, String major, String password) throws Exception {

        try {
            Connection myConnection = getConnection();
            PreparedStatement preparedStatement = myConnection.prepareStatement("INSERT INTO student (id , name , surname , major , password , is_admin) VALUES ('" + id + "','" + name + "','" + surname + "','" + major + "','" + password + "','" + 1 + "')");
            preparedStatement.executeUpdate();
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            System.out.println("Added to table");
        }

    }

    public static String loginToSql(Integer id, String password) throws Exception {

        String query = "SELECT * FROM student WHERE id=" + id + " AND password=\"" + password + "\"";
        String result = null;

        try {
            Connection myConnection = getConnection();
            Statement stmt = myConnection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            if (resultSet.next()) {

                result = resultSet.getString("id") + "^^" + resultSet.getString("is_admin");
            }

        } catch (SQLException sql) {
            sql.printStackTrace();
        }

        return result;

    }

    public static Connection getConnection() throws Exception {

        String drive = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/school";
        String name = "root";
        String password = "Dsbguner345+6810";

        try {
            Class.forName(drive);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Connection myConnection = DriverManager.getConnection(url, name, password);
            return myConnection;
        } catch (SQLException sql) {
            sql.printStackTrace();
        }

        return null;
    }
}
