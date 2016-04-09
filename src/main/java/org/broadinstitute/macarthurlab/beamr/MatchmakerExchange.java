package org.broadinstitute.macarthurlab.beamr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */

@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class MatchmakerExchange 
{
	
    public static void main( String[] args )
    {
        System.out.println( "Starting server.." );
        ApplicationContext context =
        	    new ClassPathXmlApplicationContext(new String[] {"services.xml", "spring.xml"});
        
        SpringApplication.run(MatchmakerExchange.class, args);
    }
}
