package com.wolf.test.base;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p> Description:正则表达式测试
 * 默认是贪婪模式；在量词后面直接加上一个问号？就是非贪婪模式
 * 　量词：{m,n}：m到n个
 * <p>
 * 　　　　　*：0到多个
 * <p>
 * 　　　　　+：一个到多个
 * <p>
 * 　　　　　？：0或一个
 * <p/>
 * /r Mac
 * /n Unix/Linux
 * /r/n Windows
 * <p>
 * Date: 2016/3/31
 * Time: 15:11
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class RegexTest {

	public static void main(String[] arg) {
//		test1();
//		test2();
//		test3();
//		test4();
//		test5();
//		test6();
//		test7();
//		test8();
//		test9();
//		test10();
//		testIpFromUrl();
	}

	@Test
	public void test1() {
		String text = "(content:\"rcpt to root\";pcre:\"word\";)";
		String rule1 = "content:\".+\"";    //贪婪模式
		String rule2 = "content:\".+?\"";    //非贪婪模式

		System.out.println("文本：" + text);
		System.out.println("贪婪模式：" + rule1);
		Pattern p1 = Pattern.compile(rule1);
		Matcher m1 = p1.matcher(text);
		while (m1.find()) {
			System.out.println("匹配结果：" + m1.group(0));
		}
		String s1 = m1.replaceAll("");//将匹配的都替换成""，结果是没匹配到的
		System.out.println("替换后结果:" + s1);

		System.out.println("非贪婪模式：" + rule2);
		Pattern p2 = Pattern.compile(rule2);
		Matcher m2 = p2.matcher(text);
		while (m2.find()) {
			System.out.println("匹配结果：" + m2.group(0));
		}
		String s2 = m2.replaceAll("");
		System.out.println("替换后结果:" + s2);
	}

	@Test
	public void test2() {
		String text = "<divqxxxx</divqababab<divq11111</divq";
//		String text2="<div>xxxx</div>ababab<div>11111</div>";//好像>自身带有费贪婪，上面改成q就不行了。。
		String rule1 = "<[^>]+q";    //贪婪模式
		String rule2 = "<[^>]+?q";    //非贪婪模式

		System.out.println("文本：" + text);
		System.out.println("贪婪模式：" + rule1);
		Pattern p1 = Pattern.compile(rule1);
		Matcher m1 = p1.matcher(text);
		while (m1.find()) {
			System.out.println("匹配结果：" + m1.group(0));
		}
		String s1 = m1.replaceAll("");
		System.out.println("替换后结果:" + s1);

		System.out.println("非贪婪模式：" + rule2);
		Pattern p2 = Pattern.compile(rule2);
		Matcher m2 = p2.matcher(text);
		while (m2.find()) {
			System.out.println("匹配结果：" + m2.group(0));
		}
		String s2 = m2.replaceAll("");
		System.out.println("替换后结果:" + s2);
	}

	@Test
	public void test3() {
		String htmlStr = "<div style='text-align:center;'> 整治“四风”   清弊除垢<br/>aa<script>asdf111</script>bbb<script>asdf222</script>cc<span style='font-size:14px;'> </span><span style='font-size:18px;'>公司召开党的群众路线教育实践活动动员大会</span><br/></div>";
		Pattern p_script = Pattern.compile("<script[^>]*?>[\\s\\S]*?<\\/script>", Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile("<style[^>]*?>[\\s\\S]*?<\\/style>", Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile("<[^>]*>", Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		htmlStr = htmlStr.replaceAll("&nbsp;", "");

		System.out.println(htmlStr);
	}

	/**
	 * 获取中间字符串，使用分组
	 */
	private static void test4() {
		String htmlStr = "<script type=\"text/javascript\">XXXXX</script>";
		Pattern p_script = Pattern.compile("<script[^>]*?>([\\s\\S]*?)<\\/script>", Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);

		int i = m_script.groupCount();
		System.out.println(i);

		if (m_script.find()) {
			String group = m_script.group(0);
			System.out.println(group);
			String group1 = m_script.group(1);
			System.out.println(group1);
		}

	}


	/**
	 * 匹配100以内数字
	 */
	private static void test5() {
		String htmlStr = "01";
		Pattern p_script = Pattern.compile("^([1-9]?[0-9]|100)$", Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);

		System.out.println(m_script.matches());

	}

	/**
	 * 正常捕获
	 */
	private static void test6() {
		String htmlStr = "23";
		Pattern p_script = Pattern.compile("^([1-9]?([0-9])|100)$", Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);

		if (m_script.find()) {
			String group = m_script.group(0);
			System.out.println(group);

			String group1 = m_script.group(1);
			System.out.println(group1);

			//此处只为展示，正常正则不会用这个()
			String group2 = m_script.group(2);
			System.out.println(group2);
		}

	}

	/**
	 * 正常捕获,增加效率
	 */
	private static void test7() {
		String htmlStr = "23";
		Pattern p_script = Pattern.compile("^(?:[1-9]?([0-9])|100)$", Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);

		//捕获组只是()中的个数，和group(0)没有关系
		int i = m_script.groupCount();
		System.out.println(i);

		if (m_script.find()) {
			String group = m_script.group(0);
			System.out.println(group);

			String group1 = m_script.group(1);
			System.out.println(group1);

		}

	}

	/**
	 * 非捕获
	 */
	private static void test8() {
		String htmlStr = "23";
		Pattern p_script = Pattern.compile("^([1-9]?(?:[0-9])|100)$", Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);

		if (m_script.find()) {
			String group = m_script.group(0);
			System.out.println(group);

			String group1 = m_script.group(1);
			System.out.println(group1);

			//这个报错，没有捕获到第三个
			String group2 = m_script.group(2);
			System.out.println(group2);
		}

	}

	/**
	 * \s 匹配任何不可见字符，包括空格、制表符、换页符等等。等价于[ \f\n\r\t\v]。
	 * \S 匹配任何可见字符。等价于[^ \f\n\r\t\v]。
	 * [\s\S]任意字符
	 * [\s\S]* 0个到任意多个字符
	 * [\s\S]*? 0个字符，匹配任何字符前的位置,非贪婪。
	 */
	private static void test9() {
		String htmlStr = "2312321";
		Pattern p_script = Pattern.compile("[\\s\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);

		if (m_script.find()) {
			String group = m_script.group(0);
			System.out.println(group);
		}

		Pattern p_script1 = Pattern.compile("[\\s\\S]*?", Pattern.CASE_INSENSITIVE);
		Matcher m_script1 = p_script1.matcher(htmlStr);

		//没有
		if (m_script1.find()) {
			String group = m_script1.group(0);
			System.out.println(group);
		}

		//+ 一到多个
		Pattern p_script2 = Pattern.compile("[\\S]+?", Pattern.CASE_INSENSITIVE);
		Matcher m_script2 = p_script2.matcher(htmlStr);

		if (m_script2.find()) {
			String group = m_script2.group(0);
			System.out.println(group);
		}

		//* 零到多个
		Pattern p_script3 = Pattern.compile("[\\S]*?", Pattern.CASE_INSENSITIVE);
		Matcher m_script3 = p_script3.matcher(htmlStr);

		//没有
		if (m_script3.find()) {
			String group = m_script3.group(0);
			System.out.println(group);
		}
	}

	private static void test10() {
		String s = "C:\\盘";//单个反斜杠，在java中被认为是转义符
		System.out.println(s);
		System.out.println(s.replace("\\", ""));//oldChar
		System.out.println(s.replaceAll("\\\\", ""));//regex,java先将\\\\转成\\，然后正则再弄成\

		//\r carriage return  \n new line
		String s1 = "aa\\r bb";
		System.out.println("s1:" + s1);
		String s2 = "aaasdsad\rbb";//不论\r前有什么，光标定位到开始，从新输出bb
		System.out.println("s2:" + s2);
		//java中\r 和\r\n 一样
		String s3 = "aa\nbb";
		System.out.println("s3:" + s3);
		String s4 = "aa\r\nbb";
		System.out.println("s4:" + s4);
		String s5 = "aa\n\rbb";
		System.out.println("s5:" + s5);
	}


	private static void testIpFromUrl() {
		String url = "http://localhost:8082/xxintrdriver";
//		String url = "127.0.0.12333asdd:8082/xxintrdriver";

		//?<=表示:非获取匹配，反向肯定预查，即((\w)+\.)+\w+之前要有//或者什么也没有
		Pattern p = Pattern.compile("(?<=//)((\\w)+\\.)+\\w+");
		Matcher m = p.matcher(url);
		if (m.find()) {
			System.out.println(m.group());
		}

		System.out.println("127.0.0.1".matches("((\\w)+\\.)+\\w+"));
		System.out.println("127.0.0.".matches("((\\w)+\\.)+"));
	}

	@Test
	public void testNonCapture() {
		String string1 = "industries";
		//(?:pattern)非获取匹配，匹配pattern但不获取匹配结果，不进行存储供以后使用
		Pattern p1 = Pattern.compile("industr(?:y|ies)");
		Matcher m1 = p1.matcher(string1);
		if (m1.find()) {
			System.out.println("m1==>"+m1.group());
			//报错，因为(?:pattern)不捕获
			//System.out.println(m1.group(1));
		}

		String string2 = "Windows2000";
		//(?=pattern)非获取匹配，正向肯定预查，在任何匹配pattern的字符串开始处匹配查找字符串，该匹配不需要获取供以后使用。
		Pattern p2 = Pattern.compile("Windows(?=95|98|NT|2000)");
		Matcher m2 = p2.matcher(string2);
		if (m2.find()) {
			System.out.println("m2==>"+m2.group());
		}

		String string3 = "Windows3.1";
		//(?!pattern)非获取匹配，正向否定预查，在任何不匹配pattern的字符串开始处匹配查找字符串，该匹配不需要获取供以后使用。
		Pattern p3 = Pattern.compile("Windows(?!=95|98|NT|2000)");
		Matcher m3 = p3.matcher(string3);
		if (m3.find()) {
			System.out.println("m3==>"+m3.group());
		}

		String string4 = "2000Windows";
		//(?<=pattern)非获取匹配，反向肯定预查，与正向肯定预查类似，只是方向相反。
		Pattern p4 = Pattern.compile("(?<=95|98|NT|2000)Windows");
		Matcher m4 = p4.matcher(string4);
		if (m4.find()) {
			System.out.println("m4==>"+m4.group());
		}

		String string5 = "3.1Windows";
		//(?<!pattern)非获取匹配，反向否定预查，与正向否定预查类似，只是方向相反。
		Pattern p5 = Pattern.compile("(?<!95|98|NT|2000)Windows");
		Matcher m5 = p5.matcher(string5);
		if (m5.find()) {
			System.out.println("m5==>"+m5.group());
		}

	}

	@Test
	public void testRemoveSuffix() {
		String timeOffDateString = "2016年01月12日 上午".replaceAll("(\\d+年\\d+月\\d+日)\\s+[\\u4E00-\\u9FA5]+", "$1");
		System.out.println(timeOffDateString);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String format = simpleDateFormat.format(new Date());
		String s = format + " 上午";
		System.out.println(s);
		System.out.println(s.substring(0, 11));
	}

	@Test
	public void testRemoveChinese() {
		//仅中文替换
		System.out.println("中ab哈c过".replaceAll("[\\u4e00-\\u9fa5]", "1"));
		//非ascii码替换(中文，全角啥的)
		System.out.println("中ab哈c过".replaceAll("[^\\x00-\\xff]", "1"));
		//非ascii码替换
		System.out.println("中ab哈c过".replaceAll("[^x00-xff]", "1"));

		System.out.println("ｃ中ab哈c过".replaceAll("[^\\x00-\\xff]", "1"));
		System.out.println("ｃ中ab哈c过".replaceAll("[\\u4e00-\\u9fa5]", "1"));
	}

	@Test
	public void testValidateVehicle() {
		System.out.println("津MOI877".replaceAll("^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z0-9]{5}$", "1"));
	}

	@Test
	public void testExclude() {
		Pattern pattern = Pattern.compile("LocationCityUtil\\.(?!getxxCityList)");
		Matcher matcher = pattern.matcher("LocationCityUtil.getxxx");
		System.out.println(matcher.find());
		Matcher matcher2 = pattern.matcher("LocationCityUtil.getxxCityList");
		System.out.println(matcher2.find());
	}

	@Test
	public void testCaseInsensitive() {
		String x1 = "Https://xxx.jpg".replaceFirst("(?i)https", "http");//https忽略
		System.out.println(x1);
		String x2 = "hTtps://xxx.jpg".replaceFirst("h(?i)ttps", "http");//ttps忽略
		System.out.println(x2);
		String x3 = "httPs://xxx.jpg".replaceFirst("htt((?i)p)s", "http");//p忽略
		System.out.println(x3);
		Pattern pattern = Pattern.compile("https", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher("Https://xxx.jpg");
		boolean b = matcher.find();
		System.out.println(b);
	}
}
