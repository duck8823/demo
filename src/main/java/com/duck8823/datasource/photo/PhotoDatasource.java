package com.duck8823.datasource.photo;

import com.duck8823.datasource.AbstractDatasource;
import com.duck8823.model.photo.Photo;
import com.duck8823.model.photo.PhotoRepository;
import com.duck8823.model.photo.Photo_;
import com.duck8823.model.photo.Photos;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by maeda on 2015/12/12.
 */
@Repository
public class PhotoDatasource extends AbstractDatasource implements PhotoRepository {

	@Override
	public Optional<Photo> findById(Long id) {
		return Optional.ofNullable(Photo.class.cast(getSession().createCriteria(Photo.class)
				.setFetchMode(Photo_.image.getName(), FetchMode.JOIN)
				.add(Restrictions.eq(Photo_.id.getName(), id))
				.uniqueResult()));
	}

	@Override
	public Photos list() {
		return new Photos(getSession().createCriteria(Photo.class)
				.addOrder(Order.desc(Photo_.takeDate.getName()))
				.list());
	}

	@Override
	public void save(Photo photo) {
		getSession().save(photo);
	}

	@Override
	public void delete(Photo photo) {
		getSession().delete(photo);
	}

}
