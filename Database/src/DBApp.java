import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Vector;

import exception.DBAppException;

public class DBApp implements java.io.Serializable{
	
	static PrintWriter pw;
	static BufferedReader br;
	
	public void init() {
		try {
			pw=new PrintWriter("Meta-Data.csv");
			pw.println("Table Name, Column Name, Column Type, ClusteringKey, Indexed");
			pw.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public void createTable(String strTableName,
			String strClusteringKeyColumn,
			Hashtable<String,
			String> htblColNameType )throws DBAppException{
			
			boolean alreadyExist = false;
			
			try
			{
				FileInputStream fileInput = new FileInputStream(strTableName+".class");
				alreadyExist = true;
			}
			catch(FileNotFoundException e)
			{
				alreadyExist = false;
			}
		
			if(alreadyExist)
			{
				System.out.println("File name already exist");
				return;
			}
			
			try 
			{
				br=new BufferedReader(new FileReader("Meta-Data.csv"));
				StringBuilder sb=new StringBuilder();
				while(br.ready()) 
				{
					sb.append(br.readLine());sb.append("\n");
				}
				pw=new PrintWriter("Meta-Data.csv"); 
				pw.print(sb);
				for(Entry<String, String>e:htblColNameType.entrySet()) 
				{
					pw.println(strTableName+','+e.getKey()+','+e.getValue()+
							','+(strClusteringKeyColumn.equals(e.getKey()))+','+false);
				}
				pw.flush();
				Table table = new Table(strTableName, strClusteringKeyColumn, htblColNameType);
				serialize(table,strTableName);
			} 
			catch (Exception e) 
			{
				System.err.println("Excption in create table");
			}
	}
	
	
	
	
	static void serialize(Object ob,String filename){
		try
		{
			FileOutputStream fileOutput = new FileOutputStream(filename+".class");
			ObjectOutputStream out = new ObjectOutputStream(fileOutput);
			out.writeObject(ob);
			out.close();
			fileOutput.close();
		}
		catch(Exception e)
		{
			System.out.println("Excption in ser");
		}
	}
	
	static Object deserialize(String filename) {
		try
		{
			FileInputStream fileInput = new FileInputStream(filename+".class");
			ObjectInputStream in = new ObjectInputStream(fileInput);
			Object ob = in.readObject();
			in.close();
			fileInput.close();
			return ob;
		}
		catch(Exception e)
		{
			System.out.println("Excption in deser");
			return null;
		}
	}
	
	public void insertIntoTable(String strTableName,
			 Hashtable<String,Object> htblColNameValue)
			 throws DBAppException {
		
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String strTableName = "Student";
		DBApp dbApp = new DBApp( );
		//dbApp.init();
		Hashtable htblColNameType = new Hashtable( );
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.double");
		dbApp.createTable( strTableName, "id", htblColNameType );
	}

}
