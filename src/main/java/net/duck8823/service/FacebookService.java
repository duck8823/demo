package net.duck8823.service;

import facebook4j.*;
import lombok.extern.log4j.Log4j;
import net.duck8823.context.media.Media;
import net.duck8823.context.word.TokenizedWordCount;
import net.duck8823.context.word.WordHandler;
import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by maeda on 2015/12/23.
 */
@Log4j
@Service
public class FacebookService {

	private static final String IGNORE_REGEX = "http(s)?://[^\\s]*|@[^\\s]*|\\n";

	@Autowired
	private WordHandler wordHandler;

	public List<TokenizedWordCount> getTokenizedWordCountMap(Facebook facebook) throws FacebookException {
		Tokenizer tokenizer = Tokenizer.builder().build();
		List<Token> tokens = tokenizer.tokenize(getText(facebook));
		return Collections.unmodifiableList(wordHandler.getCountList(tokens));
	}

	public List<Media> getPhotos(Facebook facebook) throws FacebookException {
		List<Media> medias = new ArrayList<>();

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);

		log.debug(facebook.getMe().getName());

		/** 写真 */
		boolean oneYearAgo = false;
		ResponseList<Photo> photos = facebook.getUploadedPhotos();
		while(!oneYearAgo){
			if(photos == null || photos.isEmpty()){
				break;
			}
			Paging<Photo> paging = photos.getPaging();
			for(Photo photo : photos){
				if(photo.getCreatedTime().getTime() >= calendar.getTimeInMillis()){
					medias.add(new Media(facebook.getPhotoURL(photo.getId()).toString(), photo.getCreatedTime()));
				} else {
					oneYearAgo = true;
					break;
				}
			}
			photos = facebook.fetchNext(paging);
		}
		return Collections.unmodifiableList(medias);
	}

	private String getText(Facebook facebook) throws FacebookException {
		StringBuffer sb = new StringBuffer();

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);

		log.debug(facebook.getMe().getName());

		boolean oneYearAgo = false;
		ResponseList<Post> posts = facebook.getPosts();
		while(!oneYearAgo){
			Paging<Post> paging = posts.getPaging();
			if(posts.isEmpty()){
				break;
			}
			for(Post post : posts){
				if(post.getCreatedTime().getTime() >= calendar.getTimeInMillis()){
					sb.append(post.getMessage()).append(" ");
				} else {
					oneYearAgo = true;
					break;
				}
			}
			posts = facebook.fetchNext(paging);
		}
		return sb.toString().replaceAll(IGNORE_REGEX, " ").replaceAll("\\s+", " ");
	}
}
