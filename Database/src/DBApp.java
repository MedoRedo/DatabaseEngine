import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

import exception.DBAppException;

public class DBApp implements java.io.Serializable{
	static PrintWriter pw;
	static BufferedReader br;
	public void init() {
	}
	public void createTable(String strTableName,
			String strClusteringKeyColumn,
			Hashtable<String,
			String> htblColNameType )throws DBAppException{
		pw = new PrintWriter()

	}
	
	public void insertIntoTable(String strTableName,
			 Hashtable<String,Object> htblColNameValue)
			 throws DBAppException {
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
