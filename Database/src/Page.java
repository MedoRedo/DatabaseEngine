import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

public class Page implements java.io.Serializable{
	
	String pageName;
	int clusteringKeyIndex;
	String clusteringKeyType;
	Vector<Vector<Object>> v;
	int N;
	
	public Page(String pageName,int clusteringKeyIndex,String clusteringKeyType) {
		this.clusteringKeyIndex = clusteringKeyIndex;
		this.clusteringKeyType = clusteringKeyType;
		this.pageName=pageName;
		v = new Vector<Vector<Object>>();
		N = getMaxRowsCountinPage();
	}
	public static int getMaxRowsCountinPage(){
		try {
			FileReader reader = new FileReader("DBApp.properties");
			Properties properties = new Properties();
			properties.load(reader);
			return Integer.parseInt(properties.getProperty("MaximumRowsCountinPage"));			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 200;
	}
	public Vector<Object> insertSorted(Vector<Object> tuple) throws Exception{
		//System.out.println(v);
		Comparable mykeyValue = (Comparable) tuple.get(clusteringKeyIndex);
		boolean inserted = false;
		int lw = 0,hi = v.size()-1,ans = v.size();
		while(lw<=hi) {
			int mid  = (lw+hi)/2;
			Vector<Object> currentTuple = v.get(mid);
			Comparable currentKey = (Comparable) currentTuple.get(clusteringKeyIndex);
			int x = mykeyValue.compareTo(currentKey);
			if(x<0) {
				ans = mid;
				hi = mid-1;
			}else {
				lw = mid+1;
			}
		}
		v.add(ans, tuple);

		if(v.size()>N)
		{
			Vector<Object> last = v.remove(v.size()-1);
			return last;
		}
		return null;
	}
	boolean update(String strTableName,Hashtable<String, Object> htblColNameValue,Comparable ClusteringKey,int keyIndex) throws Exception{
		int low =0,hi = v.size()-1,ans=-1;
		while(low<=hi)
		{
			int mid = (low+hi)/2;
			Vector<Object> curr = v.get(mid);
			Comparable key = (Comparable) curr.get(keyIndex);
			int x = key.compareTo(ClusteringKey);
			if(x <= 0)
			{
				if(x == 0)
					ans = mid;
				hi = mid-1;
			}
			else
			{
				low = mid+1;
			}
		}
		Vector<String>colNames= new Vector<String>();
		BufferedReader br=new BufferedReader(new FileReader("metadata.csv"));
		while(br.ready()) 
		{
			String[]row = br.readLine().split(",");
			if(row[0].equals(strTableName))
			{
				colNames.add(row[1]);
			}
			
		}

		if(ans == -1) return false; // Maybe an execption
		for(int i = ans;i<v.size();i++) {
			Vector<Object> curr = v.get(i);
			Comparable key = (Comparable) curr.get(keyIndex);
			int x = key.compareTo(ClusteringKey);
			if(x>0) return false;
			for(int j=0;j<colNames.size();j++) {
				if(htblColNameValue.containsKey(colNames.get(j))){
					curr.set(j, htblColNameValue.get(colNames.get(j)));
				}
			}
		}
		return true;
	}
	
	public Vector<Object> removeTupleAt(int index) {
		// not last
		if(v.size() == N)
		{
			return v.remove(index);
		}
		return null;
	}
	
	public static void main(String[] args) {

	}
}
