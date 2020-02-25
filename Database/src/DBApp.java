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
				e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		//	System.out.println("Excption in deser");
			return null;
		}
	}
	
	public void insertIntoTable(String strTableName,
			 Hashtable<String,Object> htblColNameValue)
			 throws Exception {
		
		boolean ok=true;
		
		//tuple
		Vector<Object>v=new Vector<Object>();
		int keyIndex=-1;String keytype = ""; 
		br=new BufferedReader(new FileReader("Meta-Data.csv"));
		while(br.ready()) 
		{
			String[]row = br.readLine().split(",");
			if(row[3].equals("true"))
			{
				keyIndex = v.size();
				keytype = row[2];
			}
			if(row[0].equals(strTableName)) {
				String type=row[2];
				Object value=htblColNameValue.getOrDefault(row[1], null);
				if(value==null && row[3].equals("true")) {
					ok=false;
					break;
				}
				if(value==null) {
					v.add(value);
				}
				else {
					try {
						Class cl=Class.forName(type);
						
						if(value.getClass()== cl) {
							v.add(value);
						}
						else {
							ok=false;
							break;
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(!ok)
		{
			System.out.println("Invalid insertion");
			return;
		}
		
		Table t = (Table)deserialize(strTableName);
		t.insert(v, keyIndex, keytype);
	}
	public void updateTable(String strTableName,String strClusteringKey,
			Hashtable<String,Object> htblColNameValue)  throws DBAppException{
		
	}
	
	// deleteFromTable notes:
	// htblColNameValue holds the key and value. This will be used in search
	// to identify which rows/tuples to delete.
	// htblColNameValue entries are ANDED together
	public void deleteFromTable(String strTableName,
			Hashtable<String,Object> htblColNameValue)throws Exception{// »‘ﬂ· „Êﬁ 
		
		Vector<String>colNames= new Vector<String>();
		br=new BufferedReader(new FileReader("Meta-Data.csv"));
		while(br.ready()) 
		{
			String[]row = br.readLine().split(",");
			if(row[0].equals(strTableName))
			{
				colNames.add(row[1]);
			}
		}
		
		Table table = (Table)deserialize(strTableName);
		for(int k=0;k<table.pages.size();k++)
		{
			String curr = table.pages.get(k);
			Page currentPage = (Page)deserialize(curr);
			for(int m=0;m<currentPage.v.size();m++)
			{
				Vector<Object> tuple = currentPage.v.get(m);
				boolean match = true;
				for(int i=0;i<tuple.size();i++)
				{
					if(!htblColNameValue.containsKey(colNames.get(i)))
					{
						continue;
					}
					Object val = htblColNameValue.get(colNames.get(i));
					
					if(val.getClass() != tuple.get(i).getClass())
					{
						match = false;
						break;
					}
					
					if(!val.equals(tuple.get(i)))
					{
						match = false;
						break;
					}
				}
				if(!match)
				{
					continue;
				}
				table.removeTuple(k, m);
			}
			
		}
	}
	
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String strTableName = "Student";
		DBApp dbApp = new DBApp( );
//		dbApp.init();
//		Hashtable htblColNameType = new Hashtable( );
//		htblColNameType.put("id", "java.lang.Integer");
//		htblColNameType.put("name", "java.lang.String");
//		htblColNameType.put("gpa", "java.lang.Double");
//		dbApp.createTable( strTableName, "id", htblColNameType );
//		Hashtable htblColNameValue = new Hashtable( );
//		htblColNameValue.put("id", new Integer( 2343432 ));
//		htblColNameValue.put("name", new String("Ahmed Noor" ) ); 
//		htblColNameValue.put("gpa", new Double( 0.95 ) ); 
//		dbApp.deleteFromTable( strTableName , htblColNameValue ); 
		
		Hashtable htblColNameValue = new Hashtable( );
		htblColNameValue.put("id", new Integer( 2343432 ));
		htblColNameValue.put("name", new String("Ahmed Noor" ) ); 
		htblColNameValue.put("gpa", new Double( 0.95 ) ); 
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		
		Page p=(Page)deserialize("0Student");
		serialize(p, "0Student");
		System.out.println(p.v);
		
	}
}
