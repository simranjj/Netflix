package handler;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author simra
 */

public class SeriesTitleHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public Set<QName> getHeaders() {
       return new HashSet<QName>();
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
         SOAPMessage msg = context.getMessage();
         try {
            NodeList elements = msg.getSOAPBody().getElementsByTagName("CreateSeries");
            if(elements.item(0) != null){
                Node series = elements.item(0);
                Element element =(Element) series;
                if("disney".equals(element.getElementsByTagName("title").item(0).getTextContent()));
                    return false;
            }
        } catch (SOAPException ex) {
            Logger.getLogger(SeriesTitleHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
      return false;
    }

    @Override
    public void close(MessageContext context) {
      
    }

}
