package com.duck8823.web.slack;

import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

/**
 * Slack Interactive Messages を試す
 * Created by maeda on 10/3/2016.
 */
@Transactional
@Log4j
@RequestMapping("slack")
@RestController
public class SlackController {

	@RequestMapping("callback")
	public void callback() {

	}
}
