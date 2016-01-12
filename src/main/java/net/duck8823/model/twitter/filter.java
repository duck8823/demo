package net.duck8823.model.twitter;

import lombok.Data;
import twitter4j.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by maeda on 2016/01/11.
 */
@Entity
@Table(name = "twitter_filter")
@Data
public class Filter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "filter_id")
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "filter_type")
	private FilterType filterType;

	@NotNull
	@Column(name = "keyword")
	private String keyword;

	// for hibernate
	public Filter(){
	}

	public boolean filter(Status status){
		return filterType.filter(status, keyword);
	}

	@Override
	public boolean equals(Object object){
		if(!object.getClass().isAssignableFrom(Filter.class)){
			return false;
		}
		return getId().equals(Filter.class.cast(object).getId());
	}

	@Override
	public int hashCode() {
		if(getId() == null){
			return 0;
		}
		return getId().intValue();
	}
}
