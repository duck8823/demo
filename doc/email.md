# E-Mail
  
### アプリケーションの設定
pom.xml
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```
  
application.properties
```
spring.mail.host=XXX
spring.mail.port=25
spring.mail.username=XXX
spring.mail.password=XXX
```
  
  
### Java
```java
@Autowired
private MailSender sender;

public void hoge() {
    SimpleMailMessage msg = new SimpleMailMessage();
    
    msg.setFrom("from@example.com");
    msg.setTo("to@exapmle.com");
    msg.setSubject("タイトル");
    msg.setText("本文");
    
    sender.send(msg);
}
```