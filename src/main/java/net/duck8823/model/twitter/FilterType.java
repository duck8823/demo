package net.duck8823.model.twitter;

import twitter4j.Status;

/**
 * Created by maeda on 2016/01/11.
 */
public enum FilterType {

	TEXT{
		@Override
		String getTarget(Status status) {
			return status.getText();
		}
		@Override
		String parseKeyword(String keyword) {
			return ".*" + keyword + ".*";
		}
	},
	SCREEN_NAME{
		@Override
		String getTarget(Status status) {
			return status.getUser().getScreenName();
		}
		@Override
		String parseKeyword(String keyword) {
			return "^" + keyword + "$";
		}
	},
	;

	abstract String getTarget(Status status);

	abstract String parseKeyword(String keyword);
}