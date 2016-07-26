package Final;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface TableInterface {
	String search() throws SQLException;
	boolean searchValid();
}
