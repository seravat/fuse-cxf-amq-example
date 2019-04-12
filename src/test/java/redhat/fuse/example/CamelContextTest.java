package redhat.fuse.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redhat.fuse.example.service.types.PersonType;


public class CamelContextTest extends CamelSpringTestSupport{
	

	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;
	//BrokerService broker = null;
	
    @Produce(uri = "cxf:bean:wsEndpoint?dataFormat=RAW") 
    protected ProducerTemplate inputProducerTemplate; 
	
	@Test
	public void testCamelRoute() throws Exception {
		
/*		BrokerService broker = new BrokerService();
		TransportConnector connector = new TransportConnector();
		connector.setUri(new URI("tcp://localhost:0"));
		broker.addConnector(connector);
		broker.start();*/
		
		String xmlRequest = readFile("src/test/data/SoapRequest.xml");
	    
	    if(xmlRequest != null) {
	    	inputProducerTemplate.sendBodyAndHeader(xmlRequest, "METHOD", "createPerson"); 
	    }
		
		resultEndpoint.expectedBodiesReceived(readFile("src/test/data/SoapResponse.xml"));
		resultEndpoint.assertIsSatisfied();
	}	
	
	private String readFile(String filePath) throws Exception {
		String content;
		FileInputStream fis = new FileInputStream(filePath);
		try {
			content = createCamelContext().getTypeConverter().convertTo(String.class, fis);
		} finally {
			fis.close();
		}
		return content;
	}
	
	@Override
	protected ClassPathXmlApplicationContext createApplicationContext() {
		ClassPathXmlApplicationContext classPathXmlApplicationContext = null;

		classPathXmlApplicationContext = new ClassPathXmlApplicationContext(
				new String[] { "BundleContext.xml" });

		return classPathXmlApplicationContext;
	}
	
    
	  public PersonType unmarshalPersonXML(String personXML) throws FileNotFoundException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(PersonType.class);
		    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		    StringReader reader = new StringReader(personXML);
		    PersonType person = (PersonType) jaxbUnmarshaller.unmarshal(reader);

		    return person;
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	  }

}
