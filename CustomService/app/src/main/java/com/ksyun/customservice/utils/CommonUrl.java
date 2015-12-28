package com.ksyun.customservice.utils;

public class CommonUrl {

	public static final String url ="";
	public static final String imageUrl ="";
	public static final String csUrl = "http://chat32.live800.com/live800/chatClient/chatbox.jsp?companyID=462131&configID=56871&jid=1949975560";
	public static final String ksyunUrl = "http://www.ksyun.com/";
	public static final String loginUrl = "https://www.ksyun.com/api/account/login";
	public static final String feedbackUrl = "http://192.168.16.63:8089/Service1.asmx?wlds";
	public static final String message_push = "http://weixin.sogou.com/weixinwap?ie=utf8&type=2&t=1449464403149&query=%E9%87%91%E5%B1%B1%E4%BA%91%E9%80%9A%E8%AE%AF%E7%A4%BE&pg=webSearchList";
	public static final String message_weixin = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";
	public static final String feedbackc = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><GZR_customer_Info xmlns=\"http://tempuri.org/\"><UserID>tianxing@sina.cn123131</UserID><UNAME>myname</UNAME><TEL>13845881111</TEL><EMAIL>tianxing@sina.cn</EMAIL><CASENAME>other</CASENAME><WTLX>other</WTLX><CPLX>other</CPLX><ZXLX>other</ZXLX><SJYXJ></SJYXJ><SJMS>asjdaksaksjdkasd</SJMS></GZR_customer_Info></soap:Body>" +
			"</soap:Envelope>";

	public static final String feedback_content = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
			"  <soap:Body>\n" +
			"    <GZR_customer_Info xmlns=\"http://tempuri.org/\">\n" +
			"      <UserID>tianxing@sina.cn123131</UserID>\n" +
			"      <UNAME>myname</UNAME>\n" +
			"      <TEL>13845881111</TEL>\n" +
			"      <EMAIL>tianxing@sina.cn</EMAIL>\n" +
			"      <CASENAME>other</CASENAME>\n" +
			"      <WTLX>other</WTLX>\n" +
			"      <CPLX>other</CPLX>\n" +
			"      <ZXLX>other</ZXLX>\n" +
			"      <SJYXJ>中</SJYXJ>\n" +
			"      <SJMS>asjdaksaksjdkasd</SJMS>\n" +
			"    </GZR_customer_Info>\n" +
			"  </soap:Body>\n" +
			"</soap:Envelope>";

	public static final String getThemeList    = "http://203.195.182.52:8078/tongwan-gosu-service/json/community/getThemeList";//首页主题

	public static final String AppID="wx58178583a250372a";
	public static final String AppSecret="cffe53859be8b36561bbccb04bc77f20";
	public static final String getAccessToken="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx58178583a250372a&secret=cffe53859be8b36561bbccb04bc77f20";
	public static final String getMaterial="https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=";

}
