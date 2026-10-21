package com.explorworld.dao.impl;

import java.util.List;

import org.hibernate.query.Query;
import org.hibernate.Session;

import com.explorworld.dao.GenderDAO;
import com.explorworld.model.Gender;

public class GenderDAOImpl implements GenderDAO {

	
	@Override
	// Hibernate hacer el select con los parametros automaticamente
	public Gender findById(Session session, Long id) {
		return session.get(Gender.class, id);
	}
	
	@Override
	public List<Gender> findAll(Session session) {
		// En HQL usamos el nombre de la clase java, no el de la tabla de DB
		String hql = "From Gender g ORDER BY g.name ASC";
		Query<Gender> query = session.createQuery(hql, Gender.class);
		return query.getResultList();
	}
	
	@Override
	public Gender create(Session session, Gender gender) {
		session.save(gender);
		return gender;
	}
	
	@Override
	public void  update(Session session, Gender gender) {
		session.update(gender);
	}
	
	@Override
	public void delete(Session session, Gender gender) {
		session.delete(gender);
	}
	
}
