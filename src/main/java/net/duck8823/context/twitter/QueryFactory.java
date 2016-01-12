package net.duck8823.context.twitter;

import twitter4j.Query;

/**
 * Created by maeda on 2016/01/08.
 */
public class QueryFactory {

	public static Query create(String keyword){
		Query query = new Query();
		query.setQuery(keyword);
		query.setResultType(Query.ResultType.mixed);
		query.setCount(50);
		return query;
	}
}
