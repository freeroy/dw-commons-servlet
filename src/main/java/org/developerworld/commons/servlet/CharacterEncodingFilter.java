package org.developerworld.commons.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 编码转换器
 * @author Roy Huang
 *
 */
public class CharacterEncodingFilter extends AbstractUrlFilter {

	private String encoding;

	private boolean forceEncoding = false;

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setForceEncoding(boolean forceEncoding) {
		this.forceEncoding = forceEncoding;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		if (filterConfig.getInitParameter("encoding") != null)
			setEncoding(filterConfig.getInitParameter("encoding"));
		if (filterConfig.getInitParameter("forceEncoding") != null)
			setForceEncoding(Boolean.valueOf(filterConfig.getInitParameter("forceEncoding")));
	}

	@Override
	public void doFilterWhenUrlPass(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		if (this.encoding != null && (this.forceEncoding || request.getCharacterEncoding() == null)) {
			request.setCharacterEncoding(this.encoding);
			if (this.forceEncoding) {
				response.setCharacterEncoding(this.encoding);
			}
		}
		filterChain.doFilter(request, response);
	}

}
