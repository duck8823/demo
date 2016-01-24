package com.duck8823.model.photo;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.duck8823.util.ImageUtil;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.hibernate.annotations.Type;
import org.hibernate.bytecode.internal.javassist.FieldHandled;
import org.hibernate.bytecode.internal.javassist.FieldHandler;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.Date;

/**
 * Created by maeda on 2015/12/12.
 */
@Log4j
@Entity
@Table(name = "photo")
@Data
public class Photo implements Serializable, FieldHandled {

	@Transient
	private FieldHandler fieldHandler;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "photo_id")
	private Long id;

	@Lob
	@Basic(fetch = FetchType.LAZY, optional = false)
	@Type(type="org.hibernate.type.BinaryType")
	@Column(name = "image", length = Integer.MAX_VALUE)
	private byte[] image;

	@Column(name = "thumbnail", nullable = false, length = 10485760)
	private String thumbnail;

	@Column(name = "take_date")
	private Date takeDate;

	@Column(name = "comment")
	private String comment;

	@ManyToOne
	@JoinColumn(name = "place_id")
	private Place place;

	// for hibernate
	public Photo(){
	}

	public Photo(MultipartFile file) {
		try {
			Directory directory = JpegMetadataReader.readMetadata(file.getInputStream()).getDirectory(ExifIFD0Directory.class);
			this.image = ImageUtil.resize(file.getBytes(), 1920, ImageUtil.ResizeMode.LONG);
			this.thumbnail = Base64.getEncoder().encodeToString(ImageUtil.resize(file.getBytes(), 100, ImageUtil.ResizeMode.SHORT, true));
			this.takeDate = directory.getDate(ExifIFD0Directory.TAG_DATETIME);
		} catch (Exception e){
			throw new IllegalStateException(Photo.class + " の初期化に失敗しました。 : " + e);
		}
	}

	public void setImage(byte[] image){
		if(fieldHandler != null){
			fieldHandler.writeObject(this, Photo_.image.getName(), this.image, image);
		} else {
			this.image = image;
		}
	}

	public byte[] getImage() {
		if(fieldHandler != null) {
			return byte[].class.cast(fieldHandler.readObject(this, Photo_.image.getName(), image));
		} else {
			return image;
		}
	}

	public void rotate() throws IOException {
		this.image = ImageUtil.rotate(this.getImage());
		this.thumbnail = Base64.getEncoder().encodeToString(ImageUtil.resize(this.image, 100, ImageUtil.ResizeMode.SHORT, true));
	}

	@Override
	public int hashCode(){
		if(id == null){
			return 0;
		}
		return id.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
