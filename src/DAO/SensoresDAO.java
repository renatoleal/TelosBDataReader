package DAO;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import Entities.Dados;
import Entities.Sensores;

public class SensoresDAO {

	private SessionFactory sessionFactory = null;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SensoresDAO(){}

	public SensoresDAO(SessionFactory sessionFactory){
		setSessionFactory(sessionFactory);
	}

	public List<Sensores> listSensores() {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List results = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Sensores.class);
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

	public Sensores getById(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Object o = null;
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Sensores.class);
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
		return (Sensores) o;
	}

	public Sensores save(Sensores entity) {
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

	public void delete(Sensores entity) {
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