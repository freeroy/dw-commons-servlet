package org.developerworld.commons.servlet.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.developerworld.commons.lang.StringUtils;

/**
 * 包装过的HttpServletResponse
 * 
 * @author Roy Huang
 * @version 20121225
 * 
 */
public class WrapperHttpServletResponse extends HttpServletResponseWrapper {

	private boolean isWriterUsed;
	private boolean isStreamUsed;

	private ByteArrayOutputStream byteArrayOutputStream;
	private ServletOutputStream servletOutputStream;

	private StringWriter stringWriter;
	private PrintWriter printWriter;

	public WrapperHttpServletResponse(HttpServletResponse response) {
		super(response);
	}

	@Override
	public PrintWriter getWriter() {
		if (isStreamUsed)
			throw new IllegalStateException("the output stream is used!");
		isWriterUsed = true;
		if (printWriter == null) {
			stringWriter = new StringWriter();
			printWriter = new PrintWriter(stringWriter);
		}
		return printWriter;
	}

	@Override
	public ServletOutputStream getOutputStream() {
		if (isWriterUsed)
			throw new IllegalStateException("the writer is used!");
		isStreamUsed = true;
		if (servletOutputStream == null) {
			byteArrayOutputStream = new ByteArrayOutputStream();
			servletOutputStream = new WrapperServletOutputStream(
					byteArrayOutputStream);
		}
		return servletOutputStream;
	}

	public void flushBuffer() throws IOException {
		if (isWriterUsed)
			printWriter.flush();
		else if (isStreamUsed)
			servletOutputStream.flush();
	}

	@Override
	public void reset() {
		if (isWriterUsed) {

		} else if (isStreamUsed)
			byteArrayOutputStream.reset();
	}

	@Override
	public void resetBuffer() {
		reset();
	}

	/**
	 * 获取reponse内容
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getResponseContentString() throws IOException {
		String rst = null;
		if (isWriterUsed) {
			if (printWriter != null)
				printWriter.close();
			rst = stringWriter.toString();
		} else if (isStreamUsed) {
			if (servletOutputStream != null)
				servletOutputStream.close();
			if (StringUtils.isNotBlank(getCharacterEncoding()))
				return byteArrayOutputStream.toString(getCharacterEncoding());
			else
				return byteArrayOutputStream.toString();
		}
		return rst;
	}
}
