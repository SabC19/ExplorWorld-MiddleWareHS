package com.explorworld.dao;

import java.util.List;
import org.hibernate.Session;
import com.explorworld.model.DataException;
import com.explorworld.model.PaymentMethod;

public interface PaymentMethodDAO {

    public PaymentMethod findById(Session session, Long id) throws DataException;

    public List<PaymentMethod> findAll(Session session) throws DataException;

    public List<PaymentMethod> findActive(Session session) throws DataException;

    public PaymentMethod create(Session session, PaymentMethod paymentMethod) throws DataException;

    public void update(Session session, PaymentMethod paymentMethod) throws DataException;

    public void delete(Session session, PaymentMethod paymentMethod) throws DataException;
}