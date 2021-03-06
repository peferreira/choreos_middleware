package br.usp.ime.ccsl.choreos.middleware.proxy.codegenerator;

import java.net.URL;

import org.apache.cxf.tools.wsdlto.WSDLToJava;

/*
 * WSDLToJava - From Apache CXF
 * 
 * For more detailed information checkout 
 * http://cxf.apache.org/docs/wsdl-to-java.html
 * 
 *  wsdl2java -fe <frontend name>
 * -db <data binding name>
 * -wv <[wsdl version]> 
 * -p <[wsdl namespace =]Package Name> 
 * -sn <service-name> 
 * -b <binding-name>  
 * -catalog <catalog-file-name> 
 * -d <output-directory> 
 * -compile 
 * -classdir <compile-classes-directory> 
 * -impl 
 * -server 
 * -client 
 * -all  
 * -autoNameResolution 
 * -defaultValues<=class name for DefaultValueProvider> 
 * -ant  
 * -nexclude <schema namespace [= java packagename]>
 * -exsh <(true, false)> 
 * -dns <(true, false)> 
 * -dex <(true, false)> 
 * -validate 
 * -keep  
 * -wsdlLocation <wsdlLocation attribute> 
 * -xjc<xjc arguments> 
 * -noAddressBinding 
 * -h 
 * -v -verbose -quiet 
 * -useFQCNForFaultSerialVersionUID <wsdlurl>
 * */
public class CodeGenerator {

    private CodeGeneratorHelper codeGeneratorHelper = new CodeGeneratorHelper();

    public void setCodeGeneratorHelper(CodeGeneratorHelper cgh) {
	codeGeneratorHelper = cgh;
    }
    
    public void generateServerClasses(URL wsdlInterfaceDescriptor) {
	String codeDirectory = generateServerCode(wsdlInterfaceDescriptor);
	codeGeneratorHelper.compileJavaFiles(codeDirectory, CodeGeneratorHelper.TARGET_GENERATED_SERVER_JAVA_CODE);
    }

    public String generateServerCode(URL wsdlInterfaceDescriptor) {
	return codeGeneratorHelper.generateJavaCode(wsdlInterfaceDescriptor, CodeGeneratorHelper.SERVER);
    }

    public String generateClientCode(URL wsdlInterfaceDescriptor) {
	return codeGeneratorHelper.generateJavaCode(wsdlInterfaceDescriptor, false);
    }

    public static void main(String[] args) {
	WSDLToJava.main(args);
    }
}
