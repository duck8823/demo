# データベースコネクション
データベースはh2、O/RマッパーとしてHibernateを利用する
  
  
### 設定
pom.xml
```
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
	<groupId>org.hibernate</groupId>
	<artifactId>hibernate-jpamodelgen</artifactId>
</dependency>
```
  
application.properties
```
spring.datasource.url=jdbc:h2:./db/demo;MODE=PostgreSQL
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```
  
  
JavaConfig
```java
@Configuration
public class DemoConfiguration {

	@Autowired
	private DataSource dataSource;

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setPackagesToScan("com.duck8823.model.**");
		return sessionFactory;
	}

}
```
`sessionFactory.setPackagesToScan("com.duck8823.model.**");`で、パッケージ com.duck8823.model 以下の `@Entity` をスキャンする.  
  
  
### DataSource
```java
/**
 * Hibernateデータソース基底クラス
 *
 */
public abstract class HibernateDataSource<T> {

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
		currentSession().save(entity);
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
```