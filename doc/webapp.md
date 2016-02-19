# Webアプリ
Thymeleafを使って作るウェブアプリ  
コントローラ周りを簡単にメモ    
Thymeleafのドキュメントは http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf_ja.html を参照
  
###Thymeleaf設定
```
spring.thymeleaf.cache=false	<- ホットデプロイを使えるようにする
spring.thymeleaf.check-template-location=true	<- mvn installで失敗する場合
```

###コントローラクラス
クラスに@Controllerアノテーションを付ける
クラスまたはメソッドに@RequestMappingアノテーションを付ける
```java
@Controller
@RequestMapping
public class HogeController {
	...
}
```
  
###Thymeleafのテンプレートにマッピングする  
戻り値をStringにして、/templates/からのパスを指定。（最初に / があるとローカルでは動くがJarにすると動かない。)  
```java
@Controller
@RequestMapping
public class HogeController {
	@RequestMapping
	public String hoge() {
		return "hoge";
	}
}
```
  
###JSON形式でオブジェクトを出力
オブジェクトをJSON形式で出力したい場合、メソッドに@ResponseBodyをつけて、戻り値をオブジェクトのクラスにする。  
```java
@Controller
@RequestMapping
public class HogeController {
	@ResponseBody
	@RequestMapping
	public String hoge() {
		return new Hoge("foo", "bar");
	}
}
```
```java
@Data
@AllArgsConstructor
public class Hoge {
	private String foo;
	private String bar; 
}
```
結果  
```
{
  "foo" : "foo",
  "bar" : "bar"
}
```
  
###ステータスを返す
ステータスだけを返す場合、メソッドに@ResponseStatusアノテーションをつけてHttpStatusを指定、戻り値をvoidにする
```java
@Controller
@RequestMapping
public class HogeController {
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping
	public void hoge() {
	}
}
```
  
###URLパラメータを取得
@RequestMappingの内、{}で囲む。引数に@PathVariableでプロパティを指定
```java
@Controller
@RequestMapping("hoge")
public class HogeController {
	@ResponseBody
	@RequestMapping("{name}")
	public String hoge(@PathVariable String name) {
		return name;
	}
}
```
hoge/testにアクセスした結果
```
test
```
###Thymeleafでオブジェクトにアクセス  
コントローラ
```java
@Controller
@RequestMapping("hoge")
public class HogeController {
	@ResponseBody
	public String hoge(Model model) {
		model.addAttribute("foo", "bar");
		return "hoge";
	}
}
```
hoge.html
```html
<span th:text="${foo}"></span>
```
結果
```
bar
```