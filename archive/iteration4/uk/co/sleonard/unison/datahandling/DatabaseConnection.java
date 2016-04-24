package uk.co.sleonard.unison.datahandling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hsqldb.util.DatabaseManagerSwing;

import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;
import uk.co.sleonard.unison.input.NewsGroupArticle;

/**
 * @author steve
 * 
 */
public class DatabaseConnection {
	private static Logger logger = Logger.getLogger("DatabaseConnection");

	private static Connection connection = null;

	private final static String dbDriver = "org.hsqldb.jdbcDriver";

	// TODO read from config
	private final static String DB_URL = "jdbc:hsqldb:file:DB/projectDB";

	private final static String dbUser = "sa";

	public static final String GUI_ARGS[] = { "-driver", dbDriver, "-url",
			DB_URL, "-user", dbUser };

	private static DatabaseConnection instance = null;

	public void storeUser(String senderEmail, String location) throws Exception {
		UsenetUser user = UsenetUserHelper.createUsenetUser(senderEmail,
				location);
		Transaction tx = null;
		Session session = null;

		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(user);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	static final int SQL_ERROR_UNIQUE_CONSTRAINT_VIOLATION = -104;

	public static void dump(ResultSet rs) throws SQLException {

		// the order of the rows in a cursor
		// are implementation dependent unless you use the SQL ORDER statement
		ResultSetMetaData meta = rs.getMetaData();
		int colmax = meta.getColumnCount();
		int i;
		Object o = null;
		System.out.println("OUTPUT:\n ");

		// the result set is a cursor into the data. You can only
		// point to one row at a time
		// assume we are pointing to BEFORE the first row
		// rs.next() points to next row and returns true
		// or false if there is no next row, which breaks the loop

		for (; rs.next();) {
			for (i = 0; i < colmax; ++i) {
				o = rs.getObject(i + 1); // Is SQL the first column is
				// indexed

				// with 1 not 0
				if (null == o) {
					o = "";
				}
				System.out.print(o.toString() + " ");
			}

			System.out.println(" ");
		}
	}

	// Using Instance Design Pattern (Freeman et al 2004)
	// synchronise this to ensure that it does not create more than one
	public static synchronized DatabaseConnection getInstance() {

		logger.debug("getInstance " + (null == instance ? "NEW " : "OLD"));
		if (null == instance) {
			instance = new DatabaseConnection();
		}

		try {
			if (instance.getConnection().isClosed()) {
				instance.connectToDB();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return instance;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Create the necessary database schema
		DatabaseConnection conn = getInstance();
		NewsGroupArticle.createTable(conn);

		conn.runQuery("SELECT count(*) FROM " + NewsGroupArticle.tableName);
		try {
			conn.closeConnection();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	HashMap<String, PreparedStatement> statements;

	private DatabaseConnection() {
		statements = new HashMap<String, PreparedStatement>();
		try {

			Class.forName(dbDriver);
		} catch (Exception e) {
			System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
			e.printStackTrace();
		}

		connectToDB();
	}

	public void closeConnection() throws SQLException {
		connection.commit();
		connection.close();
		System.out.println("DB closed");
	}

	private void connectToDB() {
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(DB_URL
						+ ";shutdown=true", dbUser, "");

				connection.setAutoCommit(true);
				System.out.println("connected");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public java.sql.Date convertDate(java.util.Date srcDate) {
		java.sql.Date sqlDate = new java.sql.Date(srcDate.getTime());
		return sqlDate;
	}

	public void executePreparedStatement(PreparedStatement prep)
			throws SQLException {
		prep.execute();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		closeConnection();
	}

	public Connection getConnection() {
		// ensure is still connected to DB
		connectToDB();

		return connection;
	}

	/**
	 * This function will check if the prepared statement has been used before.
	 * If so it will fetch it from memory, else it will create it and store it.
	 * 
	 * @param statementSQL
	 * @return
	 */
	public PreparedStatement getPreparedStatement(String statementSQL) {
		PreparedStatement prep = null;
		try {

			if (statements.containsKey(statementSQL)) {
				prep = statements.get(statementSQL);

			} else {
				// Use a PreparedStatement because Path and Name could contain '
				prep = connection.prepareCall(statementSQL);
				statements.put(statementSQL, prep);
			}
			// Clear all Parameters of the PreparedStatement
			prep.clearParameters();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prep;
	}

	public boolean runExecute(String executeSQL) {
		System.out.println("SQL: " + executeSQL);

		// Create a statement object
		Statement stat = null;
		try {
			stat = connection.createStatement();
			stat.execute(executeSQL);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// Close the Statement object, it is no longer used
			try {
				stat.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}

		}
		return true;
	}

	public boolean runQuery(String querySQL) {
		System.out.println("SQL: " + querySQL);

		// Create a statement object
		Statement stat = null;
		ResultSet rs = null;

		try {
			stat = connection.createStatement();

			// repeated calls to execute but we
			// choose to make a new one each time
			rs = stat.executeQuery(querySQL); // run the query

			// do something with the result set.
			dump(rs);
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// Close the Statement object, it is no longer used
			try {
				stat.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}

		}
		return true;
	}

	public boolean runUpdate(String updateSQL) {
		System.out.println("SQL: " + updateSQL);

		// Create a statement object
		Statement stat = null;
		try {
			stat = connection.createStatement();
			stat.executeUpdate(updateSQL);
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// Close the Statement object, it is no longer used
			try {
				stat.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public void showDBGui() {
		DatabaseManagerSwing.main(GUI_ARGS);
	}
}
