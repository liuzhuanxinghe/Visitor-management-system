package cn.nust.cstm.web.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.nust.commons.CommonUtils;
import cn.nust.cstm.domain.Customer;
import cn.nust.cstm.domain.PageBean;
import cn.nust.cstm.service.CustomerService;
import cn.nust.servlet.BaseServlet;


public class CustomerServlet extends BaseServlet {
	private CustomerService customerService = new CustomerService();
	
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Customer c = CommonUtils.toBean(request.getParameterMap(), Customer.class);
		c.setCid(CommonUtils.uuid());
		customerService.add(c);
		request.setAttribute("msg", "恭喜，添加人员成功！");
		return "f:/msg.jsp";
	}
	
	
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		int pc = getPc(request);//得到pc
		int ps = 10;//给定ps的值，第页10行记录
		PageBean<Customer> pb = customerService.findAll(pc, ps);//传递pc, ps给Service，得到PageBean
		
		// 设置url
		pb.setUrl(getUrl(request));
		
		request.setAttribute("pb", pb);//保存到request域中
		return "f:/list.jsp";//转发到list.jsp
	}
	
	
	private int getPc(HttpServletRequest request) {
		
		String value = request.getParameter("pc");
		if(value == null || value.trim().isEmpty()) {
			return 1;
		}
		return Integer.parseInt(value);
	}
	
	
	public String preEdit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String cid = request.getParameter("cid");
		Customer cstm = customerService.load(cid);
		request.setAttribute("cstm", cstm);
		return "f:/edit.jsp";
	}
	

	public String edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Customer c = CommonUtils.toBean(request.getParameterMap(), Customer.class);
		customerService.edit(c);
		request.setAttribute("msg", "恭喜，编辑客户成功！");
		return "f:/msg.jsp";
	}
	

	
	public String query(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Customer criteria = CommonUtils.toBean(request.getParameterMap(), Customer.class);
		
		criteria = encoding(criteria);
		
		int pc = getPc(request);//得到pc
		int ps = 10;//给定ps的值，第页10行记录
		PageBean<Customer> pb = customerService.query(criteria, pc, ps);
		
		
		pb.setUrl(getUrl(request));
		
		request.setAttribute("pb", pb);
		return "f:/list.jsp";
	}
	
	
	private Customer encoding(Customer criteria) throws UnsupportedEncodingException {
		String cname = criteria.getCname();
		String gender = criteria.getGender();
		String cellphone = criteria.getCellphone();
		String email = criteria.getEmail();
		
		if(cname != null && !cname.trim().isEmpty()) {
			cname = new String(cname.getBytes("ISO-8859-1"), "utf-8");
			criteria.setCname(cname);
		}
		
		if(gender != null && !gender.trim().isEmpty()) {
			gender = new String(gender.getBytes("ISO-8859-1"), "utf-8");
			criteria.setGender(gender);
		}
		
		if(cellphone != null && !cellphone.trim().isEmpty()) {
			cellphone = new String(cellphone.getBytes("ISO-8859-1"), "utf-8");
			criteria.setCellphone(cellphone);
		}
		
		if(email != null && !email.trim().isEmpty()) {
			email = new String(email.getBytes("ISO-8859-1"), "utf-8");
			criteria.setEmail(email);
		}
		return criteria;
	}

	
	private String getUrl(HttpServletRequest request) {
		String contextPath = request.getContextPath();//获取项目名
		String servletPath = request.getServletPath();//获取servletPath，即/CustomerServlet
		String queryString = request.getQueryString();//获取问号之后的参数部份
		
	
		if(queryString.contains("&pc=")) {
			int index = queryString.lastIndexOf("&pc=");
			queryString = queryString.substring(0, index);
		}
		
		return contextPath + servletPath + "?" + queryString;
	}
}
