package br.usp.ime.ccsl.choreos.middleware.proxy;


import java.net.MalformedURLException;
import java.net.URL;

import br.usp.ime.ccsl.choreos.middleware.proxy.codegenerator.CodeGenerator;
import br.usp.ime.ccsl.choreos.middleware.proxy.codegenerator.CodeGeneratorHelper;
import br.usp.ime.ccsl.choreos.wsdl.WsdlUtils;

public class ProxyFactory {
    public Object generateProxyImplementor(URL wsdlLocation) {

	generateServerClasses(wsdlLocation.toExternalForm());
	Object proxyInstance = getProxyInstance(wsdlLocation);

	return proxyInstance;
    }

    public static String getPortName(URL wsdlLocation) {
	return WsdlUtils.getPortName(wsdlLocation);
    }

    // TODO: Test!
    public String getClassName(URL wsdlLocation) {
	CodeGeneratorHelper cgh = new CodeGeneratorHelper();

	String destinationFolder = cgh.getDestinationFolder("", wsdlLocation);
	String packageName = destinationFolder.substring(1).replaceAll("/", "\\.");
	String className = packageName + getPortName(wsdlLocation);

	return className;
    }

    private Object getProxyInstance(URL wsdlLocation) {
	String className = getClassName(wsdlLocation)+"Impl";
	return getClassByName(className);
    }

    public Object getClassByName(String className) {
	Class<?> clazz = null;
	Object implementor = null;
	try {

	    clazz = Class.forName(className);
	    implementor = clazz.newInstance();
	    
	} catch (ClassNotFoundException e) {
	    System.out.println("Found no such class " + className + " in current directory");
	    e.printStackTrace();
	    return null;
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}
	return implementor;
    }

    private void generateServerClasses(String host) {
	CodeGenerator codeGenerator = new CodeGenerator();
	try {
	    codeGenerator.generateServerClasses(new URL(host));
	} catch (MalformedURLException e) {
	    System.out.println("Verify that the URL " + host + " is not wrong.");
	    e.printStackTrace();
	}
    }

}
