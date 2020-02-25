

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import exception.DBAppException;

public class Table implements Serializable{
	
	Vector<String>pages;
	String name;
	String clusteringKeyColumn;
	Hashtable<String,String> htblColNameType;
	
	public Table(String strTableName,
			String strClusteringKeyColumn,
			Hashtable<String,String> htblColNameType){
		name=strTableName;
		clusteringKeyColumn=strClusteringKeyColumn;
		this.htblColNameType=htblColNameType;
		pages=new Vector<String>();
	}
		
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
