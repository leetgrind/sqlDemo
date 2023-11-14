package org.example;

import com.github.javafaker.Faker;

import java.sql.*;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        try{
            Class.forName("org.postgresql.Driver");
            Connection connection =
                    DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                            "postgres",
                            "123456"
                            );

            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            insertPreparedStatement(connection);

            printData(statement);

        }
        catch (SQLException ex) {
            System.out.println("Exception " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    static void createTable(Statement statement) throws SQLException {
        String createTableQuery = "CREATE TABLE AUTHOR (ID VARCHAR(100), NAME VARCHAR(20), BOOK VARCHAR(20));";
        int result = statement.executeUpdate(createTableQuery);
        statement.close();
    }

    static void seedData(Statement statement, int count) throws SQLException {

        Faker faker = new Faker();

        for(int i=0; i<count; i++) {

            String id = UUID.randomUUID().toString();
            String name = faker.name().name();
            String bookName = faker.book().title();
            // INSERT INTO AUTHOR VALUES('abc-abc-abc', 'name', 'bookName');
            String insertQuery = "INSERT INTO AUTHOR VALUES('" + id + "', '" + name + "', '" + bookName + "');";
            int result = statement.executeUpdate(insertQuery);
        }
        statement.close();
    }

    static void printData(Statement statement) throws SQLException {

        String query = "SELECT * FROM AUTHOR";

        ResultSet resultSet = statement.executeQuery(query);

        while(resultSet.next()) {
            String id = resultSet.getString("ID");
            String name = resultSet.getString("NAME");
            String bookName = resultSet.getString("BOOK");
            System.out.println("ID: " + id);
            System.out.println(bookName + " - " + name);
            System.out.println("--------------------------------------");
        }

    }

    static void insertPreparedStatement(Connection connection) throws SQLException {

        String query = "INSERT INTO AUTHOR VALUES(?, ?, ?);";

        PreparedStatement preparedStatement =
                connection.prepareStatement(query);

        preparedStatement.setString(1, UUID.randomUUID().toString());
        preparedStatement.setString(2, "John Doe");
        preparedStatement.setString(3, "Unnamed Book");

        int result = preparedStatement.executeUpdate();
        System.out.println("Rows inserted: " + result);
        preparedStatement.close();
    }

    static void deleteById(Statement statement, String id) throws SQLException {
        String createTableQuery = "DELETE FROM AUTHOR WHERE ID='" + id +"'";
        int result = statement.executeUpdate(createTableQuery);
        System.out.println("Rows deleted: " + result);
        statement.close();
    }



    static void deleteResultSet(Statement statement, String id) throws SQLException {

        String selectQuery = "SELECT * FROM AUTHOR where id='"+id + "'";

        ResultSet rs = statement.executeQuery(selectQuery);

        System.out.println("Rows : " + rs.getFetchSize());

        while(rs.next()) {

            String dbId = rs.getString("ID");
            System.out.println("Processing row: " + dbId);

            if(id.equals(dbId)) {
                rs.deleteRow();
                System.out.println("Row deleted with ID: " + id);
                break;
            }
        }

        rs.close();
        statement.close();
    }
}