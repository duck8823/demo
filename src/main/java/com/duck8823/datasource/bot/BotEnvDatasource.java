package com.duck8823.datasource.bot;

import com.duck8823.datasource.AbstractDatasource;
import com.duck8823.model.bot.BotEnv;
import com.duck8823.model.bot.BotEnvRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by maeda on 12/31/2016.
 */
@Repository
public class BotEnvDatasource extends AbstractDatasource<BotEnv> implements BotEnvRepository {
	@Override
	public Optional<BotEnv> findById(String id) {
		return super.findById(id);
	}

	@Override
	public void save(BotEnv botEnv) {
		super.save(botEnv);
	}
}
