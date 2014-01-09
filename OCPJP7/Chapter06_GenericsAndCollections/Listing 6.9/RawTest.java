/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
------------------------------------------------------------------------------*/
import java.util.*;

//This program demonstrates usage of raw types along with generics
class RawTest{
	public static void main(String []args) {
		List list = new LinkedList();
    	list.add("First");
    	list.add("Second");
    	List<String> strList = list;  //#1
    	for	( Iterator<String> itemItr = strList.iterator(); itemItr.hasNext(); )
    		System.out.println("Item : " + itemItr.next());

		List<String> strList2 = new LinkedList<>();
		strList2.add("3");
		strList2.add("4");
		List list2 = strList2;	//#2
		for(Iterator<String> itemItr = list2.iterator(); itemItr.hasNext();)
    			System.out.println("Item : " + itemItr.next());

    	List list3 = new LinkedList<>();
		list3.add(5);
		list3.add(6);
		for(Iterator itemItr = list3.iterator(); itemItr.hasNext();)
    			System.out.println("Item : " + itemItr.next());

    	List list4 = new LinkedList<>();
		list4.add(7);
		list4.add(8);
    	System.out.println("Item : " + list4);
	}
}
