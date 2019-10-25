import java.lang.reflect.*;
import java.io.*;

public class Inspector {
	
	public void inspect(Object obj, boolean recursive) {
		Class c = null /* obj.getClass() */;
		inspectClass(c, obj, recursive, 0);
	}
	
	private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
		try {
			//pass c as null when inspecting field objects
			if(c == null){
				c = obj.getClass();
			}
			
			//If the object is a class, then it is an interface, and no instance of the object
			if(obj.getClass().isInstance(Class.class)) {
				obj = null;
			}
			
			String tabs = new String(new char[depth]).replace("\0", "\t"); //Code from https://stackoverflow.com/a/4903603
			
/*************** DECLARING CLASS ******************************/
			System.out.println(tabs + c.getName());
			
			
/*************** SUPERCLASS ******************************/
			Class superClass = c.getSuperclass();
			
/*************** explore superclass ******************************/
			if(superClass != null) {
				System.out.println(tabs + "Stepping into superclass: " + superClass.getName());
				inspectClass(c.getSuperclass(), obj, recursive, depth+1);
			}
			
			
/*************** INTERFACES ******************************/
			Class[] interfaces = c.getInterfaces();
			if(interfaces.length > 0) {
				System.out.println(tabs + "Interfaces of " + c.getName());
			}
			for(int i = 0; i < interfaces.length; i++) {
				System.out.println(tabs + " " + interfaces[i].getName());
			}

/*************** explore interfaces ******************************/
			for(int i = 0; i < interfaces.length; i++) {
				System.out.println(tabs + "Stepping into interface: " + interfaces[i].getName());
				inspectClass(interfaces[i], obj, recursive, depth+1);
			}
			System.out.println("");
			
/*************** CONSTRUCTORS ******************************/
			Constructor[] constructors = c.getDeclaredConstructors();
			if(constructors.length > 0) {
				System.out.println(tabs + "Constructors of " + c.getName());
			}
			

			for(int i = 0; i < constructors.length; i++) {
				
/*************** modifiers ******************************/
				int constModifier = constructors[i].getModifiers();
				System.out.print(tabs + " " + Modifier.toString(constModifier));

/*************** names ******************************/				
				System.out.println(" " + constructors[i].getName());
				
/*************** parameters ******************************/
				Class[] constParams = constructors[i].getParameterTypes();
				if(constParams.length > 0) {
					System.out.println(tabs + "  Parameters: ");
				}
				for(int j = 0; j < constParams.length; j++) {
					System.out.println(tabs + "   " + constParams[j].getName());
				}
				

			}
			System.out.println("");

/*************** METHODS ******************************/
			
			Method[] methods = c.getDeclaredMethods();
			if(methods.length > 0) {
				System.out.println(tabs + "Methods of " + c.getName());
			}
			
			for(int i = 0; i < methods.length; i++) {

/*************** modifiers ******************************/
			    int methModifier = methods[i].getModifiers();
			    System.out.print(tabs + " " + Modifier.toString(methModifier));
			    
/*************** return type ******************************/
			    Class methReturn = methods[i].getReturnType();
			    System.out.print( " " + methReturn.getName());
				
/*************** name******************************/				
				System.out.println(" " + methods[i].getName());
				
/*************** exceptions thrown ******************************/
				Class[] exceptions = methods[i].getExceptionTypes();
				if(exceptions.length > 0) {
					System.out.println(tabs + "  Throws: ");
				}
				for(int j = 0; j < exceptions.length; j++) {
					System.out.println(tabs + "   " + exceptions[j].getName());
				}
			
/*************** parameters ******************************/
				
				Class[] methParams = methods[i].getParameterTypes();
				if(methParams.length > 0) {
					System.out.println(tabs + "  Parameters: ");
				}
				for(int j = 0; j < methParams.length; j++) {
					System.out.println(tabs + "   " + methParams[j].getName());
				}
			    
			}
			
			System.out.println("");
/*************** FIELDS ******************************/
			
			Field[] fields = c.getDeclaredFields();
			if(fields.length > 0) {
				System.out.println(tabs + "Fields of " + c.getName());
			}
			
			for(int i = 0; i < fields.length; i++) {
				
/*************** modifiers ******************************/
				int fieldModifier = fields[i].getModifiers();
				System.out.print(tabs + " " + Modifier.toString(fieldModifier));
				
/*************** type ******************************/
				System.out.print(" " + fields[i].getType().getName());
				
/*************** name ******************************/
				System.out.print(" " + fields[i].getName());
				
/*************** value ******************************/
				if(obj != null) {
					fields[i].setAccessible(true);
					
					Object instance = fields[i].get(obj);
					if(instance != null) {
						
						boolean isObj = true;
						
						//Dealing with array
						if(instance.getClass().isArray()) {
							int length = Array.getLength(instance);
							System.out.print( " = [");
							for ( int k = 0; k < length; k++ ) {
								Object element = Array.get(instance, k);
								if(element == null || element.getClass().isPrimitive()) {
									System.out.print(element);
								}
								else {
									System.out.print(element.toString());
								}
								if(k < length-1) {
									System.out.print(",");
								}
							}
							System.out.println("]");
						}
						
						//dealing with objects
						else if(!fields[i].getType().isPrimitive()) {
							System.out.println(" = " + instance.getClass().getName()+ "@" + Integer.toHexString(System.identityHashCode(instance)));
							
						}
						
						//dealing with primitives
						else {
							System.out.println(" = " + instance);
							isObj = false;
						}
						
						if(recursive && isObj) {
							System.out.println(tabs + "Stepping into field: " + fields[i].getName()+ "@" + Integer.toHexString(System.identityHashCode(instance)));
							inspectClass(null, instance, recursive, depth+1);
						}
					}
					else {
						System.out.println(" = " + instance);
					}
				}
			}
		}
		catch (Exception e){
			System.out.println("Exception: " + e);
			e.printStackTrace();
		}
	}
}
