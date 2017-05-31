package kr.co.anajo.http;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;
import org.junit.Assert;

public class H2Test {

	public void createServer() throws SQLException {
		Server h2db = Server.createTcpServer("-tcpPort", "3060", "-tcpAllowOthers").start();
		getConnection();
		h2db.stop();
	}
	
	public void getConnection() throws SQLException {
		JdbcDataSource ds = new JdbcDataSource();
		ds.setURL("jdbc:h2:C:\\development\\db\\anajo");
		ds.setUser("sa");
		ds.setPassword("sa");
		Connection conn = ds.getConnection();
		Assert.assertNotNull(conn);
	}
}