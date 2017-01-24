package com.duck8823.model.bot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Botの設定
 * Created by maeda on 12/31/2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bot_env")
@Entity
public class BotEnv {

	@Column(name = "id")
	@Id
	private String id;

	@Column(name = "is_quiet")
	private Boolean quiet;

	@Column(name = "context")
	private String context;

	@Column(name = "mode")
	private String mode;

	public Talk talk() {
		return new Talk(null, mode, context);
	}
}
