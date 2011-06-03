
/*
 * 
 */

package hello;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.4.0
 * 2011-06-03T11:50:50.150-03:00
 * Generated source version: 2.4.0
 * 
 */


@WebServiceClient(name = "HelloWorld8081Service", 
                  wsdlLocation = "file:/home/felps/choreos/choreos_middleware/ChoreosWebServiceProxy/target/test-classes/hello.wsdl",
                  targetNamespace = "http://hello/") 
public class HelloWorld8081Service extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://hello/", "HelloWorld8081Service");
    public final static QName HelloWorld8081Port = new QName("http://hello/", "HelloWorld8081Port");
    static {
        URL url = null;
        try {
            url = new URL("file:/home/felps/choreos/choreos_middleware/ChoreosWebServiceProxy/target/test-classes/hello.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(HelloWorld8081Service.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/home/felps/choreos/choreos_middleware/ChoreosWebServiceProxy/target/test-classes/hello.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public HelloWorld8081Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public HelloWorld8081Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public HelloWorld8081Service() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     * 
     * @return
     *     returns HelloWorld8081
     */
    @WebEndpoint(name = "HelloWorld8081Port")
    public HelloWorld8081 getHelloWorld8081Port() {
        return super.getPort(HelloWorld8081Port, HelloWorld8081.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns HelloWorld8081
     */
    @WebEndpoint(name = "HelloWorld8081Port")
    public HelloWorld8081 getHelloWorld8081Port(WebServiceFeature... features) {
        return super.getPort(HelloWorld8081Port, HelloWorld8081.class, features);
    }

}