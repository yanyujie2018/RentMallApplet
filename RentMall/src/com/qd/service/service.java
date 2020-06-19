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
	
	//����û���Ϣ�����ݷ��ص�Ӱ�캯���жϳɹ����
	public boolean addUserMsg(String userId){
		int res = op.addUserMsg(userId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//��ѯ�û���Ϣ
	public userMsg findUserMsgById(String userId)
	{
		userMsg res = op.findUserMsgById(userId);
		return res;
	}
	
	//�޸��û��Ա�
	public boolean modiyfSexById(String userId,int sex){
		int res = op.modiyfSexById(userId, sex);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//�޸��û�����
	public boolean modiyfBirthById(String userId,String birthday)
	{
		int res = op.modiyfBirthById(userId, birthday);
		if(res>0)
			return true;
		else 
			return false;
	}
	
	//�޸��û�������Ϣ��ѧУ�����գ�΢�źţ����֤��
	public boolean modifyBaseById(String userId,String idCard,String school,String wxNum,String userName)
	{
		int res = op.modifyBaseById(userId, idCard, school, wxNum, userName);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//�ֿ��޸Ļ�����Ϣ
	//�޸�����
	public boolean modifyNameById(String userId,String userName)
	{
		int res = op.modifyNameById(userId, userName);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//�޸����֤
	public boolean modifyIdCardById(String userId,String idCard){
		int res = op.modifyIdCardById(userId, idCard);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//�޸�΢�ź�
	public boolean modifyWxNumById(String userId,String wxNum){
		int res = op.modifyWxNumById(userId, wxNum);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//�޸�ѧУ
	public boolean modifySchoolById(String userId,String school){
		int res = op.modifySchoolById(userId, school);
		if(res>0)
			return true;
		else
			return false;
	}
	
	
	//�޸��ֻ��Ű�
	public boolean modifyPhoneById(String userId,String phone){
		int res = op.modifyPhoneById(userId, phone);
		if(res>0)
			return true;
		else
			return false;
	}
	
	/*
	//�����Ʒ��Ϣ����ͼƬ��
	public int addGoodMsg(String userId,String goodName,String introduction,String goodDetail,int type,int unit,int deposit,int deadline,double price,String releaseDate){
		int res = op.addGoodMsg(userId, goodName, introduction, goodDetail, type, unit, deposit, deadline, price,releaseDate);
		return res;
	}
	*/
	public int addGoodMsg(goodMsg good){
		int res = op.addGoodMsg(good);
		return res;
	}
	
	// �����ƷͼƬ
	public boolean addGoodImage1(int goodId,String imagePath){
		int res=op.addGoodImage1(goodId, imagePath);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	
	// �����ƷͼƬ
	public boolean addGoodImage2(int goodId,String imagePath){
		int res=op.addGoodImage2(goodId, imagePath);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
		
		
	// �����ƷͼƬ
	public boolean addGoodImage3(int goodId,String imagePath){
		int res=op.addGoodImage3(goodId, imagePath);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//��ѯ���з�����Ϣ
	public List<goodMsg> showAllRelease(String userId){
		return op.showAllRelease(userId);
	}
	
	//���ݷ�������ƷIDɾ����������Ʒ
	public boolean deleteGoodById(int goodId){
		int res = op.deleteGoodById(goodId);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//��ѯ�����еĶ��������������к������У�
	public List<rentMsg> findRent(String rentId,int rentState){
		return op.findRent(rentId,rentState);
	}
	
	
	//��ѯ���黹�Ķ��� 
	public List<rentMsg> findWaitReturn(String rentId,int rentState){
		return op.findRent(rentId,rentState);
	}
	
	//�Ե�ǰ�û�idΪ������id������״̬Ϊ0��������Ϣ
	public List<rentMsg> findWaitSend(String releaseId,int rentState){
		return op.findWaitSend(releaseId, rentState);
	}
	
	
	//�Ե�ǰ�û�idΪ������id������״̬Ϊ0��������Ϣ
	public List<rentMsg> findWaitSend2(String releaseId){
		return op.findWaitSend2(releaseId);
	}
	
	//�Ե�ǰ�û�idΪ�ջ���id������״̬Ϊ0����1��������Ϣ(���ջ�����)
	public List<rentMsg> findWaitReceive(String rentId){
		return op.findWaitReceive(rentId);
	}
	
	//�����û����е�����ɶ�����������Ϊ�����ߵ����ж���
	public List<rentMsg> findFinishByRent(String userId)
	{
		return op.findFinishByRent(userId);
	}
	
	//�����û����е�����ɶ�����������Ϊ�����ߵ����ж���
	public List<rentMsg> findFinishByRelease(String userId)
	{
		return op.findFinishByRelease(userId);
	}
	
	//���Ե�ǰ�û�IDΪrentID��ѯ����״̬Ϊ2���ҳ�������ʱ��Ķ������޸�״̬Ϊ3
	public boolean modifyRentState(String rentId,String currentDate){
		int res = op.modifyRentState(rentId,currentDate);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//��ȡ�ղ�����
	public List<collectMsg> collectQuery(String collectUserId){
		return op.collectQuery(collectUserId);
	}
	
	//����idɾ���ղص���Ʒ
	public boolean deleteCollect(int id){
		int res = op.deleteCollect(id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//��ѯ��������
	public rentDetails QueryRentDetail(int Id){
		return op.QueryRentDetail(Id);
	}
	
	
	//����ջ��������ջ�״̬1����Ϊ2������
	public boolean modifyRentState1To2(int Id){
		int res = op.modifyRentState1To2(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//����ջأ������ջ�״̬7����Ϊ4�������
	public boolean modifyRentState7To4(int Id){
		int res = op.modifyRentState7To4(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//����黹�������黹״̬3����Ϊ7���ջ�
	public boolean modifyRentState3To7(int Id){
		int res = op.modifyRentState3To7(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//�����������������״̬0����Ϊ1���ջ�
	public boolean modifyRentState0To1(int Id){
		int res = op.modifyRentState0To1(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	
	//������ɺ�,��״̬��4��Ϊ6
	public boolean modifyRentState4To6(int Id){
		int res = op.modifyRentState4To6(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//��״̬��������2��Ϊ�黹��3
		public boolean modifyRentState2To3(int Id){
			int res = op.modifyRentState2To3(Id);
			if(res>0)
			{
				return true;
			}else
				return false;
		}
		
	//��״̬��������2��Ϊ�����5
	public boolean modifyRentState2To5(int Id){
		int res = op.modifyRentState2To5(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//��״̬�ӹ黹��3��Ϊ�����5
	public boolean modifyRentState3To5(int Id){
		//System.out.println("service3to5");
		int res = op.modifyRentState3To5(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//��״̬�������Ѻ�����5��Ϊ������Ѻ�����8
	public boolean modifyRentState5To8(int Id){
		int res = op.modifyRentState5To8(Id);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//��ȡ�û�����
	public int getUserIntegration(String userId){
		return op.getUserIntegration(userId);
	}
	
	//����������Ϣ��id��ȡ��������Ʒ��Ϣ���û��绰����
	public String getReleasePhone(int Id){
		return op.getReleasePhone(Id);
	}
	
	//����������Ϣ��id��ȡ���ø���Ʒ���û��绰����
	public String getRentPhone(int Id){
		return op.getRentPhone(Id);
	}
	
	//�鿴�û��Ƿ���������Ϣ����д���ֻ�����
	public String checkPhone(String userId){
		return op.checkPhone(userId);
	}
	
	
	//������Ʒ��������ҵ����������࣬�һ������õ���Ʒ��Ϣ
	public List<goodMsg> getHotGood(){
		return op.getHotGood();
	}
	

	//��ȡ���������������Ʒ��Ϣ
	public List<goodMsg> getNewGood(){
		return op.getNewGood();
	}
	
	//��ȡ�����������Ʒ����������Ʒ��Ϣ
	public List<goodMsg> getHotGroceries(){
		return op.getHotGroceries();
	}
	
	//��ȡ�����������ͼ��/���Ӳ�Ʒ/���й�����Ϣ
	public List<goodMsg> getHot(int type){
		return op.getHot(type);
	}
	
	//������ƷgoodId��ȡ��Ʒ��ϸ��Ϣ
	public goodMsg getGoodDetail(int goodId){
		return op.getGoodDetail(goodId);
	}
	
	//�ղ���Ʒ�����ղر�
	public boolean collectGood(String collectUserId,int collectGoodId,String collectDate){
		int res = op.collectGood(collectUserId, collectGoodId,collectDate);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//��Ʒ�ղ�����1
	public boolean addCollect(int goodId){
		int res = op.addCollect(goodId);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//ȡ���ղ�(�����û�id����Ʒid)
	public boolean cancelcollect(String collectUserId,int collectGoodId){
		int res = op.cancelcollect(collectUserId, collectGoodId);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//��Ʒ�ղ�����1
	public boolean subCollect(int goodId){
		int res = op.subCollect(goodId);
		if(res>0)
		{
			return true;
		}else
			return false;
	}
	
	//������Ʒid���û�id�鿴���û��Ƿ��ղظ���Ʒ
	public boolean findCollect(String collectUserId,int collectGoodId){
		collectMsg res = op.findCollect(collectUserId, collectGoodId);
		if(res!=null){
			return true;
		}else
			return false;
	}
	
	//������Ʒid��ȡ����
	public List<commentMsg> findComment(int commentGoodId){
		return op.findComment(commentGoodId);
	}
	
	//��ӹ��ﳵ
	public boolean addCart(String cartUserId,int cartGoodId,int cartNum,int timeLen){
		int res = op.addCart(cartUserId, cartGoodId, cartNum, timeLen);
		if(res>0){
			return true;
		}else
			return false;
	}
		
	
	//�����û�id��ȡ�û��Ĺ��ﳵ����
	public List<cartMsg> fingCartByUser(String cartUserId){
		return op.fingCartByUser(cartUserId);
	}
	
	//�ڹ��ﳵ���޸�cartNum
	public boolean modifyCartNumAdd(int cartId){
		int res = op.modifyCartNumAdd(cartId);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	//�ڹ��ﳵ���޸�cartNum
	public boolean modifyCartNumSub(int cartId){
		int res = op.modifyCartNumSub(cartId);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	
	//���ݹ��ﳵ��Ϣ��id��ɾ��
	public boolean deleteCartById(int cartId){
		int res = op.deleteCartById(cartId);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	//�ڹ��ﳵ���޸�timeLen
	public boolean modifyCartTimeAdd(int cartId){
		int res = op.modifyCartTimeAdd(cartId);
		if(res>0){
			return true;
		}else
			return false;
	}
		
	//�ڹ��ﳵ���޸�timeLen
	public boolean modifyCartTimeSub(int cartId){
		int res = op.modifyCartTimeSub(cartId);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	
	//����Ʒ���뵽���ޱ���,������״̬Ϊ0
	public boolean addRentMsg(int goodId,String releaseId,String rentId,String payDate,int rentNum,int rentLen,String remarks){
		int res = op.addRentMsg(goodId, releaseId, rentId, payDate, rentNum,rentLen,remarks);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	
	//�û��ӹ��ﳵ�µ����޸���Ʒ��
	public boolean modifyGoodMsg(int goodId,int cartNum){
		int res = op.modifyGoodMsg(goodId, cartNum);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	
	//�������
	public boolean addComment(int commentGoodId,String content,int degree,String commentUserId,int anonymous,String date){
		int res = op.addComment(commentGoodId, content, degree, commentUserId, anonymous, date);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	
	//�������۵ķ������Է����ߵĻ��ֽ����޸ģ�1���ǲ��ӷ֣�2-3���Ǽ�10�֣�4���Ǽ�20�֣�5���Ǽ�30�֣�
	public boolean modifyReleaseScore(String userId,int add){
		int res = op.modifyReleaseScore(userId, add);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	
	//������Ʒid��������ɵ���Ʒ�����ָ�ԭ״
	public boolean addNumAfterTrade(int goodId,int rentNum){
		int res = op.addNumAfterTrade(goodId, rentNum);
		if(res>0){
			return true;
		}else
			return false;
	}

	
	//��������id�޸����ޱ��е�rentDate��dueTime
	public boolean modifyRentTime(@Param("Id")int Id,String rentDate,String dueTime){
		int res = op.modifyRentTime(Id,rentDate,dueTime);
		if(res>0){
			return true;
		}else
			return false;
	}
	
	
	//���������key��goodmsg��ģ����ѯ�����Ա����֣���飬���飩
	public List<goodMsg> searchByKey(String key,int type){
		return op.searchByKey(key,type);
	}
	
	//����rentID��ѯ�������Ʒ�Ĺ黹�����Ǽ���
	public int queryReturnTime(int goodId){
		return op.queryReturnTime(goodId);
	}

	
	//�����û�id�۳��û�����
	public boolean subScoreById(String userId){
		int res = op.subScoreById(userId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//�����û�id�����û�idstateΪ1����ʾ���⣬Ĭ��Ϊ0
	public boolean updateIdState(String userId){
		int res = op.updateIdState(userId);
		if(res>0)
			return true;
		else
			return false;
				
	}
	
	//��������idɾ������
	public boolean deleteCommentById(int commentId){
		int res = op.deleteCommentById(commentId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//������ƷID������Ʒ��views��1
	public boolean addViews(int goodId){
		int res = op.addViews(goodId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//�����û�userId������û���ǰ��û����������
	public String getUserName(String userId){
		return op.getUserName(userId);
	}
	
	
	//�����û���userId����û���΢���ǳƺ�ͷ�����usermsg
	public boolean addNickAndUrl(String userId,String avatarUrl,String nickName){
		System.out.println("service="+nickName);
		int res = op.addNickAndUrl(userId, avatarUrl, nickName);
		if(res>0)
			return true;
		else
			return false;
	}
	
	
	//������Ʒ��goodID�ҵ������ߵĵ绰
	public String findPhoneByGoodId(int goodId){
		return op.findPhoneByGoodId(goodId);
	}
	
	//�Ȳ��ҵ�¼�ߣ�����userId��������δ�����ۣ�ͨ�����۱�����Ʒ�����飬��������Ʒ�����ߵ�id�������¼���Ƿ���ͬ�������ͬ�ǾͿ����Ƿ��Ѷ�����δ���Ķ��ҳ���
	public List<commentMsg> findNoReadComment(String userId){
		return op.findNoReadComment(userId);
	}
	
	//��������id������ûδ���Ļظ�������order��������
	public List<replyMsg> findNoReadReply(int replyComment,String replyToId){
		return op.findNoReadReply(replyComment,replyToId);
	}
	
	//��������commentID�޸��Ѷ�
	public boolean modifyCommentRead(int commentId){
		int res = op.modifyCommentRead(commentId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//��������commentID�����������µ�δ���ظ�����Ϊ�Ѷ�
	public boolean modifyReplyRead(int Id){
		int res = op.modifyReplyRead(Id);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//�����Ѷ����ۣ����ݵ�¼�ߵ�id�����ҳ������˶��ڵ�½�߷�������Ʒ�����ۣ����ҵ�½���Ѿ��Ķ�����
	//������ҳ��������ۣ�Ҫ��֤���Իظ�Ҳ���Ѷ���
	//�����Ȳ���������Ѷ����ۣ���controller���ٶԲ����ÿһ�����ۣ��鿴��û��δ���ظ����������ô�ʹ�list��ɾ��������Ϣ
	public List<commentMsg> findALLComment(String userId){
		return op.findALLComment(userId);
	}
	
	//�Ȳ����������Ʒ��Ϣ���̼���Ϣ(�����controller���ú�����)���ٲ������Ʒ�ķ�����Ϣ
	public goodMsg queryGood(int goodId){
		return op.queryGood(goodId);
	}
	
	//������Ʒid������Ʒ�����ߵ���Ϣ
	public userMsg queryGoodUser(int goodId){
		return op.queryGoodUser(goodId);
	}
	
	
	//��������id��������
	public commentMsg findCommentById(int commentId){
		return op.findCommentById(commentId); 
	}
	
	//��������id���Ҹ����������������лظ�
	public List<replyMsg> findReply(int commentId){
		return op.findReply(commentId);
	}
	
	//��ӻظ�
	public boolean addReply(int replyComment,String replyId,String replyToId,String replyContent,String replyDate,Integer sequence){
		int res = op.addReply(replyComment, replyId, replyToId, replyContent,replyDate,sequence);
		System.out.println("��ӻظ�="+res);
		if(res>0)
			return true;
		else
			return false;
		
	}
	
	
	//����replyComment���ҵ����������´������Ļظ��Ĵ����
	public Integer findMaxSequence(int replyComment){
		return op.findMaxSequence(replyComment);
	}
	
	//ɾ���ظ������ݻظ����id
	public boolean deleteReply(int Id){
		int res = op.deleteReply(Id);
		if(res>0)
			return true;
		else
			return false;
	}
	
	
	//�Ե�¼��id��ΪreplyToID ��ѯ�����һ��δ���ظ���Ϣ��id
	public int findreplyId(String replyToId){
		return op.findreplyId(replyToId);
	}
	
	//�Իظ����id ��ѯ�����һ��δ���ظ���Ϣ
	public replyMsg findLastReply(int Id){
		return op.findLastReply(Id);
	}
	
	//���Ҹ��������µĻظ�����������commentID���ң����ﲻ�������ǲ��Ƿ�����½�ߵģ�
	public int replyNum(int replyComment){
		return op.replyNum(replyComment);
	}
	
	
	//����commentID������һ���ظ���Ϣ
	public replyMsg findLastReply2(int replyComment){
		return op.findLastReply2(replyComment);
	}
	
	//������Ʒid���ͶԻ�˫����id����ѯ��������Ϣ
	public chatMsg queryChat(int chatGoodId,String chatA,String chatB){
		return op.queryChat(chatGoodId, chatA, chatB);
	}
	
	//������������Ϣ��id��ѯ����������Ϣ
	public List<afterChatMsg> queryChatAfter(int mainChatId){
		return op.queryChatAfter(mainChatId);
	}
	
	
	//����goodid��openId����ΪchatRightId����usermsg.userId����ΪchatLeftId)���������Ϣ
	public boolean addChat(int chatGoodId,String chatRightId,String chatLeftId,String chatDate,String chatContent,String chatTime){
		int res = op.addChat(chatGoodId, chatRightId, chatLeftId,chatDate,chatContent,chatTime);
		if(res>0)
			return true;
		else
			return false;
	}
	
	
	//����goodid��openId����ΪchatRightId����usermsg.userId����ΪchatLeftId)���������Ϣ
	public boolean addChatImage(int chatGoodId,String chatRightId,String chatLeftId,String chatDate,String chatContent,String chatTime){
		int res = op.addChatImage(chatGoodId, chatRightId, chatLeftId,chatDate,chatContent,chatTime);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//������������Ϣ��id�������������Ϣ
	public boolean addChatAfter(int mainChatId,String chatRightIdA,String chatLeftIdA,String chatContent,String chatDate,String chatTime){
		int res = op.addChatAfter(mainChatId, chatRightIdA, chatLeftIdA, chatContent, chatDate, chatTime);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//������������Ϣ��id�������������Ϣ
	public boolean addChatAfterImage(int mainChatId,String chatRightIdA,String chatLeftIdA,String chatContent,String chatDate,String chatTime){
		int res = op.addChatAfterImage(mainChatId, chatRightIdA, chatLeftIdA, chatContent, chatDate, chatTime);
		if(res>0)
			return true;
		else
			return false;
	}
		
	
	//���������ԣ����ݵ�¼��id����chatRightId��chatLeftId=id����������Ϣ
	public List<chatMsg> findMainChat(String openId){
		return op.findMainChat(openId);
	}
	
	//��������id�������Ը�Ϊ�Ѷ�
	public boolean setMainRead(int Id,String chatLeftId){
		int res = op.setMainRead(Id,chatLeftId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//����mainChatId�������������Ѷ�
	public boolean setAfterRead(int mainChatId,String chatLeftId){
		int res = op.setAfterRead(mainChatId,chatLeftId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//����������Ϣ����ʾ
	public boolean modifyShowState(int Id,int isShowR,int isShowL){
		int res=op.modifyShowState(Id, isShowR, isShowL);
		if(res>0)
			return true;
		else
			return false;
	}
	
	
	//����id������������Ϣ
	public chatMsg findMainChatById(int Id){
		return op.findMainChatById(Id);
	}
	
	//����������Ϣ��isShow����Ϊ1
	public boolean updateShow(int Id){
		int res = op.updateShow(Id);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//������Ʒid���ҷ���������ѧУ
	public String findSchool(int goodId){
		return op.findSchool(goodId);
	}
	
	//����userId����ͷ���ǳƣ�����
	public userMsg queryNameAndNickAndAvat(String userId){
		return op.queryNameAndNickAndAvat(userId);
	}
	
	//����ͷ����ҵ���Ӧ���û�����Ϊ΢���ڽ���ͷ��洢ʱ��ÿ��ͷ��ĵ�ַһ����Ψһ�ģ����Կ����ҵ�Ψһ���û���
	//�ҵ�����û��󣬷�������û������и�����Ϣ���ʹ��û�������������Ʒ��Ϣ���ٸ�����Щ��Ʒ��Ϣ���ҳ��������ۣ�ͳ�������������������������ó�������
	public userMsg findHomeMsg(String avat){
		return op.findHomeMsg(avat);
	}
	
	//������Ʒidͳ�ƶ�Ӧ������
	public int totalComment(int goodId){
		return op.totalComment(goodId);
	}
	
	//������Ʒidͳ�ƶ�Ӧ������
	public int goodComment(int goodId){
		return op.goodComment(goodId);
	}
	
	//��ӹ�ע
	public boolean addFollow(String followId,String followToId){
		int res = op.addFollow(followId, followToId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	//ȡ����ע
	public boolean cancelFollow(String followId,String followToId){
		int res = op.cancelFollow(followId, followToId);
		if(res>0)
			return true;
		else
			return false;
	}
	
	
	//��ѯ��ע״̬
	public followMsg followState(String followId,String followToId){
		return op.followState(followId, followToId);
	}
	
	//�����⻧id���������ж���״̬Ϊ2�Ķ���
	public List<rentMsg> find(String rentId,int rentState){
		return op.find(rentId, rentState);
	}
	
	//�����û�id���Ҹ��û������й�ע
	public List<followMsg> findFollow(String followId){
		return op.findFollow(followId);
	}
	
	//������Ʒid�޸���Ʒ��Ϣ
	public boolean modifyGood(int goodId,String goodName,int unit,int type,String goodDetail,int deadline,double price,int deposit,String introduction,int num){
		int res = op.modifyGood(goodId, goodName, unit, type, goodDetail, deadline, price, deposit, introduction, num);
		if(res>0)
			return true;
		else
			return false;
	}
}
