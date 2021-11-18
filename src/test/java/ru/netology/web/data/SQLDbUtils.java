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
    public static class VerificationCode {
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
            runner.update(conn, cleanUsers);
            runner.update(conn, cleanCodes);
            runner.update(conn, cleanCards);
            runner.update(conn, cleanCardTransactions);
        }
    }
    public static VerificationCode getVerificationCode() throws SQLException {
        var getCode = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1;";
        var runner = new QueryRunner();

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "user", "pass")
        ) {
            var code = runner.query(conn, getCode, new ScalarHandler<>());
            return new VerificationCode(code.toString());
        }
    }
}
