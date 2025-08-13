package persistence;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataAccessObject  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int id = -1;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void fromResultSet(ResultSet rs) throws SQLException {
		if (rs != null) {
			this.id = rs.getInt("id");
		} else {
			throw new SQLException("ResultSet is null");
		}
		
	}
	
	

}
