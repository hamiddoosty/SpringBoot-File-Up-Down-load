package com.tutorialspoint.RestReq.ServletFilter;

import java.io.IOException;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.ws.Response;

import org.springframework.stereotype.Component;

@Component
public class SimpleFilter implements Filter{
	
	@Override
	public void destroy(){
		
	}
	
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {		
		System.out.println("Remote Host:" + arg0.getRemoteHost());
		System.out.println("Remote Address:" + arg0.getRemoteAddr());
		arg2.doFilter(arg0, arg1);			
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
}
