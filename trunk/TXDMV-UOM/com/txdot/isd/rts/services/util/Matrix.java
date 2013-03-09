package com.txdot.isd.rts.services.util;

import java.util.*;
/**
 *
 * Matrix.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/21/2004	5.2.0 Merge.  Imported new class.
 * 							Ver 5.2.0			  
 * ---------------------------------------------------------------------
 */
/**
 * This class provides methods for Matrix manipulation and inquiry.  
 * 
 * @version	5.2.0		03/21/2004 
 * @author	Nancy Ting
 * <P>Creation Date:	09/06/2002
 */
public class Matrix extends java.util.AbstractList {
	private java.util.Vector vector;
/**
 * Matrix constructor comment.
 */
public Matrix() 
{
	super();
	vector = new Vector();
}
/**
 * Insert the method's description here.
 * 
 * @param row int
 * @param data java.lang.Object
 */
public void add(int row, Object data) 
{
	((Vector)vector.get(row)).add(data);	
}
/**
 * Insert the method's description here.
 * 
 * @param v java.util.Vector
 */
public void add(Vector v) 
{
	vector.add(v);	
}
/**
 * Insert the method's description here.
 * 
 * @return boolean
 * @param row int
 * @param data java.lang.Object
 */
public boolean contains(int row, Object data) 
{
	Vector v = (Vector)vector.get(row);
	for (int i=0; i<v.size(); i++)
	{
		if (v.get(i).equals(data))
			return true;
	}
	return false;
}
/**
 * Insert the method's description here.
 * 
 * @return boolean
 * @param data java.lang.Object
 */
public boolean contains(Object data)
{
	for (int i=0; i<vector.size(); i++)
	{
		Vector v = (Vector)vector.get(i);
		for (int j=0; j<v.size(); j++)
		{
			if (v.get(j).equals(data))
				return true;
		}
	}
	return false;
}
	/**
	 * Returns the element at the specified position in this list.
	 *
	 * @param index index of element to return.
	 * 
	 * @return the element at the specified position in this list.
	 * @throws IndexOutOfBoundsException if the given index is out of range
	 * 		  (<tt>index &lt; 0 || index &gt;= size()</tt>).
	 */
public Object get(int index) 
{
	return vector.get(index);
}
/**
 * Insert the method's description here.
 * 
 * @return java.lang.Object
 * @param row int
 * @param column int
 */
public Object get(int row, int column) 
{
	Vector v = (Vector)vector.get(row);
	return v.get(column);
}
	/**
	 * Returns an iterator over the elements contained in this collection.
	 *
	 * @return an iterator over the elements contained in this collection.
	 */
public java.util.Iterator iterator() 
{
	return vector.iterator();
}
/**
 * Insert the method's description here.
 * 
 * @param row int
 * @param col int
 * @param data java.lang.Object
 */
public void set(int row, int col, Object data) 
{
	Vector v = (Vector)vector.get(row);
	v.set(col, data);	
}
/**
 * Insert the method's description here.
 * 
 * @param row int
 * @param vector java.util.Vector
 */
public void set(int row, Vector v) 
{
	vector.set(row, v);
}
	/**
	 * Returns the number of elements in this collection.  If this collection
	 * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
	 * <tt>Integer.MAX_VALUE</tt>.
	 * 
	 * @return the number of elements in this collection
	 */
public int size() 
{
	return vector.size();
}
/**
 * Insert the method's description here.
 * 
 * @return int
 * @param col int
 */
public int size(int col) 
{
	return ((Vector)vector.get(col)).size();
}
/**
 * Insert the method's description here.
 * 
 * @return int
 */
public int totalSize()
{
	int size = 0;
	for (int i=0; i<vector.size(); i++)
	{
		Vector v = (Vector)vector.get(i);
		size += v.size();
	}
	return size;
}
}
