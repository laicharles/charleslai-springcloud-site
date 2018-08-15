# charleslai-springcloud-site
一、spring cloud简介

spring cloud 为开发人员提供了快速构建分布式系统的一些工具，包括配置管理、服务发现、断路器、路由、微代理、事件总线、全局锁、决策竞选、分布式会话等等。它运行环境简单，可以在开发人员的电脑上跑。另外说明spring cloud是基于springboot的，所以需要开发中对springboot有一定的了解，如果不了解的话可以看这篇文章：2小时学会springboot。另外对于“微服务架构” 不了解的话，可以通过搜索引擎搜索“微服务架构”了解下。

二、创建服务注册中心

在这里，我们需要用的的组件上Spring Cloud Netflix的Eureka ,eureka是一个服务注册和发现模块。

2.1 首先创建一个maven父项目工程。


创建项目类型


填写基础信息


点击[Finish]完成创建父项目

2.2 然后创建2个model工程:一个model工程作为服务注册中心，即Eureka Server,另一个作为Eureka Client。


创建子项目


选择SpringInitializr ,注意选择sdk和保持 Service URL

下一步->选择cloud discovery->eureka server ,然后一直下一步就行了。


创建完后的工程的pom.xml文件如下：

<dependencies>

<!--引用父类-->

<dependency>

<groupId>com.charleslai</groupId>

<artifactId>charleslai-springcloud-site</artifactId>

<version>1.0-SNAPSHOT</version>

</dependency>

<dependency>

<groupId>org.springframework.cloud</groupId>

<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>

</dependency>

<dependency>

<groupId>org.springframework.boot</groupId>

<artifactId>spring-boot-starter-test</artifactId>

<scope>test</scope>

</dependency>

说明,由于项目结构为父子结构,所以把一些公用的包应用放在父项目的pom.

而父项目的pom

增加了

<!-- Java 6 = JAX-B Version 2.0 -->

<!-- Java 7 = JAX-B Version 2.2.3 -->

<!-- Java 8 = JAX-B Version 2.2.8 -->

<dependency>

<groupId>javax.xml.bind</groupId>

<artifactId>jaxb-api</artifactId>

<version>2.3.0</version>

</dependency>

<dependency>

<groupId>com.sun.xml.bind</groupId>

<artifactId>jaxb-impl</artifactId>

<version>2.3.0</version>

</dependency>

<dependency>

<groupId>com.sun.xml.bind</groupId>

<artifactId>jaxb-core</artifactId>

<version>2.3.0</version>

</dependency>

<dependency>

<groupId>javax.activation</groupId>

<artifactId>activation</artifactId>

<version>1.1.1</version>

</dependency>

以上的引用是为了防止 运行报错,具体查看 我的另外一篇 ClassNotFoundException: javax.xml.bind.JAXBException

https://www.toutiao.com/i6589836127480316429/

2.3 启动一个服务注册中心，只需要一个注解@EnableEurekaServer，这个注解需要在springboot工程的启动application类上加：

@EnableEurekaServer

@SpringBootApplication

public class EurekaserverApplication {

public static void main(String[] args) {

SpringApplication.run(EurekaserverApplication.class, args);

}

}

2.4*eureka是一个高可用的组件，它没有后端缓存，每一个实例注册之后需要向注册中心发送心跳（因此可以在内存中完成），在默认情况下erureka server也是一个eureka client ,必须要指定一个 server。eureka server的配置文件application.yml：

server:

port: 8761

eureka:

instance:

hostname: localhost

client:

registerWithEureka: false

fetchRegistry: false

serviceUrl:

defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/

通过eureka.client.registerWithEureka：false和fetchRegistry：false来表明自己是一个eureka server.

2.5 eureka server 是有界面的，启动工程,打开浏览器访问：

http://localhost:8761 ,界面如下：


No application available 没有服务被发现 ……^_^

因为没有注册服务当然不可能有服务被发现了。

三、创建一个服务提供者 (eureka client)

当client向server注册时，它会提供一些元数据，例如主机和端口，URL，主页等。Eureka server 从每个client实例接收心跳消息。 如果心跳超时，则通常将该实例从注册server中删除。

创建过程同server类似,创建完pom.xml如下：

<dependencies>

<!--引用父类-->

<dependency>

<groupId>com.charleslai</groupId>

<artifactId>charleslai-springcloud-site</artifactId>

<version>1.0-SNAPSHOT</version>

</dependency>

<dependency>

<groupId>org.springframework.cloud</groupId>

<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>

</dependency>

<dependency>

<groupId>org.springframework.boot</groupId>

<artifactId>spring-boot-starter-test</artifactId>

<scope>test</scope>

</dependency>

</dependencies>

通过注解@EnableEurekaClient 表明自己是一个eurekaclient.

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer

@SpringBootApplication

public class CharleslaiSpringcloudEurekaserverApplication {

public static void main(String[] args) {

SpringApplication.run(CharleslaiSpringcloudEurekaserverApplication.class, args);

}

}

仅仅@EnableEurekaClient是不够的，还需要在配置文件中注明自己的服务注册中心的地址，application.yml配置文件如下：

eureka:

client:

serviceUrl:

defaultZone: http://localhost:8761/eureka/

server:

port: 8762

spring:

application:

name: service-hi

需要指明spring.application.name,这个很重要，这在以后的服务与服务之间相互调用一般都是根据这个name 。

启动工程，打开http://localhost:8761 ，即eureka server 的网址：


你会发现一个服务已经注册在服务中了，服务名为SERVICE-HI ,端口为7862

这时打开 http://localhost:8762/hi?name=forezp ，你会在浏览器上看到 :

hi forezp,i am from port:8762
