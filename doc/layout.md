# Thymeleaf Layout
ThymeleafでApache Tilesみたいなレイアウトを使いたい.
  
##### レイアウトテンプレート
htmlタグ内で xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"  
置き換える部分を layout:fragment="名前"  
  
layout.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
	  xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">
	<head>
		...
	</head>
	<body>
		<th:block layout:fragment="content">
			No content.
		</th:block>
	</body>
</html>
```
  
##### 差し込むhtml
htmlタグ内に  
xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"宣言  
layout:decorator="" でhtmlファイルを指定.  
headタグ内の要素は自動的に差し込まれる  
layout:fragment="名前"をdecoratorで指定した要素と合わせる.  
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
	  xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	  layout:decorator="layout">

	<head>
		...
	</head>
	<body>
		<th:block layout:fragment="content">
			...
		</th:block>
	</body>
</html>
```