# JFinal-ext2

基于JFinal 2.0加入一些kit，它们有

1. 扩展JFinalConfig=> JFinalExtConfig
	1.1 给每一个app设置一个name；
	1.2 从配置文件中获取文件的保存路径；
	1.3 获取devmode；
	1.4 打包DruidPlugin和ActiveRecordPlugin；
	以上让你的config更加轻便

2. 加入ActionExtentionHandler
	更方面的伪静态处理

3. com.jfinal.ext2.kit
	3.1 DateTimeKit;
	3.2 DDLKit;
	...

4. com.jfinal.ext2.validate.Validator
	默认开启短路，校验失败403