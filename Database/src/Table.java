

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

public class Table implements Serializable{
	Vector<FileInputStream>pages;
	transient String name;
	transient String ClusteringKeyColumn;
	transient Hashtable<String,String> htblColNameType;
	Table(String strTableName,String strClusteringKeyColumn,Hashtable<String,String> htblColNameType){
		name=strTableName;
		ClusteringKeyColumn=strClusteringKeyColumn;
		this.htblColNameType=htblColNameType;
		pages=new Vector<FileInputStream>();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
