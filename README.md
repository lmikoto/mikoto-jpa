# mikoto-jpa

## 项目介绍
为简化`spring-data-jpa`动态查询而定义的一套注解。
可以在不写sql的情况下使用jpa的动态查询。
## 使用文档
### 安装
通过maven引入
```xml
<dependency>
    <groupId>io.github.lmikoto</groupId>
    <artifactId>mikoto-jpa</artifactId>
    <version>1.2.2.RELEASE</version>
</dependency>
```
### 初始化
在启动类上加上`@EnableMikotoJpa`注解
### 使用
创建接口继承`BaseRepository`
```java
public interface UserRepository extends BaseRepository<UserEntity,Long> {
}
```
创建搜索对象，继承自 `DataQueryObject`
```java
@Data
public class UserSO extends DataQueryObject {

    // 搜索db中name 等于传入name
    @QueryField(type = QueryType.EQUAL)
    private String name;

    // 搜索db中age in ages
    @QueryField(type = QueryType.IN,name = "age")
    private List<Integer> ages;

    // 查询db中 createAt在 createAtBetween之间的值
    @QueryField(type = QueryType.BETWEEN,name = "createAt")
    private QueryBetween<Date> createAtBetween;

}
```
进行搜索
```
userRepository.findAll(userSO)
```
其他支持的搜索类型
```java
public enum QueryType {
    EQUAL,
    NOT_EQUAL,
    IS_NULL,
    IS_NOT_NULL,
    BETWEEN,
    GREATER_THAN,
    GREATER_THAN_EQUAL,
    LESS_THAN,
    LESS_THAN_EQUAL,
    FULL_LIKE,
    NOT_LIKE,
    LEFT_LIKE,
    RIGHT_LIKE,
    IN
}
```

## 更新日志
### 1.2.2.RELEASE
- 提供使用注解配置启动
### 1.2.1.RELEASE
- 提供分页支持
### 1.1.1.RELEASE
- 提供使用文档
### 1.1.0.RELEASE
- 提供全部常用类型支持
### 1.0.0.RELEASE
- 提供常用的 EQUAL、BETWEEN、FULL_LIKE、IN支持

