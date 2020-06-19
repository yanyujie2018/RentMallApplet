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
//import com.qd.util.AesUtil;  ��������������ܵĹ����࣬�����Ȼ������Ҫ���ܣ���ô�����Ҳ�Ͳ�����
import com.qd.util.HttpRequest;
import com.zhenzi.sms.ZhenziSmsClient;

@Controller
public class controller {
	
	@Autowired
	private service Service;
	
	/**
	 * ����û���Ϣ���ȸ���userId������û�� ���û�����û�в���ӣ�����ֱ�ӷ���true
	 * @param userId
	 * @param phone
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/addUserMsg")
	public @ResponseBody Map<String,Object> addUserMsg(String userId){
		Map<String,Object> map = new HashMap<String, Object>();
		System.out.println("adduser����");
		System.out.println("userId="+userId);
		boolean res = Service.addUserMsg(userId);
		map.put("success", res);
		return map;
	}
	
	/**
	 * �����û��˺Ų����û�
	 * @param userId
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/findUserMsgById")
	public @ResponseBody Map<String,Object> findUserMsgById(String userId){
		System.out.println("findUserMsgById="+userId);
		userMsg user = Service.findUserMsgById(userId);
		Map<String,Object> map = new HashMap<String,Object>();
		System.out.println("user="+user);
		if(user==null)  //֮ǰû��ע���
		{
			map.put("querymsg", false);
		}else{
			if(user.getSex()==0)
				map.put("sex","��");
			else if(user.getSex()==1)
				map.put("sex","Ů");
			map.put("querymsg", true);
		}
		map.put("usermsg",user);
		return map;
	}
	
	/**
	 * ����userId�޸��û��Ա�
	 * @param userId
	 * @param gender
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/modiyfSexById")
	public @ResponseBody Map<String,Object> modiyfSexById(String userId,String gender){
		Map<String,Object> map = new HashMap<String,Object>();
		int sex;
		//System.out.println("gender="+gender);
		if(gender.equals("��")){
			sex=0;
		}else
			sex=1;
		//System.out.println("sex="+sex);
		boolean res = Service.modiyfSexById(userId, sex);
		map.put("success",res);
		return map;
	}
	
	/**
	 * ����userId�޸��û�����
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
	 * �޸��û��Ļ�����Ϣ�����֤��������ѧУ��΢�źţ�
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
	 * �޸��û�����
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
	 * �޸����֤
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
	 * �޸�΢�ź�
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
	 * �޸�ѧУ
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
	 * ���sessionId
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
	 * ���Ͷ�����֤��
	 * @param number�����ֻ�����
	 */
	@RequestMapping("/sendSms")
	public @ResponseBody Object sendSms(HttpServletRequest request, String phoneNum) {
		try {
			System.out.println("phoneNum="+phoneNum);
			
			JSONObject json = null;
			
			//�������6λ��֤��
			String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
			System.out.println("verify="+verifyCode);
			
			//���Ͷ��ţ���ʼ��
			ZhenziSmsClient client = new ZhenziSmsClient("https://sms_developer.zhenzikj.com","104843", "85899518-73b7-4287-b3ee-b1d0e7d1d6f0");
			
			//��װ�����Ͷ���ʱ�Ĳ���
			Map<String,String> params = new HashMap<String,String>();
			params.put("message", "��ӭʹ��У԰�����̳ǣ�������֤��Ϊ:"+ verifyCode+ "��������Ч��Ϊ5���ӣ�����ֻ��ʹ��һ�Σ�");
			params.put("number",phoneNum);
			
			//send�������ڵ������Ͷ���,�������������Ҫ��װ��Map��
			//���ؽ����json��ʽ���ַ���, code: ����״̬��0Ϊ�ɹ�����0Ϊ����ʧ�ܣ��ɴ�data�в鿴������Ϣ
			String result = client.send(params);
			json = JSONObject.parseObject(result);
			
			System.out.println("���="+json);
			
			if(json.getIntValue("code") != 0)//���Ͷ���ʧ�ܣ���ǰ̨����ʧ����Ϣ
				return "fail";
			
			//�����Ͷ��ųɹ�
			//����֤��浽session��,ͬʱ���봴��ʱ��
			//��json��ţ�����ʹ�õ��ǰ����fastjson
			HttpSession session = request.getSession();
			
			System.out.println("session="+session);
			
			json = new JSONObject();
			
			json.put("verifyCode", verifyCode);
			json.put("createTime", System.currentTimeMillis());
			
			// ����֤�����SESSION������֮����֤�û���д���Ƿ���ȷ
			request.getSession().setAttribute("verifyCode", json);
			return "success";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * �ύ��Ϣ����֤������֤��
	 */
	@RequestMapping("/Verification")
	public @ResponseBody Object addinfo(HttpServletRequest request, String phone,String code) {
		JSONObject json = (JSONObject)request.getSession().getAttribute("verifyCode");
		if(!json.getString("verifyCode").equals(code)){
			return "��֤�����";
		}
		if((System.currentTimeMillis() - json.getLong("createTime")) > 1000 * 60 * 5){
			return "��֤�����";
		}
		
		return "success";
	}
	
	
	/**
	 * �޸İ��ֻ���
	 */
	@RequestMapping("/modifyPhoneById")
	public @ResponseBody Object modifyPhoneById(HttpServletRequest request, String phone,String code,String userId) {
		JSONObject json = (JSONObject)request.getSession().getAttribute("verifyCode");
		if(!json.getString("verifyCode").equals(code)){
			return "��֤�����";
		}
		if((System.currentTimeMillis() - json.getLong("createTime")) > 1000 * 60 * 5){
			return "��֤�����";
		}
		
		boolean res = Service.modifyPhoneById(userId, phone);
		
		if(res)
			return "success";
		else
			return "�������";
	}
	
	/**
	 * �����Ʒ��Ϣ����ͼƬ�⣩,���һ�������ող������Ϣ��goodId����map�з��أ�Ϊ����洢ͼƬ��׼��
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
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//�������ڸ�ʽ
		String releaseDate = df.format(new Date()); //��ȡ��ǰʱ��
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
	 * �����ƷͼƬ
	 * ����ͼƬ����content-typeΪmultipart/form-data�ĸ�ʽ�ϴ��ģ�����ʹ��spring-mvc����ͨ��ʹ�ò�������ʽ�Զ����Ƶĸ�ʽ��ȡ����ͼƬ��
	 * ��<strong>@RequestParam(value = "file", required = false) MultipartFile file</strong>
	 * @param goodId
	 * @param imagePath
	 * @return
	 */
	@RequestMapping("/addGoodImage")  
	public @ResponseBody Map<String,Object> addGoodImage(HttpServletRequest request,@RequestParam(value="file",required = false)MultipartFile file)throws IOException {
		Map<String,Object> map=new HashMap<String,Object>();
		System.out.println("ִ��upload");
	    request.setCharacterEncoding("UTF-8");
	    System.out.println("ִ��ͼƬ�ϴ�");
	    
	    int goodId = Integer.parseInt(request.getParameter("goodId"));
	    int index = Integer.parseInt(request.getParameter("index"));  //�������ȥ�ж����ǲ���ĵڼ���ͼƬ���Դ����ж���Ҫ���Ǹ�������ݿ�ĺ���
	    
	    String path = null;   //ͼƬ·��
	    String trueFileName= null;
	    
	    if(!file.isEmpty()) {  //������ļ����գ����ȡ��ͼƬ    	
	    	System.out.println("�ɹ���ȡ��Ƭ");	    	
	    	//��ȡͼƬ��ʼ����
            String fileName = file.getOriginalFilename();  
            System.out.println("fileName="+fileName);
            String type = null;
            
            //��ȡͼƬ����
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            
            System.out.println("ͼƬ��ʼ����Ϊ��" + fileName + " ����Ϊ��" + type);
            
            if (type != null) {
            	//����Ƿ����Ҫ���ͼƬ����
            	if ("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())) 
            	{
            		// ��Ŀ��������ʵ�ʷ������еĸ�·��
            		String realPath = request.getSession().getServletContext().getRealPath("/");
            		// �Զ�����ļ�����
            		trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;
            		int pos = trueFileName.lastIndexOf(".");
            		
            		System.out.println("pos="+pos);
            		String temp = trueFileName.substring(0, pos);
            		System.out.println("substr="+temp);
            		trueFileName = temp+".jpg";
            		
            		// ���ô��ͼƬ�ļ���·��
            		path = realPath + "/uploads/" + trueFileName;
            		
            		System.out.println("trueFileName=="+trueFileName);
            		
            		System.out.println("���ͼƬ�ļ���·��:" + path);
            		
            		file.transferTo(new File(path));
            		System.out.println("�ļ��ɹ��ϴ���ָ��Ŀ¼��");
            		map.put("upload","�ļ��ɹ��ϴ���ָ��Ŀ¼��");
            		
            	}else {
            		System.out.println("����������Ҫ���ļ�����,�밴Ҫ�������ϴ�");
            		map.put("upload", "error:�ļ����Ͳ�����,�밴Ҫ�������ϴ�");
            		return map;
            	}
            }else {
            	System.out.println("�ļ�����Ϊ��");
            	map.put("upload", "error:�ļ�����Ϊ��");
            	return map;
            }
	    }else {
	    	System.out.println("û���ҵ����Ӧ���ļ�");
	    	map.put("upload", "error:û���ҵ����Ӧ���ļ�");
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
	 * ��ѯ�û����з�����Ʒ
	 * @param userId
	 * @return
	 */
	@RequestMapping("/showAllRelease")
	public @ResponseBody Map<String,Object> showAllRelease(String userId){
		//System.out.println("find_userId="+userId);
		Map<String,Object> map=new HashMap<String,Object>();
		List<goodMsg> list = Service.showAllRelease(userId);
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
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
	 * ����goodIDɾ����������Ϣ
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
	 * ��ѯ�����еĶ��������������к������У�
	 * @param rentId
	 * @return
	 */
	@RequestMapping("/findRent")
	public @ResponseBody Map<String,Object> findRent(String rentId,int rentState){
		//System.out.println("rentId="+rentId);
		Map<String,Object> map = new HashMap<String,Object>();
		List<rentMsg> list = Service.findRent(rentId, rentState);
		
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
		}
		if(rentState==3)   //����ǲ��Ҵ��黹��������ô�Ͷ�ÿһ����Ϣ�ٴ���һ�£������ÿ����Ʒ���м���黹ʱ��
		{
			int[][] days={{0,31,28,31,30,31,30,31,31,30,31,30,31},{0,31,29,31,30,31,30,31,31,30,31,30,31}};
			//��ȡ���������
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy");//�������ڸ�ʽ
			int currentYear = Integer.parseInt(df1.format(new Date())); //��ȡ��ǰʱ��
			SimpleDateFormat df2 = new SimpleDateFormat("MM");//�������ڸ�ʽ
			int currentMonth = Integer.parseInt(df2.format(new Date())); //��ȡ��ǰʱ��
			SimpleDateFormat df3 = new SimpleDateFormat("dd");//�������ڸ�ʽ
			int currentDay = Integer.parseInt(df3.format(new Date())); //��ȡ��ǰʱ��
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
				int rest=0;  //ʣ��黹����
				int sub=0;  //���㵱ǰ���ں͹��������죬Ȼ����deadline��ȥsub����rest
				if(year==currentYear)
				{
					if(month==currentMonth)
					{
						sub=currentDay-day;
					}else{  //����·ݲ���ͬ�������ǿ����·ݵ�,monthС
						if((year%4==0 && year%100!=0)|| year%400==0)  //�ж��ǲ�������
						{
							int tmp=days[1][month]-day;
							sub = tmp+currentDay;
							
						}else{  //��������
							int tmp=days[0][month]-day;
							sub = tmp+currentDay;
						}
					}
				}else{    //����һ�꣬��ôyearС������month�϶���һ�£���Ϊ���Ĺ黹����ֻ��7��
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
	 * ��ѯ���黹�Ķ��� 
	 * @param rentId
	 * @return
	 */
	@RequestMapping("/findWaitReturn")
	public @ResponseBody Map<String,Object> findWaitReturn(String rentId,int rentState){
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<rentMsg> list = Service.findWaitReturn(rentId, rentState);
		
		int[][] days={{0,31,28,31,30,31,30,31,31,30,31,30,31},{0,31,29,31,30,31,30,31,31,30,31,30,31}};
		//��ȡ���������
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy");//�������ڸ�ʽ
		int currentYear = Integer.parseInt(df1.format(new Date())); //��ȡ��ǰʱ��
		SimpleDateFormat df2 = new SimpleDateFormat("MM");//�������ڸ�ʽ
		int currentMonth = Integer.parseInt(df2.format(new Date())); //��ȡ��ǰʱ��
		SimpleDateFormat df3 = new SimpleDateFormat("dd");//�������ڸ�ʽ
		int currentDay = Integer.parseInt(df3.format(new Date())); //��ȡ��ǰʱ��
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
			int rest=0;  //ʣ��黹����
			int sub=0;  //���㵱ǰ���ں͹��������죬Ȼ����deadline��ȥsub����rest
			if(year==currentYear)
			{
				if(month==currentMonth)
				{
					sub=currentDay-day;
				}else{  //����·ݲ���ͬ�������ǿ����·ݵ�,monthС
					if((year%4==0 && year%100!=0)|| year%400==0)  //�ж��ǲ�������
					{
						int tmp=days[1][month]-day;
						sub = tmp+currentDay;
						
					}else{  //��������
						int tmp=days[0][month]-day;
						sub = tmp+currentDay;
					}
				}
			}else{    //����һ�꣬��ôyearС������month�϶���һ�£���Ϊ���Ĺ黹����ֻ��7��
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
		
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
		}
		map.put("list",list);
		return map;
	}
	
	/**
	 * ���Ե�ǰ�û�IDΪrentID��ѯ����״̬Ϊ2���ҷ�Ϊ���飬��������ʱ�䵫�ڹ黹�����ڵĶ������޸�״̬Ϊ3�������黹���޵Ķ���״ֱ̬�Ӹ�Ϊ4
	 * @param rentId
	 * @return
	 */
	@RequestMapping("/modifyRentState")
	@Transactional 
	public @ResponseBody Map<String,Object> modifyRentState(String rentId){
		
		System.out.println("rentId#####="+rentId);
		System.out.println("modifyRentStateִ��");
		Map<String,Object> map = new HashMap<String,Object>();
		int[][] days={{0,31,28,31,30,31,30,31,31,30,31,30,31},{0,31,29,31,30,31,30,31,31,30,31,30,31}};
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//�������ڸ�ʽ
		String currentDate = df.format(new Date()); //��ȡ��ǰʱ��
		//ֻ�����Ȳ��������û���ΪrentID������״̬Ϊ2��������Ϣ��Ȼ������ȥ�ж���Щ��Ϣ���棬��Щ��ʱ�䳬�������ڹ黹���������
		//����Щ�м��뵽һ���µ�list1��
		//����һЩʱ�䳬�������ڹ黹�����ڵ��м���list2��
		//��list1�е���״̬�޸�Ϊ3
		//��list2�е���״̬�޸�Ϊ4
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
			//��õ���ʱ������
			int year=0;
			int pos1 = dueDate.indexOf("-");  //�ҵ���һ�γ���-��λ�ã���ôǰ����Ӵ��ͱ�ʾ���
			year = Integer.parseInt(dueDate.substring(0,pos1));  //��ȡ��ݲ���ת��Ϊ����
			int pos3 = dueDate.lastIndexOf("-");  //�ҵ����һ�γ���-��λ�ã����� ������
			int day = Integer.parseInt(dueDate.substring(pos3+1));
			int month = Integer.parseInt(dueDate.substring(pos1+1,pos3));
			
			//�ȸ��������Ʒ��id��ѯ��黹�����Ǽ���
			int tmp = Service.queryReturnTime(list.get(i).getGoodId());
			if(tmp==0)
			{
				tmp=3;
			}else if(tmp==1){
				tmp=5;
			}else if(tmp==2){
				tmp=7;
			}
			if(dueDate.compareTo(currentDate)<0){  //��ǰ���ڴ��ڵ������ڣ�С�ڵ������ڵĸ��������ù�
				//��������Ĺ��̼��ɼ��㵽�����ڼ��Ϲ黹���޺���ʲô����
				if(year%400==0 || (year%4==0 && year%100!=0 )){  //�����꣬ȡdays�������±�Ϊ1
					if(day+tmp>days[1][month]){  //��������·ݵ���������ô�·�+1
						day = day+tmp-days[1][month];
						month++;
						if(month>12){ //����һ��
							month-=12;
							year++;
						}
					}else{
						day = day+tmp;
					}
				}else{
					if(day+tmp>days[0][month]){  //��������·ݵ���������ô�·�+1
						day = day+tmp-days[0][month];
						month++;
						if(month>12){ //����һ��
							month-=12;
							year++;
						}
					}else{
						day = day+tmp;
					}
				}
				//���µõ��������ӳ��ַ�������
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
				//���������뵱��������ȣ�������ڵ������ڣ����ʾ��û��ʱ����list����list1��;�����ʾ��ʱ����list2��
				if(newStr.compareTo(currentDate)>=0){
					list1.add(list.get(i));  //��������֮��ֱ��ת����״̬3 ����ʾ�黹��
				}else  //��������֮��ֱ��ת����4����ʾ�������
					list2.add(list.get(i));
			}
		}
		System.out.println("list1="+list1.size());
		System.out.println("list2="+list2.size());
		boolean res1=false;
		boolean res2=false;
		//��list1�е�������Ϣ״̬����2��Ϊ3����ʾ���黹
		for(int i=0;i<list1.size();i++)
		{
			res1=Service.modifyRentState2To3(list1.get(i).getId());
		}
		//��list2�е�������Ϣ״̬����2�ĵ�5��ʾ�������Ѻ�����δ����״̬����Ҫ��goodmsg�е���Ʒ�����ָ�
		for(int i=0;i<list2.size();i++)
		{
			System.out.println("!!!!!!!!!");
			//��״̬
			res2=Service.modifyRentState2To5(list2.get(i).getId());
			//�ָ�����
			res1=Service.addNumAfterTrade(list2.get(i).getGoodId(), list2.get(i).getRentNum());
		}
		map.put("list1",list1);
		map.put("list2",list2);
		return map;
	}
	
	
	
	/**
	 * ���Ե�ǰ�û�IDΪrentID��ѯ����״̬Ϊ2���ҳ�������ʱ��Ķ������޸�״̬Ϊ3
	 * @param rentId
	 * @return
	 */
	@RequestMapping("/modifyRentState2")
	@Transactional
	public @ResponseBody Map<String,Object> modifyRentState2(String rentId){
		//System.out.println("rentId="+rentId);
		System.out.println("modifyRentState2ִ��");
		Map<String,Object> map = new HashMap<String,Object>();
		int[][] days={{0,31,28,31,30,31,30,31,31,30,31,30,31},{0,31,29,31,30,31,30,31,31,30,31,30,31}};
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//�������ڸ�ʽ
		String currentDate = df.format(new Date()); //��ȡ��ǰʱ��
		//���״̬Ϊ3����ΪrentID��������Ϣ
		List<rentMsg> list1=new LinkedList<>();
		List<rentMsg> list = Service.findRent(rentId,3);  
		String dueDate=null;
		for(int i=0;i<list.size();i++)
		{ 
			//���ÿһ����Ϣ�ĵ���ʱ��
			dueDate = list.get(i).getDueTime();
			System.out.println("dueDate = "+dueDate);
			if(dueDate==null)
				continue;
			//��õ���ʱ������
			int year=0;
			int pos1 = dueDate.indexOf("-");  //�ҵ���һ�γ���-��λ�ã���ôǰ����Ӵ��ͱ�ʾ���
			year = Integer.parseInt(dueDate.substring(0,pos1));  //��ȡ��ݲ���ת��Ϊ����
			int pos3 = dueDate.lastIndexOf("-");  //�ҵ����һ�γ���-��λ�ã����� ������
			int day = Integer.parseInt(dueDate.substring(pos3+1));
			int month = Integer.parseInt(dueDate.substring(pos1+1,pos3));
			
			//�ȸ��������Ʒ��id��ѯ��黹�����Ǽ���
			int tmp = Service.queryReturnTime(list.get(i).getGoodId());
			if(tmp==0)
			{
				tmp=3;
			}else if(tmp==1){
				tmp=5;
			}else if(tmp==2){
				tmp=7;
			}
			//��������Ĺ��̼��ɼ��㵽�����ڼ��Ϲ黹���޺���ʲô����
			if(year%400==0 || (year%4==0 && year%100!=0 )){  //�����꣬ȡdays�������±�Ϊ1
				if(day+tmp>days[1][month]){  //��������·ݵ���������ô�·�+1
					day = day+tmp-days[1][month];
					month++;
					if(month>12){ //����һ��
						month-=12;
						year++;
					}
				}else{
					day = day+tmp;
				}
			}else{
				if(day+tmp>days[0][month]){  //��������·ݵ���������ô�·�+1
					day = day+tmp-days[0][month];
					month++;
					if(month>12){ //����һ��
						month-=12;
						year++;
					}
				}else{
					day = day+tmp;
				}
			}
			//���µõ��������ӳ��ַ�������
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
			//���������뵱��������ȣ����С�ڵ���������ô��˵���Ѿ������黹����
			if(newStr.compareTo(currentDate)<0){
				list1.add(list.get(i));  
			}
		}
		System.out.println("list1="+list1.size());
		boolean res1=false;
		//��list1�е�������Ϣ��3��Ϊ5��ʾ�������Ѻ�����δ����
		for(int i=0;i<list1.size();i++)
		{
			//��״̬
			res1=Service.modifyRentState3To5(list1.get(i).getId());
			//�ָ�����
			res1=Service.addNumAfterTrade(list1.get(i).getGoodId(), list1.get(i).getRentNum());
		}
		map.put("list1",list1);
		return map;
	}
	
	
	
	
	/**
	 * �Ե�ǰ�û�idΪ������id������״̬Ϊ0��������Ϣ(�ҳ���������Ϣ)
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
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
		}
		map.put("list",list);
		return map;
	}
	
	/**
	 * �Ե�ǰ�û�id��ΪreleaseId��ѯ״̬Ϊ7��3�Ķ�����7��ʾ�⻧�Ѿ��黹���������ջأ�3��ʾ�⻧���ڹ黹�У�
	 * @param releaseId
	 * @return
	 */
	@RequestMapping("/findWaitSend2")
	public @ResponseBody Map<String,Object> findWaitSend2(String releaseId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<rentMsg> list = Service.findWaitSend2(releaseId);
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
		}
		map.put("list",list);
		return map;
	}
	
	
	/**
	 * �Ե�ǰ�û�idΪ�ջ���id������״̬Ϊ0����1��������Ϣ(���ջ�����)
	 * @param rentId
	 * @return
	 */
	@RequestMapping("/findWaitReceive")
	public @ResponseBody Map<String,Object> findWaitReceive(String rentId){
		Map<String,Object> map = new HashMap<String,Object>();
		List<rentMsg> list = Service.findWaitReceive(rentId);
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
		}
		map.put("list",list);
		return map;
	}
	
	
	/**
	 * �����û����е�����ɶ�����������Ϊ�����ߵ����ж���
	 * @param userId
	 * @return
	 */
	@RequestMapping("/findFinish")
	public @ResponseBody Map<String,Object> findFinish(String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		//�ֳ������ֲ�ѯ
		//�Ȳ���Ϊ�����ߵ�������ɶ����������ۺ�δ���ۣ�
		List<rentMsg> list1 = Service.findFinishByRelease(userId);
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
		for(int i=0;i<list1.size();i++)
		{
			list1.get(i).setImage1("http://www.ZLofCampus.top/image/"+list1.get(i).getImage1());
		}
		//�ٲ���Ϊ�����ߵ����ж��������������ۺ�δ���ۣ�δ���۵Ŀ�����ҳ�������ۣ�
		List<rentMsg> list2 = Service.findFinishByRent(userId);
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
		for(int i=0;i<list2.size();i++)
		{
			list2.get(i).setImage1("http://www.ZLofCampus.top/image/"+list2.get(i).getImage1());
		}
		map.put("list1",list1);
		map.put("list2",list2);
		return map;
	}
	
	/**
	 * ��ѯ�û����ղ���Ϣ
	 * @param collectUserId
	 * @return
	 */
	@RequestMapping("/collectQuery")
	public @ResponseBody Map<String,Object> collectQuery(String collectUserId){
		Map<String,Object> map = new HashMap<String,Object>();
		List<collectMsg> list = Service.collectQuery(collectUserId);
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
		}
		map.put("list",list);
		return map;
	}
	
	
	/**
	 * ����idɾ���ղص���Ʒ
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
	 * ��ѯ��������
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
	 * ����ջ��������ջ�״̬1����Ϊ2������
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
	 * ����ջأ������ջ�״̬7����Ϊ4�������
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
	 * ����黹�������黹״̬3����Ϊ7���ջ�
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
	 * �����������������״̬0����Ϊ1���ջ�
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
	 * ������ɺ�,��״̬��4��Ϊ6��Ѻ��δ�����������
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
	 * ������ɺ�,��״̬��5��Ϊ8��Ѻ���ѿ����������
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
	 * ��ȡ�û�����
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getUserIntegration")
	public @ResponseBody Map<String,Object> getUserIntegration(String userId){
		System.out.println("��û���");
		System.out.println("userId="+userId);
		Map<String,Object> map = new HashMap<String,Object>();
		int UserIntegration=Service.getUserIntegration(userId);
		System.out.println("����="+UserIntegration);
		map.put("userIntegration", UserIntegration);
		return map;
	}
	
	
	/**
	 * ����������Ϣ��id��ȡ��������Ʒ��Ϣ���û��绰����
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
	 * ����������Ϣ��id��ȡ���ø���Ʒ���û��绰����
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
	 * �鿴�û��Ƿ���������Ϣ����д���ֻ�����
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
	 * ������Ʒ��������ҵ����������࣬�һ������õ���Ʒ��Ϣ
	 * @return
	 */
	@RequestMapping("/getHotGood")
	public @ResponseBody Map<String,Object> getHotGood(){
		//System.out.println("getHot");
		Map<String,Object> map = new HashMap<String,Object>();
		List<goodMsg> list = Service.getHotGood();
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
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
	 * ��ȡ���������������Ʒ��Ϣ
	 * @return
	 */
	@RequestMapping("/getNewGood")
	public @ResponseBody Map<String,Object> getNewGood(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<goodMsg> list = Service.getNewGood();
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
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
	 * ��ȡ�����������Ʒ����������Ʒ��Ϣ
	 * @return
	 */
	@RequestMapping("/getHotGroceries")
	public @ResponseBody Map<String,Object> getHotGroceries(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<goodMsg> list = Service.getHotGroceries();
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
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
	 * ��ȡ�����������ͼ��/���Ӳ�Ʒ/���й�����Ϣ
	 * @param type
	 * @return
	 */
	@RequestMapping("/getHot")
	public @ResponseBody Map<String,Object> getHot(int type){
		Map<String,Object> map = new HashMap<String,Object>();
		List<goodMsg> list = Service.getHot(type);
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
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
	 * ������ƷgoodId��ȡ��Ʒ��ϸ��Ϣ
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
	 * �ղ���Ʒ�����ղر�
	 * @param collectUserId
	 * @param collectGoodId
	 * @return
	 */
	@RequestMapping("/collectGood")
	public @ResponseBody Map<String,Object> collectGood(String collectUserId,int collectGoodId){
		Map<String,Object> map = new HashMap<String,Object>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//�������ڸ�ʽ
		String collectDate = df.format(new Date()); //��ȡ��ǰʱ��
		boolean res = Service.collectGood(collectUserId, collectGoodId,collectDate);
		map.put("msg",res);
		return map;
	}
	
	
	/**
	 * ��Ʒ�ղ�����1
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
	 * ȡ���ղ�(�����û�id����Ʒid)
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
	 * ��Ʒ�ղ�����1
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
	 * ������Ʒid���û�id�鿴���û��Ƿ��ղظ���Ʒ
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
	 * ������Ʒid��ȡ����
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
				list.get(i).setUserName("�����û�");
			}
		}
		*/
		map.put("commentList", list);
		return map;
	}
	
	
	/**
	 * ��ӹ��ﳵ
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
	 * �����û�id��ȡ�û��Ĺ��ﳵ����
	 * @param cartUserId
	 * @return
	 */
	@RequestMapping("/fingCartByUser")
	public @ResponseBody Map<String,Object> fingCartByUser(String cartUserId){
		Map<String,Object> map = new HashMap<String,Object>();
		List<cartMsg> list = Service.fingCartByUser(cartUserId);
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setImage1("http://www.ZLofCampus.top/image/"+list.get(i).getImage1());
		}
		map.put("cartList", list);
		return map;	
	}
			
	
	/**
	 * �ڹ��ﳵ���޸�cartNum
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
	 * �ڹ��ﳵ���޸�cartNum(��)
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
	 * �ڹ��ﳵ���޸�timeLen
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
	 * �ڹ��ﳵ���޸�timeLen(��)
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
	 * ���ݹ��ﳵ��Ϣ��id��ɾ��
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
	 * �û��ӹ��ﳵ�µ����޸���Ʒ��
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
	 * �������
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
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//�������ڸ�ʽ
		String date = df.format(new Date()); //��ȡ��ǰʱ��
		boolean res = Service.addComment(commentGoodId, content, degree, commentUserId, anonymous, date);
		map.put("msg",res);
		return map;
	}
	
	
	//�������۵ķ������Է����ߵĻ��ֽ����޸ģ�1���ǲ��ӷ֣�2-3���Ǽ�10�֣�4���Ǽ�20�֣�5���Ǽ�30�֣�
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
	 * ������Ʒid��������ɵ���Ʒ�����ָ�ԭ״
	 * @param goodId
	 * @param rentNum
	 * @return
	 */
	@RequestMapping("/addNumAfterTrade")
	public @ResponseBody Map<String,Object> addNumAfterTrade(int goodId,int rentNum){
		Map<String,Object> map = new HashMap<String,Object>();
		System.out.println("�ջ�֮����������  goodID="+goodId+"   rentNum="+rentNum);
		boolean res = Service.addNumAfterTrade(goodId, rentNum);
		map.put("msg",res);
		return map;
	}
	
	
	
	
	/**
	 * ��������id�޸����ޱ��е�rentDate��dueTime
	 * @param Id
	 * @param timeLen
	 * @param unit
	 * @return
	 */
	@RequestMapping("/modifyRentTime")
	public @ResponseBody Map<String,Object> modifyRentTime(int Id,int timeLen,int unit){
		
		int[][] days={{0,31,28,31,30,31,30,31,31,30,31,30,31},{0,31,29,31,30,31,30,31,31,30,31,30,31}};
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//�������ڸ�ʽ
		String rentDate = df.format(new Date()); //��ȡ��ǰʱ��(��Ϊ����ʱ��)
		
		//��ȡ��ǰ���
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
		int year = Integer.parseInt(df1.format(new Date())); 
				
		//��ȡ��ǰ�·�
		SimpleDateFormat df2 = new SimpleDateFormat("MM");
		int month = Integer.parseInt(df2.format(new Date())); 
				
		//��ȡ��ǰ��
		SimpleDateFormat df3 = new SimpleDateFormat("dd");
		int day = Integer.parseInt(df3.format(new Date())); 
				
		//�������õ�ʱ���͵�λ���㵽��ʱ��
		if(unit==0){  //�������
					
			if((year%100!=0 && year%4==0)|| year%400==0)//���������
			{
				if(day+timeLen>days[1][month]){ //������һ����
					day = day+timeLen-days[1][month];
					month+=1;
					if(month>12){ //������һ��
						month=month-12;
						year+=1;
					}
				}else{ //���������
					day = day+timeLen;
				}
			}else{ //��������
				if(day+timeLen>days[0][month]){ //������һ����
					day = day+timeLen-days[0][month];
					month+=1;
					if(month>12){ //������һ��
						month=month-12;
						year+=1;
					}
				}else{ //���������
					day = day+timeLen;
				}
			}
					
		}else{ //���¼���
			if(month+timeLen>12){ //������һ��
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
	 * ����Ʒ���뵽���ޱ���,������״̬Ϊ0
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
	
		System.out.println("���������Ϣ");
		System.out.println("remarks="+remarks);
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//�������ڸ�ʽ
		String payDate = df.format(new Date()); //��ȡ��ǰʱ��(��Ϊ����ʱ��)
		//����������mapper����д���ݿ����ʱ�����rentDate��dueTimeû��ֵ�Ļ�����null��''����û�а취ͨ������ģ���Ȼ����д����MySQL������ԣ����������ﻹ�Ǵ���һ��Ĭ�ϵ�0000-00-00��Ϊֵ
		boolean res = Service.addRentMsg(goodId, releaseId, rentId, payDate, rentNum,timeLen,remarks);
		map.put("msg",res);
		return map;
	}
	
	
	
	/**
	 * ���������key��goodmsg��ģ����ѯ�����Ա����֣���飬���飩
	 * @param key
	 * @return
	 */
	@RequestMapping("/searchByKey")
	public @ResponseBody Map<String,Object> searchByKey(String key){
		System.out.println("key="+key);
		int find1 = key.indexOf("�鼮");
		int find2 = key.indexOf("��");
		int find3 = key.indexOf("��");
		int find4 = key.indexOf("��"); 
		int find5 = key.indexOf("Ь");
		int find11 = key.indexOf("ȹ");
		int find6 = key.indexOf("��");
		int find7 = key.indexOf("��");
		int find8 = key.indexOf("��");
		int find9 = key.indexOf("��");
		int find10 = key.indexOf("��");
		int find12 = key.indexOf("��");
		int find13 = key.indexOf("Ʊ");
		int type=-1;
		if(find1!=-1 || find2!=-1)  //�鼮
		{
			type = 1;
		}else if(find3!=-1 || find4!=-1 || find5!=-1 || find11!=-1)  //����
		{
			type = 0;
		}else if(find6!=-1 || find7!=-1 || find8!=-1){   //����
			type = 2;
		}else if(find10!=-1 || find9!=-1){  //����
			type = 3;
		}else if(find12!=-1 || find13!=-1){  //�ӻ�
			type = 4;
		}
		System.out.println("type="+type);
		Map<String,Object> map = new HashMap<String,Object>();
		List<goodMsg> list = Service.searchByKey(key,type);
		//��ͼƬ��·��ƴ�Ӻã�������ǰ�˾�������������
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
	 * �����û�id�۳��û�����
	 * @param userId
	 * @return
	 */
	@RequestMapping("/subScoreById")
	public @ResponseBody Map<String,Object> subScoreById(String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean res = Service.subScoreById(userId);
		int score=-1;
		if(res==true){
			//�����²��������
			score = Service.getUserIntegration(userId);
		}
		map.put("msg",res);
		map.put("score",score);
		return map;
	}
	
	
	
	/**
	 * �����û�id�����û�idstateΪ1����ʾ���⣬Ĭ��Ϊ0
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
	 * ��������idɾ������
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
	 * ������ƷID������Ʒ��views��1
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
	 * �����û�userId������û���ǰ��û����������
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
	 * �����û���userId����û���΢���ǳƺ�ͷ�����usermsg
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
	 * ������Ʒ��goodID�ҵ������ߵĵ绰
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
	 * �Ȳ��ҵ�¼�ߣ�����userId��������δ�����ۣ�ͨ�����۱�����Ʒ�����飬��������Ʒ�����ߵ�id�������¼���Ƿ���ͬ�������ͬ�ǾͿ����Ƿ��Ѷ�����δ���Ķ��ҳ���
	 * ��ʵδ�����۵Ĳ�����Ӧ��Ҳ�������۱����Ѷ����Ǵ���δ���ظ�����Ϣ
	 * �����������˴��޸�Ϊ���������ж������ٶ�ÿһ����������δ���ظ��������δ���ظ�����ô����������Ϣ����¼δ���ظ��������û��δ���ظ��Ҹ����۱���Ϊ�Ѷ�����ô��ɾ��������Ϣ����controller����ɣ�
	 * @param userId
	 * @return
	 */
	@RequestMapping("/findNoReadComment")
	public @ResponseBody Map<String,Object> findNoReadComment(String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		//�������۱���δ�����Ѷ�����Ϣ
		List<commentMsg> list = Service.findNoReadComment(userId);
		System.out.println("noReadlist1="+list.size());
		//��ÿһ����Ϣ���в���δ���ظ�
		for(int i=0;i<list.size();)
		{
			//��ÿһ�����۲��Ҵ��ڵ�δ���ظ�
			List<replyMsg> replyList = Service.findNoReadReply(list.get(i).getCommentId(),userId);
			System.out.println("replyList="+replyList.size());
			list.get(i).setReplyNoRead(replyList.size());  //����δ���Ļظ���
			if(replyList.size()>0){
				//��������һ�����͸�userId��δ���ظ������ҵ������ظ���Id�����ҵ������ظ�����Ϣ
				int id = Service.findreplyId(userId);
				replyMsg last = Service.findLastReply(id);
				
				//�ҷ����ߵ���Ϣ
				userMsg user = Service.findUserMsgById(last.getReplyId());
				//���÷����ߵ�ͷ���ǳƺ���������list(i)��
				last.setAvatarUrl_from(user.getAvatarUrl());
				last.setNickName_from(user.getNickName());
				last.setUserName_from(user.getUserName());
				last.setUserId_from(user.getUserId());
				//�һظ��������Ϣ
				userMsg user2 = Service.findUserMsgById(last.getReplyToId());
				//���÷���������ǳƺ�����
				last.setNickName_to(user2.getNickName());
				last.setUserName_to(user2.getUserName());
				last.setUserId_to(user2.getUserId());
				
				//�����һ���ظ�����Ϣ������������
				list.get(i).setLastReplyContent(last);
			}
			
			if(replyList.size()<=0 && list.get(i).getIsRead()==1)  //���۱����Ѷ�����û��δ���ظ�����ôɾ��������Ϣ
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
	 * ����ͷ����ҵ���Ӧ���û�����Ϊ΢���ڽ���ͷ��洢ʱ��ÿ��ͷ��ĵ�ַһ����Ψһ�ģ����Կ����ҵ�Ψһ���û���
	 * �ҵ�����û��󣬷�������û������и�����Ϣ���ʹ��û�������������Ʒ��Ϣ���ٸ�����Щ��Ʒ��Ϣ���ҳ��������ۣ�ͳ�������������������������ó�������
	 * @param avat
	 * @return
	 */
	@RequestMapping("/findHomeMsg")
	public @ResponseBody Map<String,Object> findHomeMsg(String avat){
		Map<String,Object> map = new HashMap<String,Object>();
		userMsg user = Service.findHomeMsg(avat);
		//�����û�id�����û������з�����Ʒ
		List<goodMsg> list = Service.showAllRelease(user.getUserId());
		//�ٸ���ÿ����Ʒ��id���Ҷ�Ӧ���ۣ�ͳ�������������ͺ�����
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
	 * ��������commentID�޸��Ѷ�
	 * @param commentId
	 * @return
	 */
	@RequestMapping("/modifyCommentRead")
	public @ResponseBody Map<String,Object> modifyCommentRead(int commentId,String userId){
		System.out.println("commentId="+commentId);
		Map<String,Object> map = new HashMap<String,Object>();
		//�ֽ����������޸�Ϊ�Ѷ�
		boolean res = Service.modifyCommentRead(commentId);
		//����һ��������������û��δ���ظ������򽫻ظ�Ҳ��Ϊ�Ѷ���û�о�ʲôҲ����
		List<replyMsg> replyList = Service.findNoReadReply(commentId,userId);
		int flag=0;
		if(replyList.size()>0){ //��δ���ظ�
			//��δ���ظ�����Ϊ�Ѷ�
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
	 * �����Ѷ����ۣ����ݵ�¼�ߵ�id�����ҳ������˶��ڵ�½�߷�������Ʒ�����ۣ����ҵ�½���Ѿ��Ķ�����
	 * ������ҳ��������ۣ�Ҫ��֤���Իظ�Ҳ���Ѷ���
	 * �����Ȳ���������Ѷ����ۣ���controller���ٶԲ����ÿһ�����ۣ��鿴��û��δ���ظ����������ô�ʹ�list��ɾ��������Ϣ
	 * @param userId
	 * @return
	 */
	@RequestMapping("/findALLComment")
	public @ResponseBody Map<String,Object> findALLComment(String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		//�������еĶԵ�½�߷�����Ʒ���Ѷ�����
		List<commentMsg> list = Service.findALLComment(userId);
		System.out.println("readList1="+list.size());
		//���β鿴�Ѷ������Ƿ���δ���ظ�
		for(int i=0;i<list.size();){
			//���Ҹ����������Ƿ���δ���ظ�
			List<replyMsg> replyList = Service.findNoReadReply(list.get(i).getCommentId(),userId);
			
			//���replyList��С����0����ô��˵����δ���ظ����ͽ�����������Ϣ��list��ɾ��(������ʵӦ���ȼ���Ҫɾ�����±꣬�ȳ���ѭ����ɾ������Ȼһɾ��list�Ĵ�СҲ����ű䣬��ô���ܾͻ�����Ϣ���жϲ�����)
			if(replyList.size()>0){
				list.remove(i);
				continue;
			}else{    //˵��û��δ���ظ���ֻ���Ѷ��ظ�����û�лظ�
				//���Ҹ��������µĻظ�����������commentID���ң����ﲻ�������ǲ��Ƿ�����½�ߵģ�
				int num = Service.replyNum(list.get(i).getCommentId());
				list.get(i).setReplyTotal(num);
				if(num>0)  //˵������list[i]���лظ�
				{
					//��ô�Ͳ�����һ���ظ���Ϣ�����뵽list��lastReplyContent��
					//����commentID���в���
					replyMsg last = Service.findLastReply2(list.get(i).getCommentId());
					//�ҷ����ߵ���Ϣ
					userMsg user = Service.findUserMsgById(last.getReplyId());
					//���÷����ߵ�ͷ���ǳƺ���������list(i)��
					last.setAvatarUrl_from(user.getAvatarUrl());
					last.setNickName_from(user.getNickName());
					last.setUserName_from(user.getUserName());
					last.setUserId_from(user.getUserId());
					//�һظ��������Ϣ
					userMsg user2 = Service.findUserMsgById(last.getReplyToId());
					//���÷���������ǳƺ�����
					last.setNickName_to(user2.getNickName());
					last.setUserName_to(user2.getUserName());
					last.setUserId_to(user2.getUserId());
					
					//���ø������۵����һ���ظ�
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
	 * �Ȳ����������Ʒ��Ϣ���̼���Ϣ(�����controller���ú�����)���ٲ������Ʒ�ķ�����Ϣ
	 * @param goodId
	 * @return
	 */
	@RequestMapping("/queryGood")
	public @ResponseBody Map<String,Object> queryGood(int goodId){
		System.out.println("��ѯ��Ʒ������goodId="+goodId);
		Map<String,Object> map = new HashMap<String,Object>();
		//�����Ʒ������Ϣ
		goodMsg good = Service.queryGood(goodId);  //������ҳ�����Ʒ��Ϣ�󣬻�Ӧ���޸�image��·������֤ҳ�����ܹ����ʵ�
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
		//�����Ʒ�����ߵ���Ϣ
		userMsg user = Service.queryGoodUser(goodId);
		map.put("usermsg",user);
		return map;
	}
	
	
	
	/**
	 * ��������id��������
	 * @param commentId
	 * @return
	 */
	@RequestMapping("/findCommentById")
	public @ResponseBody Map<String,Object> findCommentById(int commentId){
		Map<String,Object> map = new HashMap<String,Object>();
		commentMsg res = Service.findCommentById(commentId);
		map.put("commentmsg", res);
		//�ٸ���commentID���������ظ�
		List<replyMsg> list = Service.findReply(commentId);
		//��ÿһ���ظ����ֱ�������ظ��ķ����ߣ��ͽ����ߵ���Ϣ
		for(int i=0;i<list.size();i++){
			//�ҷ����ߵ���Ϣ
			userMsg user = Service.findUserMsgById(list.get(i).getReplyId());
			//���÷����ߵ�ͷ���ǳƺ���������list(i)��
			list.get(i).setAvatarUrl_from(user.getAvatarUrl());
			list.get(i).setNickName_from(user.getNickName());
			list.get(i).setUserName_from(user.getUserName());
			list.get(i).setUserId_from(user.getUserId());
			//�һظ��������Ϣ
			userMsg user2 = Service.findUserMsgById(list.get(i).getReplyToId());
			//���÷���������ǳƺ�����
			list.get(i).setNickName_to(user2.getNickName());
			list.get(i).setUserName_to(user2.getUserName());
			list.get(i).setUserId_to(user2.getUserId());
		}
		map.put("replyList", list);
		return map;
	}
	
	
	/**
	 * ��ӻظ�
	 * �ȸ���replyComment���ҵ����������´������Ļظ��Ĵ����
	 * @param replyComment
	 * @param replyId
	 * @param replyToId
	 * @param replyContent
	 * @return
	 */
	@RequestMapping("/addReply")
	public @ResponseBody Map<String,Object> addReply(int replyComment,String replyId,String replyToId,String replyContent){
		Map<String,Object> map = new HashMap<String,Object>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//�������ڸ�ʽ
		String replyDate = df.format(new Date()); //��ȡ��ǰʱ��(��Ϊ����ʱ��)
		//����replyComment���ҵ����������´������Ļظ��Ĵ����
		Integer sequence=0;
		sequence = Service.findMaxSequence(replyComment);
		if(sequence==null)
		{
			sequence=1;
		}else
			sequence+=1;
		//��ӻظ�
		boolean res = Service.addReply(replyComment, replyId, replyToId, replyContent, replyDate, sequence);
		map.put("msg", res);
		return map;
	}
	
	
	/**
	 * ɾ���ظ������ݻظ����id
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
	 * ������Ʒid���ͶԻ�˫����id���Ȳ�ѯ����Ϣ�������������Ϣ����ô���ٲ�ѯ������������Ϣ����������ŵ�������
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
		//�Ȳ�ѯ����Ϣ
		chatMsg list = Service.queryChat(chatGoodId, chatA, chatB);
		if(list!=null)   //˵����������Ϣ����ô����������Ϣ��id��ѯ������������Ϣ,����������Ϣ��һ�𷵻�
		{
			if(list.getIsImage()==1)
				list.setChatContent("http://www.ZLofCampus.top/image/"+list.getChatContent());
			List<afterChatMsg> after = Service.queryChatAfter(list.getId());
			if(after.size()>0)  //�������ظ�
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
	 * �������������Ϣ
	 * ����goodid��openId����ΪchatRightId����usermsg.userId����ΪchatLeftId)���������Ϣ
	 * ���������Ϣʱ��Ҫ�Ȳ�����û����������Ϣ���������������Ϣ����ô������idΪ����������������Ϣ
	 * ���û����������Ϣ����ô����Ӵ���Ϊ��������Ϣ
	 * @param chatGoodId
	 * @param chatRightId
	 * @param chatLeftId
	 * @return
	 */
	@RequestMapping("/addChat")
	public @ResponseBody Map<String,Object> addChat(int chatGoodId,String chatRightId,String chatLeftId,String chatContent){
		Map<String,Object> map = new HashMap<String,Object>();
		//��õ�ǰ����
		Date day=new Date();    
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String chatDate=df.format(day);
		//��ȡ��ǰʱ��
		int pos = chatDate.indexOf(' ');
		String chatTime=chatDate.substring(pos+1);
		
		//�Ȳ����û����������Ϣ��������Ʒid��˫��id
		String chatA=chatRightId;
		String chatB=chatLeftId;
		chatMsg list = Service.queryChat(chatGoodId, chatA, chatB);
		boolean res=false;
		if(list==null){  //˵��û����������Ϣ����ô�������������Ϣ
			res = Service.addChat(chatGoodId, chatRightId, chatLeftId,chatDate,chatContent,chatTime);
		}else{  //����������Ϣ����ô���������������Ϣ
			//����������Ϣ��isShow����Ϊ1
			Service.updateShow(list.getId());
			res = Service.addChatAfter(list.getId(), chatRightId, chatLeftId, chatContent, chatDate, chatTime);
		}
		map.put("msg", res);
		return map;
	}
	
	
	/**
	 * ���ͼƬ������Ϣ
	 * ����goodid��openId����ΪchatRightId����usermsg.userId����ΪchatLeftId)���������Ϣ
	 * ���������Ϣʱ��Ҫ�Ȳ�����û����������Ϣ���������������Ϣ����ô������idΪ����������������Ϣ
	 * ���û����������Ϣ����ô����Ӵ���Ϊ��������Ϣ
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
	    
	    
	    String path = null;   //ͼƬ·��
	    String trueFileName= null;
	    
	    System.out.println("file="+file);
	    if(!file.isEmpty()) {  //������ļ����գ����ȡ��ͼƬ
	    	
	    	System.out.println("�ɹ���ȡ��Ƭ");
	    	
	    	//��ȡͼƬ��ʼ����
            String fileName = file.getOriginalFilename();  
            System.out.println("fileName="+fileName);
            
            String type = null;
            
            //��ȡͼƬ����
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            
            System.out.println("ͼƬ��ʼ����Ϊ��" + fileName + " ����Ϊ��" + type);
            
            if (type != null) {
            	//����Ƿ����Ҫ���ͼƬ����
            	if ("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())) 
            	{
            		// ��Ŀ��������ʵ�ʷ������еĸ�·��
            		String realPath = request.getSession().getServletContext().getRealPath("/");
            		// �Զ�����ļ�����
            		trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;
            		int pos = trueFileName.lastIndexOf(".");
            		
            		System.out.println("pos="+pos);
            		String temp = trueFileName.substring(0, pos);
            		System.out.println("substr="+temp);
            		trueFileName = temp+".jpg";
            		
            		
            		// ���ô��ͼƬ�ļ���·��
            		path = realPath + "/uploads/" + trueFileName;
            		
            		System.out.println("trueFileName=="+trueFileName);
            		
            		System.out.println("���ͼƬ�ļ���·��:" + path);
            		
            		file.transferTo(new File(path));
            		System.out.println("�ļ��ɹ��ϴ���ָ��Ŀ¼��");
            		map.put("upload","�ļ��ɹ��ϴ���ָ��Ŀ¼��");
            		
            	}else {
            		System.out.println("����������Ҫ���ļ�����,�밴Ҫ�������ϴ�");
            		map.put("upload", "error:�ļ����Ͳ�����,�밴Ҫ�������ϴ�");
            		return map;
            	}
            }else {
            	System.out.println("�ļ�����Ϊ��");
            	map.put("upload", "error:�ļ�����Ϊ��");
            	return map;
            }
	    }else {
	    	System.out.println("û���ҵ����Ӧ���ļ�");
	    	map.put("upload", "error:û���ҵ����Ӧ���ļ�");
	    	return map;
	    }
	    
		//��õ�ǰ����
		Date day=new Date();    
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String chatDate=df.format(day);
		//��ȡ��ǰʱ��
		int pos = chatDate.indexOf(' ');
		String chatTime=chatDate.substring(pos+1);
		
		//�Ȳ����û����������Ϣ��������Ʒid��˫��id
		String chatA=chatRightId;
		String chatB=chatLeftId;
		chatMsg list = Service.queryChat(chatGoodId, chatA, chatB);
		boolean res=false;
		if(list==null){  //˵��û����������Ϣ����ô�������������Ϣ
			res = Service.addChatImage(chatGoodId, chatRightId, chatLeftId,chatDate,trueFileName,chatTime);
		}else{  //����������Ϣ����ô���������������Ϣ
			res = Service.addChatAfterImage(list.getId(), chatRightId, chatLeftId,trueFileName, chatDate, chatTime);
		}
		map.put("msg", res);
		return map;
	}
	
	
	//����openId����������Ϣ��֮����������
	/**
	 * �Ȳ�����������¼��chatRightId = opened ���� chatLeftId= openid��������Ϣ���漰��������Ʒ�����м�����ͬ����Ʒ�ţ�
	 * Ȼ�������Щ��Ʒ�ź�openId���Ȳ��ÿһ����Ʒ��Ӧ��һ��������Ϣ��������������ţ���
	 * Ȼ��ѭ���ж�list����û�� chatLeftId=openid ����isRead=0��������Ϣ������У���ô��˵����δ����Ϣ����¼�����δ����Ϣ���������Լ����һ��δ����Ϣ
	 * ���û��δ����Ϣ����ô��ֻ�ǵ����ļ�¼�����һ����Ϣ
	 * @param userId
	 * @return
	 */
	@RequestMapping("/findAllChat")
	public @ResponseBody Map<String,Object> findAllChat(String openId){
		Map<String,Object> map = new HashMap<String,Object>();
		//�Ȳ���������
		List<chatMsg> list1 = Service.findMainChat(openId);
		for(int i=0;i<list1.size();i++){  //��ÿһ�������Բ�ѯ������������Ϣ
			//������������Ա���δ������ô�ͽ�noRead����Ϊ1������Ϊ0
			if(list1.get(i).getIsRead()==0 && list1.get(i).getChatLeftId().equals(openId))
			{
				list1.get(i).setNoRead(1);
			}else{
				list1.get(i).setNoRead(0);
			}
			if(list1.get(i).getIsImage()==1)  //��ͼƬ��Ϣ����content���д���
			{
				list1.get(i).setChatContent("http://www.ZLofCampus.top/image/"+list1.get(i).getChatContent());
				
			}
			
			//����������id��ѯ������������������������
			List<afterChatMsg> list2 = Service.queryChatAfter(list1.get(i).getId());
			
			if(list2.size()>0){  //��������������Ϣ
				//��ȡδ��������Ϣ�����Լ����һ������������Ϣ
				int len =list2.size();
				int sum=0;
				//��ȡδ��������
				for(int j=0;j<len;j++)
				{
					if(list2.get(j).getIsRead()==0 && list2.get(j).getChatLeftIdA().equals(openId))  //ע�⣺���ڵ�½����˵��δ��������ָ�ɵ�½�߽��յ�δ�����ԣ���ô��ʱ��½��Ӧ����ΪchatLeftID����
						sum++;
					if(list2.get(j).getIsImage()==1)  //��ͼƬ��Ϣ,��ÿһ��ͼƬ��Ϣcontent����
						list2.get(j).setChatContent("http://www.ZLofCampus.top/image/"+list2.get(j).getChatContent());
					
				}
				list1.get(i).setNoRead(list1.get(i).getNoRead()+sum);  //��δ���������������뵽��������Ϣ��δ����������
				//��ȡ���һ����������
				list1.get(i).setLastChat(list2.get(len-1));
				list1.get(i).setAfterChatList(list2);
			}//û������������Ϣ��ʲôҲ����
			
			
			//����Ҫ�����ÿ���������У��Է��������Ϣ
			if(!list1.get(i).getChatRightId().equals(openId)){   //���ChatRightId�����Լ���id��ô���ǶԷ���id���������id��ѯ�û���Ϣ
				
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
		map.put("allChat",list1);  //����Ϣ����
		return map;
	}
	
	
	/**
	 * ������������Ϊ�Ѷ�(���������Ե�id�������������Ժ��������Ծ�����Ϊ�Ѷ�)
	 * ���������Ѷ�ʱ��һ��Ҫע����ݵ�¼�ߵ�id��chatLeftID=openId��δ��������Ϊ�Ѷ����ɣ�����Ҫ�޸ĶԷ���δ��������Ϣ
	 * @param Id
	 * @param len  ������ʾ������������û����������
	 * @return
	 */
	@RequestMapping("/setAllChatRead")
	public @ResponseBody Map<String,Object> setAllChatRead(int Id,int len,String openId){
		//System.out.println("setAllChatRead");
		//System.out.println("len="+len);
		
		Map<String,Object> map = new HashMap<String,Object>();
		//����Ҫ���ж�һ�������Ե�chatLeftId�Ƿ���openId��Ȼ���ٽ����޸ģ��������Ϊû�з��������������Խ����޸ĵ��·���ֵΪfalse��Ӱ����������ת
		//����id������������Ϣ
		chatMsg chat = Service.findMainChatById(Id);
		boolean res1=false;
		if(chat.getChatLeftId().equals(openId) && chat.getIsRead()==0){
			//�ֽ������Ա����Ϊ�Ѷ�
			res1 = Service.setMainRead(Id,openId);
		}else{  //����Ͳ���Ҫ�ĵ������ԣ���ôֱ����Ϊtrue
			res1 = true;
		}
		//�ٽ��������Ը�Ϊ�Ѷ�
		boolean res2 = false;
		if(len>0){  //�����lenֻ��˵�������������ԣ�������Щ����������û��δ���ľͲ�һ���ˣ�������Ѷ�����ô����ֵ�϶�Ϊ0������false
			System.out.println("����1");
			//�����ȸ���������id��ѯ�����е��������ԣ�
			//Ȼ����һ��һ���ж�
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
			if(sum>0)  //˵����δ����
				res2=Service.setAfterRead(Id,openId);
			else  //û��δ������������
				res2=true;  //ֱ������Ϊtrue
		}
		else{
			System.out.println("����2");
			res2=true;
		}
		//System.out.println("res2="+res2);
		map.put("msg1", res1);
		map.put("msg2", res2);
		return map;
	}
	
	
	/**
	 * ����������Ϣ����ʾ
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
	 * ������Ʒid���ҷ���������ѧУ
	 * @param goodId
	 * @return
	 */
	@RequestMapping("/findSchool")
	public @ResponseBody Map<String,Object> findSchool(int goodId){
		System.out.println("findSchool_goodId="+goodId);
		Map<String,Object> map = new HashMap<String,Object>();
		String res = Service.findSchool(goodId);
		if(res==null)
			res="���û�δ��дѧУ��Ϣ��";
		
		System.out.println("school="+res);
		
		map.put("school",res);
		return map;
	}
	
	
	/**
	 * ����userId����ͷ���ǳƣ�����
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
	 * ��ӹ�ע
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
	 * ȡ����ע
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
	 * ��ѯ��ע״̬
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
	 * �����û�id���Ҹ��û������й�ע
	 * @param followId
	 * @return
	 */
	@RequestMapping("/findFollow")
	public @ResponseBody Map<String,Object> findFollow(String followId){
		Map<String,Object> map = new HashMap<String,Object>();
		List<followMsg> list = Service.findFollow(followId);
		userMsg res=null;
		for(int i=0;i<list.size();i++)  //����ÿ������ע�ߵ�id���Ҹ�����Ϣ
		{
			res = Service.findUserMsgById(list.get(i).getFollowToId());
			list.get(i).setUser(res);
		}
		map.put("list", list);
		return map;
	}
	
	
	/**
	 * ������Ʒid�޸���Ʒ��Ϣ
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
	 * ��ȡ�û���userId(openid)
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
		 * ����String encryptedData, String iv���������ڽ��ܵ�
		 */
		
        Map<String,Object> map = new HashMap<String,Object>();
        
        //��¼ƾ֤����Ϊ��
        if (code == null || code.length() == 0) {
            map.put("status", 0);
            map.put("msg", "code ����Ϊ��");
            //System.out.println("map1:" + map);
            return map;
        }
        
        //С����Ψһ��ʶ   (��΢��С��������̨��ȡ)
        String wxspAppid = "wx462342c6fb47c998";  //����Ӧ���ǰС������Ŀ��AppId����С����ʱҪע��ȶԣ�����
        
        //С����� app secret (��΢��С��������̨��ȡ)
        String wxspSecret = "5caf8a23ee1b27c7d2dadc200e6c0be2";  //���������Լ���
        
        //��Ȩ�����
        String grant_type = "authorization_code";  //������Ҫ�Լ����𣿣�������
        
        //////////////// 1����΢�ŷ����� ʹ�õ�¼ƾ֤ code ��ȡ session_key �� openid ////////////////
        //�������
        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type=" + grant_type;
        
        //��������
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        
        //������Ӧ���ݣ�ת����json����
        JSONObject json = JSONObject.parseObject(sr);
        
        //��ȡ�Ự��Կ��session_key��
        String session_key = json.get("session_key").toString();
        
        //�û���Ψһ��ʶ��openid��
        String openid = (String) json.get("openid");
        
        map.put("session_key", session_key);
        map.put("openId", openid);
       
        return map;
    }
	
	
	
}
