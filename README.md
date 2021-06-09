# wblsrs-Springboot
Springboot + Elasticsearch + Layui + Echarts 构建一个简易的微博历史热搜查询系统

**Springboot用作后端请求处理以及展示页面以及爬取微博热搜数据**
**Elasticsearch用作存储微博热搜数据**
**页面采用Layui的组件**
**Echarts用来可视化图表**


## 结果展示
### 首页
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210609204552158.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQzNjEzNzkz,size_16,color_FFFFFF,t_70)
### 按时间查询
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210609204700687.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQzNjEzNzkz,size_16,color_FFFFFF,t_70)
**放大：**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210609211345814.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQzNjEzNzkz,size_16,color_FFFFFF,t_70#pic_center)

#### 按内容查询
##### 模糊查询
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210609204928591.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQzNjEzNzkz,size_16,color_FFFFFF,t_70)
**放大：**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210609205307754.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQzNjEzNzkz,size_16,color_FFFFFF,t_70)

##### 精确查询
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210609205137871.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQzNjEzNzkz,size_16,color_FFFFFF,t_70)
**放大：**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210609205226842.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQzNjEzNzkz,size_16,color_FFFFFF,t_70)

## 启动程序后，访问[http://127.0.0.1:8080](http://127.0.0.1:8080) 即可
