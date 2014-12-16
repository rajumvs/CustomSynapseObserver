package com.zeomega;

import java.io.IOException;
import org.apache.synapse.config.AbstractSynapseObserver;
import org.apache.synapse.core.axis2.ProxyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;


public class CustomSynapseObserverForLogging extends AbstractSynapseObserver {
      private static final Log log = LogFactory.getLog(CustomSynapseObserverForLogging.class);

       public void proxyServiceAdded(ProxyService proxy) {
               try {     
                    if(proxy.getName().equals("processbatchfileproxy")) {
                        log.info(proxy.getName());
                    } else {
                        setLogger(proxy);
                    }

               } catch (IOException e) {
                       log.error("CustomProxyObserver could not set service level logger for the proxy : " +
                                 proxy.getName(), e);
               }
       }

       public void proxyServiceRemoved(ProxyService proxy) {
               try {
                       setLogger(proxy);
               } catch (IOException e) {
                       log.error("CustomProxyObserver could not set service level logger for the proxy : " +
                                 proxy.getName(), e);
               }
       }


       private void setLogger(ProxyService proxy) throws IOException {

               String filename = "logs/" + proxy.getName() + "/" + proxy.getName() + ".log";
               String datePattern = "yyyy-MM-dd";
               String SYSTEM_LOG_PATTERN = "[%d] %5p - %x %m {%c}%n";

               PatternLayout layout = new PatternLayout(SYSTEM_LOG_PATTERN);

               DailyRollingFileAppender appender = null;

               appender = new DailyRollingFileAppender(layout, filename, datePattern);

               Logger proxyLogger = Logger.getLogger("SERVICE_LOGGER." + proxy.getName());

               proxyLogger.setLevel(Level.ALL);

               proxyLogger.setAdditivity(false);
               proxyLogger.addAppender(appender);

       }

}
