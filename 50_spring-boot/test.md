类注解
// >>> spring boot 1.4.0 版本之后使用以下两个配置
@RunWith(SpringRunner.class)
@SpringBootTest(classes=MainApplication.class)

// >>> spring boot 1.4.0 版本之前使用以下三个配置
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = DemoApplication.class)  //在spring boot 1.4.0 版本之后取消了 //classes需要指定spring boot 的启动类如：DemoApplication.class 不然WebApplicationContext不被实例化
//@WebAppConfiguration


	@Autowired TController tController;
	
	@Test
	public void Test() {
		tController.test();
	}