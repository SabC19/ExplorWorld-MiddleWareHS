package com.explorworld.service;

import java.util.List;
import com.explorworld.model.PaymentMethod;

public interface PaymentMethodService {

    public PaymentMethod findById(Long id) throws ServiceException;

    public List<PaymentMethod> findAll() throws ServiceException;

    public List<PaymentMethod> findActive() throws ServiceException;

    public PaymentMethod create(PaymentMethod paymentMethod) throws ServiceException;

    public void update(PaymentMethod paymentMethod) throws ServiceException;

    public void delete(Long id) throws ServiceException;
}