

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.sql.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.crypto.Data;

public class Table implements Serializable{
	
	Vector<String>pages;
	String name;
	String clusteringKeyColumn; //clustering column name
	Hashtable<String,String> htblColNameType;
	int pageNumber;
	
	public Table(String strTableName,
			String strClusteringKeyColumn,
			Hashtable<String,String> htblColNameType){
		name=strTableName;
		clusteringKeyColumn=strClusteringKeyColumn;
		this.htblColNameType=htblColNameType;
		pages=new Vector<String>();
	}

	public void update(Hashtable<String, Object> htblColNameValue,String strClusteringKey,int keyIndex,String keytype) throws Exception {
		
//	
//		//search
		Comparable tupleKey = DBApp.parse(keytype, strClusteringKey);
		int ans=0;
		
//		//update
		while(ans < pages.size())
		{
			String curr = pages.get(ans);
			Page currentPage = (Page)DBApp.deserialize(curr);
			try
			{
				boolean canBeMore=currentPage.update(name, htblColNameValue, tupleKey,
						keyIndex);
				DBApp.serialize(currentPage,curr);
				if(!canBeMore)
					return;
				ans++;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
	}
	public void insert(Vector<Object> tuple,int keyIndex,String keytype) {
		Vector<Object> v = tuple;
		//System.out.println(pages);
		
		//search
		Comparable tupleKey = (Comparable) tuple.get(keyIndex);
		int low =0,hi = pages.size()-1,ans=0;
		while(low<=hi)
		{
			int mid = (low+hi)/2;
			String curr = pages.get(mid);
			Page currentPage = (Page)DBApp.deserialize(curr);
			Vector startTuple = currentPage.v.get(0);
			Comparable key = (Comparable) startTuple.get(keyIndex);
			if(key.compareTo(tupleKey) <= 0)
			{
				ans = mid;
				low = mid+1;
			}
			else
			{
				hi = mid-1;
			}
		}
		
		//insert
		while(ans < pages.size())
		{
			String curr = pages.get(ans);
			Page currentPage = (Page)DBApp.deserialize(curr);
			try
			{
				v = currentPage.insertSorted(v);
				DBApp.serialize(currentPage,curr);
				if(v==null)
					return;
				ans++;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		if(v==null)
			return;
		String pageName = pageNumber+""+name;
		pageNumber++;
		Page newPage = new Page(pageName,keyIndex,keytype);
		pages.add(pageName);
		try 
		{
			newPage.insertSorted(v);
			DBApp.serialize(newPage,pageName);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void removeTuple(int pageIdx,int tupleIdx){
		Page curr = (Page)DBApp.deserialize(pages.get(pageIdx));
		curr.v.remove(tupleIdx);
		if(curr.v.size() == 0) {
			File file = new File(pages.get(pageIdx)+".class");
			file.delete();
			pages.remove(pageIdx);
		}else {
			DBApp.serialize(curr, pages.get(pageIdx));	
		}		
	}
	
	public static void main(String[] args) {

	}

}
