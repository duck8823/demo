#AOP
Seasar2でいうところのインターセプタ  
ほんの一部

####クラス
@Aspectアノテーションを付ける
```java
@Aspect
@Component
public class HogeAspect {
}
```

####メソッド
指定メソッドの前で処理する場合、@Before  
指定メソッドの後で処理する場合、@After
指定メソッドの前と後ろで処理する場合、@Aroundを指定しAOPメソッド内でjoinPoint.proceed()を呼び出す. 戻り値のオブジェクトをreturnする.
#####@Aroundのexecutionの書き方
######サブパッケージを含むパッケージは以下を指定
```java
@Around("execution(* com.duck8823..*(..))")
```
######サブクラスを含むパッケージ配下を指定
```java
@Around("execution(* com.duck8823..*.*(..))")
```
######クラスを指定
```java
@Around("execution(* com.duck8823.Hoge.*(..))")
```
######メソッドを指定
```java
@Around("execution(* com.duck8823.Hoge.hoge(..))")
```
######修飾子を指定
privateの例
```java
@Around("execution(private com.duck8823..*(..))")
```
  
  
メソッドの実行時間をログ出力するAOP
```java
@Around("execution(* com.duck8823..*(..))")
public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
	StopWatch stopWatch = new StopWatch();
	stopWatch.start();
	Object result = joinPoint.proceed();
	stopWatch.stop();
	log.debug(stopWatch.getTotalTimeMillis() + "ms");
	return result;
}
```
