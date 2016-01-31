package com.duck8823.model.twitter;

import com.duck8823.context.twitter.TestStatus;
import com.duck8823.context.twitter.TestUser;
import org.junit.Test;
import twitter4j.Status;
import twitter4j.User;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by maeda on 2016/01/31.
 */
public class FilterTypeTest {

	@Test
	public void スクリーンネームが一致した場合に真となる() throws Exception {
		User user = new TestUser("TEST_NAME", "TEST_SCREEN_NAME");
		Status status = new TestStatus(user, "TEXT");
		Filter filter = new Filter(FilterType.SCREEN_NAME, "TEST_SCREEN_NAME");
		assertTrue(filter.filter(status));
	}

	@Test
	public void スクリーンネームが一致しない場合に偽となる() throws Exception {
		User user = new TestUser("NAME", "TEST_SCREEN_NAME");
		Status status = new TestStatus(user, "TEXT");
		Filter filter = new Filter(FilterType.SCREEN_NAME, "SCREEN_NAME");
		assertFalse(filter.filter(status));
	}

	@Test
	public void ツイートに文字列が含まれる場合に真となる() throws Exception {
		User user = new TestUser("NAME", "SCREEN_NAME");
		Status status = new TestStatus(user, "TEST_TEXT");
		Filter filter = new Filter(FilterType.TEXT, "TEST");
		assertTrue(filter.filter(status));
	}

	@Test
	public void ツイートに文字列が含まれない場合に偽となる() throws Exception {
		User user = new TestUser("NAME", "SCREEN_NAME");
		Status status = new TestStatus(user, "TEXT");
		Filter filter = new Filter(FilterType.TEXT, "TEST");
		assertFalse(filter.filter(status));
	}
}