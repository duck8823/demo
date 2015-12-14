package net.duck8823.model.photo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by maeda on 2015/12/12.
 */
@Data
public class UploadFiles {

	private MultipartFile[] files;

}
