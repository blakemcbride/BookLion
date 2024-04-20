package booklion.client.utils;

import java.util.LinkedList;

import com.google.gwt.dom.client.Element;

/**
 * @author Blake McBride
 */
public class XMLUtils {
	
	public static Element findElement(Element e, boolean trace, int ... level)  {
		return elementWorker(e, trace, new LinkedList<Integer>(), level);
	}
	
	public static Element findElement(Element e, int ... level)  {
		return elementWorker(e, false, new LinkedList<Integer>(), level);
	}
	
	public static void traceElements(Element e, int ... level)  {
		elementWorker(e, true, new LinkedList<Integer>(), level);
	}
	
	private static Element elementWorker(Element e, boolean trace, LinkedList<Integer> ll, int ... level) {
		int syblingNum = 0;
		boolean correctLevel = level != null  &&  ll.size() + 1 == level.length;
		while (e != null) {
			if (trace) {
				for (int i : ll)
					System.err.print("" + i + " ");
				System.err.println("" + syblingNum + " " + e.getClassName());
			}
			if (correctLevel  &&  level[level.length-1] == syblingNum) {
				int idx = 0;
				boolean match = true ;
				for (int i : ll)
					if (i != level[idx++]) {
						match = false;
						break;
					}
				if (match)
					return e;
			}
			
			ll.addLast(syblingNum);
			Element ret = elementWorker(e.getFirstChildElement(), trace, ll, level);
			if (ret != null)
				return ret;
			ll.removeLast();
			e = e.getNextSiblingElement();
			syblingNum++;
		}
		return null;
	}

}
