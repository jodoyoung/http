package kr.co.anajo.http;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;
import org.junit.Assert;
import org.junit.Test;

public class H2Test {

	@Test
	public void createServer() throws SQLException {
		Server h2db = Server.createTcpServer(null).start();
		getConnection();
		h2db.stop();
	}
	
	public void getConnection() throws SQLException {
		JdbcDataSource ds = new JdbcDataSource();
		ds.setURL("jdbc:h2:~/test");
		ds.setUser("sa");
		ds.setPassword("sa");
		Connection conn = ds.getConnection();
		Assert.assertNotNull(conn);
	}
}