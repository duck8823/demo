# error handling
Exceptionが発生した場合に特定の処理を実行したい.  
  
@AfterThrowingで指定.  
executionの書き方は [AOP](./aop.md)
```java
@AfterThrowing(value = "execution(* com.duck8823..*(..))", throwing = "t")
public void hoge(Throwable t) {
	...
}
```