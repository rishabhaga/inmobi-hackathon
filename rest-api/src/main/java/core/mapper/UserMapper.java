package core.mapper;

import core.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rishabh.agarwal on 23/05/15.
 */
public class UserMapper implements ResultSetMapper<User> {
    @Override
    public User map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new User()
                .setId(resultSet.getString("id"))
                .setUuid(resultSet.getString("uuid"))
                .setDeviceId(resultSet.getString("deviceId"));
    }
}
