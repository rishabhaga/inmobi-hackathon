package dao;

import core.Contacts;
import core.mapper.ContactsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * Created by rishabh.agarwal on 23/05/15.
 */
@RegisterMapper(ContactsMapper.class)
public interface ContactsDAO {
    @SqlQuery("select * from contacts where fromId = :fromId")
    List<Contacts> findByFromId(@Bind("fromId") String fromId);

    @SqlUpdate("insert into contacts values (:fromId, :toId)")
    int insert(@BindBean Contacts contacts);

    @SqlUpdate("delete from contacts where fromId = :fromId and toId = :toId")
    int delete(@BindBean Contacts contacts);
}
