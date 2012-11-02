import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;


/**
 * 
 */

/**
 * @author weihangfan
 *
 */
public class DataSet {
	private File database;
	public void execute(String[] commands, File file) throws ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		try{
			connection = DriverManager.getConnection("jdbc:sqlite:"+file.getAbsolutePath());
			Statement statement = connection.createStatement();
			for (int i=0;i<commands.length;i++)
				statement.executeUpdate(commands[i]);
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
		}
		finally{
			try{
				if(connection!=null){
					connection.close();
				}
			}
			catch(SQLException e){
				System.err.println(e);
			}
		}
	}
	
	public void createTable(Hashtable table,File file,String name){
		String[] commandsToExecute = new String[2];
		commandsToExecute[0] = "drop table if exists "+name;
		commandsToExecute[1] = "create table "+name; 
		try {
			execute(commandsToExecute,file);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void insert(String key, Double value){
		
	}
}
