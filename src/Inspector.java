import java.lang.reflect.*;
import java.io.*;

public class Inspector {
	
	public void inspect(Object obj, boolean recursive) {
		Class c = obj.getClass();
		inspectClass(c, obj, recursive, 0);
	}
	
	
	//TODO: change to pass current object instead of null
	private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
		try {
			recursive = false;
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
				System.out.println(tabs + "-" + interfaces[i].getName());
			}

/*************** explore interfaces ******************************/
			for(int i = 0; i < interfaces.length; i++) {
				System.out.println(tabs + "Stepping into interface: " + interfaces[i].getName());
				inspectClass(interfaces[i], obj, recursive, depth+1);
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
				Class[] constParams = constructors[i].getParameterTypes();
				for(int j = 0; j < constParams.length; j++) {
					System.out.println(tabs + "---" + constParams[j].getName());
				}
				
/*************** modifiers ******************************/
				int constModifier = constructors[i].getModifiers();
				System.out.println(tabs + "----" + constructors[i].getName() + " modifier: " + Modifier.toString(constModifier));
			}
			

/*************** METHODS ******************************/
			
			Method[] methods = c.getDeclaredMethods();
			if(methods.length > 0) {
				System.out.println(tabs + "Methods of " + c.getName());
			}
			
/*************** name******************************/
			for(int i = 0; i < methods.length; i++) {
				System.out.println(tabs + "-" + methods[i].getName());
				
/*************** exceptions thrown ******************************/
				Class[] exceptions = methods[i].getExceptionTypes();
				for(int j = 0; j < exceptions.length; j++) {
					System.out.println(tabs + "--" + exceptions[j].getName());
				}
			
/*************** parameters ******************************/
				
				Class[] methParams = methods[i].getParameterTypes();
				for(int j = 0; j < methParams.length; j++) {
					System.out.println(tabs + "---" + methParams[j].getName());
				}
				
/*************** return type ******************************/
			    Class methReturn = methods[i].getReturnType();
			    System.out.println(tabs + " " + methReturn.getName());
			    
/*************** modifiers ******************************/
			    int methModifier = methods[i].getModifiers();
			    System.out.println(tabs + "-" + methods[i].getName() + " modifier: " + Modifier.toString(methModifier));
			}
			
/*************** FIELDS ******************************/
			
			Field[] fields = c.getDeclaredFields();
			if(fields.length > 0) {
				System.out.println(tabs + "Fields of " + c.getName());
			}
/*************** name ******************************/
			
			for(int i = 0; i < fields.length; i++) {
				System.out.println(tabs + " " + fields[i].getName());
				
/*************** type ******************************/
				System.out.println(tabs + "  " + fields[i].getType().getName());
				
/*************** modifiers ******************************/
				int fieldModifier = fields[i].getModifiers();
				System.out.println(tabs + "  " + fields[i].getName() + " modifier: " + Modifier.toString(fieldModifier));
				
/*************** value ******************************/
				if(obj != null) {
					fields[i].setAccessible(true);
					
					Object instance = fields[i].get(obj);
					if(instance != null) {
						
						if(recursive) {
							System.out.println(tabs + "Stepping into field: " + fields[i].getName()+ "@" + Integer.toHexString(System.identityHashCode(instance)));
							inspectClass(instance.getClass(), instance, recursive, depth+1);
						}
						
						//Dealing with array
						if(instance.getClass().isArray()) {
							int length = Array.getLength(instance);
							for ( int k = 0; k < length; k++ ) {
								Object element = Array.get(instance, k);
								if(element.getClass().isPrimitive()) {
									System.out.println(tabs + " " + element);
								}
								else {
									System.out.println(tabs + " " + Integer.toHexString(System.identityHashCode(element)));
								}
								
								
							}
						}
						
						//dealing with objects
						else if(!fields[i].getType().isPrimitive()) {
							System.out.println(tabs + " " + instance.getClass().getName()+ "@" + Integer.toHexString(System.identityHashCode(instance)));
							
						}
						
						//dealing with primitives
						else {
							System.out.println(tabs + " = " + instance);
						}
						
						
						
						
						
					}
					else {
						System.out.println(tabs + " = " + instance);
					}
				}
				
				
				
			}
			    //current val of each field
			        //if field is an object ref, and recursive is set to false,
			        //  print out "reference value" directly
			        //  (name of object's class + identity hash code
			
			    //if recursive is true, inspect each field/array obj as well.
			
//			Field[] fields = c.getDeclaredFields();
		}
		catch (Exception e){
			System.out.println("exception: " + e);
			e.printStackTrace();
		}
		
	}
}
