package com.qd.dao;


import java.util.List;

import javax.jws.WebParam;

import org.apache.ibatis.annotations.Param;

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


public interface operation {
	//授权之后，获取用户账号，用户手机号码(其余项默认为0)，添加用户信息；返回影响行数
	public int addUserMsg(@Param("userId")String userId);
	
	//根据userId查询用户信息
	public userMsg findUserMsgById(@Param("userId")String userId);
	
	//根据userId修改用户的性别
	public int modiyfSexById(@Param("userId")String userId,@Param("sex")int sex);
	
	//根据userID修改用户生日
	public int modiyfBirthById(@Param("userId")String userId,@Param("birthday")String birthday);

	//修改用户基本信息（学校，生日，微信号，身份证）
	public int modifyBaseById(@Param("userId")String userId,@Param("idCard")String idCard,@Param("school")String school,@Param("wxNum")String wxNum,@Param("userName")String userName);

	//分开修改基本信息
	//修改姓名
	public int modifyNameById(@Param("userId")String userId,@Param("userName")String userName);
	
	//修改身份证
	public int modifyIdCardById(@Param("userId")String userId,@Param("idCard")String idCard);
	
	//修改学校
	public int modifySchoolById(@Param("userId")String userId,@Param("school")String school);
		
	//修改微信号
	public int modifyWxNumById(@Param("userId")String userId,@Param("wxNum")String wxNum);
	
	//修改手机绑定
	public int modifyPhoneById(@Param("userId")String userId,@Param("phone")String phone);
	
	//添加商品信息（除图片），使用后一种方式，可以将新插入的信息的id返回并注入到bean中
	//public int addGoodMsg(@Param("userId")String userId,@Param("goodName")String goodName,@Param("introduction")String introduction,@Param("goodDetail")String goodDetail,@Param("type")int type,@Param("unit")int unit,@Param("deposit")int deposit,@Param("deadline")int deadline,@Param("price")double price,@Param("releaseDate")String releaseDate);
	public int addGoodMsg(@Param("good")goodMsg good);
	
	//添加图片
	public int addGoodImage1(@Param("goodId")int goodId,@Param("imagePath")String imagePath);
	//添加图片
	public int addGoodImage2(@Param("goodId")int goodId,@Param("imagePath")String imagePath);
	//添加图片
	public int addGoodImage3(@Param("goodId")int goodId,@Param("imagePath")String imagePath);

	//根据用户id查询所有发布信息
	public List<goodMsg> showAllRelease(@Param("userId")String userId);
	
	//根据发布的商品ID删除发布的商品
	public int deleteGoodById(@Param("goodId")int goodId);
	
	//查询租用中的订单（包括出租中和租用中）
	public List<rentMsg> findRent(@Param("rentId")String rentId,@Param("rentState")int rentState);
	
	//查询待归还的订单 
	public List<rentMsg> findWaitReturn(@Param("rentId")String rentId,@Param("rentState")int rentState);
	
	//以当前用户id为发货者id，查找状态为0的租赁信息(待发货订单)
	public List<rentMsg> findWaitSend(@Param("releaseId")String releaseId,@Param("rentState")int rentState);

	
	//以当前用户id为发货者id，查找状态为0的租赁信息(待发货订单)
	public List<rentMsg> findWaitSend2(@Param("releaseId")String releaseId);
	
	//以当前用户id为收货者id，查找状态为0或者1的租赁信息(待收货订单)
	public List<rentMsg> findWaitReceive(@Param("rentId")String rentId);
	
	//查找用户所有的已完成订单（包括作为租用者所有订单(包括已评价和未评价))
	public List<rentMsg> findFinishByRent(@Param("userId")String userId);
	
	//查找用户作为发布者的所有已完成订单（包括已评价和未评价）
	public List<rentMsg> findFinishByRelease(@Param("userId")String userId);
	
	//先以当前用户ID为rentID查询订单状态为2并且超过租赁时间的订单，修改状态为3
	public int modifyRentState(@Param("rentId")String rentId,@Param("currentDate")String currentDate);
	
	//获取收藏数据
	public List<collectMsg> collectQuery(@Param("collectUserId")String collectUserId);
	
	//根据id删除收藏的商品
	public int deleteCollect(@Param("id")int id);
	
	//查询订单详情
	public rentDetails QueryRentDetail(@Param("Id")int Id);
	
	//点击收货，将待收货状态1，改为2租用中
	public int modifyRentState1To2(@Param("Id")int Id);
	
	//点击收回，将待收回状态7，改为4交易完成
	public int modifyRentState7To4(@Param("Id")int Id);
	
	//点击归还，将待归还状态3，改为7待收回
	public int modifyRentState3To7(@Param("Id")int Id);
	
	//评论完成后,将状态由4改为6
	public int modifyRentState4To6(@Param("Id")int Id);
	
	//点击发货，将待发货状态0，改为1待收货
	public int modifyRentState0To1(@Param("Id")int Id);
	
	//将状态从租用中2改为归还中3
	public int modifyRentState2To3(@Param("Id")int Id);
	
	//将状态从租用中2改为已完成5 
	public int modifyRentState2To5(@Param("Id")int Id);
	
	//将状态从归还中3改为已完成5
	public int modifyRentState3To5(@Param("Id")int Id);
	
	//将状态从已完成押金扣留5改为已评价押金扣留8
	public int modifyRentState5To8(@Param("Id")int Id);
	
	//获取用户积分
	public int getUserIntegration(@Param("userId")String userId);
	
	//根据租赁信息的id获取发布该商品信息的用户电话号码
	public String getReleasePhone(@Param("Id")int Id);
	
	//根据租赁信息的id获取租用该商品的用户电话号码
	public String getRentPhone(@Param("Id")int Id);
	
	//查看用户是否已完善信息，填写了手机号码
	public String checkPhone(@Param("userId")String userId);
	
	//根据商品浏览次数找到浏览次数最多，且还能租用的商品信息
	public List<goodMsg> getHotGood();
	
	//获取最近发布的三条商品信息
	public List<goodMsg> getNewGood();
	
	//获取浏览最多的日用品或其他类商品信息
	public List<goodMsg> getHotGroceries();
	
	//获取浏览最多的三条图书/电子产品/出行工具信息
	public List<goodMsg> getHot(@Param("type")int type);
	
	//根据商品goodId获取商品详细信息
	public goodMsg getGoodDetail(@Param("goodId")int goodId);
	
	//收藏商品进入收藏表
	public int collectGood(@Param("collectUserId")String collectUserId,@Param("collectGoodId")int collectGoodId,@Param("collectDate")String collectDate);

	//商品收藏数加1
	public int addCollect(@Param("goodId")int goodId);
	
	//取消收藏(根据用户id和商品id)
	public int cancelcollect(@Param("collectUserId")String collectUserId,@Param("collectGoodId")int collectGoodId);
	
	//商品收藏数减1
	public int subCollect(@Param("goodId")int goodId);
	
	//根据商品id和用户id查看该用户是否收藏该商品
	public collectMsg findCollect(@Param("collectUserId")String collectUserId,@Param("collectGoodId")int collectGoodId);
	
	//根据商品id获取评论
	public List<commentMsg> findComment(@Param("commentGoodId")int commentGoodId);
	
	//添加购物车
	public int addCart(@Param("cartUserId")String cartUserId,@Param("cartGoodId")int cartGoodId,@Param("cartNum")int cartNum,@Param("timeLen")int timeLen);
	
	//根据用户id获取用户的购物车内容
	public List<cartMsg> fingCartByUser(@Param("cartUserId")String cartUserId);
	
	//在购物车中修改cartNum(加)
	public int modifyCartNumAdd(@Param("cartId")int cartId);
	
	//在购物车中修改cartNum（减）
	public int modifyCartNumSub(@Param("cartId")int cartId);
	
	//根据购物车信息的id号删除
	public int deleteCartById(@Param("cartId")int cartId);
	
	//在购物车中修改timeLen(加)
	public int modifyCartTimeAdd(@Param("cartId")int cartId);
	
	//在购物车中修改timeLen（减）
	public int modifyCartTimeSub(@Param("cartId")int cartId);
	
	//将商品插入到租赁表中,并设置状态为0
	public int addRentMsg(@Param("goodId")int goodId,@Param("releaseId")String releaseId,@Param("rentId")String rentId,@Param("payDate")String payDate,@Param("rentNum")int rentNum,@Param("rentLen")int rentLen,@Param("remarks")String remarks);
	
	//用户从购物车下单后修改商品表
	public int modifyGoodMsg(@Param("goodId")int goodId,@Param("cartNum")int cartNum);
	
	//添加评论
	public int addComment(@Param("commentGoodId")int commentGoodId,@Param("content")String content,@Param("degree")int degree,@Param("commentUserId")String commentUserId,@Param("anonymous")int anonymous,@Param("date")String date);

	//根据评价的分数，对发布者的积分进行修改（1颗星不加分，2-3颗星加10分，4颗星加20分，5颗星加30分）
	public int modifyReleaseScore(@Param("userId")String userId,@Param("add")int add);
	
	//根据商品id将交易完成的商品数量恢复原状
	public int addNumAfterTrade(@Param("goodId")int goodId,@Param("rentNum")int rentNum);
	
	//根据租赁id修改租赁表中的rentDate和dueTime
	public int modifyRentTime(@Param("Id")int Id,@Param("rentDate")String rentDate,@Param("dueTime")String dueTime);

	//根据输入的key在goodmsg中模糊查询，（对比名字，简介，详情）
	public List<goodMsg> searchByKey(@Param("key")String key,@Param("type")int type);
	
	//根据rentID查询出这个商品的归还期限是几天
	public int queryReturnTime(@Param("goodId")int goodId);

	//根据用户id扣除用户积分
	public int subScoreById(@Param("userId")String userId);
	
	//根据用户id设置用户idstate为1，表示被封，默认为0
	public int updateIdState(@Param("userId")String userId);
	
	//根据评论id删除评论
	public int deleteCommentById(@Param("commentId")int commentId);
	
	//根据商品ID将该商品的views加1
	public int addViews(@Param("goodId")int goodId);
	
	//根据用户userId查出来用户当前有没有完善姓名
	public String getUserName(@Param("userId")String userId);
	
	//根据用户的userId添加用户的微信昵称和头像进入usermsg
	public int addNickAndUrl(@Param("userId")String userId,@Param("avatarUrl")String avatarUrl,@Param("nickName")String nickName);

	//根据商品的goodID找到发布者的电话
	public String findPhoneByGoodId(@Param("goodId")int goodId);
	
	//先查找登录者（根据userId）的所有未读评论（通过评论表与商品表联查，查出这个商品发布者的id，看与登录者是否相同，如果相同那就看看是否已读，将未读的都找出来--这样只找到了别人对登录发布商品的未读评价，但没有找到他人对登陆者的未读回复信息
	//还应该找到评价本身已读，但是存在未读回复的评价；而不应该在未读评价下找未读回复
	//但是还应该查找评论本身已读，但是存在未读回复的评论（这个在controller层增加函数查即可）
	//基于上述条件此处修改为，查找所有订单，再对每一条订单查找未读回复，如果有未读回复，那么则保留该条信息并记录未读回复数；如果没有未读回复且该评论本身为已读，那么就删除这条信息（不过这里只需要查询出所有评论信息，其余在controller层完成）
	public List<commentMsg> findNoReadComment(@Param("userId")String userId);
	
	
	//根据评论id查找有没未读的回复，按照order进行排序
	public List<replyMsg> findNoReadReply(@Param("replyComment")int replyComment,@Param("replyToId")String replyToId);
	
	//根据评论commentID修改已读
	public int modifyCommentRead(@Param("commentId")int commentId);
	
	//根据回复ID将未读回复均改为已读
	public int modifyReplyRead(@Param("Id")int Id);
	
	//查找已读评论，根据登录者的id，查找出其他人对于登陆者发布的商品的评论，并且登陆者已经阅读过的
	//这里查找出来的评论，要保证所以回复也是已读的
	//这里先查出来所有已读评论，在controller层再对查出的每一条评论，查看有没有未读回复，如果有那么就从list中删除该条信息
	public List<commentMsg> findALLComment(@Param("userId")String userId);
	
	//先查出发布该商品信息的商家信息(这个在controller调用函数查)，再查出该商品的发布信息
	public goodMsg queryGood(@Param("goodId")int goodId);
	
	//根据商品id查找商品发布者的信息
	public userMsg queryGoodUser(@Param("goodId")int goodId); 
	
	//根据评论id查找评论
	public commentMsg findCommentById(@Param("commentId")int commentId);
	
	//根据评论id查找该条评论下属的所有回复
	public List<replyMsg> findReply(@Param("commentId")int commentId);
	
	//添加回复
	public int addReply(@Param("replyComment")int replyComment,@Param("replyId")String replyId,@Param("replyToId")String replyToId,@Param("replyContent")String replyContent,@Param("replyDate")String replyDate,@Param("sequence")Integer sequence);
	
	//根据replyComment，找到该条评论下次序最大的回复的次序号
	public Integer findMaxSequence(@Param("replyComment")int replyComment);
	
	//删除回复，根据回复表的id
	public int deleteReply(@Param("Id")int Id);
	
	//以登录者id作为replyToID 查询出最后一条未读回复信息的id
	public int findreplyId(@Param("replyToId")String replyToId);
	
	//以回复表的id 查询出最后一条未读回复信息
	public replyMsg findLastReply(@Param("Id")int Id);
	
	
	//查找该条评论下的回复总数，根据commentID查找（这里不再区分是不是发给登陆者的）
	public int replyNum(@Param("replyComment")int replyComment);
	
	//根据commentID查出最后一条回复信息
	public replyMsg findLastReply2(@Param("replyComment")int replyComment);
	
	//根据商品id，和对话双方的id，查询主聊天信息
	public chatMsg queryChat(@Param("chatGoodId")int chatGoodId,@Param("chatA")String chatA,@Param("chatB")String chatB);
	
	//根据主留言信息的id查询下属留言信息
	public List<afterChatMsg> queryChatAfter(@Param("mainChatId")int mainChatId);
	
	//根据goodid和openId（作为chatRightId），usermsg.userId（作为chatLeftId)添加主聊天信息
	public int addChat(@Param("chatGoodId")int chatGoodId,@Param("chatRightId")String chatRightId,@Param("chatLeftId")String chatLeftId,@Param("chatDate")String chatDate,@Param("chatContent")String chatContent,@Param("chatTime")String chatTime);
	
	//根据主聊天信息的id添加下属聊天信息
	public int addChatAfter(@Param("mainChatId")int mainChatId,@Param("chatRightIdA")String chatRightIdA,@Param("chatLeftIdA")String chatLeftIdA,@Param("chatContent")String chatContent,@Param("chatDate")String chatDate,@Param("chatTime")String chatTime);
	
	//根据goodid和openId（作为chatRightId），usermsg.userId（作为chatLeftId)添加主聊天信息
	public int addChatImage(@Param("chatGoodId")int chatGoodId,@Param("chatRightId")String chatRightId,@Param("chatLeftId")String chatLeftId,@Param("chatDate")String chatDate,@Param("chatContent")String chatContent,@Param("chatTime")String chatTime);
	
	//根据主聊天信息的id添加下属聊天信息
	public int addChatAfterImage(@Param("mainChatId")int mainChatId,@Param("chatRightIdA")String chatRightIdA,@Param("chatLeftIdA")String chatLeftIdA,@Param("chatContent")String chatContent,@Param("chatDate")String chatDate,@Param("chatTime")String chatTime);
	
	//查找主留言，根据登录者id查找chatRightId或chatLeftId=id的主留言信息
	public List<chatMsg> findMainChat(@Param("openId")String openId);
	
	//根据留言id将主留言改为已读
	public int setMainRead(@Param("Id")int Id,@Param("chatLeftId")String chatLeftId);
	
	//根据mainChatId设置下属评论已读
	public int setAfterRead(@Param("mainChatId")int mainChatId,@Param("chatLeftId")String chatLeftId);
	
	//设置留言信息的显示
	public int modifyShowState(@Param("Id")int Id,@Param("isShowR")int isShowR,@Param("isShowL")int isShowL);

	//根据id查找主留言信息
	public chatMsg findMainChatById(@Param("Id")int Id);
	
	//将主聊天信息的isShow都改为1
	public int updateShow(@Param("Id")int Id);
	
	//根据商品id查找发布者所在学校
	public String findSchool(@Param("goodId")int goodId);
	
	//根据userId查找头像，昵称，名字
	public userMsg queryNameAndNickAndAvat(@Param("userId")String userId);
	
	//根据头像查找到对应的用户（因为微信在进行头像存储时，每个头像的地址一定是唯一的，所以可以找到唯一的用户）
	//找到这个用户后，返回这个用户的所有个人信息，和此用户发布的所有商品信息，再根据这些商品信息查找出所有评论，统计五星评论数和总评论数，得出好评率
	public userMsg findHomeMsg(@Param("avat")String avat);
	
	//根据商品id统计对应评论数
	public int totalComment(@Param("goodId")int goodId);
	
	//根据商品id统计对应好评数
	public int goodComment(@Param("goodId")int goodId);
	
	//添加关注
	public int addFollow(@Param("followId")String followId,@Param("followToId")String followToId);
	
	//取消关注
	public int cancelFollow(@Param("followId")String followId,@Param("followToId")String followToId);
	
	//查询关注状态
	public followMsg followState(@Param("followId")String followId,@Param("followToId")String followToId);
	
	//根据租户id查找其所有订单状态为2的订单
	public List<rentMsg> find(@Param("rentId")String rentId,@Param("rentState")int rentState);
	
	//根据用户id查找该用户的所有关注
	public List<followMsg> findFollow(@Param("followId")String followId);
	
	//根据商品id修改商品信息
	public int modifyGood(@Param("goodId")int goodId,@Param("goodName")String goodName,@Param("unit")int unit,@Param("type")int type,@Param("goodDetail")String goodDetail,@Param("deadline")int deadline,@Param("price")double price,@Param("deposit")int deposit,@Param("introduction")String introduction,@Param("num")int num);
}
