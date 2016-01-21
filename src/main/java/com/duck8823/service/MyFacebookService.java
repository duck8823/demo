package com.duck8823.service;

import com.duck8823.model.facebook.Post;
import com.duck8823.model.photo.Photo;
import facebook4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.DateUtils;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.Locale;

/**
 * Created by maeda on 2016/01/21.
 */
@Service
public class MyFacebookService {

	@Qualifier("myFacebook")
	@Autowired
	private Facebook facebook;

	public void post(Post post) throws FacebookException {
		String date = DateUtils.format(new Date(System.currentTimeMillis()), "yyyy/MM/dd", Locale.getDefault());
		AlbumUpdate albumUpdate = new AlbumUpdate(date, post.getMessage());
		String albumId = facebook.createAlbum(albumUpdate);
		for(Photo photo : post.getPhotos()) {
			facebook.addAlbumPhoto(albumId, new Media("album photo", new ByteArrayInputStream(photo.getImage())), DateUtils.format(photo.getTakeDate(), "yyyy/MM/dd hh:mm", Locale.getDefault()));
		}
	}
}
