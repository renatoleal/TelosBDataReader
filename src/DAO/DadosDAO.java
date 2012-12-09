package DAO;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import util.HibernateUtil;
import Entities.Dados;
import Entities.Sensores;

public class DadosDAO {

	private SessionFactory sessionFactory = null;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public DadosDAO(SessionFactory sessionFactory){
		setSessionFactory(sessionFactory);
	}

	public Dados getLastDataFromSensor(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Object o = null;
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Dados.class);
			criteria.add(Restrictions.eq("idSensor", id));
			criteria.addOrder(Order.desc("timeTicks"));
		    criteria.setMaxResults(1);

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
		return (Dados) o;
	}

	public List<Dados> getNextDataFromSensor(int id, long timeTicks) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List results = null;
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Dados.class);
			criteria.add(Restrictions.eq("idSensor", id));
			criteria.add(Restrictions.ge("timeTicks", timeTicks));
			criteria.addOrder(Order.asc("timeTicks"));

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
	
	public List<Dados> getRangeFromSensor(int id, long startDate, long endDate) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();

		Criteria criteria = session.createCriteria(Dados.class);
		criteria.add(Restrictions.ge("time", startDate));
		criteria.add(Restrictions.le("time", endDate));

		List results = criteria.list();
		t.commit();
		return results;
	}

	public Dados save(Dados entity) {
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

	public void delete(Dados entity) {
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


