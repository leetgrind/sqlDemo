package org.example;

import com.github.javafaker.Faker;

import java.sql.*;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        try{
            Class.forName("org.sqlite.JDBC");
            Connection connection =
                    DriverManager.getConnection("jdbc:sqlite:test.db");

            Statement statement = connection.createStatement();

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

    }}