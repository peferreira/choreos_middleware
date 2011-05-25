import org.apache.axis2.wsdl.WSDL2Java;


public class CodeGenerator {
    public static void main(String[] args) throws Exception {
        WSDL2Java.main(new String[] {
                       "-d", "src/main/java",
                        "-p", "br.usp.ime.ccsl.choreos.middleware.spike",
                        "-s", 
                        "-wv", "1.1",
                        "-ss",
                        "-sd",
                        "-ssi",
                        "-uri", "src/main/resources/RoleAssignmentService.wsdl"
                        });
        System.out.println("Done!");
    }

}
