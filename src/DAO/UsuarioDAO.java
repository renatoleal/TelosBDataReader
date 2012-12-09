package DAO;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import Entities.Dados;
import Entities.Sensores;
import Entities.Usuario;

public class UsuarioDAO {

	private SessionFactory sessionFactory = null;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public UsuarioDAO(){}

	public UsuarioDAO(SessionFactory sessionFactory){
		setSessionFactory(sessionFactory);
	}
	
	public int getLogin(String login, String senha) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List results = null;
		try{
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Usuario.class);
			
			criteria.add(Restrictions.eq("login", login ));
			criteria.add(Restrictions.eq("senha", senha ));
			
			results = criteria.list();
			tx.commit();
		}
		catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close();
		}
		
		int resultsInt= results.size();
		
		return resultsInt;
	}
		
	public Usuario getByLogin(String login) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Object o = null;
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Usuario.class);
			criteria.add(Restrictions.eq("login", login));

			List results = criteria.list();
			if(!results.isEmpty())
				o = results.get(0);
			tx.commit();
		}
		catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close();
		}
		return (Usuario) o;
	}

	public List<Usuario> listUsuarios() {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List results = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Usuario.class);
			results = criteria.list();
			tx.commit();
		}
		catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close();
		}
		return results;
	}
	
	public List<Usuario> listAdmins() {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List results = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Usuario.class);
			criteria.add(Restrictions.eq("admin", true));
			results = criteria.list();
			tx.commit();
		}
		catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close();
		}
		return results;
	}

	public Usuario getById(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Object o = null;
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Usuario.class);
			criteria.add(Restrictions.eq("id", id));

			List results = criteria.list();
			if(!results.isEmpty())
				o = results.get(0);
			tx.commit();
		}
		catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close();
		}
		return (Usuario) o;
	}
	public int getLastId() {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List results = null;
		Usuario u = new Usuario();
		int a = 0;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Usuario.class);
			criteria.addOrder(Order.asc("id"));
			results = criteria.list();
			u = (Usuario) results.get(results.size() - 1);
			tx.commit();
		}
		catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close();
		}
		return u.getId();
	}
	
	
	public Usuario save(Usuario entity) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(entity);
			tx.commit();
		}
		catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close();
		}
		return entity;
	}

	public void delete(Usuario entity) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(entity);
			tx.commit();
		}
		catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close();
		}
	}
}