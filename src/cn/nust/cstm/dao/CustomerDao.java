package cn.nust.cstm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.nust.cstm.domain.Customer;
import cn.nust.cstm.domain.PageBean;
import cn.nust.jdbc.TxQueryRunner;


public class CustomerDao {
	private QueryRunner qr = new TxQueryRunner();

	
	public void add(Customer c) {
		try {
			String sql = "insert into t_customer values(?,?,?,?,?,?,?)";
			Object[] params = { c.getCid(), c.getCname(), c.getGender(),
					c.getBirthday(), c.getCellphone(), c.getEmail(),
					c.getDescription()};
			qr.update(sql, params);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public PageBean<Customer> findAll(int pc, int ps) {
		try {
		
			PageBean<Customer> pb = new PageBean<Customer>();
			pb.setPc(pc);
			pb.setPs(ps);
			
			String sql = "select count(*) from t_customer";
			Number num = (Number)qr.query(sql, new ScalarHandler());
			int tr = num.intValue();
			pb.setTr(tr);
			
			sql = "select * from t_customer order by cname limit ?,?";
			List<Customer> beanList = qr.query(sql, 
					new BeanListHandler<Customer>(Customer.class), 
					(pc-1)*ps, ps);
			pb.setBeanList(beanList);
			return pb;
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	public Customer load(String cid) {
		try {
			String sql = "select * from t_customer where cid=?";
			return qr.query(sql, new BeanHandler<Customer>(Customer.class), cid);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	public void edit(Customer c) {
		try {
			String sql = "update t_customer set cname=?,gender=?,birthday=?," +
					"cellphone=?,email=?,description=? where cid=?";
			Object[] params = {c.getCname(), c.getGender(),
					c.getBirthday(), c.getCellphone(), c.getEmail(),
					c.getDescription(), c.getCid()};
			qr.update(sql, params);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	
	public PageBean<Customer> query(Customer criteria, int pc, int ps) {
		try {
			
			PageBean<Customer> pb = new PageBean<Customer>();
			pb.setPc(pc);
			pb.setPs(ps);
			
			
			StringBuilder cntSql = new StringBuilder("select count(*) from t_customer");
			StringBuilder whereSql = new StringBuilder(" where 1=1");
			
			List<Object> params = new ArrayList<Object>();
			String cname = criteria.getCname();
			if(cname != null && !cname.trim().isEmpty()) {
				whereSql.append(" and cname like ?");
				params.add("%" + cname + "%");
			}
			
			String gender = criteria.getGender();
			if(gender != null && !gender.trim().isEmpty()) {
				whereSql.append(" and gender=?");
				params.add(gender);
			}
			
			String cellphone = criteria.getCellphone();
			if(cellphone != null && !cellphone.trim().isEmpty()) {
				whereSql.append(" and cellphone like ?");
				params.add("%" + cellphone + "%");
			}
			
			String email = criteria.getEmail();
			if(email != null && !email.trim().isEmpty()) {
				whereSql.append(" and email like ?");
				params.add("%" + email + "%");
			}
			
		
			Number num = (Number)qr.query(cntSql.append(whereSql).toString(), 
					new ScalarHandler(), params.toArray());
			int tr = num.intValue();
			pb.setTr(tr);
			
			
			StringBuilder sql = new StringBuilder("select * from t_customer");
			
			StringBuilder limitSql = new StringBuilder(" limit ?,?");
			
			params.add((pc-1)*ps);
			params.add(ps);
			// 执行之
			List<Customer> beanList = qr.query(sql.append(whereSql).append(limitSql).toString(), 
					new BeanListHandler<Customer>(Customer.class), 
					params.toArray());
			pb.setBeanList(beanList);
			
			return pb;
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
