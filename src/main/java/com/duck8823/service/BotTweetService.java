package com.duck8823.service;

import com.duck8823.context.twitter.QueryFactory;
import com.duck8823.model.twitter.FilterRepository;
import com.duck8823.model.twitter.Filters;
import com.duck8823.model.twitter.Tweet;
import com.duck8823.util.ImageUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import twitter4j.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by maeda on 2015/12/21.
 */
@Log4j
@Service
public class BotTweetService {

	@Qualifier("bot")
	@Autowired
	private Twitter twitter;

	@Autowired
	private FilterRepository filterRepository;

	public void post(Tweet tweet) throws TwitterException {
		String message = "";
		if(tweet.getTimestamp() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
			message = sdf.format(tweet.getTimestamp()) + "\n";
		}
		message += tweet.getMessage();
		StatusUpdate status = new StatusUpdate(message);
		if(tweet.getPhoto() != null){
			status.setMedia("photo", new ByteArrayInputStream(tweet.getPhoto().getImage()));
		}
		twitter.updateStatus(status);
	}

	public void retweet() throws TwitterException, IOException, URISyntaxException {
		int cnt = 0;
		Filters filters = new Filters(filterRepository.list());
		for(Status status : twitter.getHomeTimeline()){
			if(status.getText() != null && status.getText().matches(".*水族館.*") && !filters.filter(status) && containsPhoto(status.getMediaEntities()) && !status.isRetweeted() && !detectFaces(status.getMediaEntities())){
				twitter.retweetStatus(status.getId());
				cnt++;
			}
		}
		log.debug(cnt + " 件の投稿をリツイートしました.");
	}

	public void favorite() throws TwitterException {
		int cnt = 0;
		Filters filters = filterRepository.list();
		QueryResult result = twitter.search(QueryFactory.create("水族館"));
		result.getTweets().stream()
				.filter(status -> !containsPhoto(status.getMediaEntities()))
				.filter(filters::filter)
				.filter(status -> {
					try {
						log.debug("自身のツイートです.");
						return status.getUser().getId() == twitter.getOAuthAccessToken().getUserId();
					} catch (TwitterException e) {
						return false;
					}
				})
				.filter(status -> {
					try {
						return twitter.showStatus(status.getId()).isFavorited();
					} catch (TwitterException e) {
						return false;
					}
				})
				.forEach(status -> {
					try {
						twitter.createFavorite(status.getId());
					} catch (TwitterException e){
						log.warn("このツイートはお気に入りに登録できません. : " + e.getMessage() + " (@" + status.getUser().getScreenName() + ")");
					}
				});
		log.debug(cnt + " 件の投稿をお気に入りに登録しました.");
	}

	public void followBack() throws TwitterException {
		int cnt = 0;
		Set<Long> followIds = getFollowIds();
		for(Long id : followIds){
			if(twitter.showUser(id).isFollowRequestSent() || twitter.showUser(id).isProtected()) {
				continue;
			}
			twitter.createFriendship(id);
			cnt++;
		}
		log.debug(cnt + " 人のユーザをフォローしました.");
	}

	public void unfollow() throws TwitterException {
		Set<Long> unfollowIds = getUnfollowIds();
		for(Long id : unfollowIds){
			twitter.destroyFriendship(id);
		}
		log.debug(unfollowIds.size() + " 人のユーザをフォロー解除しました.");
	}

	private boolean containsPhoto(MediaEntity... mediaEntities){
		return Arrays.asList(mediaEntities).stream().filter(mediaEntity -> mediaEntity.getType().equals("photo")).count() > 0;
	}

	private boolean detectFaces(MediaEntity... mediaEntities) throws IOException, URISyntaxException {
		for(MediaEntity mediaEntity : mediaEntities){
			if(ImageUtil.getInstance().detectFaces(ImageUtil.readFromURL(mediaEntity.getMediaURL()))){
				log.debug("顔を検出しました. : " + mediaEntity.getMediaURL());
				return true;
			}
		}
		return false;
	}

	private Set<Long> getFollowIds() throws TwitterException {
		Set<Long> followIds = new HashSet<>();
		Set<Long> friendIds = getFriendIds();
		getFollowerIds().stream().filter(id -> !friendIds.contains(id)).forEach(followIds::add);
		return followIds;
	}

	private Set<Long> getUnfollowIds() throws TwitterException {
		Set<Long> unfollowIds = new HashSet<>();
		Set<Long> followerIds = getFollowerIds();
		getFriendIds().stream().filter(id -> !followerIds.contains(id)).forEach(unfollowIds::add);
		return unfollowIds;
	}

	private Set<Long> getFriendIds() throws TwitterException {
		Set<Long> friends = new HashSet<>();
		IDs ids = twitter.getFriendsIDs(twitter.getId(), -1L);
		while(true){
			for(Long id : ids.getIDs()){
				friends.add(id);
			}
			if(!ids.hasNext()){
				break;
			}
			ids = twitter.getFriendsIDs(ids.getNextCursor());
		}
		return friends;
	}

	private Set<Long> getFollowerIds() throws TwitterException {
		Set<Long> followers = new HashSet<>();
		IDs ids = twitter.getFollowersIDs(twitter.getId(), -1L);
		while(true){
			for(Long id : ids.getIDs()){
				followers.add(id);
			}
			if(!ids.hasNext()){
				break;
			}
			ids = twitter.getFollowersIDs(ids.getNextCursor());
		}
		return followers;
	}

}
