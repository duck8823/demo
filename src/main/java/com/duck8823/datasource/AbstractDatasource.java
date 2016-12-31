package com.duck8823.datasource;

import com.duck8823.util.Generics;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by maeda on 2016/01/11.
 */
public abstract class AbstractDatasource<T> {

	 @Autowired
	 private LocalSessionFactoryBean sessionFactory;

	 /**
	  * 識別子によって検索する
	  * @param id 識別子
	 * @return エンティティ
	 */
	@SuppressWarnings("unchecked")
	protected Optional<T> findById(Serializable id) {
		Assert.notNull(id);
		T t = (T) currentSession().get(getEntityClass(), id);
		return Optional.ofNullable(t);
	}

	/**
	 * 条件を指定してエンティティを取得する.
	 * @param criteria 検索条件
	 * @return エンティティ
	 */
	@SuppressWarnings("unchecked")
	protected Optional<T> uniqueResult(Criteria criteria){
		Assert.notNull(criteria);
		T t = (T) criteria.uniqueResult();
		return Optional.ofNullable(t);
	}

	/**
	 * 全てのエンティティを取得する.
	 * @return エンティティのリスト
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findAll() {
		List<T> list = criteria().list();
		if(list == null || list.isEmpty()){
			throw new EntityNotFoundException();
		}
		return Collections.unmodifiableList(list);
	}

	/**
	 * 条件を指定してエンティティのリストを取得する.
	 * @param criteria 検索条件
	 * @return エンティティのリスト
	 */
	@SuppressWarnings("unchecked")
	protected List<T> list(Criteria criteria){
		Assert.notNull(criteria);
		List<T> list = criteria.list();
		if(list == null || list.isEmpty()){
			throw new EntityNotFoundException();
		}
		return Collections.unmodifiableList(list);
	}

	/**
	 * エンティティを新規追加あるいは更新する.
	 * @param entity エンティティ
	 */
	protected void save(T entity) {
		Assert.notNull(entity);
		currentSession().merge(entity);
	}

	/**
	 * エンティティを削除する.
	 * @param entity エンティティ
	 */
	protected void delete(T entity) {
		Assert.notNull(entity);
		currentSession().delete(entity);
	}

	/**
	 * 現在のセッションを取得する.
	 * @return 現在のセッション
	 */
	protected Session currentSession() {
		return sessionFactory.getObject().getCurrentSession();
	}

	/**
	 * {@code <T>} 用の {@link Criteria}クエリを新規作成する.
	 * @return {@link Criteria}クエリ
	 */
	protected Criteria criteria() {
		return currentSession().createCriteria(getEntityClass());
	}

	/**
	 * エンティティクラスを取得する
	 * @return エンティティクラス
	 */
	private Class<?> getEntityClass() {
		Class<?> clazz = Generics.getTypeParameter(getClass());
		if(clazz.getAnnotation(Entity.class) == null){
			throw new IllegalStateException(clazz.getName() + "はエンティティではありません.");
		}
		return clazz;
	}

}
