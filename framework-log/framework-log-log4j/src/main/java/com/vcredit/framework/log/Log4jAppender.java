package com.vcredit.framework.log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.jdbc.JDBCAppender;

public class Log4jAppender extends JDBCAppender {
	@Override
	protected Connection getConnection() throws SQLException {
		if (!DriverManager.getDrivers().hasMoreElements())
			setDriver("sun.jdbc.odbc.JdbcOdbcDriver");
		com.mysql.jdbc.Connection cn = (com.mysql.jdbc.Connection) connection;
		try {
			cn.ping();
		} catch (Exception e) {
			System.out.println("LOG4J连接失效，将重新获取连接");
			connection = null;
		}
		if (connection == null) {
			connection = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
		}

		return connection;
	}
}
