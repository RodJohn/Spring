
freemarker、thymeleaf和velocity取舍

1.支持
freemarker、thymeleaf都还有更新。而velocity已经6年没更新了。springboot

2.性能
thymeleaf<freemarker

3.生成静态页面
thymeleaf:
freemarker：
4.开发效率
thymeleaf:使用了标签属性作为语法，使得前端和后段可以并行开发
更加接近HTML——没有奇怪的标签，只是增加了一些有意义的属性。
普通属性可以直接在浏览器中打开它,可以作为静态原型



我们再来对比前两者：目前了解，性能方面，但是它由于使用了标签属性作为语法，模板页面直接用浏览器渲染，使得前端和后段可以并行开发。freemarker使用</>这样的语法，就无法直接使浏览器渲染出原本页面的样子。




模板引擎的作用
将业务数据和视图层分开
html/js/css代码跟java代码的分离

模版引擎分两种
客户端引擎,
主要结合js实现html，
通过ajax获取model
好处:客户端分担服务器压力
服务端引擎，
由服务端生成完整的html返回客户端。
将模板文件转换成class文件,每次请求的时候绑定数据产生html文件
好处:便于seo




开发效率和运行效率\


