package dao;

import core.User;
import core.mapper.UserMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

/**
 * Created by rishabh.agarwal on 23/05/15.
 */
@RegisterMapper(UserMapper.class)
public interface UserDAO {
//    @SqlQuery("select * from user where id = :id")
//    User findById(@Bind("id") String id);

    @SqlUpdate("insert into user values ( :id, :uuid, :deviceId)")
    int insert(@BindBean User user);
}
