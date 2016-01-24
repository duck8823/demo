package com.duck8823.service;

import java.util.Optional;

/**
 * Created by maeda on 2016/01/24.
 */
public abstract class DataSourceService<T> {

	public abstract Optional<T> findById(Long id);
}
