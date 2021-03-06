package interceptor;

import java.util.List;

import org.apache.cxf.Bus;
import org.apache.cxf.binding.Binding;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.endpoint.ServerRegistry;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.InterceptorChain;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.Service;

public class ProxyInterceptor extends AbstractSoapInterceptor {

    public ProxyInterceptor() {
	super(Phase.READ);
    }

    public void handleMessage(SoapMessage message) throws Fault {
	Exchange ex = message.getExchange();

	Bus bus = CXFBusFactory.getDefaultBus();

	ServerRegistry serverRegistry = bus.getExtension(ServerRegistry.class);
	List<Server> servers = serverRegistry.getServers();
	Endpoint ep = ex.getEndpoint();
	ep = chooseServerEndpoint(servers, ep);

	ex.put(Endpoint.class, ep);
	ex.put(Binding.class, ep.getBinding());
	ex.put(Service.class, ep.getService());

	InterceptorChain chain = message.getInterceptorChain();
	chain.add(ep.getInInterceptors());
	chain.add(ep.getBinding().getInInterceptors());
	chain.add(ep.getService().getInInterceptors());

	chain.setFaultObserver(ep.getOutFaultObserver());
    }

    private Endpoint chooseServerEndpoint(List<Server> servers, Endpoint ep) {
	
	for (Server server : servers) {
	    if (meetChangingCriteria(ep, server.getEndpoint())) {
		System.out.println("Troquei o ep do address de " + server.getEndpoint().getEndpointInfo().getAddress());
		ep = server.getEndpoint();
		System.out.println("para");
	    }
	    System.out.println(server.getEndpoint().getEndpointInfo().getAddress());
	}
	return ep;
    }

    private boolean meetChangingCriteria(Endpoint original, Endpoint destination) {

	return !destination.getEndpointInfo().getAddress().equals(original.getEndpointInfo().getAddress());
    }

}
