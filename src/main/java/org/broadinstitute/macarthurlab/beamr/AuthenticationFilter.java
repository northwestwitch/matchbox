/**
 * 
 */
package org.broadinstitute.macarthurlab.beamr;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter implements Filter{
	private static final String X_AUTH_TOKEN_HEADER="X-Auth-Token";
	private static final String ACCEPT_HEADER="Accept";



	@Override
    public void destroy() {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {

            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            
            //----extract and validate headers from request----
            
            //Unauthorized
            if (!validateXAuthToken(request.getHeader(AuthenticationFilter.getxAuthTokenHeader()))){
            	response.sendError(401);
            }
            //unsupported API version
            if (!validateAcceptHeader(request.getHeader(AuthenticationFilter.getAcceptHeader()))){
            	response.sendError(406);
            }
            chain.doFilter(request, response);
    }

    
    /**
     * Validate this accept header
     * @param acceptHeader	An accept header from a request
     * @return	true if validated, false otherwise
     */
    private boolean validateAcceptHeader(String acceptHeader){
    	if (acceptHeader.equals("application/vnd.ga4gh.matchmaker.v0.1+json")){
    		return true;
    	}
    	return false;
    }
   
    
    
    /**
     * Validate this accept header
     * @param xAuthToken	A X-Auth-Token header from a request
     * @return	true if validated, false otherwise
     */
    private boolean validateXAuthToken(String xAuthToken){
    	//TODO past testing, swap this with a Mongo entity (AuthorizedToken.find({}))Call to see if in system
    	if (xAuthToken.equals("854a439d278df4283bf5498ab020336cdc416a7d")){
    		return true;
    	}
    	return false;
    }
    
    
    /**
	 * @return the xAuthTokenHeader
	 */
	public static String getxAuthTokenHeader() {
		return X_AUTH_TOKEN_HEADER;
	}
	
	/**
	 * @return the acceptHeader
	 */
	public static String getAcceptHeader() {
		return ACCEPT_HEADER;
	}

	@Override
	/**
	 * Part of implementation contract, but not needed 
	 */
    public void init(FilterConfig arg0) throws ServletException {
    }

}