import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

public class Page implements java.io.Serializable{
	
	String pageName;
	int clusteringKeyIndex;
	String clusteringKeyType;
	Vector<Vector<Object>> v;
	static int N;
	
	public Page(String pageName,int clusteringKeyIndex,String clusteringKeyType) {
		this.clusteringKeyIndex = clusteringKeyIndex;
		this.clusteringKeyType = clusteringKeyType;
		this.pageName=pageName;
		v = new Vector<Vector<Object>>();
		N=4;
	}
	
	public Vector<Object> insertSorted(Vector<Object> tuple) throws Exception{
		Object mykeyValue = tuple.get(clusteringKeyIndex);
		boolean inserted = false;
		Class keyClass = Class.forName(clusteringKeyType);
		Method method = keyClass.getMethod("compareTo", keyClass);
		for(int i=0;i<v.size();i++)
		{
			Object currKey = v.get(i).get(clusteringKeyIndex);
			int x = (int)method.invoke(mykeyValue, currKey);
			if (x < 0)
			{
				v.insertElementAt(tuple,i);
				inserted = true;
				break;
			}
		}
		if(!inserted)
		{
			v.add(tuple);
		}
		if(v.size()>N)
		{
			Vector<Object> last = v.remove(v.size()-1);
			return last;
		}
		return null;
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
