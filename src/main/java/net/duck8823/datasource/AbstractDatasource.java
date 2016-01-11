package net.duck8823.datasource;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

/**
 * Created by maeda on 2016/01/11.
 */
public abstract class AbstractDatasource {

	@Autowired
	private LocalSessionFactoryBean sessionFactory;

	protected Session getSession(){
		return sessionFactory.getObject().getCurrentSession();
	}
}
