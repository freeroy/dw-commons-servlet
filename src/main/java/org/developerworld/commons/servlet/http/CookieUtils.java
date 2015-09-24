package org.developerworld.commons.servlet.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie管理类
 * 
 * @author Roy Huang
 * @version 20110930
 * 
 */
public class CookieUtils {

	/**
	 * 获取cookie数组
	 * 
	 * @param request
	 * @return
	 */
	public static Cookie[] getCookieArray(HttpServletRequest request) {
		return request.getCookies();
	}

	/**
	 * 获取cookie集和
	 * 
	 * @param request
	 * @return
	 */
	public static List<Cookie> getCookieList(HttpServletRequest request) {
		List<Cookie> rst = new ArrayList<Cookie>();
		Cookie[] cookies = getCookieArray(request);
		if (cookies != null) {
			for (Cookie cookie : cookies)
				rst.add(cookie);
		}
		return rst;
	}

	/**
	 * 获取cookie key/value
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, Cookie> getCookieMap(HttpServletRequest request) {
		Map<String, Cookie> rst = new HashMap<String, Cookie>();
		Cookie[] cookies = getCookieArray(request);
		if (cookies != null) {
			for (Cookie cookie : cookies)
				rst.put(cookie.getName(), cookie);
		}
		return rst;
	}

	/**
	 * 获取指定cookie对象
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie rst = null;
		Cookie[] cookies = getCookieArray(request);
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					rst = cookie;
					break;
				}
			}
		}
		return rst;
	}

	/**
	 * 获取执行cookie的值
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		String rst = null;
		Cookie cookie = getCookie(request, name);
		if (cookie != null)
			rst = cookie.getValue();
		return rst;
	}

	/**
	 * 获取cookie的值
	 * 
	 * @param request
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request,
			String name, String defaultValue) {
		String rst = getCookieValue(request, name);
		if (rst == null)
			rst = defaultValue;
		return rst;
	}

	/**
	 * 添加一个cookie
	 * 
	 * @param response
	 * @param cookie
	 */
	public static void addCookie(HttpServletResponse response, Cookie cookie) {
		response.addCookie(cookie);
	}

	/**
	 * 添加一堆cookie
	 * 
	 * @param response
	 * @param cookies
	 */
	public static void addCookies(HttpServletResponse response,
			Collection<Cookie> cookies) {
		for (Cookie cookie : cookies)
			response.addCookie(cookie);
	}

	/**
	 * 添加一堆cookie
	 * 
	 * @param response
	 * @param cookies
	 */
	public static void addCookies(HttpServletResponse response,
			Map<String, Cookie> cookies) {
		addCookies(response, cookies.values());
	}

	/**
	 * 删除cookie
	 * 
	 * @param response
	 * @param cookie
	 */
	public static void removeCookie(HttpServletResponse response, Cookie cookie) {
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	/**
	 * 删除cookie
	 * 
	 * @param response
	 * @param name
	 */
	public static void removeCookie(HttpServletResponse response, String name) {
		removeCookie(response, new Cookie(name, ""));
	}

	/**
	 * 删除一堆cookie
	 * 
	 * @param response
	 * @param cookies
	 */
	public static void removeCookies(HttpServletResponse response,
			Collection<Cookie> cookies) {
		for (Cookie cookie : cookies)
			removeCookie(response, cookie);
	}

	/**
	 * 删除一堆cookie
	 * 
	 * @param response
	 * @param cookies
	 */
	public static void removeCookies(HttpServletResponse response,
			Map<String, Cookie> cookies) {
		removeCookies(response, cookies.values());
	}

	/**
	 * 清空所有cookie
	 * 
	 * @param request
	 * @param response
	 */
	public static void clearCookies(HttpServletRequest request,
			HttpServletResponse response) {
		List<Cookie> cookies = getCookieList(request);
		for (Cookie cookie : cookies)
			removeCookie(response, cookie);
	}
}
