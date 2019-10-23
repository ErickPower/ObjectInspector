import java.lang.reflect.*;
import java.io.*;

public class Inspector {
	
	public void inspect(Object obj, boolean recursive) {
		Class c = obj.getClass();
		inspectClass(c, obj, recursive, 0);
	}
	
	private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
		if(c == null) {
			c = obj.getClass();
		}
		try {
			String tabs = new String(new char[depth]).replace("\0", "\t"); //Code from https://stackoverflow.com/a/4903603
			
/*************** DECLARING CLASS ******************************/
			System.out.println(tabs + c.getName());
			
			
/*************** SUPERCLASS ******************************/
			Class superClass = c.getSuperclass();
			
/*************** explore superclass ******************************/
			if(superClass != null) {
				System.out.println(tabs + "Superclass: " + superClass.getName());
				inspectClass(c.getSuperclass(), null, recursive, depth+1);
			}
			
			
/*************** INTERFACES ******************************/
			Class[] interfaces = c.getInterfaces();
			if(interfaces.length > 0) {
				System.out.println(tabs + "Interfaces of " + c.getName());
			}
			for(int i = 0; i < interfaces.length; i++) {
				System.out.println(tabs + "-" + interfaces[i].getName());
			}

/*************** explore interfaces ******************************/
			for(int i = 0; i < interfaces.length; i++) {
				inspectClass(c.getSuperclass(), null, recursive, depth+1);
			}
			
			
/*************** CONSTRUCTORS ******************************/
			Constructor[] constructors = c.getDeclaredConstructors();
			if(constructors.length > 0) {
				System.out.println(tabs + "Constructors of " + c.getName());
			}
			
/*************** names ******************************/
			for(int i = 0; i < constructors.length; i++) {
				System.out.println(tabs + "--" + constructors[i].getName());
				
/*************** parameters ******************************/
				Class[] params = constructors[i].getParameterTypes();
				for(int j = 0; j < params.length; j++) {
					System.out.println(tabs + "---" + params[j].getName());
				}
				
/*************** modifiers ******************************/
				int modif = constructors[i].getModifiers();
				System.out.println(tabs + "----" + constructors[i].getName() + " modifier: " + Modifier.toString(modif));
			}
			


			
			//methods of the class
			    //name
			    //exceptions thrown
			    //parameter types
			    //return type
			    //modifiers
			
			//fields the class declares
			    //name
			    //type
			    //modifiers
			    //current val of each field
			        //if field is an object ref, and recursive is set to false,
			        //  print out "reference value" directly
			        //  (name of object's class + identity hash code
			
			    //if recursive is true, inspect each field/array obj as well.
			
			Field[] fields = c.getDeclaredFields();
		}
		catch (Exception e){
			System.out.println("exception: " + e);
			e.printStackTrace();
		}
		
	}
}
