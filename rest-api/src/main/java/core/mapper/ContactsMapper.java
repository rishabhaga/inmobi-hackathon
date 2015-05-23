package core.mapper;

import core.Contacts;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rishabh.agarwal on 23/05/15.
 */
public class ContactsMapper implements ResultSetMapper<Contacts> {
    @Override
    public Contacts map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Contacts()
                .setFromId(r.getString("fromId"))
                .setToId(r.getString("toId"));
    }
}
