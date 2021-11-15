package ru.netology.web.data;

import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLDbUtils {
    public SQLDbUtils() {
    }

    @Value
    public static class CodeInfo {
        String code;
    }

    public static void cleanTables() throws SQLException {
        var cleanUsers = "DELETE FROM users";
        var cleanCodes = "DELETE FROM auth_codes";
        var cleanCards = "DELETE FROM cards";
        var cleanCardTransactions = "DELETE FROM card_transactions";
        var runner = new QueryRunner();

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "user", "pass");
        ) {
            runner.update(conn, cleanCardTransactions);
            runner.update(conn, cleanCards);
            runner.update(conn, cleanCodes);
            runner.update(conn, cleanUsers);
        }
    }
    public static CodeInfo getVerificationCode() throws SQLException {
        var getCode = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1;";
        var runner = new QueryRunner();

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass")
        ) {
            var code = runner.query(conn, getCode, new ScalarHandler<>());
            return new CodeInfo(code.toString());
        }
    }
}