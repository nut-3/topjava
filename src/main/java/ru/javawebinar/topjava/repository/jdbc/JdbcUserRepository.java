package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.validateObject;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final ResultSetExtractor<List<User>> EXTRACTOR = rs -> {
        Map<Integer, User> result = new LinkedHashMap<>();
        while (rs.next()) {
            User user = ROW_MAPPER.mapRow(rs, rs.getRow());
            if (user != null) {
                String role = rs.getString("role");

                if (!result.containsKey(user.getId())) {
                    user.setRoles(role != null ? Set.of(Role.valueOf(role)) : null);
                    result.put(user.getId(), user);
                } else {
                    if (role != null) {
                        result.get(user.getId()).getRoles().add(Role.valueOf(role));
                    }
                }
            }
        }
        return new ArrayList<>(result.values());
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final SimpleJdbcInsert insertRoles;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.insertRoles = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user_roles")
                .usingColumns("user_id", "role");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        validateObject(user);

        BeanPropertySqlParameterSource parameterSourceUser = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSourceUser);

            List<MapSqlParameterSource> parameterSourceRole = new ArrayList<>();
            user.getRoles().forEach(role -> parameterSourceRole.add(new MapSqlParameterSource()
                    .addValue("user_id", newKey.intValue())
                    .addValue("role", role)));

            insertRoles.executeBatch(parameterSourceRole.toArray(new MapSqlParameterSource[0]));

            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSourceUser) == 0) {
            return null;
        }
        Set<Role> currentRoles = get(user.id()).getRoles();
        Set<Role> updateRoles = user.getRoles();

        Set<Role> differences = currentRoles.stream()
                .filter(role -> !updateRoles.contains(role))
                .collect(Collectors.toSet());
        List<Object[]> arguments = new ArrayList<>();
        if (!differences.isEmpty()) {
            differences.forEach(role -> arguments.add(new Object[]{user.id(), role.name()}));
            jdbcTemplate.batchUpdate("DELETE FROM user_roles where user_id=? AND role=?", arguments);
        }

        differences = updateRoles.stream()
                .filter(role -> !currentRoles.contains(role))
                .collect(Collectors.toSet());
        arguments.clear();
        if (!differences.isEmpty()) {
            differences.forEach(role -> arguments.add(new Object[]{user.id(), role.name()}));
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", arguments);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("""
                SELECT u.id, u.email, u.name, u.password, u.enabled, u.calories_per_day,
                u.registered, ur.role FROM users u LEFT OUTER JOIN user_roles ur
                ON u.id = ur.user_id WHERE u.id=?
                """, EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("""
                SELECT u.id, u.email, u.name, u.password, u.enabled, u.calories_per_day,
                u.registered, ur.role FROM users u LEFT JOIN user_roles ur
                ON u.id = ur.user_id WHERE u.email=?
                """, EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("""
                SELECT u.id, u.email, u.name, u.password, u.enabled, u.calories_per_day,
                u.registered, ur.role FROM users u LEFT JOIN user_roles ur
                ON u.id = ur.user_id ORDER BY u.name, u.email
                """, EXTRACTOR);
    }
}
