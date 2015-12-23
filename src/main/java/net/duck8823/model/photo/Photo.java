package net.duck8823.model.photo;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.exif.ExifIFD0Directory;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import net.duck8823.util.ImageUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.bytecode.instrumentation.spi.FieldInterceptor;
import org.hibernate.bytecode.internal.javassist.FieldHandled;
import org.hibernate.bytecode.internal.javassist.FieldHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.Transactional;
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
public class Photo implements FieldHandled {

	@Transient
	private FieldHandler fieldHandler;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "photo_id")
	private Long id;

	@Lob
	@Basic(fetch = FetchType.LAZY, optional = false)
	@Column(name = "image")
	private byte[] image;

	@Column(name = "thumbnail", nullable = false, length = Integer.MAX_VALUE)
	private String thumbnail;

	@Column(name = "take_date")
	private Date takeDate;

	@Column(name = "comment")
	private String comment;

	// for hibernate
	public Photo(){
	}

	public Photo(Long id){
		this.id = id;
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
		return id.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
