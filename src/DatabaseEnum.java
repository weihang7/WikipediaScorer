import java.io.File;
import java.util.Enumeration;

/**
 * @author weihangfan
 *
 */
public enum DatabaseEnum implements Enumeration<int[]> {
	;
	private DataSet db;
	private DatabaseEnum(File db){
		this.db=new DataSet(db);
	}
	@Override
	public boolean hasMoreElements() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] nextElement() {
		// TODO Auto-generated method stub
		return null;
	}

}
