package br.uece.sisdoc.configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

//filtra outras requisições para verificar a presença do JWT no header
public class JWTAuthenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", "*");
    	    	
    	if(((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
    		
    		((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers", "*");
    		((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
    		((HttpServletResponse) response).setHeader("Access-Control-Max-Age", "3600");
    		((HttpServletResponse) response).setStatus(200);
    		return;
    	}
		
		Authentication authentication = TokenAuthenticationService
				.getAuthentication((HttpServletRequest) request);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
		
	}

	
	
}
