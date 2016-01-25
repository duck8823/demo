package com.duck8823.service;

import com.duck8823.model.photo.Photo;
import com.duck8823.model.photo.PhotoRepository;
import com.duck8823.model.photo.Photos;
import com.duck8823.web.photo.ManagePlaceForm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by maeda on 2015/12/12.
 */
@Service
public class PhotoService extends DataSourceService {

	@Autowired
	private PhotoRepository photoRepository;


	public Optional<Photo> findById(Long id){
		return photoRepository.findById(id);
	}

	public Photo get(Long id) {
		return findById(id).get();
	}

	public Optional<Photo> random(){
		List<Photo> photos = list();
		Photo photo = Photo.class.cast(photos.get(new Random().nextInt(photos.size())));
		return findById(photo.getId());
	}

	public Photos list() {
		return photoRepository.list();
	}

	public void save(Photo photo){
		photoRepository.save(photo);
	}

	public void save(Photos photos) throws TwitterException {
		photos.stream().forEach(photoRepository::save);
	}

	public void addInfo(ManagePlaceForm form) {
		Photo photo = findById(form.getId()).get();
		BeanUtils.copyProperties(form, photo);
		save(photo);
	}

	public byte[] getImage(Long id) {
		return findById(id).get().getImage();
	}

	public void delete(Long id) {
		Photo photo = findById(id).get();
		photoRepository.delete(photo);
	}

	public void rotate(Long id) throws IOException {
		Photo photo = findById(id).get();
		photo.rotate();
		save(photo);
	}
}
