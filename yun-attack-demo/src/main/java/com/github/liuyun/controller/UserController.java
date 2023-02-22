package com.github.liuyun.controller;

import com.github.liuyun.model.User;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    private final JdbcTemplate jdbcTemplate;

    public UserController(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @GetMapping("/users")
    public List<User> getUsers(String username) {
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        });
        return users;
    }

}