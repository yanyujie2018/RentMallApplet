package com.qd.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.qd.dao.operation;

@Service
public class service {
	@Autowired
	private operation op;
	
	//添加用户信息，根据返回的影响函数判断成功与否
	public boolean addUserMsg(String userId){
		int res = op.addUserMsg(userId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//查询用户信息
	public userMsg findUserMsgById(String userId)
	{
		userMsg res = op.findUserMsgById(userId);
		return res;
	}
	
	//修改用户性别
	public boolean modiyfSexById(String userId,int sex){
		int res = op.modiyfSexById(userId, sex);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//修改用户生日
	public boolean modiyfBirthById(String userId,String birthday)
	{
		int res = op.modiyfBirthById(userId, birthday);
		if(res>0)
			return true;
		else 
			return false;
	}
	
	//修改用户基本信息（学校，生日，微信号，身份证）
	public boolean modifyBaseById(String userId,String idCard,String school,String wxNum,String userName)
	{
		int res = op.modifyBaseById(userId, idCard, school, wxNum, userName);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//分开修改基本信息
	//修改姓名
	public boolean modifyNameById(String userId,String userName)
	{
		int res = op.modifyNameById(userId, userName);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//修改身份证
	public boolean modifyIdCardById(String userId,String idCard){
		int res = op.modifyIdCardById(userId, idCard);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//修改微信号
	public boolean modifyWxNumById(String userId,String wxNum){
		int res = op.modifyWxNumById(userId, wxNum);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//修改学校
	public boolean modifySchoolById(String userId,String school){
		int res = op.modifySchoolById(userId, school);
		if(res>0)
			return true;
		else
			return false;
	}
	
	
	//修改手机号绑定
	public boolean modifyPhoneById(String userId,String phone){
		int res = op.modifyPhoneById(userId, phone);
		if(res>0)
			return true;
		else
			return false;
	}
	
	/*
	//添加商品信息（除图片）
	public int addGoodMsg(String userId,String goodName,String introduction,String goodDetail,int type,int unit,int deposit,int deadline,double price,String releaseDate){
		int res = op.addGoodMsg(userId, goodName, introduction, goodDetail, type, unit, deposit, deadline, price,releaseDate);
		return res;
	}
	*/
	public int addGoodMsg(goodMsg good){
		int res = op.addGoodMsg(good);
		return res;
	}
	
	// 添加商品图片
	public boolean addGoodImage1(int goodId,String imagePath){
		int res=op.addGoodImage1(goodId, imagePath);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	
	// 添加商品图片
	public boolean addGoodImage2(int goodId,String imagePath){
		int res=op.addGoodImage2(goodId, imagePath);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
		
		
	// 添加商品图片
	public boolean addGoodImage3(int goodId,String imagePath){
		int res=op.addGoodImage3(goodId, imagePath);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//查询所有发布信息
	public List<goodMsg> showAllRelease(String userId){
		return op.showAllRelease(userId);
	}
	
	//根据发布的商品ID删除发布的商品
	public boolean deleteGoodById(int goodId){
		int res = op.deleteGoodById(goodId);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//查询租用中的订单（包括出租中和租用中）
	public List<rentMsg> findRent(String rentId,int rentState){
		return op.findRent(rentId,rentState);
	}
	
	
	//查询待归还的订单 
	public List<rentMsg> findWaitReturn(String rentId,int rentState){
		return op.findRent(rentId,rentState);
	}
	
	//以当前用户id为发货者id，查找状态为0的租赁信息
	public List<rentMsg> findWaitSend(String releaseId,int rentState){
		return op.findWaitSend(releaseId, rentState);
	}
	
	
	//以当前用户id为发货者id，查找状态为0的租赁信息
	public List<rentMsg> findWaitSend2(String releaseId){
		return op.findWaitSend2(releaseId);
	}
	
	//以当前用户id为收货者id，查找状态为0或者1的租赁信息(待收货订单)
	public List<rentMsg> findWaitReceive(String rentId){
		return op.findWaitReceive(rentId);
	}
	
	//查找用户所有的已完成订单（包括作为租用者的所有订单
	public List<rentMsg> findFinishByRent(String userId)
	{
		return op.findFinishByRent(userId);
	}
	
	//查找用户所有的已完成订单（包括作为租用者的所有订单
	public List<rentMsg> findFinishByRelease(String userId)
	{
		return op.findFinishByRelease(userId);
	}
	
	//先以当前用户ID为rentID查询订单状态为2并且超过租赁时间的订单，修改状态为3
	public boolean modifyRentState(String rentId,String currentDate){
		int res = op.modifyRentState(rentId,currentDate);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//获取收藏数据
	public List<collectMsg> collectQuery(String collectUserId){
		return op.collectQuery(collectUserId);
	}
	
	//根据id删除收藏的商品
	public boolean deleteCollect(int id){
		int res = op.deleteCollect(id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//查询订单详情
	public rentDetails QueryRentDetail(int Id){
		return op.QueryRentDetail(Id);
	}
	
	
	//点击收货，将待收货状态1，改为2租用中
	public boolean modifyRentState1To2(int Id){
		int res = op.modifyRentState1To2(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//点击收回，将待收回状态7，改为4交易完成
	public boolean modifyRentState7To4(int Id){
		int res = op.modifyRentState7To4(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//点击归还，将待归还状态3，改为7待收回
	public boolean modifyRentState3To7(int Id){
		int res = op.modifyRentState3To7(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//点击发货，将待发货状态0，改为1待收货
	public boolean modifyRentState0To1(int Id){
		int res = op.modifyRentState0To1(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	
	//评论完成后,将状态由4改为6
	public boolean modifyRentState4To6(int Id){
		int res = op.modifyRentState4To6(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//将状态从租用中2改为归还中3
		public boolean modifyRentState2To3(int Id){
			int res = op.modifyRentState2To3(Id);
			if(res>0)
			{
				return true;
			}else
				return false;
		}
		
	//将状态从租用中2改为已完成5
	public boolean modifyRentState2To5(int Id){
		int res = op.modifyRentState2To5(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//将状态从归还中3改为已完成5
	public boolean modifyRentState3To5(int Id){
		//System.out.println("service3to5");
		int res = op.modifyRentState3To5(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//将状态从已完成押金扣留5改为已评价押金扣留8
	public boolean modifyRentState5To8(int Id){
		int res = op.modifyRentState5To8(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//获取用户积分
	public int getUserIntegration(String userId){
		return op.getUserIntegration(userId);
	}
	
	//根据租赁信息的id获取发布该商品信息的用户电话号码
	public String getReleasePhone(int Id){
		return op.getReleasePhone(Id);
	}
	
	//根据租赁信息的id获取租用该商品的用户电话号码
	public String getRentPhone(int Id){
		return op.getRentPhone(Id);
	}
	
	//查看用户是否已完善信息，填写了手机号码
	public String checkPhone(String userId){
		return op.checkPhone(userId);
	}
	
	
	//根据商品浏览次数找到浏览次数最多，且还能租用的商品信息
	public List<goodMsg> getHotGood(){
		return op.getHotGood();
	}
	

	//获取最近发布的三条商品信息
	public List<goodMsg> getNewGood(){
		return op.getNewGood();
	}
	
	//获取浏览最多的日用品或其他类商品信息
	public List<goodMsg> getHotGroceries(){
		return op.getHotGroceries();
	}
	
	//获取浏览最多的三条图书/电子产品/出行工具信息
	public List<goodMsg> getHot(int type){
		return op.getHot(type);
	}
	
	//根据商品goodId获取商品详细信息
	public goodMsg getGoodDetail(int goodId){
		return op.getGoodDetail(goodId);
	}
	
	//收藏商品进入收藏表
	public boolean collectGood(String collectUserId,int collectGoodId,String collectDate){
		int res = op.collectGood(collectUserId, collectGoodId,collectDate);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//商品收藏数加1
	public boolean addCollect(int goodId){
		int res = op.addCollect(goodId);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//取消收藏(根据用户id和商品id)
	public boolean cancelcollect(String collectUserId,int collectGoodId){
		int res = op.cancelcollect(collectUserId, collectGoodId);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//商品收藏数减1
	public boolean subCollect(int goodId){
		int res = op.subCollect(goodId);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//根据商品id和用户id查看该用户是否收藏该商品
	public boolean findCollect(String collectUserId,int collectGoodId){
		collectMsg res = op.findCollect(collectUserId, collectGoodId);
		if(res!=null){
			return true;
		}else
			return false;
	}
	
	//根据商品id获取评论
	public List<commentMsg> findComment(int commentGoodId){
		return op.findComment(commentGoodId);
	}
	
	//添加购物车
	public boolean addCart(String cartUserId,int cartGoodId,int cartNum,int timeLen){
		int res = op.addCart(cartUserId, cartGoodId, cartNum, timeLen);
		if(res>0){
			return true;
		}else
			return false;
	}
		
	
	//根据用户id获取用户的购物车内容
	public List<cartMsg> fingCartByUser(String cartUserId){
		return op.fingCartByUser(cartUserId);
	}
	
	//在购物车中修改cartNum
	public boolean modifyCartNumAdd(int cartId){
		int res = op.modifyCartNumAdd(cartId);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	//在购物车中修改cartNum
	public boolean modifyCartNumSub(int cartId){
		int res = op.modifyCartNumSub(cartId);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	
	//根据购物车信息的id号删除
	public boolean deleteCartById(int cartId){
		int res = op.deleteCartById(cartId);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	//在购物车中修改timeLen
	public boolean modifyCartTimeAdd(int cartId){
		int res = op.modifyCartTimeAdd(cartId);
		if(res>0){
			return true;
		}else
			return false;
	}
		
	//在购物车中修改timeLen
	public boolean modifyCartTimeSub(int cartId){
		int res = op.modifyCartTimeSub(cartId);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	
	//将商品插入到租赁表中,并设置状态为0
	public boolean addRentMsg(int goodId,String releaseId,String rentId,String payDate,int rentNum,int rentLen,String remarks){
		int res = op.addRentMsg(goodId, releaseId, rentId, payDate, rentNum,rentLen,remarks);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	
	//用户从购物车下单后修改商品表
	public boolean modifyGoodMsg(int goodId,int cartNum){
		int res = op.modifyGoodMsg(goodId, cartNum);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	
	//添加评论
	public boolean addComment(int commentGoodId,String content,int degree,String commentUserId,int anonymous,String date){
		int res = op.addComment(commentGoodId, content, degree, commentUserId, anonymous, date);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	
	//根据评价的分数，对发布者的积分进行修改（1颗星不加分，2-3颗星加10分，4颗星加20分，5颗星加30分）
	public boolean modifyReleaseScore(String userId,int add){
		int res = op.modifyReleaseScore(userId, add);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	
	//根据商品id将交易完成的商品数量恢复原状
	public boolean addNumAfterTrade(int goodId,int rentNum){
		int res = op.addNumAfterTrade(goodId, rentNum);
		if(res>0){
			return true;
		}else
			return false;
	}

	
	//根据租赁id修改租赁表中的rentDate和dueTime
	public boolean modifyRentTime(@Param("Id")int Id,String rentDate,String dueTime){
		int res = op.modifyRentTime(Id,rentDate,dueTime);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	
	//根据输入的key在goodmsg中模糊查询，（对比名字，简介，详情）
	public List<goodMsg> searchByKey(String key,int type){
		return op.searchByKey(key,type);
	}
	
	//根据rentID查询出这个商品的归还期限是几天
	public int queryReturnTime(int goodId){
		return op.queryReturnTime(goodId);
	}

	
	//根据用户id扣除用户积分
	public boolean subScoreById(String userId){
		int res = op.subScoreById(userId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//根据用户id设置用户idstate为1，表示被封，默认为0
	public boolean updateIdState(String userId){
		int res = op.updateIdState(userId);
		if(res>0)
			return true;
		else
			return false;
				
	}
	
	//根据评论id删除评论
	public boolean deleteCommentById(int commentId){
		int res = op.deleteCommentById(commentId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//根据商品ID将该商品的views加1
	public boolean addViews(int goodId){
		int res = op.addViews(goodId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//根据用户userId查出来用户当前有没有完善姓名
	public String getUserName(String userId){
		return op.getUserName(userId);
	}
	
	
	//根据用户的userId添加用户的微信昵称和头像进入usermsg
	public boolean addNickAndUrl(String userId,String avatarUrl,String nickName){
		System.out.println("service="+nickName);
		int res = op.addNickAndUrl(userId, avatarUrl, nickName);
		if(res>0)
			return true;
		else
			return false;
	}
	
	
	//根据商品的goodID找到发布者的电话
	public String findPhoneByGoodId(int goodId){
		return op.findPhoneByGoodId(goodId);
	}
	
	//先查找登录者（根据userId）的所有未读评论（通过评论表与商品表联查，查出这个商品发布者的id，看与登录者是否相同，如果相同那就看看是否已读，将未读的都找出来
	public List<commentMsg> findNoReadComment(String userId){
		return op.findNoReadComment(userId);
	}
	
	//根据评论id查找有没未读的回复，按照order进行排序
	public List<replyMsg> findNoReadReply(int replyComment,String replyToId){
		return op.findNoReadReply(replyComment,replyToId);
	}
	
	//根据评论commentID修改已读
	public boolean modifyCommentRead(int commentId){
		int res = op.modifyCommentRead(commentId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//根据评论commentID将这条评论下的未读回复均改为已读
	public boolean modifyReplyRead(int Id){
		int res = op.modifyReplyRead(Id);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//查找已读评论，根据登录者的id，查找出其他人对于登陆者发布的商品的评论，并且登陆者已经阅读过的
	//这里查找出来的评论，要保证所以回复也是已读的
	//这里先查出来所有已读评论，在controller层再对查出的每一条评论，查看有没有未读回复，如果有那么就从list中删除该条信息
	public List<commentMsg> findALLComment(String userId){
		return op.findALLComment(userId);
	}
	
	//先查出发布该商品信息的商家信息(这个在controller调用函数查)，再查出该商品的发布信息
	public goodMsg queryGood(int goodId){
		return op.queryGood(goodId);
	}
	
	//根据商品id查找商品发布者的信息
	public userMsg queryGoodUser(int goodId){
		return op.queryGoodUser(goodId);
	}
	
	
	//根据评论id查找评论
	public commentMsg findCommentById(int commentId){
		return op.findCommentById(commentId); 
	}
	
	//根据评论id查找该条评论下属的所有回复
	public List<replyMsg> findReply(int commentId){
		return op.findReply(commentId);
	}
	
	//添加回复
	public boolean addReply(int replyComment,String replyId,String replyToId,String replyContent,String replyDate,Integer sequence){
		int res = op.addReply(replyComment, replyId, replyToId, replyContent,replyDate,sequence);
		System.out.println("添加回复="+res);
		if(res>0)
			return true;
		else
			return false;
		
	}
	
	
	//根据replyComment，找到该条评论下次序最大的回复的次序号
	public Integer findMaxSequence(int replyComment){
		return op.findMaxSequence(replyComment);
	}
	
	//删除回复，根据回复表的id
	public boolean deleteReply(int Id){
		int res = op.deleteReply(Id);
		if(res>0)
			return true;
		else
			return false;
	}
	
	
	//以登录者id作为replyToID 查询出最后一条未读回复信息的id
	public int findreplyId(String replyToId){
		return op.findreplyId(replyToId);
	}
	
	//以回复表的id 查询出最后一条未读回复信息
	public replyMsg findLastReply(int Id){
		return op.findLastReply(Id);
	}
	
	//查找该条评论下的回复总数，根据commentID查找（这里不再区分是不是发给登陆者的）
	public int replyNum(int replyComment){
		return op.replyNum(replyComment);
	}
	
	
	//根据commentID查出最后一条回复信息
	public replyMsg findLastReply2(int replyComment){
		return op.findLastReply2(replyComment);
	}
	
	//根据商品id，和对话双方的id，查询主聊天信息
	public chatMsg queryChat(int chatGoodId,String chatA,String chatB){
		return op.queryChat(chatGoodId, chatA, chatB);
	}
	
	//根据主留言信息的id查询下属留言信息
	public List<afterChatMsg> queryChatAfter(int mainChatId){
		return op.queryChatAfter(mainChatId);
	}
	
	
	//根据goodid和openId（作为chatRightId），usermsg.userId（作为chatLeftId)添加聊天信息
	public boolean addChat(int chatGoodId,String chatRightId,String chatLeftId,String chatDate,String chatContent,String chatTime){
		int res = op.addChat(chatGoodId, chatRightId, chatLeftId,chatDate,chatContent,chatTime);
		if(res>0)
			return true;
		else
			return false;
	}
	
	
	//根据goodid和openId（作为chatRightId），usermsg.userId（作为chatLeftId)添加聊天信息
	public boolean addChatImage(int chatGoodId,String chatRightId,String chatLeftId,String chatDate,String chatContent,String chatTime){
		int res = op.addChatImage(chatGoodId, chatRightId, chatLeftId,chatDate,chatContent,chatTime);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//根据主聊天信息的id添加下属聊天信息
	public boolean addChatAfter(int mainChatId,String chatRightIdA,String chatLeftIdA,String chatContent,String chatDate,String chatTime){
		int res = op.addChatAfter(mainChatId, chatRightIdA, chatLeftIdA, chatContent, chatDate, chatTime);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//根据主聊天信息的id添加下属聊天信息
	public boolean addChatAfterImage(int mainChatId,String chatRightIdA,String chatLeftIdA,String chatContent,String chatDate,String chatTime){
		int res = op.addChatAfterImage(mainChatId, chatRightIdA, chatLeftIdA, chatContent, chatDate, chatTime);
		if(res>0)
			return true;
		else
			return false;
	}
		
	
	//查找主留言，根据登录者id查找chatRightId或chatLeftId=id的主留言信息
	public List<chatMsg> findMainChat(String openId){
		return op.findMainChat(openId);
	}
	
	//根据留言id将主留言改为已读
	public boolean setMainRead(int Id,String chatLeftId){
		int res = op.setMainRead(Id,chatLeftId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//根据mainChatId设置下属评论已读
	public boolean setAfterRead(int mainChatId,String chatLeftId){
		int res = op.setAfterRead(mainChatId,chatLeftId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//设置留言信息的显示
	public boolean modifyShowState(int Id,int isShowR,int isShowL){
		int res=op.modifyShowState(Id, isShowR, isShowL);
		if(res>0)
			return true;
		else
			return false;
	}
	
	
	//根据id查找主留言信息
	public chatMsg findMainChatById(int Id){
		return op.findMainChatById(Id);
	}
	
	//将主聊天信息的isShow都改为1
	public boolean updateShow(int Id){
		int res = op.updateShow(Id);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//根据商品id查找发布者所在学校
	public String findSchool(int goodId){
		return op.findSchool(goodId);
	}
	
	//根据userId查找头像，昵称，名字
	public userMsg queryNameAndNickAndAvat(String userId){
		return op.queryNameAndNickAndAvat(userId);
	}
	
	//根据头像查找到对应的用户（因为微信在进行头像存储时，每个头像的地址一定是唯一的，所以可以找到唯一的用户）
	//找到这个用户后，返回这个用户的所有个人信息，和此用户发布的所有商品信息，再根据这些商品信息查找出所有评论，统计五星评论数和总评论数，得出好评率
	public userMsg findHomeMsg(String avat){
		return op.findHomeMsg(avat);
	}
	
	//根据商品id统计对应评论数
	public int totalComment(int goodId){
		return op.totalComment(goodId);
	}
	
	//根据商品id统计对应好评数
	public int goodComment(int goodId){
		return op.goodComment(goodId);
	}
	
	//添加关注
	public boolean addFollow(String followId,String followToId){
		int res = op.addFollow(followId, followToId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//取消关注
	public boolean cancelFollow(String followId,String followToId){
		int res = op.cancelFollow(followId, followToId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	
	//查询关注状态
	public followMsg followState(String followId,String followToId){
		return op.followState(followId, followToId);
	}
	
	//根据租户id查找其所有订单状态为2的订单
	public List<rentMsg> find(String rentId,int rentState){
		return op.find(rentId, rentState);
	}
	
	//根据用户id查找该用户的所有关注
	public List<followMsg> findFollow(String followId){
		return op.findFollow(followId);
	}
	
	//根据商品id修改商品信息
	public boolean modifyGood(int goodId,String goodName,int unit,int type,String goodDetail,int deadline,double price,int deposit,String introduction,int num){
		int res = op.modifyGood(goodId, goodName, unit, type, goodDetail, deadline, price, deposit, introduction, num);
		if(res>0)
			return true;
		else
			return false;
	}
}
