package com.qd.controller;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.qd.bean.afterChatMsg;
import com.qd.bean.cartMsg;
import com.qd.bean.chatMsg;
import com.qd.bean.collectMsg;
import com.qd.bean.commentMsg;
import com.qd.bean.followMsg;
import com.qd.bean.goodMsg;
import com.qd.bean.rentDetails;
import com.qd.bean.rentMsg;
import com.qd.bean.replyMsg;
import com.qd.bean.userMsg;
import com.qd.service.service;
//import com.qd.util.AesUtil;  这个类是用来解密的工具类，这里既然不再需要解密，那么这个类也就不用了
import com.qd.util.HttpRequest;
import com.zhenzi.sms.ZhenziSmsClient;

@Controller
public class controller {
	
	@Autowired
	private service Service;
	
	/**
	 * 添加用户信息（先根据userId查找有没有 该用户，若没有测添加，若有直接返回true
	 * @param userId
	 * @param phone
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/addUserMsg")
	public @ResponseBody Map<String,Object> addUserMsg(String userId){
		Map<String,Object> map = new HashMap<String, Object>();
		System.out.println("adduser测试");
		System.out.println("userId="+userId);
		boolean res = Service.addUserMsg(userId);
		map.put("success", res);
		return map;
	}
	
	/**
	 * 根据用户账号查找用户
	 * @param userId
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/findUserMsgById")
	public @ResponseBody Map<String,Object> findUserMsgById(String userId){
		System.out.println("findUserMsgById="+userId);
		userMsg user = Service.findUserMsgById(userId);
		Map<String,Object> map = new HashMap<String,Object>();
		System.out.println("user="+user);
		if(user==null)  //之前没有注册过
		{
			map.put("querymsg", false);
		}else{
			if(user.getSex()==0)
				map.put("sex","男");
			else if(user.getSex()==1)
				map.put("sex","女");
			map.put("querymsg", true);
		}
		map.put("usermsg",user);
		return map;
	}
	
	/**
	 * 根据userId修改用户性别
	 * @param userId
	 * @param gender
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/modiyfSexById")
	public @ResponseBody Map<String,Object> modiyfSexById(String userId,String gender){
		Map<String,Object> map = new HashMap<String,Object>();
		int sex;
		//System.out.println("gender="+gender);
		if(gender.equals("男")){
			sex=0;
		}else
			sex=1;
		//System.out.println("sex="+sex);
		boolean res = Service.modiyfSexById(userId, sex);
		map.put("success",res);
		return map;
	}
	
	/**
	 * 根据userId修改用户生日
	 * @param userId
	 * @param birthday
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/modiyfBirthById")
	public @ResponseBody Map<String,Object> modiyfBirthById(String userId,String birthday)
	{
		//System.out.println("userId="+userId);
		//System.out.println("birthday="+birthday);
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modiyfBirthById(userId, birthday);
		map.put("success",res);
		return map;
	}
	
	/**
	 * 修改用户的基本信息（身份证，姓名，学校，微信号）
	 * @param userId
	 * @param idCard
	 * @param school
	 * @param wxNum
	 * @param userName
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/modifyBaseById")
	public @ResponseBody Map<String,Object> modifyBaseById(String userId,String idCard,String school,String wxNum,String userName)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		//System.out.println("userId="+userId);
		System.out.println("idCard="+idCard);
		System.out.println("school="+school);
		System.out.println("wxNum="+wxNum);
		System.out.println("userName="+userName);
		
		boolean res = Service.modifyBaseById(userId, idCard, school, wxNum, userName);
		map.put("success", res);
		return map;
	}
	
	/**
	 * 修改用户姓名
	 * @param userId
	 * @param userName
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/modifyNameById")
	public @ResponseBody Map<String,Object> modifyNameById(String userId,String userName){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyNameById(userId, userName);
		map.put("success",res);
		return map;
	}
	
	/**
	 * 修改身份证
	 * @param userId
	 * @param idCard
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/modifyIdCardById")
	public @ResponseBody Map<String,Object> modifyIdCardById(String userId,String idCard)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyIdCardById(userId, idCard);
		map.put("success",res);
		return map;
	}
	
	/**
	 * 修改微信号
	 * @param userId
	 * @param wxNum
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/modifyWxNumById")
	public @ResponseBody Map<String,Object> modifyWxNumById(String userId,String wxNum)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyWxNumById(userId, wxNum);
		map.put("success",res);
		return map;
	}
	
	/**
	 * 修改学校
	 * @param userId
	 * @param school
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/modifySchoolById")
	public @ResponseBody Map<String,Object> modifySchoolById(String userId,String school)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifySchoolById(userId, school);
		map.put("success",res);
		return map;
	}
	
	
	/**
	 * 获得sessionId
	 */
	@RequestMapping("/getSessionId")
	public @ResponseBody Object getSessionId(HttpServletRequest request) {
		System.out.println("request="+request);
		try {
			HttpSession session = request.getSession();
			System.out.println("session="+session.getId());
			return session.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 发送短信验证码
	 * @param number接收手机号码
	 */
	@RequestMapping("/sendSms")
	public @ResponseBody Object sendSms(HttpServletRequest request, String phoneNum) {
		try {
			System.out.println("phoneNum="+phoneNum);
			
			JSONObject json = null;
			
			//随机生成6位验证码
			String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
			System.out.println("verify="+verifyCode);
			
			//发送短信，初始化
			ZhenziSmsClient client = new ZhenziSmsClient("https://sms_developer.zhenzikj.com","104843", "85899518-73b7-4287-b3ee-b1d0e7d1d6f0");
			
			//封装请求发送短信时的参数
			Map<String,String> params = new HashMap<String,String>();
			params.put("message", "欢迎使用校园租赁商城！您的验证码为:"+ verifyCode+ "，该码有效期为5分钟，该码只能使用一次！");
			params.put("number",phoneNum);
			
			//send方法用于单条发送短信,所有请求参数需要封装到Map中
			//返回结果是json格式的字符串, code: 发送状态，0为成功。非0为发送失败，可从data中查看错误信息
			String result = client.send(params);
			json = JSONObject.parseObject(result);
			
			System.out.println("结果="+json);
			
			if(json.getIntValue("code") != 0)//发送短信失败，想前台返回失败信息
				return "fail";
			
			//若发送短信成功
			//将验证码存到session中,同时存入创建时间
			//以json存放，这里使用的是阿里的fastjson
			HttpSession session = request.getSession();
			
			System.out.println("session="+session);
			
			json = new JSONObject();
			
			json.put("verifyCode", verifyCode);
			json.put("createTime", System.currentTimeMillis());
			
			// 将认证码存入SESSION，用于之后验证用户填写的是否正确
			request.getSession().setAttribute("verifyCode", json);
			return "success";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 提交信息并验证短信验证码
	 */
	@RequestMapping("/Verification")
	public @ResponseBody Object addinfo(HttpServletRequest request, String phone,String code) {
		JSONObject json = (JSONObject)request.getSession().getAttribute("verifyCode");
		if(!json.getString("verifyCode").equals(code)){
			return "验证码错误";
		}
		if((System.currentTimeMillis() - json.getLong("createTime")) > 1000 * 60 * 5){
			return "验证码过期";
		}
		
		return "success";
	}
	
	
	/**
	 * 修改绑定手机号
	 */
	@RequestMapping("/modifyPhoneById")
	public @ResponseBody Object modifyPhoneById(HttpServletRequest request, String phone,String code,String userId) {
		JSONObject json = (JSONObject)request.getSession().getAttribute("verifyCode");
		if(!json.getString("verifyCode").equals(code)){
			return "验证码错误";
		}
		if((System.currentTimeMillis() - json.getLong("createTime")) > 1000 * 60 * 5){
			return "验证码过期";
		}
		
		boolean res = Service.modifyPhoneById(userId, phone);
		
		if(res)
			return "success";
		else
			return "网络错误";
	}
	
	/**
	 * 添加商品信息（除图片外）,并且会查出这个刚刚插入的信息的goodId放入map中返回，为后面存储图片做准备
	 * @param userId
	 * @param goodName
	 * @param introduction
	 * @param goodDetail
	 * @param type
	 * @param unit
	 * @param deposit
	 * @param deadline
	 * @param price
	 * @return
	 */
	@RequestMapping("/addGoodMsg")
	public @ResponseBody Map<String,Object> addGoodMsg(String userId,String goodName,String introduction,String goodDetail,int type,int unit,int deposit,int deadline,double price,int num)
	{
		Map<String,Object> map=new HashMap<String,Object>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String releaseDate = df.format(new Date()); //获取当前时间
		goodMsg good=new goodMsg();
		good.setCollect(0);
		good.setDeadline(deadline);
		good.setDeposit(deposit);
		good.setGoodDetail(goodDetail);
		good.setGoodName(goodName);
		good.setIntroduction(introduction);
		good.setPrice(price);
		good.setReleaseDate(releaseDate);
		good.setType(type);
		good.setUnit(unit);
		good.setUserId(userId);
		good.setNum(num);
		//int goodId = Service.addGoodMsg(userId, goodName, introduction, goodDetail, type, unit, deposit, deadline, price,releaseDate);
		int res = Service.addGoodMsg(good);
		Integer goodId=good.getGoodId();
		if(res>0)
			map.put("msg",true);
		else
			map.put("msg",false);
		map.put("goodId",goodId);
		return map;
	}
	
	/**
	 * 添加商品图片
	 * 由于图片是以content-type为multipart/form-data的格式上传的，所以使用spring-mvc可以通过使用参数的形式以二进制的格式获取到该图片。
	 * 即<strong>@RequestParam(value = "file", required = false) MultipartFile file</strong>
	 * @param goodId
	 * @param imagePath
	 * @return
	 */
	@RequestMapping("/addGoodImage")  
	public @ResponseBody Map<String,Object> addGoodImage(HttpServletRequest request,@RequestParam(value="file",required = false)MultipartFile file)throws IOException {
		Map<String,Object> map=new HashMap<String,Object>();
		System.out.println("执行upload");
	    request.setCharacterEncoding("UTF-8");
	    System.out.println("执行图片上传");
	    
	    int goodId = Integer.parseInt(request.getParameter("goodId"));
	    int index = Integer.parseInt(request.getParameter("index"));  //根据这个去判断这是插入的第几个图片，以此来判断需要用那个添加数据库的函数
	    
	    String path = null;   //图片路径
	    String trueFileName= null;
	    
	    if(!file.isEmpty()) {  //传入的文件不空，则获取到图片    	
	    	System.out.println("成功获取照片");	    	
	    	//获取图片初始名称
            String fileName = file.getOriginalFilename();  
            System.out.println("fileName="+fileName);
            String type = null;
            
            //获取图片类型
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            
            System.out.println("图片初始名称为：" + fileName + " 类型为：" + type);
            
            if (type != null) {
            	//检查是否符合要求的图片类型
            	if ("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())) 
            	{
            		// 项目在容器中实际发布运行的根路径
            		String realPath = request.getSession().getServletContext().getRealPath("/");
            		// 自定义的文件名称
            		trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;
            		int pos = trueFileName.lastIndexOf(".");
            		
            		System.out.println("pos="+pos);
            		String temp = trueFileName.substring(0, pos);
            		System.out.println("substr="+temp);
            		trueFileName = temp+".jpg";
            		
            		// 设置存放图片文件的路径
            		path = realPath + "/uploads/" + trueFileName;
            		
            		System.out.println("trueFileName=="+trueFileName);
            		
            		System.out.println("存放图片文件的路径:" + path);
            		
            		file.transferTo(new File(path));
            		System.out.println("文件成功上传到指定目录下");
            		map.put("upload","文件成功上传到指定目录下");
            		
            	}else {
            		System.out.println("不是我们想要的文件类型,请按要求重新上传");
            		map.put("upload", "error:文件类型不符合,请按要求重新上传");
            		return map;
            	}
            }else {
            	System.out.println("文件类型为空");
            	map.put("upload", "error:文件类型为空");
            	return map;
            }
	    }else {
	    	System.out.println("没有找到相对应的文件");
	    	map.put("upload", "error:没有找到相对应的文件");
	    	return map;
	    }
	    
	    boolean res=false;
	    if(index==0)
	    	res=Service.addGoodImage1(goodId,trueFileName);
	    else if(index==1)
	    	res=Service.addGoodImage2(goodId,trueFileName);
	    else if(index==2)
	    	res=Service.addGoodImage3(goodId,trueFileName);
		map.put("msg",res);
		return map;
	}
	
	/**
	 * 查询用户所有发布商品
	 * @param userId
	 * @return
	 */
	@RequestMapping("/showAllRelease")
	public @ResponseBody Map<String,Object> showAllRelease(String userId){
		//System.out.println("find_userId="+userId);
		Map<String,Object> map=new HashMap<String,Object>();
		List<goodMsg> list = Service.showAllRelease(userId);
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).getImage1()!=null &&  list.get(i).getImage1()!="")
				list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
			if(list.get(i).getImage2()!=null &&  list.get(i).getImage2()!="")
				list.get(i).setImage2("http://www.ZLofCampus.top/image/"+list.get(i).getImage2());
			if(list.get(i).getImage3()!=null &&  list.get(i).getImage3()!="")
				list.get(i).setImage3("http://www.ZLofCampus.top/image/"+list.get(i).getImage3());
		}
		map.put("list", list);
		return map;
	}
	
	/**
	 * 根据goodID删除发布的信息
	 * @param goodId
	 * @return
	 */
	@RequestMapping("/deleteGoodById")
	public @ResponseBody Map<String,Object> deleteGoodById(int goodId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.deleteGoodById(goodId);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 查询租用中的订单（包括出租中和租用中）
	 * @param rentId
	 * @return
	 */
	@RequestMapping("/findRent")
	public @ResponseBody Map<String,Object> findRent(String rentId,int rentState){
		//System.out.println("rentId="+rentId);
		Map<String,Object> map = new HashMap<String,Object>();
		List<rentMsg> list = Service.findRent(rentId, rentState);
		
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
		}
		if(rentState==3)   //如果是查找待归还订单，那么就对每一条信息再处理一下，计算出每件商品还有几天归还时间
		{
			int[][] days={{0,31,28,31,30,31,30,31,31,30,31,30,31},{0,31,29,31,30,31,30,31,31,30,31,30,31}};
			//获取今天的日期
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy");//设置日期格式
			int currentYear = Integer.parseInt(df1.format(new Date())); //获取当前时间
			SimpleDateFormat df2 = new SimpleDateFormat("MM");//设置日期格式
			int currentMonth = Integer.parseInt(df2.format(new Date())); //获取当前时间
			SimpleDateFormat df3 = new SimpleDateFormat("dd");//设置日期格式
			int currentDay = Integer.parseInt(df3.format(new Date())); //获取当前时间
			System.out.println("currentYear="+currentYear+"  currentMonth="+currentMonth+"   currentDay="+currentDay);
			for(int j=0;j<list.size();j++)
			{
				String date = list.get(j).getDueTime();
				int dead = list.get(j).getDeadline();
				int pos=date.indexOf('-');
				int year = Integer.parseInt(date.substring(0,pos));
				date = date.substring(pos+1);
				pos = date.indexOf('-');
				int month = Integer.parseInt(date.substring(0,pos));
				int day = Integer.parseInt(date.substring(pos+1));
				System.out.println("year="+year+"  month="+month+"   day="+day);
				int rest=0;  //剩余归还天数
				int sub=0;  //计算当前日期和过期那天差几天，然后用deadline减去sub就是rest
				if(year==currentYear)
				{
					if(month==currentMonth)
					{
						sub=currentDay-day;
					}else{  //如果月份不相同，所以是跨了月份的,month小
						if((year%4==0 && year%100!=0)|| year%400==0)  //判断是不是闰年
						{
							int tmp=days[1][month]-day;
							sub = tmp+currentDay;
							
						}else{  //不是闰年
							int tmp=days[0][month]-day;
							sub = tmp+currentDay;
						}
					}
				}else{    //不是一年，那么year小，并且month肯定是一月，因为最大的归还期限只有7天
					if((year%4==0 && year%100!=0)|| year%400==0){
						int tmp = days[1][12]-day;
						sub = tmp+currentDay;
					}else{
						int tmp = days[0][12]-day;
						sub = tmp+currentDay;
					}
				}
				System.out.println("sub="+sub);
				if(list.get(j).getDeadline()==0)
					rest = 3-sub;
				else if(list.get(j).getDeadline()==1)
					rest = 5-sub;
				else if(list.get(j).getDeadline()==2)
					rest = 7-sub;
				System.out.println("rest="+rest);
				list.get(j).setRest(rest);
			}
			
			
		}
		map.put("list",list);
		return map;
	}
	
	
	/**
	 * 查询待归还的订单 
	 * @param rentId
	 * @return
	 */
	@RequestMapping("/findWaitReturn")
	public @ResponseBody Map<String,Object> findWaitReturn(String rentId,int rentState){
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<rentMsg> list = Service.findWaitReturn(rentId, rentState);
		
		int[][] days={{0,31,28,31,30,31,30,31,31,30,31,30,31},{0,31,29,31,30,31,30,31,31,30,31,30,31}};
		//获取今天的日期
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy");//设置日期格式
		int currentYear = Integer.parseInt(df1.format(new Date())); //获取当前时间
		SimpleDateFormat df2 = new SimpleDateFormat("MM");//设置日期格式
		int currentMonth = Integer.parseInt(df2.format(new Date())); //获取当前时间
		SimpleDateFormat df3 = new SimpleDateFormat("dd");//设置日期格式
		int currentDay = Integer.parseInt(df3.format(new Date())); //获取当前时间
		System.out.println("currentYear="+currentYear+"  currentMonth="+currentMonth+"   currentDay="+currentDay);
		for(int j=0;j<list.size();j++)
		{
			String date = list.get(j).getDueTime();
			int dead = list.get(j).getDeadline();
			int pos=date.indexOf('-');
			int year = Integer.parseInt(date.substring(0,pos));
			date = date.substring(pos+1);
			pos = date.indexOf('-');
			int month = Integer.parseInt(date.substring(0,pos));
			int day = Integer.parseInt(date.substring(pos+1));
			System.out.println("year="+year+"  month="+month+"   day="+day);
			int rest=0;  //剩余归还天数
			int sub=0;  //计算当前日期和过期那天差几天，然后用deadline减去sub就是rest
			if(year==currentYear)
			{
				if(month==currentMonth)
				{
					sub=currentDay-day;
				}else{  //如果月份不相同，所以是跨了月份的,month小
					if((year%4==0 && year%100!=0)|| year%400==0)  //判断是不是闰年
					{
						int tmp=days[1][month]-day;
						sub = tmp+currentDay;
						
					}else{  //不是闰年
						int tmp=days[0][month]-day;
						sub = tmp+currentDay;
					}
				}
			}else{    //不是一年，那么year小，并且month肯定是一月，因为最大的归还期限只有7天
				if((year%4==0 && year%100!=0)|| year%400==0){
					int tmp = days[1][12]-day;
					sub = tmp+currentDay;
				}else{
					int tmp = days[0][12]-day;
					sub = tmp+currentDay;
				}
			}
			System.out.println("sub="+sub);
			if(list.get(j).getDeadline()==0)
				rest = 3-sub;
			else if(list.get(j).getDeadline()==1)
				rest = 5-sub;
			else if(list.get(j).getDeadline()==2)
				rest = 7-sub;
			System.out.println("rest="+rest);
			list.get(j).setRest(rest);
		}
		
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
		}
		map.put("list",list);
		return map;
	}
	
	/**
	 * 先以当前用户ID为rentID查询订单状态为2并且分为两组，超过租赁时间但在归还期限内的订单，修改状态为3；超过归还期限的订单状态直接改为4
	 * @param rentId
	 * @return
	 */
	@RequestMapping("/modifyRentState")
	@Transactional 
	public @ResponseBody Map<String,Object> modifyRentState(String rentId){
		
		System.out.println("rentId#####="+rentId);
		System.out.println("modifyRentState执行");
		Map<String,Object> map = new HashMap<String,Object>();
		int[][] days={{0,31,28,31,30,31,30,31,31,30,31,30,31},{0,31,29,31,30,31,30,31,31,30,31,30,31}};
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String currentDate = df.format(new Date()); //获取当前时间
		//只能是先查出来这个用户作为rentID的所有状态为2的租赁信息，然后依次去判断这些信息里面，那些是时间超过但还在归还期限里的行
		//把这些行加入到一个新的list1中
		//另外一些时间超过并不在归还期限内的行加入list2中
		//将list1中的行状态修改为3
		//将list2中的行状态修改为4
		List<rentMsg> list1=new LinkedList<>();
		List<rentMsg> list2=new LinkedList<>();
		List<rentMsg> list = Service.find(rentId,2);
		String dueDate=null;
		for(int i=0;i<list.size();i++)
		{
			dueDate = list.get(i).getDueTime();
			if(dueDate==null){
				continue;
			}
			System.out.println("dueDate = "+dueDate);
			//获得到期时间的年份
			int year=0;
			int pos1 = dueDate.indexOf("-");  //找到第一次出现-的位置，那么前面的子串就表示年份
			year = Integer.parseInt(dueDate.substring(0,pos1));  //截取年份并且转换为整型
			int pos3 = dueDate.lastIndexOf("-");  //找到最后一次出现-的位置，后面 就是天
			int day = Integer.parseInt(dueDate.substring(pos3+1));
			int month = Integer.parseInt(dueDate.substring(pos1+1,pos3));
			
			//先根据这个商品的id查询其归还期限是几天
			int tmp = Service.queryReturnTime(list.get(i).getGoodId());
			if(tmp==0)
			{
				tmp=3;
			}else if(tmp==1){
				tmp=5;
			}else if(tmp==2){
				tmp=7;
			}
			if(dueDate.compareTo(currentDate)<0){  //当前日期大于到期日期，小于到期日期的根本都不用管
				//经过下面的过程即可计算到期日期加上归还期限后，是什么日期
				if(year%400==0 || (year%4==0 && year%100!=0 )){  //是闰年，取days的数组下标为1
					if(day+tmp>days[1][month]){  //如果超过月份的天数，那么月份+1
						day = day+tmp-days[1][month];
						month++;
						if(month>12){ //超过一年
							month-=12;
							year++;
						}
					}else{
						day = day+tmp;
					}
				}else{
					if(day+tmp>days[0][month]){  //如果超过月份的天数，那么月份+1
						day = day+tmp-days[0][month];
						month++;
						if(month>12){ //超过一年
							month-=12;
							year++;
						}
					}else{
						day = day+tmp;
					}
				}
				//将新得到日期连接成字符串类型
				String newStr = year+"-";
				if(month<10)
					newStr = newStr+"0"+month+"-";
				else
					newStr = newStr+month+"-";
				if(day<10)
					newStr = newStr+"0"+day;
				else
					newStr = newStr+day;
				System.out.println("newStr="+newStr);
				//将新日期与当天日期相比，如果大于当天日期，则表示还没超时，将list加入list1中;否则表示超时加入list2中
				if(newStr.compareTo(currentDate)>=0){
					list1.add(list.get(i));  //这个里面的之后直接转换到状态3 ，表示归还中
				}else  //这个里面的之后直接转换到4，表示交易完成
					list2.add(list.get(i));
			}
		}
		System.out.println("list1="+list1.size());
		System.out.println("list2="+list2.size());
		boolean res1=false;
		boolean res2=false;
		//将list1中的租赁信息状态均从2改为3，表示待归还
		for(int i=0;i<list1.size();i++)
		{
			res1=Service.modifyRentState2To3(list1.get(i).getId());
		}
		//将list2中的租赁信息状态均从2改到5表示交易完成押金扣留未评价状态，还要将goodmsg中的商品个数恢复
		for(int i=0;i<list2.size();i++)
		{
			System.out.println("!!!!!!!!!");
			//改状态
			res2=Service.modifyRentState2To5(list2.get(i).getId());
			//恢复个数
			res1=Service.addNumAfterTrade(list2.get(i).getGoodId(), list2.get(i).getRentNum());
		}
		map.put("list1",list1);
		map.put("list2",list2);
		return map;
	}
	
	
	
	/**
	 * 先以当前用户ID为rentID查询订单状态为2并且超过租赁时间的订单，修改状态为3
	 * @param rentId
	 * @return
	 */
	@RequestMapping("/modifyRentState2")
	@Transactional
	public @ResponseBody Map<String,Object> modifyRentState2(String rentId){
		//System.out.println("rentId="+rentId);
		System.out.println("modifyRentState2执行");
		Map<String,Object> map = new HashMap<String,Object>();
		int[][] days={{0,31,28,31,30,31,30,31,31,30,31,30,31},{0,31,29,31,30,31,30,31,31,30,31,30,31}};
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String currentDate = df.format(new Date()); //获取当前时间
		//查出状态为3的作为rentID的租赁信息
		List<rentMsg> list1=new LinkedList<>();
		List<rentMsg> list = Service.findRent(rentId,3);  
		String dueDate=null;
		for(int i=0;i<list.size();i++)
		{ 
			//获得每一条信息的到期时间
			dueDate = list.get(i).getDueTime();
			System.out.println("dueDate = "+dueDate);
			if(dueDate==null)
				continue;
			//获得到期时间的年份
			int year=0;
			int pos1 = dueDate.indexOf("-");  //找到第一次出现-的位置，那么前面的子串就表示年份
			year = Integer.parseInt(dueDate.substring(0,pos1));  //截取年份并且转换为整型
			int pos3 = dueDate.lastIndexOf("-");  //找到最后一次出现-的位置，后面 就是天
			int day = Integer.parseInt(dueDate.substring(pos3+1));
			int month = Integer.parseInt(dueDate.substring(pos1+1,pos3));
			
			//先根据这个商品的id查询其归还期限是几天
			int tmp = Service.queryReturnTime(list.get(i).getGoodId());
			if(tmp==0)
			{
				tmp=3;
			}else if(tmp==1){
				tmp=5;
			}else if(tmp==2){
				tmp=7;
			}
			//经过下面的过程即可计算到期日期加上归还期限后，是什么日期
			if(year%400==0 || (year%4==0 && year%100!=0 )){  //是闰年，取days的数组下标为1
				if(day+tmp>days[1][month]){  //如果超过月份的天数，那么月份+1
					day = day+tmp-days[1][month];
					month++;
					if(month>12){ //超过一年
						month-=12;
						year++;
					}
				}else{
					day = day+tmp;
				}
			}else{
				if(day+tmp>days[0][month]){  //如果超过月份的天数，那么月份+1
					day = day+tmp-days[0][month];
					month++;
					if(month>12){ //超过一年
						month-=12;
						year++;
					}
				}else{
					day = day+tmp;
				}
			}
			//将新得到日期连接成字符串类型
			String newStr = year+"-";
			if(month<10)
				newStr = newStr+"0"+month+"-";
			else
				newStr = newStr+month+"-";
			if(day<10)
				newStr = newStr+"0"+day;
			else
				newStr = newStr+day;
			System.out.println("newStr="+newStr);
			//将新日期与当天日期相比，如果小于当天日期那么就说明已经超过归还期限
			if(newStr.compareTo(currentDate)<0){
				list1.add(list.get(i));  
			}
		}
		System.out.println("list1="+list1.size());
		boolean res1=false;
		//将list1中的租赁信息从3改为5表示交易完成押金扣留未评论
		for(int i=0;i<list1.size();i++)
		{
			//改状态
			res1=Service.modifyRentState3To5(list1.get(i).getId());
			//恢复个数
			res1=Service.addNumAfterTrade(list1.get(i).getGoodId(), list1.get(i).getRentNum());
		}
		map.put("list1",list1);
		return map;
	}
	
	
	
	
	/**
	 * 以当前用户id为发货者id，查找状态为0的租赁信息(找出待发货信息)
	 * @param releaseId
	 * @param rentState
	 * @return
	 */
	@RequestMapping("/findWaitSend")
	public @ResponseBody Map<String,Object> findWaitSend(String releaseId,int rentState){
		//System.out.println("releaseId="+releaseId);
		//System.out.println("rentState="+rentState);
		Map<String,Object> map = new HashMap<String,Object>();
		List<rentMsg> list = Service.findWaitSend(releaseId, rentState);
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
		}
		map.put("list",list);
		return map;
	}
	
	/**
	 * 以当前用户id作为releaseId查询状态为7或3的订单（7表示租户已经归还，等物主收回；3表示租户正在归还中）
	 * @param releaseId
	 * @return
	 */
	@RequestMapping("/findWaitSend2")
	public @ResponseBody Map<String,Object> findWaitSend2(String releaseId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<rentMsg> list = Service.findWaitSend2(releaseId);
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
		}
		map.put("list",list);
		return map;
	}
	
	
	/**
	 * 以当前用户id为收货者id，查找状态为0或者1的租赁信息(待收货订单)
	 * @param rentId
	 * @return
	 */
	@RequestMapping("/findWaitReceive")
	public @ResponseBody Map<String,Object> findWaitReceive(String rentId){
		Map<String,Object> map = new HashMap<String,Object>();
		List<rentMsg> list = Service.findWaitReceive(rentId);
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
		}
		map.put("list",list);
		return map;
	}
	
	
	/**
	 * 查找用户所有的已完成订单（包括作为租用者的所有订单
	 * @param userId
	 * @return
	 */
	@RequestMapping("/findFinish")
	public @ResponseBody Map<String,Object> findFinish(String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		//分成两部分查询
		//先查作为出租者的所有完成订单（已评论和未评论）
		List<rentMsg> list1 = Service.findFinishByRelease(userId);
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list1.size();i++)
		{
			list1.get(i).setImage1("http://www.ZLofCampus.top/image/"+list1.get(i).getImage1());
		}
		//再查作为租用者的所有订单（包括已评论和未评论，未评论的可以在页面上评论）
		List<rentMsg> list2 = Service.findFinishByRent(userId);
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list2.size();i++)
		{
			list2.get(i).setImage1("http://www.ZLofCampus.top/image/"+list2.get(i).getImage1());
		}
		map.put("list1",list1);
		map.put("list2",list2);
		return map;
	}
	
	/**
	 * 查询用户的收藏信息
	 * @param collectUserId
	 * @return
	 */
	@RequestMapping("/collectQuery")
	public @ResponseBody Map<String,Object> collectQuery(String collectUserId){
		Map<String,Object> map = new HashMap<String,Object>();
		List<collectMsg> list = Service.collectQuery(collectUserId);
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
		}
		map.put("list",list);
		return map;
	}
	
	
	/**
	 * 根据id删除收藏的商品
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteCollect")
	public @ResponseBody Map<String,Object> deleteCollect(int id){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.deleteCollect(id);
		map.put("msg",res);
		return map;
	}
	
	/**
	 * 查询订单详情
	 * @param Id
	 * @return
	 */
	@RequestMapping("/QueryRentDetail")
	public @ResponseBody Map<String,Object> QueryRentDetail(int Id){
		Map<String,Object> map = new HashMap<String,Object>();
		rentDetails detail = Service.QueryRentDetail(Id);
		detail.setImage1("http://www.ZLofCampus.top/image/"+detail.getImage1());
		map.put("msg",detail);
		return map;
	}
	
	/**
	 * 点击收货，将待收货状态1，改为2租用中
	 * @param Id
	 * @return
	 */
	@RequestMapping("/modifyRentState1To2")
	public @ResponseBody Map<String,Object> modifyRentState1To2(int Id){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyRentState1To2(Id);
		map.put("msg",res);
		return map;
	}
	
	/**
	 * 点击收回，将待收回状态7，改为4交易完成
	 * @param Id
	 * @return
	 */
	@RequestMapping("/modifyRentState7To4")
	public @ResponseBody Map<String,Object> modifyRentState7To4(int Id){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyRentState7To4(Id);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 点击归还，将待归还状态3，改为7待收回
	 * @param Id
	 * @return
	 */
	@RequestMapping("/modifyRentState3To7")
	public @ResponseBody Map<String,Object> modifyRentState3To7(int Id){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyRentState3To7(Id);
		map.put("msg",res);
		return map;
	}
	
	/**
	 * 点击发货，将待发货状态0，改为1待收货
	 * @param Id
	 * @return
	 */
	@RequestMapping("/modifyRentState0To1")
	public @ResponseBody Map<String,Object> modifyRentState0To1(int Id){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyRentState0To1(Id);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 评论完成后,将状态由4改为6（押金未扣留的情况）
	 * @param Id
	 * @return
	 */
	@RequestMapping("/modifyRentState4To6")
	public @ResponseBody Map<String,Object> modifyRentState4To6(int Id){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyRentState4To6(Id);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 评论完成后,将状态由5改为8（押金已扣留的情况）
	 * @param Id
	 * @return
	 */
	@RequestMapping("/modifyRentState5To8")
	public @ResponseBody Map<String,Object> modifyRentState5To8(int Id){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyRentState5To8(Id);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 获取用户积分
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getUserIntegration")
	public @ResponseBody Map<String,Object> getUserIntegration(String userId){
		System.out.println("获得积分");
		System.out.println("userId="+userId);
		Map<String,Object> map = new HashMap<String,Object>();
		int UserIntegration=Service.getUserIntegration(userId);
		System.out.println("积分="+UserIntegration);
		map.put("userIntegration", UserIntegration);
		return map;
	}
	
	
	/**
	 * 根据租赁信息的id获取发布该商品信息的用户电话号码
	 * @param Id
	 * @return
	 */
	@RequestMapping("/getReleasePhone")
	public @ResponseBody Map<String,Object> getReleasePhone(int Id){
		Map<String,Object> map = new HashMap<String,Object>();
		String res = Service.getReleasePhone(Id);
		if(res=="" || res==null)
			map.put("msg",false);
		else
			map.put("msg",true);
		map.put("releasePhone", res);
		return map;
	}
	
	/**
	 * 根据租赁信息的id获取租用该商品的用户电话号码
	 * @param Id
	 * @return
	 */
	@RequestMapping("/getRentPhone")
	public @ResponseBody Map<String,Object> getRentPhone(int Id){
		Map<String,Object> map = new HashMap<String,Object>();
		String res = Service.getRentPhone(Id);
		if(res=="" || res==null)
			map.put("msg",false);
		else
			map.put("msg",true);
		map.put("rentPhone", res);
		return map;
	}
	

	/**
	 * 查看用户是否已完善信息，填写了手机号码
	 */
	@RequestMapping("/checkPhone")
	public @ResponseBody Map<String,Object> checkPhone(String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		String res = Service.checkPhone(userId);
		System.out.println("phone="+res);
		if(res=="" || res==null)
			map.put("check",false);
		else
			map.put("check",true);
		return map;
	}
	
	
	/**
	 * 根据商品浏览次数找到浏览次数最多，且还能租用的商品信息
	 * @return
	 */
	@RequestMapping("/getHotGood")
	public @ResponseBody Map<String,Object> getHotGood(){
		//System.out.println("getHot");
		Map<String,Object> map = new HashMap<String,Object>();
		List<goodMsg> list = Service.getHotGood();
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).getImage1()!=null &&  list.get(i).getImage1()!="")
				list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
			if(list.get(i).getImage2()!=null &&  list.get(i).getImage2()!="")
				list.get(i).setImage2("http://www.ZLofCampus.top/image/"+list.get(i).getImage2());
			if(list.get(i).getImage3()!=null &&  list.get(i).getImage3()!="")
				list.get(i).setImage3("http://www.ZLofCampus.top/image/"+list.get(i).getImage3());
		}
		System.out.println("list="+list.size());
		map.put("hotList",list);
		return map;
	}
	
	
	/**
	 * 获取最近发布的三条商品信息
	 * @return
	 */
	@RequestMapping("/getNewGood")
	public @ResponseBody Map<String,Object> getNewGood(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<goodMsg> list = Service.getNewGood();
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).getImage1()!=null &&  list.get(i).getImage1()!="")
				list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
			if(list.get(i).getImage2()!=null &&  list.get(i).getImage2()!="")
				list.get(i).setImage2("http://www.ZLofCampus.top/image/"+list.get(i).getImage2());
			if(list.get(i).getImage3()!=null &&  list.get(i).getImage3()!="")
				list.get(i).setImage3("http://www.ZLofCampus.top/image/"+list.get(i).getImage3());
		}
		System.out.println("newlist="+list.size());
		map.put("newList",list);
		return map;
	}
	
	
	/**
	 * 获取浏览最多的日用品或其他类商品信息
	 * @return
	 */
	@RequestMapping("/getHotGroceries")
	public @ResponseBody Map<String,Object> getHotGroceries(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<goodMsg> list = Service.getHotGroceries();
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).getImage1()!=null &&  list.get(i).getImage1()!="")
				list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
			if(list.get(i).getImage2()!=null &&  list.get(i).getImage2()!="")
				list.get(i).setImage2("http://www.ZLofCampus.top/image/"+list.get(i).getImage2());
			if(list.get(i).getImage3()!=null &&  list.get(i).getImage3()!="")
				list.get(i).setImage3("http://www.ZLofCampus.top/image/"+list.get(i).getImage3());
		}
		System.out.println("list="+list.size());
		map.put("groceriesList",list);
		return map;
	}
	
	/**
	 * 获取浏览最多的三条图书/电子产品/出行工具信息
	 * @param type
	 * @return
	 */
	@RequestMapping("/getHot")
	public @ResponseBody Map<String,Object> getHot(int type){
		Map<String,Object> map = new HashMap<String,Object>();
		List<goodMsg> list = Service.getHot(type);
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).getImage1()!=null &&  list.get(i).getImage1()!="")
				list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
			if(list.get(i).getImage2()!=null &&  list.get(i).getImage2()!="")
				list.get(i).setImage2("http://www.ZLofCampus.top/image/"+list.get(i).getImage2());
			if(list.get(i).getImage3()!=null &&  list.get(i).getImage3()!="")
				list.get(i).setImage3("http://www.ZLofCampus.top/image/"+list.get(i).getImage3());
		}
		System.out.println("list="+list.size());
		map.put("list",list);
		return map;
	}
	
	
	/**
	 * 根据商品goodId获取商品详细信息
	 * @param goodId
	 * @return
	 */
	@RequestMapping("/getGoodDetail")
	public @ResponseBody Map<String,Object> getGoodDetail(int goodId){
		Map<String,Object> map = new HashMap<String,Object>();
		goodMsg list = Service.getGoodDetail(goodId);
		if(list.getImage1()!=null &&  list.getImage1()!="")
			list.setImage1("http://www.ZLofCampus.top/image/"+list.getImage1());
		if(list.getImage2()!=null &&  list.getImage2()!="")
			list.setImage2("http://www.ZLofCampus.top/image/"+list.getImage2());
		if(list.getImage3()!=null &&  list.getImage3()!="")
			list.setImage3("http://www.ZLofCampus.top/image/"+list.getImage3());
		map.put("detailList",list);
		return map;
	}
	
	
	/**
	 * 收藏商品进入收藏表
	 * @param collectUserId
	 * @param collectGoodId
	 * @return
	 */
	@RequestMapping("/collectGood")
	public @ResponseBody Map<String,Object> collectGood(String collectUserId,int collectGoodId){
		Map<String,Object> map = new HashMap<String,Object>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String collectDate = df.format(new Date()); //获取当前时间
		boolean res = Service.collectGood(collectUserId, collectGoodId,collectDate);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 商品收藏数加1
	 * @param goodId
	 * @return
	 */
	@RequestMapping("/addCollect")
	public @ResponseBody Map<String,Object> addCollect(int goodId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.addCollect(goodId);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 取消收藏(根据用户id和商品id)
	 * @param collectUserId
	 * @param collectGoodId
	 * @return
	 */
	@RequestMapping("/cancelcollect")
	public @ResponseBody Map<String,Object> cancelcollect(String collectUserId,int collectGoodId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.cancelcollect(collectUserId, collectGoodId);
		map.put("msg",res);
		return map;
	}
	
	/**
	 * 商品收藏数减1
	 * @param goodId
	 * @return
	 */
	@RequestMapping("/subCollect")
	public @ResponseBody Map<String,Object> subCollect(int goodId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.subCollect(goodId);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 根据商品id和用户id查看该用户是否收藏该商品
	 * @param collectUserId
	 * @param collectGoodId
	 * @return
	 */
	@RequestMapping("/findCollect")
	public @ResponseBody Map<String,Object> findCollect(String collectUserId,int collectGoodId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.findCollect(collectUserId, collectGoodId);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 根据商品id获取评论
	 * @param goodId
	 * @return
	 */
	@RequestMapping("/findComment")
	public @ResponseBody Map<String,Object> findComment(int commentGoodId){
		Map<String,Object> map = new HashMap<String,Object>();
		List<commentMsg> list = Service.findComment(commentGoodId);
		/*
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).getUserName()==null || list.get(i).getUserName()=="" || list.get(i).getAnonymous()==1){
				list.get(i).setUserName("匿名用户");
			}
		}
		*/
		map.put("commentList", list);
		return map;
	}
	
	
	/**
	 * 添加购物车
	 * @param cartUserId
	 * @param cartGoodId
	 * @param cartNum
	 * @param timeLen
	 * @return
	 */
	@RequestMapping("/addCart")
	public @ResponseBody Map<String,Object> addCart(String cartUserId,int cartGoodId,int cartNum,int timeLen){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.addCart(cartUserId, cartGoodId, cartNum, timeLen);
		map.put("msg", res);
		return map;
	}
	
	
	/**
	 * 根据用户id获取用户的购物车内容
	 * @param cartUserId
	 * @return
	 */
	@RequestMapping("/fingCartByUser")
	public @ResponseBody Map<String,Object> fingCartByUser(String cartUserId){
		Map<String,Object> map = new HashMap<String,Object>();
		List<cartMsg> list = Service.fingCartByUser(cartUserId);
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
		}
		map.put("cartList", list);
		return map;	
	}
			
	
	/**
	 * 在购物车中修改cartNum
	 * @param cartId
	 * @return
	 */
	@RequestMapping("/modifyCartNumAdd")
	public @ResponseBody Map<String,Object> modifyCartNumAdd(int cartId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyCartNumAdd(cartId);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 在购物车中修改cartNum(减)
	 * @param cartId
	 * @return
	 */
	@RequestMapping("/modifyCartNumSub")
	public @ResponseBody Map<String,Object> modifyCartNumSub(int cartId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyCartNumSub(cartId);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 在购物车中修改timeLen
	 * @param cartId
	 * @return
	 */
	@RequestMapping("/modifyCartTimeAdd")
	public @ResponseBody Map<String,Object> modifyCartTimeAdd(int cartId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyCartTimeAdd(cartId);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 在购物车中修改timeLen(减)
	 * @param cartId
	 * @return
	 */
	@RequestMapping("/modifyCartTimeSub")
	public @ResponseBody Map<String,Object> modifyCartTimeSub(int cartId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyCartTimeSub(cartId);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 根据购物车信息的id号删除
	 * @param cartId
	 * @return
	 */
	@RequestMapping("/deleteCartById")
	public @ResponseBody Map<String,Object> deleteCartById(int cartId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.deleteCartById(cartId);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 用户从购物车下单后修改商品表
	 * @param goodId
	 * @param cartNum
	 * @return
	 */
	@RequestMapping("/modifyGoodMsg")
	public @ResponseBody Map<String,Object> modifyGoodMsg(int goodId,int cartNum){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyGoodMsg(goodId, cartNum);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 添加评论
	 * @param commentGoodId
	 * @param content
	 * @param degree
	 * @param commentUserId
	 * @param anonymous
	 * @return
	 */
	@RequestMapping("/addComment")
	public @ResponseBody Map<String,Object> addComment(int commentGoodId,String content,int degree,String commentUserId,int anonymous){
		Map<String,Object> map = new HashMap<String,Object>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String date = df.format(new Date()); //获取当前时间
		boolean res = Service.addComment(commentGoodId, content, degree, commentUserId, anonymous, date);
		map.put("msg",res);
		return map;
	}
	
	
	//根据评价的分数，对发布者的积分进行修改（1颗星不加分，2-3颗星加10分，4颗星加20分，5颗星加30分）
	@RequestMapping("/modifyReleaseScore")
	public @ResponseBody Map<String,Object> modifyReleaseScore(String userId,int degree){
		Map<String,Object> map = new HashMap<String,Object>();
		int add=0;
		if(degree==2 || degree==3){
			add=10;
		}else if(degree==4){
			add=20;
		}else if(degree==5){
			add=30;
		}
		boolean res = Service.modifyReleaseScore(userId,add);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 根据商品id将交易完成的商品数量恢复原状
	 * @param goodId
	 * @param rentNum
	 * @return
	 */
	@RequestMapping("/addNumAfterTrade")
	public @ResponseBody Map<String,Object> addNumAfterTrade(int goodId,int rentNum){
		Map<String,Object> map = new HashMap<String,Object>();
		System.out.println("收回之后增加数量  goodID="+goodId+"   rentNum="+rentNum);
		boolean res = Service.addNumAfterTrade(goodId, rentNum);
		map.put("msg",res);
		return map;
	}
	
	
	
	
	/**
	 * 根据租赁id修改租赁表中的rentDate和dueTime
	 * @param Id
	 * @param timeLen
	 * @param unit
	 * @return
	 */
	@RequestMapping("/modifyRentTime")
	public @ResponseBody Map<String,Object> modifyRentTime(int Id,int timeLen,int unit){
		
		int[][] days={{0,31,28,31,30,31,30,31,31,30,31,30,31},{0,31,29,31,30,31,30,31,31,30,31,30,31}};
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String rentDate = df.format(new Date()); //获取当前时间(作为付款时间)
		
		//获取当前年份
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
		int year = Integer.parseInt(df1.format(new Date())); 
				
		//获取当前月份
		SimpleDateFormat df2 = new SimpleDateFormat("MM");
		int month = Integer.parseInt(df2.format(new Date())); 
				
		//获取当前天
		SimpleDateFormat df3 = new SimpleDateFormat("dd");
		int day = Integer.parseInt(df3.format(new Date())); 
				
		//根据租用的时长和单位计算到期时间
		if(unit==0){  //按天计算
					
			if((year%100!=0 && year%4==0)|| year%400==0)//如果是闰年
			{
				if(day+timeLen>days[1][month]){ //到了下一个月
					day = day+timeLen-days[1][month];
					month+=1;
					if(month>12){ //超过了一年
						month=month-12;
						year+=1;
					}
				}else{ //还在这个月
					day = day+timeLen;
				}
			}else{ //不是闰年
				if(day+timeLen>days[0][month]){ //到了下一个月
					day = day+timeLen-days[0][month];
					month+=1;
					if(month>12){ //超过了一年
						month=month-12;
						year+=1;
					}
				}else{ //还在这个月
					day = day+timeLen;
				}
			}
					
		}else{ //按月计算
			if(month+timeLen>12){ //到了下一年
				year+=1;
				month = month+timeLen-12;
			}else{
				month+=timeLen;
			}
		}
				
		String dueTime=null;
		if(month<10)
			dueTime=year+"-0"+month;
		else
			dueTime=year+"-"+month;
		if(day<10)
			dueTime=dueTime+"-0"+day;
		else
			dueTime=dueTime+"-"+day;
					
		System.out.println("dueTime="+dueTime);
		
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyRentTime(Id, rentDate, dueTime);
		map.put("msg",res);
		return map;
		
	}
	
	
	/**
	 * 将商品插入到租赁表中,并设置状态为0
	 * @param goodId
	 * @param releaseId
	 * @param rentId
	 * @param unit
	 * @param timeLen
	 * @param rentNum
	 * @return
	 */
	@RequestMapping("/addRentMsg")
	public @ResponseBody Map<String,Object> addRentMsg(int goodId,String releaseId,String rentId,int unit,int timeLen,int rentNum,String remarks){
	
		System.out.println("添加租赁信息");
		System.out.println("remarks="+remarks);
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String payDate = df.format(new Date()); //获取当前时间(作为付款时间)
		//这里由于在mapper里面写数据库语句时，如果rentDate和dueTime没有值的话，传null和''都是没有办法通过编译的（虽然这种写法在MySQL里面可以），所以这里还是传入一个默认的0000-00-00作为值
		boolean res = Service.addRentMsg(goodId, releaseId, rentId, payDate, rentNum,timeLen,remarks);
		map.put("msg",res);
		return map;
	}
	
	
	
	/**
	 * 根据输入的key在goodmsg中模糊查询，（对比名字，简介，详情）
	 * @param key
	 * @return
	 */
	@RequestMapping("/searchByKey")
	public @ResponseBody Map<String,Object> searchByKey(String key){
		System.out.println("key="+key);
		int find1 = key.indexOf("书籍");
		int find2 = key.indexOf("书");
		int find3 = key.indexOf("服");
		int find4 = key.indexOf("衣"); 
		int find5 = key.indexOf("鞋");
		int find11 = key.indexOf("裙");
		int find6 = key.indexOf("行");
		int find7 = key.indexOf("车");
		int find8 = key.indexOf("工");
		int find9 = key.indexOf("电");
		int find10 = key.indexOf("智");
		int find12 = key.indexOf("卡");
		int find13 = key.indexOf("票");
		int type=-1;
		if(find1!=-1 || find2!=-1)  //书籍
		{
			type = 1;
		}else if(find3!=-1 || find4!=-1 || find5!=-1 || find11!=-1)  //衣物
		{
			type = 0;
		}else if(find6!=-1 || find7!=-1 || find8!=-1){   //出行
			type = 2;
		}else if(find10!=-1 || find9!=-1){  //电子
			type = 3;
		}else if(find12!=-1 || find13!=-1){  //杂货
			type = 4;
		}
		System.out.println("type="+type);
		Map<String,Object> map = new HashMap<String,Object>();
		List<goodMsg> list = Service.searchByKey(key,type);
		//将图片的路径拼接好，这样在前端就无需再做处理
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).getImage1()!=null &&  list.get(i).getImage1()!="")
				list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
			if(list.get(i).getImage2()!=null &&  list.get(i).getImage2()!="")
				list.get(i).setImage2("http://www.ZLofCampus.top/image/"+list.get(i).getImage2());
			if(list.get(i).getImage3()!=null &&  list.get(i).getImage3()!="")
				list.get(i).setImage3("http://www.ZLofCampus.top/image/"+list.get(i).getImage3());
		}
		map.put("searchList",list);
		return map;
	}
	
	
	
	/**
	 * 根据用户id扣除用户积分
	 * @param userId
	 * @return
	 */
	@RequestMapping("/subScoreById")
	public @ResponseBody Map<String,Object> subScoreById(String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.subScoreById(userId);
		int score=-1;
		if(res==true){
			//在重新查出来积分
			score = Service.getUserIntegration(userId);
		}
		map.put("msg",res);
		map.put("score",score);
		return map;
	}
	
	
	
	/**
	 * 根据用户id设置用户idstate为1，表示被封，默认为0
	 * @param userId
	 * @return
	 */
	@RequestMapping("/updateIdState")
	public @ResponseBody Map<String,Object> updateIdState(String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.updateIdState(userId);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 根据评论id删除评论
	 * @param commentId
	 * @return
	 */
	@RequestMapping("/deleteCommentById")
	public @ResponseBody Map<String,Object> deleteCommentById(int commentId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.deleteCommentById(commentId);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 根据商品ID将该商品的views加1
	 * @param goodId
	 * @return
	 */
	@RequestMapping("/addViews")
	public @ResponseBody Map<String,Object> addViews(int goodId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.addViews(goodId);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 根据用户userId查出来用户当前有没有完善姓名
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getUserName")
	public @ResponseBody Map<String,Object> getUserName(String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		String userName = Service.getUserName(userId);
		//System.out.println("userName="+userName);
		if(userName.length()<=0)
			userName=null;
		System.out.println("userName="+userName);
		map.put("userName",userName);
		return map;
	}
	
	
	
	/**
	 * 根据用户的userId添加用户的微信昵称和头像进入usermsg
	 * @param userId
	 * @param avatarUrl
	 * @param nickName
	 * @return
	 */
	@RequestMapping("/addNickAndUrl")
	public @ResponseBody Map<String,Object> addNickAndUrl(String userId,String avatarUrl,String nickName){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.addNickAndUrl(userId, avatarUrl, nickName);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 根据商品的goodID找到发布者的电话
	 * @param goodId
	 * @return
	 */
	@RequestMapping("/findPhoneByGoodId")
	public @ResponseBody Map<String,Object> findPhoneByGoodId(int goodId){
		Map<String,Object> map = new HashMap<String,Object>();
		String phone = Service.findPhoneByGoodId(goodId);
		map.put("phone",phone);
		return map;
	}
	
	
	
	/**
	 * 先查找登录者（根据userId）的所有未读评论（通过评论表与商品表联查，查出这个商品发布者的id，看与登录者是否相同，如果相同那就看看是否已读，将未读的都找出来
	 * 其实未读评论的查找中应该也包括评论本身已读但是存在未读回复的信息
	 * 基于上述，此处修改为，查找所有订单，再对每一条订单查找未读回复，如果有未读回复，那么则保留该条信息并记录未读回复数；如果没有未读回复且该评论本身为已读，那么就删除这条信息（在controller层完成）
	 * @param userId
	 * @return
	 */
	@RequestMapping("/findNoReadComment")
	public @ResponseBody Map<String,Object> findNoReadComment(String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		//查找评论本身未读及已读的信息
		List<commentMsg> list = Service.findNoReadComment(userId);
		System.out.println("noReadlist1="+list.size());
		//对每一条信息进行查找未读回复
		for(int i=0;i<list.size();)
		{
			//对每一条评论查找存在的未读回复
			List<replyMsg> replyList = Service.findNoReadReply(list.get(i).getCommentId(),userId);
			System.out.println("replyList="+replyList.size());
			list.get(i).setReplyNoRead(replyList.size());  //设置未读的回复数
			if(replyList.size()>0){
				//查出来最后一条发送给userId的未读回复，先找到这条回复的Id，再找到这条回复的信息
				int id = Service.findreplyId(userId);
				replyMsg last = Service.findLastReply(id);
				
				//找发出者的信息
				userMsg user = Service.findUserMsgById(last.getReplyId());
				//设置发布者的头像，昵称和姓名进入list(i)中
				last.setAvatarUrl_from(user.getAvatarUrl());
				last.setNickName_from(user.getNickName());
				last.setUserName_from(user.getUserName());
				last.setUserId_from(user.getUserId());
				//找回复对象的信息
				userMsg user2 = Service.findUserMsgById(last.getReplyToId());
				//设置发布对象的昵称和姓名
				last.setNickName_to(user2.getNickName());
				last.setUserName_to(user2.getUserName());
				last.setUserId_to(user2.getUserId());
				
				//将最后一条回复的信息添加入这个评论
				list.get(i).setLastReplyContent(last);
			}
			
			if(replyList.size()<=0 && list.get(i).getIsRead()==1)  //评论本身已读，且没有未读回复，那么删除这条信息
			{
				list.remove(i);
				continue;
			}
			i++;
		}
		System.out.println("noReadlist2="+list.size());
		map.put("list",list);
		return map;
	}
	
	
	
	/**
	 * 根据头像查找到对应的用户（因为微信在进行头像存储时，每个头像的地址一定是唯一的，所以可以找到唯一的用户）
	 * 找到这个用户后，返回这个用户的所有个人信息，和此用户发布的所有商品信息，再根据这些商品信息查找出所有评论，统计五星评论数和总评论数，得出好评率
	 * @param avat
	 * @return
	 */
	@RequestMapping("/findHomeMsg")
	public @ResponseBody Map<String,Object> findHomeMsg(String avat){
		Map<String,Object> map = new HashMap<String,Object>();
		userMsg user = Service.findHomeMsg(avat);
		//根据用户id查找用户的所有发布商品
		List<goodMsg> list = Service.showAllRelease(user.getUserId());
		//再根据每条商品的id查找对应评论，统计总评论数，和好评数
		double sum_total=0;
		double sum_good=0;
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).getImage1()!=null)
				list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
			if(list.get(i).getImage2()!=null)
				list.get(i).setImage2("http://www.ZLofCampus.top/image/"+list.get(i).getImage2());
			if(list.get(i).getImage3()!=null)
				list.get(i).setImage3("http://www.ZLofCampus.top/image/"+list.get(i).getImage3());
			sum_total+=Service.totalComment(list.get(i).getGoodId());
			sum_good+=Service.goodComment(list.get(i).getGoodId());
		}
		double rate = 0;
		DecimalFormat df = new DecimalFormat("0.00");
		if(sum_total!=0)
			rate = sum_good/sum_total;
		System.out.println("rate2="+df.format(rate*100));
		map.put("user",user);
		map.put("goods",list);
		map.put("rate",df.format(rate*100));
		return map;
	}
	
	
	/**
	 * 根据评论commentID修改已读
	 * @param commentId
	 * @return
	 */
	@RequestMapping("/modifyCommentRead")
	public @ResponseBody Map<String,Object> modifyCommentRead(int commentId,String userId){
		System.out.println("commentId="+commentId);
		Map<String,Object> map = new HashMap<String,Object>();
		//现将本条评论修改为已读
		boolean res = Service.modifyCommentRead(commentId);
		//查找一下这条评论下有没有未读回复，有则将回复也变为已读，没有就什么也不做
		List<replyMsg> replyList = Service.findNoReadReply(commentId,userId);
		int flag=0;
		if(replyList.size()>0){ //有未读回复
			//将未读回复都改为已读
			for(int i=0;i<replyList.size();i++)
			{
				boolean tmp = Service.modifyReplyRead(replyList.get(i).getId());
				if(tmp==false)
					flag=1;
			}
			map.put("noRead",replyList.size());
		}else{
			map.put("noRead",0);
		}
		map.put("msg1",res);
		if(replyList.size()>0){
			if(flag==0)
				map.put("msg2",true);
			else
				map.put("msg2",false);
		}
		else{
			map.put("msg2",true);
		}
		return map;
	}
	
	
	
	/**
	 * 查找已读评论，根据登录者的id，查找出其他人对于登陆者发布的商品的评论，并且登陆者已经阅读过的
	 * 这里查找出来的评论，要保证所以回复也是已读的
	 * 这里先查出来所有已读评论，在controller层再对查出的每一条评论，查看有没有未读回复，如果有那么就从list中删除该条信息
	 * @param userId
	 * @return
	 */
	@RequestMapping("/findALLComment")
	public @ResponseBody Map<String,Object> findALLComment(String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		//查找所有的对登陆者发布商品的已读评论
		List<commentMsg> list = Service.findALLComment(userId);
		System.out.println("readList1="+list.size());
		//依次查看已读评论是否有未读回复
		for(int i=0;i<list.size();){
			//查找该条评论下是否有未读回复
			List<replyMsg> replyList = Service.findNoReadReply(list.get(i).getCommentId(),userId);
			
			//如果replyList大小大于0，那么就说明有未读回复，就将这条评论信息从list中删除(这里其实应该先记下要删除的下标，等出了循环再删除，不然一删除list的大小也会跟着变，那么可能就会有信息是判断不到的)
			if(replyList.size()>0){
				list.remove(i);
				continue;
			}else{    //说明没有未读回复，只有已读回复或者没有回复
				//查找该条评论下的回复总数，根据commentID查找（这里不再区分是不是发给登陆者的）
				int num = Service.replyNum(list.get(i).getCommentId());
				list.get(i).setReplyTotal(num);
				if(num>0)  //说明评论list[i]下有回复
				{
					//那么就查出最后一条回复信息，放入到list的lastReplyContent中
					//根据commentID进行查找
					replyMsg last = Service.findLastReply2(list.get(i).getCommentId());
					//找发出者的信息
					userMsg user = Service.findUserMsgById(last.getReplyId());
					//设置发布者的头像，昵称和姓名进入list(i)中
					last.setAvatarUrl_from(user.getAvatarUrl());
					last.setNickName_from(user.getNickName());
					last.setUserName_from(user.getUserName());
					last.setUserId_from(user.getUserId());
					//找回复对象的信息
					userMsg user2 = Service.findUserMsgById(last.getReplyToId());
					//设置发布对象的昵称和姓名
					last.setNickName_to(user2.getNickName());
					last.setUserName_to(user2.getUserName());
					last.setUserId_to(user2.getUserId());
					
					//设置该条评论的最后一条回复
					list.get(i).setLastReplyContent(last);
				}
			}
			i++;
		}
		System.out.println("readList2="+list.size());
		map.put("list", list);
		return map;
	}
	
	
	/**
	 * 先查出发布该商品信息的商家信息(这个在controller调用函数查)，再查出该商品的发布信息
	 * @param goodId
	 * @return
	 */
	@RequestMapping("/queryGood")
	public @ResponseBody Map<String,Object> queryGood(int goodId){
		System.out.println("查询商品和物主goodId="+goodId);
		Map<String,Object> map = new HashMap<String,Object>();
		//查出商品发布信息
		goodMsg good = Service.queryGood(goodId);  //这里查找出来商品信息后，还应该修改image的路径，保证页面上能够访问到
		if(good.getImage1()!=null){
			good.setImage1("http://www.ZLofCampus.top/image/"+good.getImage1());
		}
		if(good.getImage2()!=null){
			good.setImage2("http://www.ZLofCampus.top/image/"+good.getImage2());
		}
		if(good.getImage3()!=null){
			good.setImage3("http://www.ZLofCampus.top/image/"+good.getImage3());
		}
		System.out.println("image1="+good.getImage1());
		map.put("goodmsg",good);  
		//查出商品发布者的信息
		userMsg user = Service.queryGoodUser(goodId);
		map.put("usermsg",user);
		return map;
	}
	
	
	
	/**
	 * 根据评论id查找评论
	 * @param commentId
	 * @return
	 */
	@RequestMapping("/findCommentById")
	public @ResponseBody Map<String,Object> findCommentById(int commentId){
		Map<String,Object> map = new HashMap<String,Object>();
		commentMsg res = Service.findCommentById(commentId);
		map.put("commentmsg", res);
		//再根据commentID查找下属回复
		List<replyMsg> list = Service.findReply(commentId);
		//对每一条回复，分别查这条回复的发出者，和接收者的信息
		for(int i=0;i<list.size();i++){
			//找发出者的信息
			userMsg user = Service.findUserMsgById(list.get(i).getReplyId());
			//设置发布者的头像，昵称和姓名进入list(i)中
			list.get(i).setAvatarUrl_from(user.getAvatarUrl());
			list.get(i).setNickName_from(user.getNickName());
			list.get(i).setUserName_from(user.getUserName());
			list.get(i).setUserId_from(user.getUserId());
			//找回复对象的信息
			userMsg user2 = Service.findUserMsgById(list.get(i).getReplyToId());
			//设置发布对象的昵称和姓名
			list.get(i).setNickName_to(user2.getNickName());
			list.get(i).setUserName_to(user2.getUserName());
			list.get(i).setUserId_to(user2.getUserId());
		}
		map.put("replyList", list);
		return map;
	}
	
	
	/**
	 * 添加回复
	 * 先根据replyComment，找到该条评论下次序最大的回复的次序号
	 * @param replyComment
	 * @param replyId
	 * @param replyToId
	 * @param replyContent
	 * @return
	 */
	@RequestMapping("/addReply")
	public @ResponseBody Map<String,Object> addReply(int replyComment,String replyId,String replyToId,String replyContent){
		Map<String,Object> map = new HashMap<String,Object>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String replyDate = df.format(new Date()); //获取当前时间(作为付款时间)
		//根据replyComment，找到该条评论下次序最大的回复的次序号
		Integer sequence=0;
		sequence = Service.findMaxSequence(replyComment);
		if(sequence==null)
		{
			sequence=1;
		}else
			sequence+=1;
		//添加回复
		boolean res = Service.addReply(replyComment, replyId, replyToId, replyContent, replyDate, sequence);
		map.put("msg", res);
		return map;
	}
	
	
	/**
	 * 删除回复，根据回复表的id
	 * @param Id
	 * @return
	 */
	@RequestMapping("/deleteReply")
	public @ResponseBody Map<String,Object> deleteReply(int Id){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.deleteReply(Id);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * 根据商品id，和对话双方的id，先查询主信息，如果存在主信息，那么就再查询下属的聊天信息，并按照序号递增排序
	 * @param chatGoodId
	 * @param chatA
	 * @param chatB
	 * @return
	 */
	@RequestMapping("/queryChat")
	public @ResponseBody Map<String,Object> queryChat(int chatGoodId,String chatA,String chatB){
		//System.out.println("chatA="+chatA);
		//System.out.println("chatB="+chatB);
		Map<String,Object> map = new HashMap<String,Object>();
		//先查询主信息
		chatMsg list = Service.queryChat(chatGoodId, chatA, chatB);
		if(list!=null)   //说明存在主信息，那么就依据主信息的id查询下属的聊天信息,并加入主信息中一起返回
		{
			if(list.getIsImage()==1)
				list.setChatContent("http://www.ZLofCampus.top/image/"+list.getChatContent());
			List<afterChatMsg> after = Service.queryChatAfter(list.getId());
			if(after.size()>0)  //有下属回复
			{
				for(int i=0;i<after.size();i++)
				{
					if(after.get(i).getIsImage()==1)
						after.get(i).setChatContent("http://www.ZLofCampus.top/image/"+after.get(i).getChatContent());
				}
				list.setAfterChatList(after);
			}
		}
		map.put("chatList",list);
		return map;
	}
	
	

	/**
	 * 添加文字留言信息
	 * 根据goodid和openId（作为chatRightId），usermsg.userId（作为chatLeftId)添加聊天信息
	 * 添加聊天信息时，要先查找有没有主聊天信息，如果有主聊天信息，那么就以主id为外键添加下属聊天信息
	 * 如果没有主聊天信息，那么就添加此条为主聊天信息
	 * @param chatGoodId
	 * @param chatRightId
	 * @param chatLeftId
	 * @return
	 */
	@RequestMapping("/addChat")
	public @ResponseBody Map<String,Object> addChat(int chatGoodId,String chatRightId,String chatLeftId,String chatContent){
		Map<String,Object> map = new HashMap<String,Object>();
		//获得当前日期
		Date day=new Date();    
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String chatDate=df.format(day);
		//获取当前时间
		int pos = chatDate.indexOf(' ');
		String chatTime=chatDate.substring(pos+1);
		
		//先查查有没有主聊天信息，根据商品id和双方id
		String chatA=chatRightId;
		String chatB=chatLeftId;
		chatMsg list = Service.queryChat(chatGoodId, chatA, chatB);
		boolean res=false;
		if(list==null){  //说明没有主聊天信息，那么就添加主聊天信息
			res = Service.addChat(chatGoodId, chatRightId, chatLeftId,chatDate,chatContent,chatTime);
		}else{  //有主聊天信息，那么就添加下属聊天信息
			//将主聊天信息的isShow都改为1
			Service.updateShow(list.getId());
			res = Service.addChatAfter(list.getId(), chatRightId, chatLeftId, chatContent, chatDate, chatTime);
		}
		map.put("msg", res);
		return map;
	}
	
	
	/**
	 * 添加图片留言信息
	 * 根据goodid和openId（作为chatRightId），usermsg.userId（作为chatLeftId)添加聊天信息
	 * 添加聊天信息时，要先查找有没有主聊天信息，如果有主聊天信息，那么就以主id为外键添加下属聊天信息
	 * 如果没有主聊天信息，那么就添加此条为主聊天信息
	 * @param chatGoodId
	 * @param chatRightId
	 * @param chatLeftId
	 * @return
	 */
	@RequestMapping("/addChatImage")    
	public @ResponseBody Map<String,Object> addChatImage(HttpServletRequest request,@RequestParam(value="file",required = false)MultipartFile file)throws IOException{
		Map<String,Object> map = new HashMap<String,Object>();
	    request.setCharacterEncoding("UTF-8");
	    
	    String chatRightId = request.getParameter("chatRightId");
	    String chatLeftId = request.getParameter("chatLeftId");
	    int chatGoodId = Integer.parseInt(request.getParameter("chatGoodId"));
	    
	    
	    String path = null;   //图片路径
	    String trueFileName= null;
	    
	    System.out.println("file="+file);
	    if(!file.isEmpty()) {  //传入的文件不空，则获取到图片
	    	
	    	System.out.println("成功获取照片");
	    	
	    	//获取图片初始名称
            String fileName = file.getOriginalFilename();  
            System.out.println("fileName="+fileName);
            
            String type = null;
            
            //获取图片类型
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            
            System.out.println("图片初始名称为：" + fileName + " 类型为：" + type);
            
            if (type != null) {
            	//检查是否符合要求的图片类型
            	if ("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())) 
            	{
            		// 项目在容器中实际发布运行的根路径
            		String realPath = request.getSession().getServletContext().getRealPath("/");
            		// 自定义的文件名称
            		trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;
            		int pos = trueFileName.lastIndexOf(".");
            		
            		System.out.println("pos="+pos);
            		String temp = trueFileName.substring(0, pos);
            		System.out.println("substr="+temp);
            		trueFileName = temp+".jpg";
            		
            		
            		// 设置存放图片文件的路径
            		path = realPath + "/uploads/" + trueFileName;
            		
            		System.out.println("trueFileName=="+trueFileName);
            		
            		System.out.println("存放图片文件的路径:" + path);
            		
            		file.transferTo(new File(path));
            		System.out.println("文件成功上传到指定目录下");
            		map.put("upload","文件成功上传到指定目录下");
            		
            	}else {
            		System.out.println("不是我们想要的文件类型,请按要求重新上传");
            		map.put("upload", "error:文件类型不符合,请按要求重新上传");
            		return map;
            	}
            }else {
            	System.out.println("文件类型为空");
            	map.put("upload", "error:文件类型为空");
            	return map;
            }
	    }else {
	    	System.out.println("没有找到相对应的文件");
	    	map.put("upload", "error:没有找到相对应的文件");
	    	return map;
	    }
	    
		//获得当前日期
		Date day=new Date();    
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String chatDate=df.format(day);
		//获取当前时间
		int pos = chatDate.indexOf(' ');
		String chatTime=chatDate.substring(pos+1);
		
		//先查查有没有主聊天信息，根据商品id和双方id
		String chatA=chatRightId;
		String chatB=chatLeftId;
		chatMsg list = Service.queryChat(chatGoodId, chatA, chatB);
		boolean res=false;
		if(list==null){  //说明没有主聊天信息，那么就添加主聊天信息
			res = Service.addChatImage(chatGoodId, chatRightId, chatLeftId,chatDate,trueFileName,chatTime);
		}else{  //有主聊天信息，那么就添加下属聊天信息
			res = Service.addChatAfterImage(list.getId(), chatRightId, chatLeftId,trueFileName, chatDate, chatTime);
		}
		map.put("msg", res);
		return map;
	}
	
	
	//根据openId查找聊天信息，之后再做处理
	/**
	 * 先查出来在聊天记录中chatRightId = opened 或者 chatLeftId= openid的聊天消息中涉及到几个商品（查有几个不同的商品号）
	 * 然后根据这些商品号和openId，先查出每一个商品对应的一组聊天信息（按照序号升序排），
	 * 然后循环判断list中有没有 chatLeftId=openid 并且isRead=0的聊天信息，如果有，那么就说明有未读信息，记录下这个未读信息的条数，以及最后一条未读信息
	 * 如果没有未读信息，那么就只是单纯的记录下最后一条信息
	 * @param userId
	 * @return
	 */
	@RequestMapping("/findAllChat")
	public @ResponseBody Map<String,Object> findAllChat(String openId){
		Map<String,Object> map = new HashMap<String,Object>();
		//先查找主留言
		List<chatMsg> list1 = Service.findMainChat(openId);
		for(int i=0;i<list1.size();i++){  //对每一条主留言查询下属的留言信息
			//如果该条主留言本身未读，那么就将noRead先置为1，否则为0
			if(list1.get(i).getIsRead()==0 && list1.get(i).getChatLeftId().equals(openId))
			{
				list1.get(i).setNoRead(1);
			}else{
				list1.get(i).setNoRead(0);
			}
			if(list1.get(i).getIsImage()==1)  //是图片信息，对content进行处理
			{
				list1.get(i).setChatContent("http://www.ZLofCampus.top/image/"+list1.get(i).getChatContent());
				
			}
			
			//根据主留言id查询该条主留言下属的所有留言
			List<afterChatMsg> list2 = Service.queryChatAfter(list1.get(i).getId());
			
			if(list2.size()>0){  //有下属的留言信息
				//获取未读留言信息数，以及最后一条下属留言信息
				int len =list2.size();
				int sum=0;
				//获取未读留言数
				for(int j=0;j<len;j++)
				{
					if(list2.get(j).getIsRead()==0 && list2.get(j).getChatLeftIdA().equals(openId))  //注意：对于登陆者来说，未读留言是指由登陆者接收的未读留言，那么此时登陆者应该作为chatLeftID出现
						sum++;
					if(list2.get(j).getIsImage()==1)  //是图片信息,对每一条图片信息content处理
						list2.get(j).setChatContent("http://www.ZLofCampus.top/image/"+list2.get(j).getChatContent());
					
				}
				list1.get(i).setNoRead(list1.get(i).getNoRead()+sum);  //将未读下属留言数加入到主留言信息的未读留言数中
				//获取最后一条下属留言
				list1.get(i).setLastChat(list2.get(len-1));
				list1.get(i).setAfterChatList(list2);
			}//没有下属留言信息就什么也不做
			
			
			//还需要查出来每条主留言中，对方的身份信息
			if(!list1.get(i).getChatRightId().equals(openId)){   //如果ChatRightId不是自己的id那么就是对方的id，就以这个id查询用户信息
				
				userMsg user = Service.findUserMsgById(list1.get(i).getChatRightId());
				list1.get(i).setAvatarUrl_other(user.getAvatarUrl());
				list1.get(i).setUserName_other(user.getUserName());
				list1.get(i).setNickName_other(user.getNickName());
			}else{
				
				userMsg user = Service.findUserMsgById(list1.get(i).getChatLeftId());
				list1.get(i).setAvatarUrl_other(user.getAvatarUrl());
				list1.get(i).setUserName_other(user.getUserName());
				list1.get(i).setNickName_other(user.getNickName());
			}
		}
		map.put("allChat",list1);  //将信息返回
		return map;
	}
	
	
	/**
	 * 设置所有留言为已读(根据主留言的id，将该条主留言和下属留言均设置为已读)
	 * 设置留言已读时，一定要注意根据登录者的id将chatLeftID=openId的未读留言设为已读即可，而不要修改对方的未读留言信息
	 * @param Id
	 * @param len  用来表示该条主留言有没有下属留言
	 * @return
	 */
	@RequestMapping("/setAllChatRead")
	public @ResponseBody Map<String,Object> setAllChatRead(int Id,int len,String openId){
		//System.out.println("setAllChatRead");
		//System.out.println("len="+len);
		
		Map<String,Object> map = new HashMap<String,Object>();
		//这里要先判断一下主留言的chatLeftId是否是openId，然后再进行修改，否则会因为没有符合条件的主留言进行修改导致返回值为false，影响正常的跳转
		//根据id查找主留言信息
		chatMsg chat = Service.findMainChatById(Id);
		boolean res1=false;
		if(chat.getChatLeftId().equals(openId) && chat.getIsRead()==0){
			//现将主留言本身改为已读
			res1 = Service.setMainRead(Id,openId);
		}else{  //本身就不需要改的主留言，那么直接置为true
			res1 = true;
		}
		//再将下属留言改为已读
		boolean res2 = false;
		if(len>0){  //这里的len只是说明有下属的留言，但是这些下属留言有没有未读的就不一定了，如果都已读，那么返回值肯定为0，则导致false
			System.out.println("测试1");
			//可以先根据主留言id查询出所有的下属留言；
			//然后再一条一条判断
			List<afterChatMsg> list = Service.queryChatAfter(Id);
			int sum=0;
			for(int i=0;i<list.size();i++)
			{
				if(list.get(i).getChatLeftIdA().equals(openId) && list.get(i).getIsRead()==0){
					//System.out.println("###");
					sum++;
				}
					
			}
			//System.out.println("sum="+sum);
			if(sum>0)  //说明有未读的
				res2=Service.setAfterRead(Id,openId);
			else  //没有未读的下属留言
				res2=true;  //直接设置为true
		}
		else{
			System.out.println("测试2");
			res2=true;
		}
		//System.out.println("res2="+res2);
		map.put("msg1", res1);
		map.put("msg2", res2);
		return map;
	}
	
	
	/**
	 * 设置留言信息的显示
	 * @param Id
	 * @param isShowR
	 * @param isShowL
	 * @return
	 */
	@RequestMapping("/modifyShowState")
	public @ResponseBody Map<String,Object> modifyShowState(int Id,int isShowR,int isShowL){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyShowState(Id, isShowR, isShowL);
		map.put("msg", res);
		return map;
	}
	
	
	/**
	 * 根据商品id查找发布者所在学校
	 * @param goodId
	 * @return
	 */
	@RequestMapping("/findSchool")
	public @ResponseBody Map<String,Object> findSchool(int goodId){
		System.out.println("findSchool_goodId="+goodId);
		Map<String,Object> map = new HashMap<String,Object>();
		String res = Service.findSchool(goodId);
		if(res==null)
			res="该用户未填写学校信息！";
		
		System.out.println("school="+res);
		
		map.put("school",res);
		return map;
	}
	
	
	/**
	 * 根据userId查找头像，昵称，名字
	 * @param userId
	 * @return
	 */
	@RequestMapping("/queryNameAndNickAndAvat")
	public @ResponseBody Map<String,Object> queryNameAndNickAndAvat(String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		userMsg res = Service.queryNameAndNickAndAvat(userId);
		map.put("user", res);
		return map;
	}
	
	
	/**
	 * 添加关注
	 * @param followId
	 * @param followToId
	 * @return
	 */
	@RequestMapping("/addFollow")
	public @ResponseBody Map<String,Object> addFollow(String followId,String followToId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.addFollow(followId, followToId);
		map.put("msg", res);
		return map;
	}
	
	
	/**
	 * 取消关注
	 * @param followId
	 * @param followToId
	 * @return
	 */
	@RequestMapping("/cancelFollow")
	public @ResponseBody Map<String,Object> cancelFollow(String followId,String followToId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.cancelFollow(followId, followToId);
		map.put("msg", res);
		return map;
	}
	
	
	/**
	 * 查询关注状态
	 * @param followId
	 * @param followToId
	 * @return
	 */
	@RequestMapping("/followState")
	public @ResponseBody Map<String,Object> followState(String followId,String followToId){
		Map<String,Object> map = new HashMap<String,Object>();
		followMsg res = Service.followState(followId, followToId);
		if(res==null)
			map.put("msg", false);
		else
			map.put("msg", true);
		return map;
	}
	
	
	/**
	 * 根据用户id查找该用户的所有关注
	 * @param followId
	 * @return
	 */
	@RequestMapping("/findFollow")
	public @ResponseBody Map<String,Object> findFollow(String followId){
		Map<String,Object> map = new HashMap<String,Object>();
		List<followMsg> list = Service.findFollow(followId);
		userMsg res=null;
		for(int i=0;i<list.size();i++)  //根据每个被关注者的id查找个人信息
		{
			res = Service.findUserMsgById(list.get(i).getFollowToId());
			list.get(i).setUser(res);
		}
		map.put("list", list);
		return map;
	}
	
	
	/**
	 * 根据商品id修改商品信息
	 * @param goodId
	 * @return
	 */
	@RequestMapping("/modifyGood")
	public @ResponseBody Map<String,Object> modifyGood(int goodId,String goodName,int unit,int type,String goodDetail,int deadline,double price,int deposit,String introduction,int num){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.modifyGood(goodId, goodName, unit, type, goodDetail, deadline, price, deposit, introduction, num);
		map.put("msg",res);
		return map;
	}
	
	
	
	/**
	 * 获取用户的userId(openid)
	 * @param code
	 * @param encryptedData
	 * @param iv
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET,value = "/getOpenid")
    public @ResponseBody Map<String,Object> getOpenid(String code) throws Exception{
        
		System.out.println("code="+code);
		/*
		 * 参数String encryptedData, String iv这里是用于解密的
		 */
		
        Map<String,Object> map = new HashMap<String,Object>();
        
        //登录凭证不能为空
        if (code == null || code.length() == 0) {
            map.put("status", 0);
            map.put("msg", "code 不能为空");
            //System.out.println("map1:" + map);
            return map;
        }
        
        //小程序唯一标识   (在微信小程序管理后台获取)
        String wxspAppid = "wx462342c6fb47c998";  //这里应该填当前小程序项目的AppId，换小程序时要注意比对！！！
        
        //小程序的 app secret (在微信小程序管理后台获取)
        String wxspSecret = "5caf8a23ee1b27c7d2dadc200e6c0be2";  //这里是我自己的
        
        //授权（必填）
        String grant_type = "authorization_code";  //这里需要自己填吗？？？？？
        
        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid ////////////////
        //请求参数
        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type=" + grant_type;
        
        //发送请求
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        
        //解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(sr);
        
        //获取会话密钥（session_key）
        String session_key = json.get("session_key").toString();
        
        //用户的唯一标识（openid）
        String openid = (String) json.get("openid");
        
        map.put("session_key", session_key);
        map.put("openId", openid);
       
        return map;
    }
	
	
	
}
