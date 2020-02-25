

import java.io.File;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

public class Table implements Serializable{
	
	Vector<String>pages;
	String name;
	String clusteringKeyColumn; //clustering column name
	Hashtable<String,String> htblColNameType;
	
	
	public Table(String strTableName,
			String strClusteringKeyColumn,
			Hashtable<String,String> htblColNameType){
		name=strTableName;
		clusteringKeyColumn=strClusteringKeyColumn;
		this.htblColNameType=htblColNameType;
		pages=new Vector<String>();
	}
	
	
	public void insert(Vector<Object> tuple,int keyIndex,String keytype) {
		Vector<Object> v = tuple;
		//System.out.println(pages);
		for(String curr:pages) 
		{
			Page currentPage = (Page)DBApp.deserialize(curr);
			//System.out.println(currentPage.v);
			try 
			{
				v = currentPage.insertSorted(v);
				DBApp.serialize(currentPage,curr);
				if(v==null)
					return;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		if(v==null)
			return;
		String pageName =pages.size()+""+name;
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
		Page last = (Page)DBApp.deserialize(pages.get(pages.size()-1));
		Vector<Object>next;
		for(int i=pages.size()-1;i>pageIdx;i--) {
			next = last.v.remove(0);
			if(last.v.size() == 0) {
				File file = new File(pages.get(i));
				file.delete();
				pages.remove(i);
			}else {
				DBApp.serialize(last, pages.get(i));
			}
			last = (Page) DBApp.deserialize(pages.get(i-1));
			last.v.add(next);
		}
		last.v.remove(tupleIdx);
		DBApp.serialize(last, pages.get(pageIdx));
		
	}
	
	public static void main(String[] args) {

	}

}
