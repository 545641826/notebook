# 重构日志
## 前言
"我要去重构代码了,拜."有什么比这更凄美的诀别吗?确实,重构很烦,特别是重构一些很垃圾的代码.

这次我要重构的是我大一寒假写的代码,那时的我只学过c,没有面向对象概念,代码毫无风格,在网上搜了个框架来不及消化便草草改成同学要求的样子.两天时间,无中生有,我还是蛮佩服自己的,但代码确实糟糕.

~~这坨屎~~这段代码是为一个美术专业的同学重构的,对某些人来说注释可能比较详细,但这方面我不接受任何批评(皮),其他不足的地方还请多多指教.

重构之路开始咯 201810031520ylin
## 代码介绍
这是用Android studio写的java代码,前端靠鼠标,代码靠复制,好像用到了access数据库.当时的我并不懂什么面向对象,什么数据库,靠脑洞意淫出了各种乱七八糟的概念,大部分在我后来的学习生活中才得到印证,甚至很久之后才知道他们的名字,这很痛苦,也很有趣.
## 顺序
@txu:可以根据我重构的顺序阅读代码哦!我不清楚怎么才算重要,就把你可能改的代码注释详细点,其它一笔带过.所以遇见看不懂的你可以直接跳过.此外,特别重要的注释我会在注释内容里加"@txu:".如:"\<!-- @txu: -->  ","// @txu:".

- 顺序逻辑:从主界面(AndroidManifest.xml)代码出发,依次遍历程序自动运行的各部分代码.在文档末会标注下一页代码的文件名.
- 结构图:
```mermaid
graph LR
a[AndroidManifest.xml] --> aa[src/main/AndroidManifest.xml]
aa --> ab[com.example.student]
aa --> [com.example.student_info]
ab --> ac[com.example.SearchView]
ab --> ad[com.example.addActivity]
```
## 阶段201810032313ylin
重构之路还很长,改表名就打乱了很多代码,很多变量名没改,函数也需要封装.不过注释总算是完成了,其他的慢慢改吧.
@txu:java\com\example\addActivity.java的结尾是一段分享按钮实现的代码.
