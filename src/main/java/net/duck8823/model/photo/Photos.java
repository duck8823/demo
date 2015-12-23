package net.duck8823.model.photo;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by maeda on 2015/12/12.
 */
public class Photos extends LinkedHashSet<Photo> {

	public Photos(List<Photo> photos){
		super();
		photos.stream().forEach(this::add);
	}

	public Photos(UploadFiles files){
		super();
		Arrays.asList(files.getFiles()).parallelStream().forEach(file -> this.add(new Photo(file)));
	}

}
