package com.duck8823.service;

import com.duck8823.model.bot.BotEnv;
import com.duck8823.model.bot.BotEnvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by maeda on 12/31/2016.
 */
@Transactional(readOnly = true)
@Service
public class BotEnvService {

	@Autowired
	private BotEnvRepository botEnvRepository;

	public void save(BotEnv botEnv) {
		botEnvRepository.save(botEnv);
	}

	public Optional<BotEnv> findById(String id) {
		return botEnvRepository.findById(id);
	}
}
