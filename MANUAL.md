@JFinal 的 JF2.1 已经 release 了，加了众多牛逼的特性，让我们的coding更加的爽了。JF 是一个精心设计的的服务端框架，极简极小是其优势，而 jfinal-ext2是在 jfinal 基础之上做了一个精心的扩展，让你更省心。

前两天，我刚刚更新了 jfinal-ext2(以下简称 jfex2)到 v2.0.1版本，此版本基于 JF2.1，整合了 JF2.1的 Generator，自动 mapping 等。同时更新了配置格式。本文主要讲解如何在项目中使用jfex2。

jfex2有部分特性基于 jfinal-ext，但发现 jfinal-ext 基于JF1.9，很久没有更新了。于是，我将 jfinal-ext 的 code 自己 copy 到了 jfex2中，并将其做了修改以适应 JF2.1，对于出错部分，我没有过多的去考虑，能改的我就改了，比较打动的，我就删除了。这里点一下，JF2.1吧2.0的 Logger 改名成 Log 了，可是伤透了心"_"。

####1. jfex2的依赖
完整的依赖列表

```java
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.jfinal</groupId>
	<artifactId>jfinal-ext2</artifactId>
	<version>2.0.1</version>
	<name>jfinal-ext2</name>
	<description>jfinal-ext2</description>
	<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.8.2</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>com.jfinal</groupId>
			<artifactId>jfinal</artifactId>
			<version>2.1-SNAPSHOT</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context</artifactId>
			<version>3.2.4.RELEASE</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>druid</artifactId>
			<version>1.0.5</version>
		</dependency>
		<dependency>
			<groupId>log4j</groupId>
			<artifactId>log4j</artifactId>
			<version>1.2.16</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>com.jfinal</groupId>
			<artifactId>cos</artifactId>
			<version>26Dec2008</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>fastjson</artifactId>
			<version>1.2.6</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
			<version>3.1</version>
		</dependency>
		<dependency>
			<groupId>commons-collections</groupId>
			<artifactId>commons-collections</artifactId>
			<version>3.2.1</version>
		</dependency>
		<dependency>
			<groupId>com.google.guava</groupId>
			<artifactId>guava</artifactId>
			<version>18.0</version>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>5.1.34</version>
		</dependency>
		<!-- jfext dependency start -->
		<dependency>
			<groupId>org.freemarker</groupId>
			<artifactId>freemarker</artifactId>
			<version>2.3.21</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.quartz-scheduler</groupId>
			<artifactId>quartz</artifactId>
			<version>1.8.6</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.quartz-scheduler</groupId>
			<artifactId>quartz</artifactId>
			<version>2.2.1</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.apache.poi</groupId>
			<artifactId>poi</artifactId>
			<version>3.10.1</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.apache.poi</groupId>
			<artifactId>poi-ooxml</artifactId>
			<version>3.10.1</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.mongodb</groupId>
			<artifactId>mongo-java-driver</artifactId>
			<version>2.11.2</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>it.sauronsoftware.cron4j</groupId>
			<artifactId>cron4j</artifactId>
			<version>2.2.5</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.apache.activemq</groupId>
			<artifactId>activemq-core</artifactId>
			<version>5.7.0</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>javax.jms</groupId>
			<artifactId>jms-api</artifactId>
			<version>1.1-rev-1</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>net.sf.jxls</groupId>
			<artifactId>jxls-core</artifactId>
			<version>0.9.9</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>redis.clients</groupId>
			<artifactId>jedis</artifactId>
			<version>2.2.1</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.crazycake</groupId>
			<artifactId>shiro-redis</artifactId>
			<version>1.0.0-RELEASE</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.apache.shiro</groupId>
			<artifactId>shiro-all</artifactId>
			<version>1.2.2</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>net.sf.jxls</groupId>
			<artifactId>jxls-core</artifactId>
			<version>0.9.9</version>
			<scope>provided</scope>
		</dependency>
		<!-- jfext dependency end -->
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.3</version>
				<configuration>
					<source>1.7</source>
					<target>1.7</target>
				</configuration>
			</plugin>
			<plugin>
				<artifactId>maven-source-plugin</artifactId>
				<version>2.4</version>
				<executions>
					<execution>
						<phase>package</phase>
						<goals>
							<goal>jar-no-fork</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
</project>
```
其中部分为 jfex2依赖,大多为 jfext 依赖,使用时可以更加自己的需要添加依赖;

####2. 配置说明

完整的配置

```shell
#-----------------------------------------------------------------#
#  database　Config
# 1. db.ds: db datasource name, use ',' split. eg :mysql,oracle;
# 2. db.*.active:ture, use db,* is the ds name;
# 3. db.*.url: db url,* is the ds name;
# 4. db.*.user: db username,* is the ds name;
# 5. db.*.password: db password,* is the ds name, 
#	use `java -cp druid-xx.jar com.alibaba.druid.filter.config.ConfigTools your_password`
#	generate your encrypt password;
# 6. db.*.initsize: db pool init size,* is the ds name;
# 7. db.*.maxactive: db pool maxactive,* is the ds name;
# 8. db.showsql: ture, show execute sql;
#-----------------------------------------------------------------#
db.ds = mysql //配置数据源,目前主要测试了 mysql,oracle 没有经过测试,使用者自行测试。db.ds 数据格式,比如使用了 mysql和 oracle,那么 db.ds = mysql,oracle。注意使用英文输入下的","。以下说明均以 db.ds = mysql 进行说明;
db.mysql.active = false //是否激活数据源,命名规范为->db.数据源名称.active,比如 db.mysql.active,db.oracle.active。
db.mysql.url = testing_host/db //数据库 url,命名规范->db.数据源名.url,比如 db.mysql.url,db.oracle.url。 
db.mysql.user = //数据库用户,命名规范->db.数据源名.user,比如db.mysql.user,db.oracle.user。
db.mysql.password = lCzd9geWAuAuJtLhpaG/J+d28H8KiMFAWopxXkYpMNdUai6Xe/LsPqMQeg5MIrmvtMa+hzycdRhWs29ZUPU1IQ== //数据库(已经加密)密码,命名规范->db.数据源名.password,比如 db.mysql.password,db.oracle.password。
db.mysql.initsize = 6 //数据库连接池初始大小,命名规范->db.数据源名.initsize,比如 db.mysql.initsize,db.oracle.initsize。
db.mysql.maxactive = 100 //数据库连接池最大激活数,命名规范->db.数据源名.maxactive,比如 db.mysql.maxactive,db.oracle.maxactive。
db.showsql = true //是否显示 sql
#-----------------------------------------------------------------#
# Generator Config
# 1. ge.run: true, generate the model and basemmodels;//是否自动生成 BaseModel和 Model;
# 2. ge.dict: true, generate the data dict;//是否生成数据词典;
# 3. ge.base.model.outdir: the basemodel output dir path;//BaseModel 导出目录,可为绝对路径,也可以为相对路径;
# 4. ge.base.model.package: the basemodel package;//BaseModel的包名;
# 5. ge.model.outdir: the model output dir path;//Model导出目录,可为绝对路径,也可以为相对路径;
# 6. ge.model.package: the model package;//Model 的包名;
#-----------------------------------------------------------------#
ge.run=true
ge.dict=true
ge.base.model.outdir=
ge.base.model.package=
ge.model.outdir=
ge.model.package=
#-----------------------------------------------------------------#
## App Config
# 1. app.dev: true, the app is debug mode;
# 2. app.upload.basedir: upload file save base dir;
# 3. app.post: ture, use Http Post method;
# 4. app.name: app's name;
#-----------------------------------------------------------------#
app.dev = true
app.upload.basedir = /var/uploads //上传文件的存储路径,最后的文件路径为:/var/uploads/app.name/xxxx,也就是/var/uploads/app 的名称/xxx。
app.post = true //是否全部使用 post 方式。注意哦!!!这里设置了true 的话,你的 Controller 的 Action在浏览器上访问会404哦。
app.name = 
```

``特别注意
app.post = true //是否全部使用 post 方式。注意哦!!!这里设置了true 的话,你的 Controller 的 Action在浏览器上访问会404哦。
``

####3. jfex2-Config & StandaloneDbConfig

3.1 jfex2-config 主要用于 Web 项目中使用,当然也可以用于非 web 项目,建议在非web 中使用StandaloneDbConfig。

几个重要的参数说明

- JFinalConfigExt.APP_NAME 应用名称
- JFinalConfigExt.UPLOAD_SAVE_DIR 应用存储路径
- JFinalConfigExt.DEV_MODE 应用的模式

3.2 jfex2-config做了什么?

jfex2-config 主要做的是解析 cfg.txt 配置文件的工作。并集成JF2.1Generator,数据库,ErrorPageView等配置。

3.3 怎样在项目中使用 jfext2-config

- 首先集成 JFinalConfigExt 做一个之类比如是这样的:

	```java
	package cn.zhucongqi;
	...
	public class Config extends JFinalConfigExt {
		...
	}
	```
-  在 web.xml 配置
	
	```xml
	<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd" id="WebApp_ID" version="3.1">
  <display-name>JFinal-Ext2-demo</display-name>
  <welcome-file-list>
    <welcome-file>index.html</welcome-file>
    <welcome-file>index.htm</welcome-file>
    <welcome-file>index.jsp</welcome-file>
    <welcome-file>default.html</welcome-file>
    <welcome-file>default.htm</welcome-file>
    <welcome-file>default.jsp</welcome-file>
  </welcome-file-list>
  <filter>
    <filter-name>jfinal</filter-name>
    <filter-class>com.jfinal.core.JFinalFilter</filter-class>
    <init-param>
      <param-name>configClass</param-name>
      <param-value>cn.zhucongqi.Config</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>jfinal</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  <error-page>
    <error-code>403</error-code>
    <location>/WEB-INF/errorpages/403.jsp</location>
  </error-page>
  <error-page>
    <error-code>404</error-code>
    <location>/WEB-INF/errorpages/404.jsp</location>
  </error-page>
  <error-page>
    <error-code>500</error-code>
    <location>/WEB-INF/errorpages/500.jsp</location>
  </error-page>
</web-app>
	```
	
####4. jfex2结构

```
.
├── java
│   └── com
│       └── jfinal
│           └── ext2
│               ├── config
│               │   ├── JFinalConfigExt.java
│               │   └── StandaloneDbConfig.java
│               ├── core //Controller 扩展
│               │   ├── ControllerExt.java
│               │   └── Service.java
│               ├── ge //自动生成 BaseModel和 Model 的入口;
│               │   └── Ge.java
│               ├── handler
│               │   └── ActionExtentionHandler.java
│               ├── interceptor
│               │   ├── ExceptionInterceptorExt.java
│               │   └── NotFoundActionInterceptor.java
│               ├── kit
│               │   ├── DDLKit.java
│               │   ├── DateTimeKit.java
│               │   ├── EncodingKit.java
│               │   ├── HexKit.java
│               │   ├── HttpExtKit.java
│               │   ├── JsonExtKit.java
│               │   ├── MobileValidateKit.java
│               │   ├── PageViewKit.java //PageView 的工具
│               │   ├── RandomKit.java
│               │   └── SqlKit.java
│               ├── plugin
│               │   ├── activerecord
│               │   │   ├── generator
│               │   │   │   └── ModelExtGenerator.java
│               │   │   └── tx
│               │   │       ├── DbTx.java
│               │   │       ├── DbTxInterceptor.java
│               │   │       └── Tx.java
│               │   ├── druid
│               │   │   └── DruidEncryptPlugin.java
│               │   └── spring
│               │       ├── Inject.java
│               │       ├── IocInterceptor.java
│               │       └── SpringPlugin.java
│               ├── upload
│               │   └── filerenamepolicy
│               │       ├── CustomNameFileRenamePolicy.java
│               │       ├── CustomParentDirFileRenamePolicy.java
│               │       ├── DateRandomFileRenamePolicy.java
│               │       ├── FileRenamePolicyWrapper.java
│               │       ├── NamePolicy.java
│               │       └── RandomFileRenamePolicy.java
│               └── validate
│                   └── ValidatorExt.java
└── resources
    ├── cfg.txt.example
    ├── errorpages
    │   ├── 403.jsp
    │   ├── 404.jsp
    │   └── 500.jsp
    ├── log4j.properties.example
    ├── readme.md
    └── web.xml.example
```

####5. jfex2-Ge

jfex2-Ge 怎样使用?

- 选中项目;
- 右击项目,在菜单中选择Debug As -> Debug Configurations...;
- 弹出Debug Configurations窗口,在左边找到Java Application双击它;
- 此时在右边出现配置窗口,最上面是 name,设置一个名,比如为 Ge;
- 在下面的第一栏为“Main”,确认Project 为当前已经选择的 Project;
- Main.class 中输入 "com.jfinal.ext2.ge.Ge";
- 配置完成点击 Debug 即可;