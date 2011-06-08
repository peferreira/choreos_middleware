package ime.usp.br.proxy;

import ime.usp.br.proxy.codeGenerator.CodeGenerator;
import ime.usp.br.proxy.codeGenerator.CodeGeneratorHelper;
import ime.usp.br.proxy.generic.GenericImpl;

import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;

public class ProxyFactory {
    public Object generateProxyImplementor(URL wsdlLocation) {

	generateServerClasses(wsdlLocation.toExternalForm());
	Object proxyInstance = getProxyInstance(wsdlLocation);

	return proxyInstance;
    }

    public static String getPortName(URL wsdlLocation) {
	CodeGeneratorHelper cgh = new CodeGeneratorHelper();
	return cgh.getPortName(wsdlLocation);
    }

    public String getClassLocation(URL wsdlLocation) {
	CodeGeneratorHelper cgh = new CodeGeneratorHelper();

	String namespace = cgh.getNamespace(wsdlLocation);
	String destinationFolder = cgh.getDestinationFolder("", namespace);
	String packageName = destinationFolder.substring(1).replaceAll("/", "\\.");
	String className = packageName + getPortName(wsdlLocation);

	return className;
    }

    // Class is ** SUPPOSED ** to be generic. No need for a warning!
    @SuppressWarnings("unchecked")
    public Object getProxyInstance(URL wsdlLocation) {
	Class cls = null;
	String className = getClassLocation(wsdlLocation);
	Object implementor = null;
	try {
	    cls = Class.forName(className);

	    implementor = Proxy.newProxyInstance(cls.getClassLoader(), cls.getClasses(), new GenericImpl());

	} catch (ClassNotFoundException e) {
	    System.out.println("Found no such class " + className + " in current directory");
	    e.printStackTrace();
	    return null;
	}
	// ProxyInterceptor proxyService = new ProxyInterceptor();

	// server.getEndpoint().getInInterceptors().add(proxyService);
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
