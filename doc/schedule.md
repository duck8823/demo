# schedule
Seasar2でいうところのS2Chronos.  
  
  
#### スケジュールの有効化
`@Configuration`クラスに`@EnableScheduling`アノテーションをつける.  
```java
@EnableScheduling
@Configuration
public class Configuration {
  ...
```
  
  
#### スケジュール
メソッドに`@Scheduled`アノテーションを付ける。
cron形式で記述できる。
```java
@Scheduled(cron = "0 0 * * * *", zone = "Asia/Tokyo")
public void task() {
    ...
}
```