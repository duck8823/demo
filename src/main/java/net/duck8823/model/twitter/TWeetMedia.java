package net.duck8823.model.twitter;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * Created by maeda on 2015/12/21.
 */
@Getter
@AllArgsConstructor
public class TweetMedia {

	private String mediaURL;

	private Date createAt;

}
