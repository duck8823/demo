package com.duck8823.model.photo;

import lombok.Data;
import lombok.extern.log4j.Log4j;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * Created by maeda on 2016/01/23.
 */
@Log4j
@Entity
@Table(name = "place")
@Data
public class Place implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "place_id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "url")
	private URL url;

	@OneToMany(mappedBy = "place")
	private List<Photo> photos;

	// for hibernate
	public Place(){
	}

	public Place(String name, URL url) {
		this.name = name;
		this.url = url;
	}

	@Override
	public boolean equals(Object object){
		if(!object.getClass().isAssignableFrom(Place.class)){
			return false;
		}
		return getId().equals(Place.class.cast(object).getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().intValue() : 0;
	}
}
