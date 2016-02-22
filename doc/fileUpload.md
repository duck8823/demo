#File Upload
ファイルのアップロード  
  
###Formクラス
[MultipartFile] (https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html)で受け取ることができる.  

```java
@Data
public class UploadFiles {

	private MultipartFile[] files;

}
```

###Controllerクラス
```java
@Controller
@RequestMapping("path/to/upload")
public class {
	@RequestMapping(method = RequestMethod.POST)
	public String upload(UploadFiles files) {
		...
	}
}
```
###HTML
```html
<form method="post" enctype="multipart/form-data" th:action="@{/path/to/upload}">
	<input type="file" name="files" multiple="multiple" class="form-control" />
	...
</form>
```
