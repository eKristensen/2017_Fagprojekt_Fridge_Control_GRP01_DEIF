import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class Database {
	private static String username = "fagprojekt";

	private static String password = "gf3qAdOPH1l9YtSp";

	private static String connectionString = "jdbc:mysql://172.22.22.104:3306/fagprojekt";
	private static Connection connection;
	private static Statement cmd;
	private static ResultSet data;

	private static long sendtime = System.currentTimeMillis() / 1000L;
	private static ArrayList<DEIF> sendcache = new ArrayList<DEIF>();

	public static void main(String[] args) {

	}

	public static void sendTomySQL(DEIF data) throws java.lang.ClassNotFoundException {
		if ((sendtime + 1 * 60) <= (System.currentTimeMillis() / 1000L)) {
			CommitCache();
			sendtime = System.currentTimeMillis() / 1000L;
		}
		sendcache.add(data);
	}

	public static void CommitCache() throws java.lang.ClassNotFoundException {
		try {
			System.out.println("Sending");
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat minute = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
			//System.out.println(date.format(new Date((Long) jsonObject.get("timestamp"))));
			//System.out.println(minute.format(new Date((Long) jsonObject.get("timestamp"))));
			connection = getConnection();
			String sql = "INSERT INTO `deif` (`ID`, `timestamp`, `datetime`, `minute`, `deifid`, `name`, `latitude`, `longitude`, `active_power_a`, `active_power_b`, `active_power_c`, `apparent_power_a`, `apparent_power_b`, `apparent_power_c`, `current_average`, `current_neutral`, `current_phase_a`, `current_phase_b`, `current_phase_c`, `frequency`, `reactive_power_a`, `reactive_power_b`, `reactive_power_c`, `voltage_a_n`, `voltage_b_n`, `voltage_c_n`) VALUES ";

			for (int i = 0; i < sendcache.size(); i++) {
				if (i != 0)
					sql = sql + ",";
				sql = sql + "(NULL";
				sql = sql + ", '" + sendcache.get(i).getTimestamp() + "'";
				//sql = sql + ", CURRENT_TIME()";
				//sql = sql + ", CURRENT_TIME()";;
				sql = sql + ",'" + date.format(new Date((Long) sendcache.get(i).getTimestamp())) + "'";
				sql = sql + ",'" + minute.format(new Date((Long) sendcache.get(i).getTimestamp())) + "'";
				//sql = sql + ", DATE_FORMAT(FROM_UNIXTIME(`" + sendcache.get(i).getTimestamp() + "`), '%Y-%m-%d %H:%i:%s')";
				//sql = sql + ", DATE_FORMAT(FROM_UNIXTIME(`" + sendcache.get(i).getTimestamp() + "`), '%Y-%m-%d %H:%i:00')";
				sql = sql + ", '" + sendcache.get(i).getID() + "'";
				sql = sql + ", '" + sendcache.get(i).getName() + "'";
				sql = sql + ", '" + sendcache.get(i).getLocation().get("latitude") + "'";
				sql = sql + ", '" + sendcache.get(i).getLocation().get("longitude") + "'";
				Map<String,Double> measurements = new HashMap<String,Double>();
			    for (int j = 0; j < sendcache.get(i).getMeasurements().size(); j++) {
			    	String id = ((JSONObject) sendcache.get(i).getMeasurements().get(j)).get("id").toString();
			    	Double value = (Double) ((JSONObject) sendcache.get(i).getMeasurements().get(j)).get("value");
			    	measurements.put(id,value);
			    }
				sql = sql + ", '" + measurements.get("active_power_a") + "'";
				sql = sql + ", '" + measurements.get("active_power_b") + "'";
				sql = sql + ", '" + measurements.get("active_power_c") + "'";
				sql = sql + ", '" + measurements.get("apparent_power_a") + "'";
				sql = sql + ", '" + measurements.get("apparent_power_b") + "'";
				sql = sql + ", '" + measurements.get("apparent_power_c") + "'";
				sql = sql + ", '" + measurements.get("current_average") + "'";
				if (measurements.containsKey("current_neutral")) sql = sql + ", '" + measurements.get("current_neutral") + "'";
				else sql = sql + ", NULL";//temp curneautral;
				sql = sql + ", '" + measurements.get("current_phase_a") + "'";
				sql = sql + ", '" + measurements.get("current_phase_b") + "'";
				sql = sql + ", '" + measurements.get("current_phase_c") + "'";
				sql = sql + ", '" + measurements.get("frequency") + "'";
				sql = sql + ", '" + measurements.get("reactive_power_a") + "'";
				sql = sql + ", '" + measurements.get("reactive_power_b") + "'";
				sql = sql + ", '" + measurements.get("reactive_power_c") + "'";
				sql = sql + ", '" + measurements.get("voltage_a_n") + "'";
				sql = sql + ", '" + measurements.get("voltage_b_n") + "'";
				sql = sql + ", '" + measurements.get("voltage_c_n") + "'";
				sql = sql + ")";
			}
			sendcache.clear();
			sql = sql + ";";
			//System.out.println(sql);
			cmd = connection.createStatement();
			cmd.executeUpdate(sql);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}




	public static Connection getConnection() throws java.lang.ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(connectionString, username, password);
			if (!conn.isClosed())
				return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}





}