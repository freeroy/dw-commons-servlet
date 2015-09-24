package org.developerworld.commons.servlet;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.developerworld.commons.lang.StringUtils;

/**
 * 提供过滤路径的Filter 统配表达式优先与正则表达式，排除路径优先包含路径
 * 
 * @author Roy Huang
 * @version 20120117
 * 
 */
public abstract class AbstractUrlFilter implements Filter {

	private static Log log = LogFactory.getLog(AbstractUrlFilter.class);

	public final static String TYPE_NOT_MATCH_NO_WORK = "TYPE_NOT_MATCH_NO_WORK";
	public final static String TYPE_NOT_MATCH_JUMP_FILTER = "TYPE_NOT_MATCH_JUMP_FILTER";

	public AbstractUrlFilter() {
		log.info(this.getClass() + " new ");
	}

	// 表达式
	private String includeRegular;
	private String excludeRegular;
	private String includeWildcard;
	private String excludeWildcard;
	private String filterType;

	public String getIncludeRegular() {
		return includeRegular;
	}

	public String getExcludeRegular() {
		return excludeRegular;
	}

	public String getIncludeWildcard() {
		return includeWildcard;
	}

	public String getExcludeWildcard() {
		return excludeWildcard;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setIncludeRegular(String includeRegular) {
		this.includeRegular = includeRegular;
	}

	public void setExcludeRegular(String excludeRegular) {
		this.excludeRegular = excludeRegular;
	}

	public void setIncludeWildcard(String includeWildcard) {
		this.includeWildcard = includeWildcard;
	}

	public void setExcludeWildcard(String excludeWildcard) {
		this.excludeWildcard = excludeWildcard;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.includeRegular = filterConfig.getInitParameter("includeRegular");
		this.excludeRegular = filterConfig.getInitParameter("excludeRegular");
		this.includeWildcard = filterConfig.getInitParameter("includeWildcard");
		this.excludeWildcard = filterConfig.getInitParameter("excludeWildcard");
		this.filterType = filterConfig.getInitParameter("filterType");
		if(this.filterType==null)
			this.filterType=TYPE_NOT_MATCH_JUMP_FILTER;
		log.info(this.getClass() + " init ");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest _request = (HttpServletRequest) request;
		String url = _request.getServletPath();
		boolean pass = true;
		if (pass && includeRegular != null) {
			Pattern pattern = Pattern.compile(includeRegular);
			Matcher matcher = pattern.matcher(url);
			pass = matcher.matches();
		}
		if (pass && excludeRegular != null) {
			Pattern pattern = Pattern.compile(excludeRegular);
			Matcher matcher = pattern.matcher(url);
			pass = !matcher.matches();
		}
		if (pass && includeWildcard != null)
			pass = StringUtils.wildcardCapture(includeWildcard, url);
		if (pass && excludeWildcard != null)
			pass = !StringUtils.wildcardCapture(excludeWildcard, url);
		// 若通过，则继续执行
		if (pass)
			doFilterWhenUrlPass(request, response, filterChain);
		else
			doWhenUnpass(request, response, filterChain);
	}

	/**
	 * 当不通过就执行
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doWhenUnpass(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		if (TYPE_NOT_MATCH_JUMP_FILTER.equals(filterType))
			arg2.doFilter(arg0, arg1);
	}

	/**
	 * 要求子类必须实现的方法
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws ServletException
	 * @throws IOException
	 */
	abstract public void doFilterWhenUrlPass(ServletRequest arg0,
			ServletResponse arg1, FilterChain arg2) throws IOException,
			ServletException;

	public void destroy() {
		log.info(this.getClass() + " destroy ");
	}

}
