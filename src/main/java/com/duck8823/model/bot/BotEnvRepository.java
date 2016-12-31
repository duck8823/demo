package com.duck8823.model.bot;

import java.util.Optional;

/**
 * Created by maeda on 12/31/2016.
 */
public interface BotEnvRepository {

	Optional<BotEnv> findById(String id);

	void save(BotEnv botEnv);
}
