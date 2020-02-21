import java.util.Hashtable;
import java.util.Vector;

import exception.DBAppException;

public class DBApp implements java.io.Serializable{
	
	Vector<Table>DB;
	public void init() {
		DB=new Vector<Table>();
	}
	public void createTable(String strTableName,String strClusteringKeyColumn,Hashtable<String,String> htblColNameType )throws DBAppException{
		DB.add(new Table(strTableName, strClusteringKeyColumn, htblColNameType));
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
