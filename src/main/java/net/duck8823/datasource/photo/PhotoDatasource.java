package net.duck8823.datasource.photo;

import net.duck8823.model.photo.Photo;
import net.duck8823.model.photo.PhotoRepository;
import net.duck8823.model.photo.Photos;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by maeda on 2015/12/12.
 */
@Repository
public class PhotoDatasource implements PhotoRepository {

	@Autowired
	private LocalSessionFactoryBean sessionFactory;

	@Override
	public Optional<Photo> findById(Long id) {
		return Optional.ofNullable(Photo.class.cast(getSession().createCriteria(Photo.class).setFetchMode("data", FetchMode.JOIN).add(Restrictions.eq("id", id)).uniqueResult()));
	}

	@Override
	public Optional<Photo> random() {
		List<Object> lists = getSession().createCriteria(Photo.class).list();
		Photo photo = Photo.class.cast(lists.get(new Random().nextInt(lists.size())));
		return findById(photo.getId());
	}

	@Override
	public Photos list() {
		return new Photos(getSession().createCriteria(Photo.class).addOrder(Order.desc("takeDate")).list());
	}

	@Override
	public void save(Photo photo) {
		getSession().save(photo);
	}

	@Override
	public void delete(Photo photo) {
		getSession().delete(photo);
	}

	private Session getSession(){
		return sessionFactory.getObject().getCurrentSession();
	}
}
