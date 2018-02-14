package com.btx.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Test {

	private static String dayIndex = "";

	private static String weekIndex = "";

	private static Map<Integer,StringBuilder> wordDayText = new HashMap();
	//private static Map<Integer,StringBuilder> weekDayText = new HashMap();
	static {

		wordDayText.put(1, new StringBuilder().append("<html><body>").append("夜深了，收起迷茫眼睛，开始进入睡眠状态。晚安。。。。。").append("</body></html>"));

		wordDayText.put(2, new StringBuilder().append("<html><body>").append("别玩了，已经到了休息时间，记得明天还要奋斗呢！！！晚安。。。").append("</body></html>"));

		wordDayText.put(3, new StringBuilder().append("<html><body>").append("嘟嘟嘟嘟。。。已经夜里十一点了，全国99.999999999999999...%的人都已经睡了，就差你世界就进入休眠状态了，还不睡吗？？？晚安。。。").append("</body></html>"));

		wordDayText.put(4, new StringBuilder().append("<html><body>").append("是在玩王者呢，还是在发呆呢？希望你是在发呆思考人生，重新拾起文艺少女范吧，安安静静做个睡美人。晚安。。。。。。").append("</body></html>"));

		wordDayText.put(5, new StringBuilder().append("<html><body>").append("生活就是这样无聊叠着无聊，但请不要忘了，我们还有梦和远方。晚安。。。。。。。。。").append("</body></html>"));

		wordDayText.put(6, new StringBuilder().append("<html><body>").append("今天可以放松的玩了，因为明天不用上班，哈哈。。。。").append("</body></html>"));

		wordDayText.put(7, new StringBuilder().append("<html><body>").append("一切的原因都归结于明天不用上班，哈哈。。但是要保证身体哦，还是尽量早点晚安。。。").append("</body></html>"));
	}

	public String getText(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		String week = sdf.format(date);
		Map<Integer,StringBuilder> emplate = new HashMap<Integer,StringBuilder>();
		String todayIndex ="";
		if(!"星期六".equals(week)&&!"星期五".equals(week))
		{
			todayIndex = ""+new Random().nextInt(4);
			while(dayIndex.contains(todayIndex))
			{
				todayIndex = ""+new Random().nextInt(5);
			}
			dayIndex+=todayIndex;
			weekIndex="";
			return Test.wordDayText.get(Integer.parseInt(todayIndex)+1).toString();
		}
		else
		{
			if("星期五".equals(week))
			{
				dayIndex="";

			}
			String r = new Random().nextInt(2)+"";
			while(weekIndex.contains(r))
			{
				r = new Random().nextInt(2)+"";
			}
			weekIndex+=r;
			return Test.wordDayText.get(Integer.parseInt(r)+6).toString();

		}


	}
	//	@Scheduled(cron="0 0 23 * * ?")
	//	public void sendMail() throws MessagingException
	//	{
	//		//String sendText = MyRun2.wordDayText.get(i).toString();
	//		String sendText = getText();
	//		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();  
	//		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);  
	//		
	//		mimeMessage.setContent(sendText.toString(), "text/html;charset=gb2312");
	//		mimeMessage.setHeader("X-Mailer", "Microsoft Outlook Express 6.00.2900.2869");
	//		message.setFrom("751634819@qq.com");  
	//		message.setTo("121828445@qq.com");  
	//		message.setSubject("日常晚安提醒");  
	//		message.setText(sendText,true); 
	//		this.mailSender.send(mimeMessage); 
	//		System.out.println("发送了");
	//	}


	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Date> list = new ArrayList();
		list.add(sdf.parse("2018-02-04"));
		list.add(sdf.parse("2018-02-05"));
		list.add(sdf.parse("2018-02-06"));
		list.add(sdf.parse("2018-02-07"));
		list.add(sdf.parse("2018-02-08"));
		list.add(sdf.parse("2018-02-09"));
		list.add(sdf.parse("2018-02-10"));
		list.add(sdf.parse("2018-02-11"));
		list.add(sdf.parse("2018-02-12"));
		for(Date d:list)
		{
			System.out.println(new Test().getText(d)+new SimpleDateFormat("EEEE").format(d));

		}
	}

}
