package net.duck8823.service;

import net.duck8823.model.photo.Photo;
import net.duck8823.model.photo.PhotoRepository;
import net.duck8823.model.photo.Photos;
import net.duck8823.model.tweeet.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by maeda on 2015/12/12.
 */
@Service
public class PhotoService {

	@Autowired
	private PhotoRepository photoRepository;

	public Optional<Photo> findById(Long id){
		return photoRepository.findById(id);
	}

	public Optional<Photo> random(){
		return photoRepository.random();
	}

	public Photos list() {
		return photoRepository.list();
	}

	public void save(Photo photo){
		photoRepository.save(photo);
	}

	public void save(Photos photos) throws Exception {
		photos.stream().forEach(photoRepository::save);
		new Tweet(photos.size() + " 枚の写真をアップロードしました。").post();
	}

	public void delete(Photo photo){
		photoRepository.delete(photo);
	}
}
