package com.duck8823.model.twitter;

import com.duck8823.TestConfiguration;
import com.duck8823.context.twitter.QueryFactory;
import com.duck8823.context.twitter.TestStatus;
import com.duck8823.context.twitter.TestUser;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import twitter4j.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by maeda on 2016/01/31.
 */
@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class FilterTypeTest {

	@Test
	public void スクリーンネームが一致した場合に真となる() throws Exception {
		User user = new TestUser("TEST_NAME", "TEST_SCREEN_NAME");
		Status status = new TestStatus(user, "TEXT");
		Filter filter = new Filter(FilterType.SCREEN_NAME, "TEST_SCREEN_NAME");
		assertTrue(filter.find(status));
	}

	@Test
	public void スクリーンネームが一致しない場合に偽となる() throws Exception {
		User user = new TestUser("NAME", "TEST_SCREEN_NAME");
		Status status = new TestStatus(user, "TEXT");
		Filter filter = new Filter(FilterType.SCREEN_NAME, "SCREEN_NAME");
		assertFalse(filter.find(status));
	}

	@Test
	public void ツイートに文字列が含まれる場合に真となる() throws Exception {
		User user = new TestUser("NAME", "SCREEN_NAME");
		Status status = new TestStatus(user, "TEST_TEXT");
		Filter filter = new Filter(FilterType.TEXT, "TEST");
		assertTrue(filter.find(status));
	}

	@Test
	public void ツイートに文字列が含まれない場合に偽となる() throws Exception {
		User user = new TestUser("NAME", "SCREEN_NAME");
		Status status = new TestStatus(user, "TEXT");
		Filter filter = new Filter(FilterType.TEXT, "TEST");
		assertFalse(filter.find(status));
	}
}