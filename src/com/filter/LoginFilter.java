package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter{

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		Boolean flag = false;
		
		if(req instanceof HttpServletRequest){
			
			HttpServletRequest request = (HttpServletRequest)req;
			
			HttpSession session = request.getSession();
			
			if(session != null){
				
				if(session.getAttribute("customInfo")!=null){
					
					flag = true;
					
					
				}
				
				
			}
			
			
		}
		
		if(flag){
			chain.doFilter(req, resp);
			
		}else{
			
			RequestDispatcher rd = req.getRequestDispatcher("/member/main.jsp");
			rd.forward(req, resp);
			
		}
		
		
		
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
