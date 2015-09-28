# keywordRecommendation
# API概述
目前提供3个 rest api：
- 文章主题预测接口
- 文章上传接口
- 主题热度查询接口

## 1.文章主题预测接口
地址：`http://123.59.47.76:2015/rest/artipredict/pred`

http参数：msg

输入字段：（BASE64编码）(注意数字与字符串类型的区别)
`{
	"articleid":200013431,
	
	"title":"xxx",
	
	"subcontent":"xxx",
	
	"content":"xxx",
	
	"appid":20,
	
	"keywords":null

}`

返回结果：(BASE64编码)
	''{
	''	"keywords": "(品牌,0.009502435237931574),(艺术,0.0069297525536256045),(技术,0.00655500812398464),(作品,0.006464332915292815),(系统,0.0047250207689718635),(媒体,0.0035890279073948453),(设计,0.003376003667210478),(消费者,0.002989399143943464),(销售,0.002968648343033899),(艺术家,0.0029448432139741586)",
	''	"articleid": 200013431,
	''	"related_arts": "200022448,200022850,200021064,200020649,200012421",
	''	"appid": 20
	''}

## 2.文章上传接口
地址：`http://123.59.47.76:2015/rest/artipredict/upload`
http 参数:msg
输入字段：（BASE64编码）(注意数字与字符串类型的区别)
	''{
	''	"articleid":200013431,
	''	"title”:”xxx”,
	''	"subcontent":"xxx”,
	''	"content”:”xxx”,
	''	"appid":20,
	''	"keywords”:””
	''}
返回结果：(BASE64编码)
	''{
	''	"articleid":12,
	''	"appid":20,
	''	“detail”:”1”  ##交互详情，正常情况下返回“1”
	''}

## 3.主题热度查询接口
地址：`http://123.59.47.76:2015/rest/artipredict/hot`
http参数： msg
输入字段：（BASE64编码）(注意数字与字符串类型的区别)
	''{
	''	"startDate": "2015-09-01",  #目前时间参数无效
	''	"stopDate": "2015-09-10",
	''	"appid": 20
	''}
返回结果：(BASE64编码) 
	''{
	''	"topic_hot": "(美国,美元,增长,全球,数据,企业,管理,投资,报告,收入,2151),(设计,系列,设计师,风格,时尚,世界,文化,上海,传统,时装,333),(星座,幸运,事情,爱情,喜欢,朋友,事业,东西,男人,来说,94),(大学,美国,学生,纽约,学校,教育,翻译,学院,The,杂志,42),(技术,系统,设计,奥迪,生产,设备,机器人,车型,创新,未来,21),(皮肤,健康,方法,大脑,身体,成分,饮料,作用,肌肤,效果,10),(电影,影片,故事,演员,好莱坞,小说,拍摄,观众,角色,美国,10),(航空,飞机,机场,新加坡,航班,警方,无人,乘客,飞行,美国,7),(伦敦,黄金,英国,澳门,瑞典,期货,MW,金价,世界,网球,6),(用户,谷歌,网站,游戏,数据,信息,软件,微软,社交,科技,5),(医疗,医生,病毒,医院,治疗,患者,埃博拉,药物,疾病,感染,4),(政治,社会,主义,世界,历史,自由,时代,选举,民主,文化,4),(摄影师,美国,当地,伊朗,以色列,英国,地区,时区,恐怖,埃及,4),(国际,瑞士,调查,指控,足联,主席,投票,腐败,丑闻,委员会,2),(品牌,消费者,销售,产品,营销,销售额,时尚,门店,全球,集团,1),(手机,智能,网络,电脑,设备,电子,小米,三星,联想,平板,1),(漫画,奢侈品,习近平,反腐,英雄,超级,戛纳,动漫,丹麦,漫威,1),(游客,迪士尼,公园,新西兰,巧克力,奶粉,搜狐,乐园,牛奶,主题,1),(巴西,比赛,足球,世界杯,体育,俱乐部,球员,阿根廷,球队,运动员,1),(投资,投资者,指数,基金,交易,股票,美元,股市,上涨,下跌,1)",
	''	"appid": 20
	''}
