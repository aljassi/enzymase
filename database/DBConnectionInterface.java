package database;

import java.sql.Connection;

public interface DBConnectionInterface {
	
	public void closeConnection();

	public Connection getConnection() throws Exception;
}
