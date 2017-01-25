package com.wolf.test.base;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * <p> Description: DecimalFormat用来格式小数,
 * 对于小数最后一位：
 * #对0无效，没有就不管了，不是0则(默认HALF_EVEN)
 * 0对0有效，没有则补0，(默认HALF_EVEN)
 * <p/>
 * Date: 2015/9/8
 * Time: 10:20
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class DecimalFormatTest {

	@Test
	public void testDecimalFormat() {
		//NumberFormat df = NumberFormat.getInstance();
		DecimalFormat df = new DecimalFormat();
		df.setRoundingMode(RoundingMode.DOWN);
		double data = 1203.065607809;
		System.out.println("格式化之前：" + data);

		String pattern = "0.0";//1203.4
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

		pattern = "#.#";
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));


		pattern = "##.00";//1203.41
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

		pattern = "##.##";//1203.41
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

		pattern = "##.###";//1203.406
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

		pattern = "######.###";//1203.406
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

// 可以在模式后加上自己想要的任何字符，比如单位
		pattern = "00000000.000kg";//00001203.406kg
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

//#表示如果存在就显示字符，如果不存在就不显示，只能用在模式的两头
		pattern = "##000.000kg";//1203.406kg
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

//-表示输出为负数，必须放在最前面
		pattern = "-000.000";//-1203.406
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

//,是分组分隔符(如果没有特殊需要一般用  ,00  即可，需要保证位数补零用0，否则用#) ：输出结果12,03.41
		pattern = "0,00.0#";//-12,03.41
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

		pattern = "00,00.0#";//-12,03.41
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

		pattern = ",00.0#";//-12,03.41
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(120003.0));

		pattern = ",##.0#";//-12,03.41
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(120003.0));

		pattern = "000000000000,00.0#";//-12,03.41
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

//E表示输出为指数，”E“之前的字符串是底数的格式，之后的是指数的格式。
		pattern = "0.00E000";//1.20E003
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

//%表示乘以100并显示为百分数，要放在最后
		pattern = "0.00%";//120340.56%
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

//"\u2030"表示乘以1000并显示为千分数，要放在最后
		pattern = "0.00\u2030";//203405.61‰
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

//"\u00A4"货币符号，要放在两端*****1203.41￥
		pattern = "0.00\u00A4";//1203.41￥
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

//'用于在前缀或或后缀中为特殊字符加引号，要创建单引号本身，请连续使用两个单引号："# o''clock"。
		pattern = "'#'#" ;//#1203
// pattern = "'#'" ;//#1203
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

		pattern = "# o''clock" ;//1203 o'clock
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));
//''放在中间或后面单引号就显示在最后，放在最前面单引号就显示在最前
// pattern = "# o''clock.000" ;//1203.406 o'clock
// pattern = "# .000o''clock";//1203.406 o'clock
// pattern = "# .000''";//1203.406 '
// pattern = "# .''000";//1203.406 '
		pattern = "''# .000";//'1203.406
		df.applyPattern(pattern);
		System.out.println("采用" + pattern + "模式格式化后：" + df.format(data));

		//设定舍入模式
		df.setRoundingMode(RoundingMode.UP);
	}

	@Test
	public void testNumberFormat(){
		NumberFormat ddf1=NumberFormat.getNumberInstance() ;
		ddf1.setMaximumFractionDigits(2);
		//四舍五入
		System.out.println(ddf1.format(0.126d));

		System.out.println(ddf1.format(new BigDecimal("0.126")));

		NumberFormat fmt = NumberFormat.getPercentInstance();
		//先乘以100再取小数位,四舍五入
		fmt.setMaximumFractionDigits(1);
		System.out.println(fmt.format(0.1266d));

		//最小两位小数
		fmt.setMinimumFractionDigits(2);
		System.out.println(fmt.format(0.12d));

		String aDecimal = getADecimal(1.34);
		System.out.println(aDecimal);
	}


	private String getADecimal(double target){
		DecimalFormat df = new DecimalFormat("#.0");
		df.setRoundingMode(RoundingMode.DOWN);
		return df.format(target);
	}

	@Test
	public void testPercent(){
		NumberFormat fmt = NumberFormat.getPercentInstance();
		fmt.setMaximumFractionDigits(1);
		System.out.println(fmt.format(1.2367d));
		System.out.println(fmt.format(1.2364d));
	}
}
