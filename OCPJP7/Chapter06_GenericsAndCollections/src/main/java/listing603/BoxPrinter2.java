package listing603;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805: 
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
------------------------------------------------------------------------------*/
// The program demonstrates "Object" based implementation and associated lack of type-safety 
class BoxPrinter2 {
	private Object val; 
	public BoxPrinter2(Object arg) {
		val = arg; 
	}  	
	public String toString() {
		return "[" + val + "]"; 
	} 
	public Object getValue() {
		return val; 	
	}
} 
 