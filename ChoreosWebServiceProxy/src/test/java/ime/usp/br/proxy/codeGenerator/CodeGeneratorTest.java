package ime.usp.br.proxy.codeGenerator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.URL;

import org.junit.Test;


public class CodeGeneratorTest {

    @Test
    public void generateServerCodeTest() throws Exception {
	CodeGeneratorHelper cgh = mock(CodeGeneratorHelper.class);
	CodeGenerator cg = new CodeGenerator();
	cg.setCodeGeneratorHelper(cgh);
	URL url = new URL("http://bla.br");
	cg.generateServerCode(url);
	
	verify(cgh).generateJavaCode(url, true);
	verify(cgh).includeProxyCodeIntoGeneratedJavaFiles(url);
    }
    
    @Test
    public void generateServerClassesTest() throws Exception {
	CodeGeneratorHelper cgh = mock(CodeGeneratorHelper.class);
	CodeGenerator cg = new CodeGenerator();
	URL url = new URL("http://bla.br");
	
	when(cgh.includeProxyCodeIntoGeneratedJavaFiles(url)).thenReturn("generateServerCodeReturn");
	
	cg.setCodeGeneratorHelper(cgh);
	
	cg.generateServerClasses(url);
	
	verify(cgh).generateJavaCode(url, true);
	verify(cgh).includeProxyCodeIntoGeneratedJavaFiles(url);
	verify(cgh).compileJavaFiles("generateServerCodeReturn", CodeGeneratorHelper.TARGET_GENERATED_SERVER_JAVA_CODE);
    }
}