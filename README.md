# asiya_mylove
##### asiya_mylove是用springcloud开发的视频网站，可以简单实现用户和视频的管理，视频的点赞评论和播放，用户点赞等功能。此项目是我的第一个微服务项目，继承自springboot实现的[asiya](https://github.com/lclasiya/asiya)项目，以后会不断增加新功能，优化代码。目前使用到的主要后端技术点如下：
* 用户的权限管理和单点登录方面使用***oauth2***和***jwt***实现
* 视频检索方面使用***elasticsearch***实现，，可根据喜好或关键字来检索
* 视频的点赞和评论功能使用***redis***实现，其数据通过quartz定时存入数据库
* 视频模块添加视频后，通过***rabbitmq***储存视频id和session，然后在搜索模块获得消息，再根据视频id和session通过feign来获得视频信息，最后加入elasticsearch里面。rabbitmq过程事务化，保证视频添加万无一失
* 用户点赞方面使用redis锁保证点赞和取消点赞的原子性
* 用户头像，视频等都存在***fastdfs***上
***
### 各工具版本
##### springboot-2.0.3,&emsp;   springcloud-Finchley.RELEASE,&emsp;   redis-4.0.14,&emsp;    elasticsearch-5.5.0,&emsp;   rabbitmq-3.7.7-management,&emsp;   fastdfs-(docker pull delron/fastdfs)    
#### 主页:
![homepage](https://github.com/lclasiya/asiya_mylove/blob/master/common/src/main/java/li/changlin/common/images/主页.png)
#### 用户注册:
![register](https://github.com/lclasiya/asiya_mylove/blob/master/common/src/main/java/li/changlin/common/images/%E7%94%A8%E6%88%B7%E6%B3%A8%E5%86%8C.png)
#### 视频上传:
![upload](https://github.com/lclasiya/asiya_mylove/blob/master/common/src/main/java/li/changlin/common/images/%E8%A7%86%E9%A2%91%E4%B8%8A%E4%BC%A0.png)
#### 用户页:
![userProfile](https://github.com/lclasiya/asiya_mylove/blob/master/common/src/main/java/li/changlin/common/images/userProfile.png)
#### 视频播放页:
![videoPage](https://github.com/lclasiya/asiya_mylove/blob/master/common/src/main/java/li/changlin/common/images/videoPage.png)
#### redis:
![redis](https://github.com/lclasiya/asiya_mylove/blob/master/common/src/main/java/li/changlin/common/images/redis.png)
