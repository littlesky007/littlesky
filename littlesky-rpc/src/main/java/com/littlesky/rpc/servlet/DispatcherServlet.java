package com.littlesky.rpc.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.littlesky.common.ParamUtil;

public class DispatcherServlet extends HttpServlet{

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String param = getParam(req);

		try {
			String result = ParamUtil.invokeMethod(param);
			sendResponseMessage(resp,result);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private String getParam(HttpServletRequest req) throws IOException, UnsupportedEncodingException {
		InputStream in = req.getInputStream();
		StringBuffer sb = new StringBuffer();

		BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
		String s = null;
		while((s = br.readLine()) !=null)
		{
			sb.append(s);
		}
		if(sb == null || sb.toString().length() == 0)
		{
			return null;
		}
		return sb.toString();
	}



	private void sendResponseMessage(HttpServletResponse response, String message) throws IOException
	{
		response.setContentType("text/json; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(message);
		response.getWriter().flush();
		response.getWriter().close();
	}

}
