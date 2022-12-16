import static spark.Spark.get;
import static spark.Spark.post;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import data.User;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class Main {
    public static String databaseUrl = "jdbc:postgresql://localhost:5432/";



    public static void main(String[] args) throws SQLException {
        ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
        ((JdbcConnectionSource)connectionSource).setUsername("postgres");
        ((JdbcConnectionSource)connectionSource).setPassword("pwd");

        System.out.println(((JdbcConnectionSource) connectionSource).getUrl());

        Dao<User,String> userDao = DaoManager.createDao(connectionSource, User.class);
        System.out.println("User created.");
        TableUtils.createTableIfNotExists(connectionSource, User.class);

        System.out.println("User created.");

        post("/users", new Route() {
            @Override
            public Object handle(Request request, Response response) throws SQLException {
                String username = request.queryParams("username");
                String email = request.queryParams("email");

                User user = new User();
                user.setUsername(username);
                user.setEmail(email);

                userDao.create(user);

                response.status(201); // 201 Created
                return "";
            }
        });
    }
}

