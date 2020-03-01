import java.awt.Polygon;
import java.util.Hashtable;

import exception.DBAppException;

public class Test {
	static DBApp dbApp;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String strTableName = "Student";
		dbApp = new DBApp( );
//		dbApp.init();
//		Hashtable htblColNameType = new Hashtable( );
//		htblColNameType.put("id", "java.lang.Integer");
//		htblColNameType.put("name", "java.lang.String");
//		htblColNameType.put("gpa", "java.lang.Double");
//		dbApp.createTable( strTableName, "id", htblColNameType );
//		insertTest(strTableName);
		updateTest(strTableName);
		Page p = (Page) dbApp.deserialize("0Student");
		System.out.println(p.v);
//		p = (Page) dbApp.deserialize("2Student");
//		System.out.println(p.v);
//		delete(strTableName);
//		p = (Page) dbApp.deserialize("0Student");
//		System.out.println(p.v);
//		p = (Page) dbApp.deserialize("2Student");
//		System.out.println(p.v);
		
		
	}
	static void updateTest(String strTableName) throws Exception{
		Hashtable htblColNameValue = new Hashtable( );
		htblColNameValue.put("gpa", new Double( 0.001 ) );
		dbApp.updateTable(strTableName, "78452", htblColNameValue);;
		htblColNameValue.clear( );
		
	}
	static void insertTest(String strTableName) throws Exception {
		Hashtable htblColNameValue = new Hashtable( );
		htblColNameValue.put("id", new Integer( 2343432 ));
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.95 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 453455 ));
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.95 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 5674567 ));
		htblColNameValue.put("name", new String("Dalia Noor" ) );
		htblColNameValue.put("gpa", new Double( 1.25 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 23498 ));
		htblColNameValue.put("name", new String("John Noor" ) );
		htblColNameValue.put("gpa", new Double( 1.5 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 78452 ));
		htblColNameValue.put("name", new String("Zaky Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.88 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
	}
	static void delete(String strTableName) throws Exception {
		Hashtable htblColNameValue = new Hashtable( );
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		dbApp.deleteFromTable(strTableName , htblColNameValue );
		htblColNameValue.clear( );
	}
	
}
