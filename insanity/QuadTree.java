package insanity;

import java.util.LinkedList;

public class QuadTree<Key extends Comparable, Value>
{
	private Node root;
	
	private class Node
	{
		Key x,y;
		Node NW,NE,SE,SW;
		Value value;
		
		Node(Key x, Key y, Value value)
		{
			this.x = x;
			this.y = y;
			this.value = value;
		}
	}
	
	public void insert(Key x, Key y, Value value)
	{
		root = insert(root, x, y, value);
	}
	
	private Node insert(Node h, Key x, Key y, Value value)
	{
		if(h == null)
		{
			return new Node(x,y,value);
		}
		else if(less(x,h.x)&&less(y,h.y))
		{
			h.SW = insert(h.SW,x,y,value);
		}
		else if(less(x,h.x)&&!less(y,h.y))
		{
			System.out.println("a");
			h.NW = insert(h.NW,x,y,value);
		}
		else if(!less(x,h.x)&&less(y,h.y))
		{
			System.out.println("b");
			h.SE = insert(h.SE,x,y,value);
		}
		else if(!less(x,h.x)&&!less(y,h.y))
		{
			System.out.println("c");
			h.NE = insert(h.NE,x,y,value);
		}
		return h;
	}
	
	private boolean less(Key k1, Key k2)
	{
		return k1.compareTo(k2) < 0;
	}
	
	public LinkedList<Value> query2D(Interval2D<Key> rect)
	{
		return query2D(root, rect, new LinkedList<Value>());
	}
	
	private LinkedList<Value> query2D(Node h, Interval2D<Key> rect, LinkedList<Value> l)
	{
		if(h==null) return l;

		Key xmin = rect.intervalX.low;
		Key ymin = rect.intervalY.low;
		Key xmax = rect.intervalX.high;
		Key ymax = rect.intervalY.high;
		
		if(rect.contains(h.x, h.y))
		{
			l.add(h.value);
		}
		if(less(xmin, h.x)&&less(ymin, h.y))
		{
			l = query2D(h.SW, rect, l);
		}
        if(less(xmin, h.x)&&!less(ymax, h.y))
		{
			l = query2D(h.NW, rect, l);
		}
        if(!less(xmax, h.x)&&less(ymin, h.y))
		{
			l = query2D(h.SE, rect, l);
		}
        if(!less(xmax, h.x)&&!less(ymax, h.y)) 
		{
			l = query2D(h.NE, rect, l);
		}
		return l;
	}
}
