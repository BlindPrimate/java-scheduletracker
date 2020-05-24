package scheduler.dbAccessors;

import java.sql.Connection;

public abstract class DataAccessor {

    protected Connection conn;

    public void DataAccessor() {
        this.conn = new DBConnection().getConnection();
    }

}
