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
	//��Ȩ֮�󣬻�ȡ�û��˺ţ��û��ֻ�����(������Ĭ��Ϊ0)������û���Ϣ������Ӱ������
	public int addUserMsg(@Param("userId")String userId);
	
	//����userId��ѯ�û���Ϣ
	public userMsg findUserMsgById(@Param("userId")String userId);
	
	//����userId�޸��û����Ա�
	public int modiyfSexById(@Param("userId")String userId,@Param("sex")int sex);
	
	//����userID�޸��û�����
	public int modiyfBirthById(@Param("userId")String userId,@Param("birthday")String birthday);

	//�޸��û�������Ϣ��ѧУ�����գ�΢�źţ����֤��
	public int modifyBaseById(@Param("userId")String userId,@Param("idCard")String idCard,@Param("school")String school,@Param("wxNum")String wxNum,@Param("userName")String userName);

	//�ֿ��޸Ļ�����Ϣ
	//�޸�����
	public int modifyNameById(@Param("userId")String userId,@Param("userName")String userName);
	
	//�޸����֤
	public int modifyIdCardById(@Param("userId")String userId,@Param("idCard")String idCard);
	
	//�޸�ѧУ
	public int modifySchoolById(@Param("userId")String userId,@Param("school")String school);
		
	//�޸�΢�ź�
	public int modifyWxNumById(@Param("userId")String userId,@Param("wxNum")String wxNum);
	
	//�޸��ֻ���
	public int modifyPhoneById(@Param("userId")String userId,@Param("phone")String phone);
	
	//�����Ʒ��Ϣ����ͼƬ����ʹ�ú�һ�ַ�ʽ�����Խ��²������Ϣ��id���ز�ע�뵽bean��
	//public int addGoodMsg(@Param("userId")String userId,@Param("goodName")String goodName,@Param("introduction")String introduction,@Param("goodDetail")String goodDetail,@Param("type")int type,@Param("unit")int unit,@Param("deposit")int deposit,@Param("deadline")int deadline,@Param("price")double price,@Param("releaseDate")String releaseDate);
	public int addGoodMsg(@Param("good")goodMsg good);
	
	//���ͼƬ
	public int addGoodImage1(@Param("goodId")int goodId,@Param("imagePath")String imagePath);
	//���ͼƬ
	public int addGoodImage2(@Param("goodId")int goodId,@Param("imagePath")String imagePath);
	//���ͼƬ
	public int addGoodImage3(@Param("goodId")int goodId,@Param("imagePath")String imagePath);

	//�����û�id��ѯ���з�����Ϣ
	public List<goodMsg> showAllRelease(@Param("userId")String userId);
	
	//���ݷ�������ƷIDɾ����������Ʒ
	public int deleteGoodById(@Param("goodId")int goodId);
	
	//��ѯ�����еĶ��������������к������У�
	public List<rentMsg> findRent(@Param("rentId")String rentId,@Param("rentState")int rentState);
	
	//��ѯ���黹�Ķ��� 
	public List<rentMsg> findWaitReturn(@Param("rentId")String rentId,@Param("rentState")int rentState);
	
	//�Ե�ǰ�û�idΪ������id������״̬Ϊ0��������Ϣ(����������)
	public List<rentMsg> findWaitSend(@Param("releaseId")String releaseId,@Param("rentState")int rentState);

	
	//�Ե�ǰ�û�idΪ������id������״̬Ϊ0��������Ϣ(����������)
	public List<rentMsg> findWaitSend2(@Param("releaseId")String releaseId);
	
	//�Ե�ǰ�û�idΪ�ջ���id������״̬Ϊ0����1��������Ϣ(���ջ�����)
	public List<rentMsg> findWaitReceive(@Param("rentId")String rentId);
	
	//�����û����е�����ɶ�����������Ϊ���������ж���(���������ۺ�δ����))
	public List<rentMsg> findFinishByRent(@Param("userId")String userId);
	
	//�����û���Ϊ�����ߵ���������ɶ��������������ۺ�δ���ۣ�
	public List<rentMsg> findFinishByRelease(@Param("userId")String userId);
	
	//���Ե�ǰ�û�IDΪrentID��ѯ����״̬Ϊ2���ҳ�������ʱ��Ķ������޸�״̬Ϊ3
	public int modifyRentState(@Param("rentId")String rentId,@Param("currentDate")String currentDate);
	
	//��ȡ�ղ�����
	public List<collectMsg> collectQuery(@Param("collectUserId")String collectUserId);
	
	//����idɾ���ղص���Ʒ
	public int deleteCollect(@Param("id")int id);
	
	//��ѯ��������
	public rentDetails QueryRentDetail(@Param("Id")int Id);
	
	//����ջ��������ջ�״̬1����Ϊ2������
	public int modifyRentState1To2(@Param("Id")int Id);
	
	//����ջأ������ջ�״̬7����Ϊ4�������
	public int modifyRentState7To4(@Param("Id")int Id);
	
	//����黹�������黹״̬3����Ϊ7���ջ�
	public int modifyRentState3To7(@Param("Id")int Id);
	
	//������ɺ�,��״̬��4��Ϊ6
	public int modifyRentState4To6(@Param("Id")int Id);
	
	//�����������������״̬0����Ϊ1���ջ�
	public int modifyRentState0To1(@Param("Id")int Id);
	
	//��״̬��������2��Ϊ�黹��3
	public int modifyRentState2To3(@Param("Id")int Id);
	
	//��״̬��������2��Ϊ�����5 
	public int modifyRentState2To5(@Param("Id")int Id);
	
	//��״̬�ӹ黹��3��Ϊ�����5
	public int modifyRentState3To5(@Param("Id")int Id);
	
	//��״̬�������Ѻ�����5��Ϊ������Ѻ�����8
	public int modifyRentState5To8(@Param("Id")int Id);
	
	//��ȡ�û�����
	public int getUserIntegration(@Param("userId")String userId);
	
	//����������Ϣ��id��ȡ��������Ʒ��Ϣ���û��绰����
	public String getReleasePhone(@Param("Id")int Id);
	
	//����������Ϣ��id��ȡ���ø���Ʒ���û��绰����
	public String getRentPhone(@Param("Id")int Id);
	
	//�鿴�û��Ƿ���������Ϣ����д���ֻ�����
	public String checkPhone(@Param("userId")String userId);
	
	//������Ʒ��������ҵ����������࣬�һ������õ���Ʒ��Ϣ
	public List<goodMsg> getHotGood();
	
	//��ȡ���������������Ʒ��Ϣ
	public List<goodMsg> getNewGood();
	
	//��ȡ�����������Ʒ����������Ʒ��Ϣ
	public List<goodMsg> getHotGroceries();
	
	//��ȡ�����������ͼ��/���Ӳ�Ʒ/���й�����Ϣ
	public List<goodMsg> getHot(@Param("type")int type);
	
	//������ƷgoodId��ȡ��Ʒ��ϸ��Ϣ
	public goodMsg getGoodDetail(@Param("goodId")int goodId);
	
	//�ղ���Ʒ�����ղر�
	public int collectGood(@Param("collectUserId")String collectUserId,@Param("collectGoodId")int collectGoodId,@Param("collectDate")String collectDate);

	//��Ʒ�ղ�����1
	public int addCollect(@Param("goodId")int goodId);
	
	//ȡ���ղ�(�����û�id����Ʒid)
	public int cancelcollect(@Param("collectUserId")String collectUserId,@Param("collectGoodId")int collectGoodId);
	
	//��Ʒ�ղ�����1
	public int subCollect(@Param("goodId")int goodId);
	
	//������Ʒid���û�id�鿴���û��Ƿ��ղظ���Ʒ
	public collectMsg findCollect(@Param("collectUserId")String collectUserId,@Param("collectGoodId")int collectGoodId);
	
	//������Ʒid��ȡ����
	public List<commentMsg> findComment(@Param("commentGoodId")int commentGoodId);
	
	//��ӹ��ﳵ
	public int addCart(@Param("cartUserId")String cartUserId,@Param("cartGoodId")int cartGoodId,@Param("cartNum")int cartNum,@Param("timeLen")int timeLen);
	
	//�����û�id��ȡ�û��Ĺ��ﳵ����
	public List<cartMsg> fingCartByUser(@Param("cartUserId")String cartUserId);
	
	//�ڹ��ﳵ���޸�cartNum(��)
	public int modifyCartNumAdd(@Param("cartId")int cartId);
	
	//�ڹ��ﳵ���޸�cartNum������
	public int modifyCartNumSub(@Param("cartId")int cartId);
	
	//���ݹ��ﳵ��Ϣ��id��ɾ��
	public int deleteCartById(@Param("cartId")int cartId);
	
	//�ڹ��ﳵ���޸�timeLen(��)
	public int modifyCartTimeAdd(@Param("cartId")int cartId);
	
	//�ڹ��ﳵ���޸�timeLen������
	public int modifyCartTimeSub(@Param("cartId")int cartId);
	
	//����Ʒ���뵽���ޱ���,������״̬Ϊ0
	public int addRentMsg(@Param("goodId")int goodId,@Param("releaseId")String releaseId,@Param("rentId")String rentId,@Param("payDate")String payDate,@Param("rentNum")int rentNum,@Param("rentLen")int rentLen,@Param("remarks")String remarks);
	
	//�û��ӹ��ﳵ�µ����޸���Ʒ��
	public int modifyGoodMsg(@Param("goodId")int goodId,@Param("cartNum")int cartNum);
	
	//�������
	public int addComment(@Param("commentGoodId")int commentGoodId,@Param("content")String content,@Param("degree")int degree,@Param("commentUserId")String commentUserId,@Param("anonymous")int anonymous,@Param("date")String date);

	//�������۵ķ������Է����ߵĻ��ֽ����޸ģ�1���ǲ��ӷ֣�2-3���Ǽ�10�֣�4���Ǽ�20�֣�5���Ǽ�30�֣�
	public int modifyReleaseScore(@Param("userId")String userId,@Param("add")int add);
	
	//������Ʒid��������ɵ���Ʒ�����ָ�ԭ״
	public int addNumAfterTrade(@Param("goodId")int goodId,@Param("rentNum")int rentNum);
	
	//��������id�޸����ޱ��е�rentDate��dueTime
	public int modifyRentTime(@Param("Id")int Id,@Param("rentDate")String rentDate,@Param("dueTime")String dueTime);

	//���������key��goodmsg��ģ����ѯ�����Ա����֣���飬���飩
	public List<goodMsg> searchByKey(@Param("key")String key,@Param("type")int type);
	
	//����rentID��ѯ�������Ʒ�Ĺ黹�����Ǽ���
	public int queryReturnTime(@Param("goodId")int goodId);

	//�����û�id�۳��û�����
	public int subScoreById(@Param("userId")String userId);
	
	//�����û�id�����û�idstateΪ1����ʾ���⣬Ĭ��Ϊ0
	public int updateIdState(@Param("userId")String userId);
	
	//��������idɾ������
	public int deleteCommentById(@Param("commentId")int commentId);
	
	//������ƷID������Ʒ��views��1
	public int addViews(@Param("goodId")int goodId);
	
	//�����û�userId������û���ǰ��û����������
	public String getUserName(@Param("userId")String userId);
	
	//�����û���userId����û���΢���ǳƺ�ͷ�����usermsg
	public int addNickAndUrl(@Param("userId")String userId,@Param("avatarUrl")String avatarUrl,@Param("nickName")String nickName);

	//������Ʒ��goodID�ҵ������ߵĵ绰
	public String findPhoneByGoodId(@Param("goodId")int goodId);
	
	//�Ȳ��ҵ�¼�ߣ�����userId��������δ�����ۣ�ͨ�����۱�����Ʒ�����飬��������Ʒ�����ߵ�id�������¼���Ƿ���ͬ�������ͬ�ǾͿ����Ƿ��Ѷ�����δ���Ķ��ҳ���--����ֻ�ҵ��˱��˶Ե�¼������Ʒ��δ�����ۣ���û���ҵ����˶Ե�½�ߵ�δ���ظ���Ϣ
	//��Ӧ���ҵ����۱����Ѷ������Ǵ���δ���ظ������ۣ�����Ӧ����δ����������δ���ظ�
	//���ǻ�Ӧ�ò������۱����Ѷ������Ǵ���δ���ظ������ۣ������controller�����Ӻ����鼴�ɣ�
	//�������������˴��޸�Ϊ���������ж������ٶ�ÿһ����������δ���ظ��������δ���ظ�����ô����������Ϣ����¼δ���ظ��������û��δ���ظ��Ҹ����۱���Ϊ�Ѷ�����ô��ɾ��������Ϣ����������ֻ��Ҫ��ѯ������������Ϣ��������controller����ɣ�
	public List<commentMsg> findNoReadComment(@Param("userId")String userId);
	
	
	//��������id������ûδ���Ļظ�������order��������
	public List<replyMsg> findNoReadReply(@Param("replyComment")int replyComment,@Param("replyToId")String replyToId);
	
	//��������commentID�޸��Ѷ�
	public int modifyCommentRead(@Param("commentId")int commentId);
	
	//���ݻظ�ID��δ���ظ�����Ϊ�Ѷ�
	public int modifyReplyRead(@Param("Id")int Id);
	
	//�����Ѷ����ۣ����ݵ�¼�ߵ�id�����ҳ������˶��ڵ�½�߷�������Ʒ�����ۣ����ҵ�½���Ѿ��Ķ�����
	//������ҳ��������ۣ�Ҫ��֤���Իظ�Ҳ���Ѷ���
	//�����Ȳ���������Ѷ����ۣ���controller���ٶԲ����ÿһ�����ۣ��鿴��û��δ���ظ����������ô�ʹ�list��ɾ��������Ϣ
	public List<commentMsg> findALLComment(@Param("userId")String userId);
	
	//�Ȳ����������Ʒ��Ϣ���̼���Ϣ(�����controller���ú�����)���ٲ������Ʒ�ķ�����Ϣ
	public goodMsg queryGood(@Param("goodId")int goodId);
	
	//������Ʒid������Ʒ�����ߵ���Ϣ
	public userMsg queryGoodUser(@Param("goodId")int goodId); 
	
	//��������id��������
	public commentMsg findCommentById(@Param("commentId")int commentId);
	
	//��������id���Ҹ����������������лظ�
	public List<replyMsg> findReply(@Param("commentId")int commentId);
	
	//��ӻظ�
	public int addReply(@Param("replyComment")int replyComment,@Param("replyId")String replyId,@Param("replyToId")String replyToId,@Param("replyContent")String replyContent,@Param("replyDate")String replyDate,@Param("sequence")Integer sequence);
	
	//����replyComment���ҵ����������´������Ļظ��Ĵ����
	public Integer findMaxSequence(@Param("replyComment")int replyComment);
	
	//ɾ���ظ������ݻظ����id
	public int deleteReply(@Param("Id")int Id);
	
	//�Ե�¼��id��ΪreplyToID ��ѯ�����һ��δ���ظ���Ϣ��id
	public int findreplyId(@Param("replyToId")String replyToId);
	
	//�Իظ����id ��ѯ�����һ��δ���ظ���Ϣ
	public replyMsg findLastReply(@Param("Id")int Id);
	
	
	//���Ҹ��������µĻظ�����������commentID���ң����ﲻ�������ǲ��Ƿ�����½�ߵģ�
	public int replyNum(@Param("replyComment")int replyComment);
	
	//����commentID������һ���ظ���Ϣ
	public replyMsg findLastReply2(@Param("replyComment")int replyComment);
	
	//������Ʒid���ͶԻ�˫����id����ѯ��������Ϣ
	public chatMsg queryChat(@Param("chatGoodId")int chatGoodId,@Param("chatA")String chatA,@Param("chatB")String chatB);
	
	//������������Ϣ��id��ѯ����������Ϣ
	public List<afterChatMsg> queryChatAfter(@Param("mainChatId")int mainChatId);
	
	//����goodid��openId����ΪchatRightId����usermsg.userId����ΪchatLeftId)�����������Ϣ
	public int addChat(@Param("chatGoodId")int chatGoodId,@Param("chatRightId")String chatRightId,@Param("chatLeftId")String chatLeftId,@Param("chatDate")String chatDate,@Param("chatContent")String chatContent,@Param("chatTime")String chatTime);
	
	//������������Ϣ��id�������������Ϣ
	public int addChatAfter(@Param("mainChatId")int mainChatId,@Param("chatRightIdA")String chatRightIdA,@Param("chatLeftIdA")String chatLeftIdA,@Param("chatContent")String chatContent,@Param("chatDate")String chatDate,@Param("chatTime")String chatTime);
	
	//����goodid��openId����ΪchatRightId����usermsg.userId����ΪchatLeftId)�����������Ϣ
	public int addChatImage(@Param("chatGoodId")int chatGoodId,@Param("chatRightId")String chatRightId,@Param("chatLeftId")String chatLeftId,@Param("chatDate")String chatDate,@Param("chatContent")String chatContent,@Param("chatTime")String chatTime);
	
	//������������Ϣ��id�������������Ϣ
	public int addChatAfterImage(@Param("mainChatId")int mainChatId,@Param("chatRightIdA")String chatRightIdA,@Param("chatLeftIdA")String chatLeftIdA,@Param("chatContent")String chatContent,@Param("chatDate")String chatDate,@Param("chatTime")String chatTime);
	
	//���������ԣ����ݵ�¼��id����chatRightId��chatLeftId=id����������Ϣ
	public List<chatMsg> findMainChat(@Param("openId")String openId);
	
	//��������id�������Ը�Ϊ�Ѷ�
	public int setMainRead(@Param("Id")int Id,@Param("chatLeftId")String chatLeftId);
	
	//����mainChatId�������������Ѷ�
	public int setAfterRead(@Param("mainChatId")int mainChatId,@Param("chatLeftId")String chatLeftId);
	
	//����������Ϣ����ʾ
	public int modifyShowState(@Param("Id")int Id,@Param("isShowR")int isShowR,@Param("isShowL")int isShowL);

	//����id������������Ϣ
	public chatMsg findMainChatById(@Param("Id")int Id);
	
	//����������Ϣ��isShow����Ϊ1
	public int updateShow(@Param("Id")int Id);
	
	//������Ʒid���ҷ���������ѧУ
	public String findSchool(@Param("goodId")int goodId);
	
	//����userId����ͷ���ǳƣ�����
	public userMsg queryNameAndNickAndAvat(@Param("userId")String userId);
	
	//����ͷ����ҵ���Ӧ���û�����Ϊ΢���ڽ���ͷ��洢ʱ��ÿ��ͷ��ĵ�ַһ����Ψһ�ģ����Կ����ҵ�Ψһ���û���
	//�ҵ�����û��󣬷�������û������и�����Ϣ���ʹ��û�������������Ʒ��Ϣ���ٸ�����Щ��Ʒ��Ϣ���ҳ��������ۣ�ͳ�������������������������ó�������
	public userMsg findHomeMsg(@Param("avat")String avat);
	
	//������Ʒidͳ�ƶ�Ӧ������
	public int totalComment(@Param("goodId")int goodId);
	
	//������Ʒidͳ�ƶ�Ӧ������
	public int goodComment(@Param("goodId")int goodId);
	
	//��ӹ�ע
	public int addFollow(@Param("followId")String followId,@Param("followToId")String followToId);
	
	//ȡ����ע
	public int cancelFollow(@Param("followId")String followId,@Param("followToId")String followToId);
	
	//��ѯ��ע״̬
	public followMsg followState(@Param("followId")String followId,@Param("followToId")String followToId);
	
	//�����⻧id���������ж���״̬Ϊ2�Ķ���
	public List<rentMsg> find(@Param("rentId")String rentId,@Param("rentState")int rentState);
	
	//�����û�id���Ҹ��û������й�ע
	public List<followMsg> findFollow(@Param("followId")String followId);
	
	//������Ʒid�޸���Ʒ��Ϣ
	public int modifyGood(@Param("goodId")int goodId,@Param("goodName")String goodName,@Param("unit")int unit,@Param("type")int type,@Param("goodDetail")String goodDetail,@Param("deadline")int deadline,@Param("price")double price,@Param("deposit")int deposit,@Param("introduction")String introduction,@Param("num")int num);
}
