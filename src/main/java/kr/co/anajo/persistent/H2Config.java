package kr.co.anajo.persistent;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;

import kr.co.anajo.context.annotation.Initialize;

public class H2Config {

	@Initialize
	public void init() {
		try {
			JdbcConnectionPool cp = JdbcConnectionPool.create("jdbc:h2:~/test", "sa", "sa");
			// for (int i = 0; i < args.length; i++) {
			Connection conn = cp.getConnection();
			conn.createStatement().execute("");
			conn.close();
			// }
			cp.dispose();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}