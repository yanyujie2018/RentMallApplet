// pages/index/message/message.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    uid: '',
    winWidth: 0,
    winHeight: 0,
    // tab切换  
    currentTab: 0,
    goodsComments: [],
    goodsAllComments: [],
    content: '',
    goodsId: '',
    replyId: '',
    replyToId:'',
    commentId: '',
    releaseName: '',
    releaseFocus: false,
    refresh: 1,
    from_state:'',
    allChat:'',  //用来接收所有的留言信息
    showModal:false,
    delete_Right:'',   //要删除的留言信息的对话者id1
    delete_Left:'',
    delete_id:'',
    delete_index:'',
    total_Comment:0,
    total_Chat:0

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    /** 
   * 获取系统信息 
   */
    var that = this;
    wx.getSystemInfo({

      success: function (res) {
        that.setData({
          winWidth: res.windowWidth,
          winHeight: res.windowHeight
        });
      }
    });
    
    that.getUnreadComment()   //查找未读评论
    that.getAllComments()   //查找已读评论
    that.getChatMsg()   //查找聊天记录

    this.setData({
      releaseFocus:false,
      showModal:false
    })
    wx.hideShareMenu();  //只能去掉三个小点中的转发功能，没法取消三个小点
    console.log("消息页面未读条数="+getApp().globalData.noRead)
    if(getApp().globalData.noRead>0){
      wx.setTabBarBadge({
        index: 3,
        text: String(getApp().globalData.noRead),
      })
    }else{
      wx.removeTabBarBadge({
        index: 3,
      })
    }
    

  },


  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.getUnreadComment()
    this.getAllComments()
    this.getChatMsg()
    var that = this
    this.setData({
      refresh: this.data.refresh + 1,
      releaseFocus:false,
      showModal:false
    })
    setTimeout(function () {
      if (getApp().globalData.noRead == that.data.total_Chat + that.data.total_Comment) {
        console.log("YES")
      } else {
        console.log("NO")
        getApp().globalData.noRead = that.data.total_Chat + that.data.total_Comment
        if (getApp().globalData.noRead > 0) {
          wx.setTabBarBadge({
            index: 3,
            text: String(getApp().globalData.noRead),
          })
        } else {
          wx.removeTabBarBadge({
            index: 3,
          })
        }
      }
    }, 500)

  },


  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  /** 
    * 滑动切换tab，切换tab时，也要针对对应的tab进行信息的查找并且显示在消息的小红圈中 
    */
  bindChange: function (e) {

    var that = this;
    console.log("滑动改变tab="+e.detail.current)
    if (e.detail.current==0)
    {
      this.getUnreadComment();
    }else if(e.detail.current==1){
      this.getAllComments();
    }else{
      this.getChatMsg();
    }

    setTimeout(function(){
      if(getApp().globalData.noRead==that.data.total_Chat+that.data.total_Comment){
        console.log("YES")
      }else{
        console.log("NO")
        getApp().globalData.noRead=that.data.total_Chat+that.data.total_Comment
        if (getApp().globalData.noRead > 0){
          wx.setTabBarBadge({
            index: 3,
            text: String(getApp().globalData.noRead),
          })
        }else{
          wx.removeTabBarBadge({
            index: 3,
          })
        }
      }
    },500)

    that.setData({ 
      currentTab: e.detail.current,
      releaseFocus:false, 
    });
    


  },

  /** 
   * 点击tab切换（如果是通过点击tab进行切换的话，那么该函数和滑动tab的函数均会调用，但是如果是通过滑动tab切换的话，只会调用滑动tab的函数）
   */
  swichNav: function (e) {

    var that = this;
    console.log("点击tab切换")
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current
      })
    }
  },

  /**
   * 获取未读评论
   */
  getUnreadComment: function () {
    var that = this
    //先查找登录者（根据userId）的所有未读评论（通过评论表与商品表联查，查出这个商品发布者的id，看与登录者是否相同，如果相同那就看看是否已读，将未读的都找出来
    //其实未读评论的查找中应该也包括评论本身已读但是存在未读回复的信息
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/findNoReadComment.do',
      data: { "userId": getApp().globalData.openid },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        console.log("未读评论")
        console.log(res);
        let sum = 0;
        for (var i = 0; i < res.data.list.length; i++) {
          if (res.data.list[i].isRead == 0)  //本身未读，也要算上
            sum = sum + 1
          sum = sum + res.data.list[i].replyNoRead
        }
        console.log("sum2="+sum)
        that.setData({
          goodsComments:res.data.list,
          total_Comment:sum
        })
      }
    })

  },

  /**
   * 获取已读评论
   */
  getAllComments: function () {
    var that = this
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/findALLComment.do',
      data: { "userId": getApp().globalData.openid },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        console.log("查找已读评论")
        console.log(res);
        that.setData({
          goodsAllComments:res.data.list
        })
      }
    })

  },

  //获取留言记录
  getChatMsg(){
    var that = this;
    var list=[]
    var openid=getApp().globalData.openid
    //先查找主留言(页面上是否显示该条留言需要在页面上对isShowR和isShowL，openId和chatRightID，chatLeftID综合判断),再查找每条主留言下的附属留言，并查出最后一条留言加入查出的主留言中
    //如果没有下属留言，那么主留言就是最后一条留言
    //如果有下属留言，那么最后一条留言为下属的最后一条留言
    //统计未读留言数，加入查出的每一条主留言中
    //页面上显示的聊天框的头像和昵称姓名永远都是对方的，即不可能是登陆者的
    //所以再查出主留言的非登陆者对象的头像和昵称，姓名。
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/findAllChat.do',
      data: { "openId": getApp().globalData.openid },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        console.log("获取所有留言信息");
        console.log(res);
        //查出来后，查看每一条主留言的显示状态，然后在每一个数组中对应的加上show，设置为true或者false，表示是否显示
        list=res.data.allChat;
        let sum = 0;
        for(var i=0;i<list.length;i++)
        {
          if (list[i].chatRightId == openid && list[i].isShowR==1 && list[i].noRead == 0){  //没有未读留言才会不显示
            list[i].show=false
          }else{
            list[i].show = true
          }
          if (list[i].chatLeftId == openid && list[i].isShowL == 1 && list[i].noRead == 0) {
            list[i].show = false
          } else {
            list[i].show = true
          }
          sum = sum + list[i].noRead
        }
        console.log("测试list")
        console.log("sum1="+sum)
        console.log(list)
        that.setData({
          allChat: list,
          total_Chat:sum
        })
      }
    })
  },

  //将所有的留言信息设置为已读
  allReadChat(){
    var chat = this.data.allChat
    var that = this;
    console.log("设置全部留言为已读")
    for(var i=0;i<chat.length;i++){
      if(chat[i].noRead>0){
        var no = chat[i].noRead
        wx.request({
          url: 'http://ZLofCampus.top/RentMall/setAllChatRead.do',
          data: { "Id": chat[i].id, "len": chat[i].afterChatList.length,"openId":getApp().globalData.openid },
          method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
          header: { "content-type": "application/json" }, // 设置请求的 header
          success: function (res) {
            console.log("设置所有留言为已读")
            console.log(res)
            if (res.data.msg1 && res.data.msg2) {
              //对全局的noRead进行更新
              getApp().globalData.noRead = getApp().globalData.noRead - no
              if(getApp().globalData.noRead>0){
                //重新设置小红圈中的数字显示
                wx.setTabBarBadge({
                  index: 3,
                  text: String(getApp().globalData.noRead),
                })
              }else{  //已经没有未读消息，那么就去掉显示
                wx.removeTabBarBadge({
                  index: 3,
                })
              }
              //重新查找留言信息
              that.getChatMsg();
            } else {
              wx.showToast({
                title: '网络出错啦....',
                icon: 'none',
                duration: 1000
              })
            }
          }
        })
        
      }
    }
  },

  //点击进入留言详情页面
  goChat(e){
    var that =this
    console.log("进入留言详情")
    console.log(e);
    var Id = e.currentTarget.dataset.id;
    var index = e.currentTarget.dataset.index
    var len = this.data.allChat[index].afterChatList.length;
    var chatGoodId = e.currentTarget.dataset.chatgoodid;
    var chatRightId = e.currentTarget.dataset.chatrightid;
    var chatLeftId = e.currentTarget.dataset.chatleftid;
    var noread = e.currentTarget.dataset.noread
    //先将这条主留言和其下属留言该为已读
    //之后再跳转详情页面
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/setAllChatRead.do',
      data: { "Id": Id, "len": len,"openId":getApp().globalData.openid},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        console.log("测试1hhhhhhh")
        console.log(res)
        if(res.data.msg1 && res.data.msg2) 
        {
          console.log("测试2hhhhhhh")
          //对全局的noRead进行更新
          getApp().globalData.noRead = getApp().globalData.noRead - that.data.allChat[index].noRead
          if (getApp().globalData.noRead > 0) {
            //重新设置小红圈中的数字显示
            wx.setTabBarBadge({
              index: 3,
              text: String(getApp().globalData.noRead),
            })
          } else {  //已经没有未读消息，那么就去掉显示
            wx.removeTabBarBadge({
              index: 3,
            })
          }
          //跳转页面
          wx.navigateTo({
            url: '../chat/chat?chatGoodId=' + chatGoodId + "&state=1&Id=" + Id+"&chatRightId="+chatRightId+"&chatLeftId="+chatLeftId,
          })
        }else{
          console.log("测试3hhhhhhh")
          wx.showToast({
            title: '网络出错啦....',
            icon: 'none',
            duration: 1000
          })
        }
      }
    })
    
  },


  //在message页面直接回复的回复框点击提交按钮之后调用的函数
  replyCommentHandler: function (event) {
    var that = this;
    console.log(event.detail.value) 
    if (event.detail.value.content == "") {
      wx.showToast({
        title: "评论不能为空",
        icon: "none",
        // image: '/pages/images/warning.png',
        duration: 2000
      })
      return false;
    }

    
    var replyContent = event.detail.value.content
    var goodsId = this.data.goodsId
    var replyComment = this.data.commentId
    var replyId=this.data.replyId
    var replyToId = this.data.replyToId
    var state = this.data.from_state
    
    if(state==0){  //是从未读页面来的，那么就需要修改已读
      //先将评论以及下属回复置为已读，成功之后再提交回复
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/modifyCommentRead.do',
        data: { "commentId": replyComment, "userId": getApp().globalData.openid },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("标记已读回复2")
          console.log(res);
          if (res.data.msg1 == true && res.data.msg2 == true) {  //修改已读成功
            
            //对全局的noRead进行更新
            getApp().globalData.noRead = getApp().globalData.noRead - res.data.noRead
            if (getApp().globalData.noRead > 0) {
              //重新设置小红圈中的数字显示
              wx.setTabBarBadge({
                index: 3,
                text: String(getApp().globalData.noRead),
              })
            } else {  //已经没有未读消息，那么就去掉显示
              wx.removeTabBarBadge({
                index: 3,
              })
            }
            

            //添加回复信息
            wx.request({
              url: 'http://ZLofCampus.top/RentMall/addReply.do',
              data: { "replyComment": replyComment, "replyId": replyId, "replyToId": replyToId, "replyContent": replyContent },
              method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
              header: { "content-type": "application/json" }, // 设置请求的 header
              success: function (res) {
                console.log("添加回复")
                console.log(res)
                if (res.data.msg) {
                  wx.showToast({
                    title: '回复成功！',
                    icon: 'none',
                    duration: 1500,
                  })
                  // 将当前评论内容置空，下次继续评论
                  that.setData({
                    content: '',
                    releaseFocus:false  //隐藏起来回复框，再次点击回复时在出来
                  });
                  // 评论后重新获取全部已读和未读评论内容
                  that.getUnreadComment();
                  that.getAllComments();
                }
              }
            })
          } else {
            wx.showToast({
              title: '网络出错啦....',
              icon: 'none',
              duration: 1000
            })
          }
        }
      })
    }else{      //是从已读页面来的，直接回复即可，不需要修改已读
      console.log("已读页面直接回复")
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/addReply.do',
        data: { "replyComment": replyComment, "replyId": replyId, "replyToId": replyToId, "replyContent": replyContent },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success:function(res){
          if (res.data.msg) {
            wx.showToast({
              title: '回复成功！',
              icon: 'none',
              duration: 1500,
            })
            // 将当前评论内容置空，下次继续评论
            that.setData({
              content: '',
              releaseFocus: false  //隐藏起来回复框，再次点击回复时在出来
            });
            // 评论后重新获取全部已读和未读评论内容
            that.getUnreadComment();
            that.getAllComments();
          }
        }
      })
    }
    

  },


  /**
  * 点击评论直接进行回复
  */
  bindReply: function (e) {
    console.log("直接回复")
    console.log(e.currentTarget.dataset)
    //如果显示的内容是评论本身的内容，那么回复时的username就是评论者的姓名或昵称（如果是匿名用户的话，那么就是匿名用户）（所以要传过来评论者的name，nickname，anonymous，lastReplyContent）
    //如果显示的内容是最后一条回复的内容，那么回复时的username就是回复的replyID，同时这里也要判断是不是匿名
    //若lastReplyContent为空那么说明显示的评论本身
    //var lastReplyContent = e.currentTarget.dataset.lastreply
   
    var state = e.currentTarget.dataset.state;
    var index = e.currentTarget.dataset.index;
    var anonymous = e.currentTarget.dataset.anonymous
    var userName = e.currentTarget.dataset.username;
    var nickName = e.currentTarget.dataset.nickname;
    var openid = getApp().globalData.openid;
    
    var name=''
    //let relpyToId = e.currentTarget.dataset.commentuserid  //默认回复的对象是评论者
    var toId=''
    var lastReplyContent = null;
    if(state==0)
      lastReplyContent = this.data.goodsComments[index].lastReplyContent  //最后一条评论的内容
    else{
      lastReplyContent = this.data.goodsAllComments[index].lastReplyContent  //最后一条评论的内容
    }
    console.log("测试9"+lastReplyContent)

    if (lastReplyContent == null){  //显示的内容是评论本身
      toId = e.currentTarget.dataset.commentuserid   //发出评论的人的ID
      if (anonymous == 1) {
        name = "匿名用户"
      } else {
        if (userName == '' || userName == null) {
          name = nickName
        } else
          name = userName
      }
    }else{  //显示的内容是最后一条回复
      var userName_R=''
      var nickName_R=''
      var userId_R=''
      if(state==0)
      {
        userName_R = this.data.goodsComments[index].lastReplyContent.userName_from    
        nickName_R = this.data.goodsComments[index].lastReplyContent.nickName_from
        userId_R = this.data.goodsComments[index].lastReplyContent.userId_from
      }else{
        userName_R = this.data.goodsAllComments[index].lastReplyContent.userName_from
        nickName_R = this.data.goodsAllComments[index].lastReplyContent.nickName_from
        userId_R = this.data.goodsAllComments[index].lastReplyContent.userId_from
      }
      toId = userId_R;
      if (anonymous == 1 && userId_R!=openid) {  //回复对方，其该条评论是匿名时，那么对方的名字不会显示
        name = "匿名用户"
      } else if (anonymous == 1 && userId_R == openid) {  //回复自己，那么即使评论是匿名，也会显示出自己的名字或者昵称
        if (userName_R == '' || userName_R == null) {
          name = nickName_R
        } else
          name = userName_R
      } else if (anonymous == 0){  //不是匿名
        if (userName_R == '' || userName_R == null) {
          name = nickName_R
        } else
          name = userName_R
      }
    }

    this.setData({
      releaseFocus: true,
      //replyId: e.currentTarget.dataset.replyid,
      commentId: e.currentTarget.dataset.commentid,
      releaseName: name,
      goodsId: e.currentTarget.dataset.goodsid,
      replyId: openid,
      replyToId: toId,
      from_state:state
    })
  },


  //跳到详情处,可以进行回复
  backGoods: function (e) {
    var index = e.currentTarget.dataset.index
    var state = e.currentTarget.dataset.state
    this.changeRead(e.currentTarget.dataset.commentid,index);  //将点击的评论修改为已读
    console.log("跳到评论详情处")
    console.log("goodid="+e.currentTarget.dataset.commentgoodid)
    if(state==0)
      getApp().globalData.noRead = getApp().globalData.noRead - this.data.goodsComments[index].replyNoRead

    if (getApp().globalData.noRead>0)
    {
      wx.setTabBarBadge({
        index: 3,
        text: String(getApp().globalData.noRead),
      })
    }else{
      wx.removeTabBarBadge({
        index: 3,
      })
    }
    
    wx.navigateTo({
      url: '../commentDetail/commentDetail?commentGoodId=' + e.currentTarget.dataset.commentgoodid+"&commentId="+e.currentTarget.dataset.commentid
    })
  },

  /**
   * 标记已读
   */
  changeRead: function (commentId,index) {
    var that = this

    //这里需要将点击的评论改为已读，然后将这条评论下的回复也均改为已读
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/modifyCommentRead.do',
      data: { "commentId": commentId,"userId":getApp().globalData.openid},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        console.log("标记已读")
        console.log(res)
        if(res.data.msg1==true && res.data.msg2==true)
        {
          //对全局的noRead进行更新
          getApp().globalData.noRead = getApp().globalData.noRead - res.data.noRead
          if (getApp().globalData.noRead > 0) {
            //重新设置小红圈中的数字显示
            wx.setTabBarBadge({
              index: 3,
              text: String(getApp().globalData.noRead),
            })
          } else {  //已经没有未读消息，那么就去掉显示
            wx.removeTabBarBadge({
              index: 3,
            })
          }

           //重新查找已读评论和未读评论
          that.getUnreadComment();
          that.getAllComments();
        }else{
          wx.showToast({
            title: '网络出错啦....',
            icon:'none',
            duration:1000
          })
        }
      }
    })
  },

  /**
   * 全部为已读
   */
  allRead: function () {
    var that = this
    console.log("全部标记为已读")
    var list = this.data.goodsComments;
    for (var i = 0; i < this.data.goodsComments.length; i++) {
      //将每一条评论及其附属的回复都标志为已读
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/modifyCommentRead.do',
        data: { "commentId": list[i].commentId, "userId": getApp().globalData.openid},
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("标记已读")
          console.log(res)
          if (res.data.msg1 == true && res.data.msg2 == true) {

            //对全局的noRead进行更新
            getApp().globalData.noRead = getApp().globalData.noRead - res.data.noRead
            if (getApp().globalData.noRead > 0) {
              //重新设置小红圈中的数字显示
              wx.setTabBarBadge({
                index: 3,
                text: String(getApp().globalData.noRead),
              })
            } else {  //已经没有未读消息，那么就去掉显示
              wx.removeTabBarBadge({
                index: 3,
              })
            } 

            //重新查找已读评论和未读评论
            that.getUnreadComment();
            that.getAllComments();
          } else {
            wx.showToast({
              title: '网络出错啦....',
              icon: 'none',
              duration: 1000
            })
          }
        }
      })

      /*
      //  list.push(this.data.goodsComments[i].id);
      get('/comments/isRead/' + this.data.goodsComments[i].id, null).then((res) => {
        if (res.statusCode == '200') {
          //  that.setData({
          //    goodsAllComments: res.data
          //  })
        } else {
          wx.showToast({
            title: res.data.message,
            icon: "none",
            // image: '/pages/images/warning.png',
            duration: 2000
          })
        }
      });
      */

      //  console.log(this.data.goodsComments[i].id)
    }
    setTimeout(function () {
      this.getUnreadComment()
    }.bind(this), 1000)
  },

  hindChat(e){
    console.log("长按不再显示留言")
    console.log(e)
    var openid = getApp().globalData.openid
    var index = e.currentTarget.dataset.index
    var Id = e.currentTarget.dataset.id
    var chatRightId=e.currentTarget.dataset.chatrightid
    var chatLeftId = e.currentTarget.dataset.chatleftid
    //显示弹窗
    this.setData({
      showModal:true,
      delete_Left:chatLeftId,
      delete_Right:chatRightId,
      delete_id:Id,
      delete_index:index
    })
  },


  //关闭提示框
  hideModal: function () {
    this.setData({
      showModal: false,
    });
  },
  
  //点击提示框的取消之后
  onCancel: function () {
    this.hideModal();
  },

  /**
   * 对话框确认按钮点击事件
   */
  onConfirm: function () {
    //留言信息删除（即如果没有新信息将不再显示给该登陆者）
    this.noShowChat();
    this.hideModal();  //关闭提示框
  },

  //将这条留言对于本登陆者设置为不再显示，除非有新的留言，否则不再显示
  noShowChat(){
    var that=this
    var openid = getApp().globalData.openid
    var deleteR = this.data.delete_Right;
    var deleteL = this.data.delete_Left
    var deleteId = this.data.delete_id
    var index = this.data.delete_index
    var isShowR=0;
    var isShowL=0;
    if(deleteR==openid){
      isShowR=1;
    }else if(deleteL==openid){
      isShowL=1;
    }
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/modifyShowState.do',
      data: { "Id": deleteId, "isShowR": isShowR, "isShowL": isShowL },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        if(res.data.msg==false)
        {
          wx.showToast({
            title: '网络出错啦....',
            icon: 'none',
            duration: 1000
          })
        }else{
          console.log("不再显示成功")
          var tmp = that.data.allChat
          tmp[index].show=false;
          that.setData({
            allChat:tmp
          })
          //然后如果这条留言还有未读留言，那么就都要修改为已读
          if(that.data.allChat[index].noRead>0){
            wx.request({
              url: 'http://ZLofCampus.top/RentMall/setAllChatRead.do',
              data: { "Id": that.data.allChat[index].id, "len": that.data.allChat[index].afterChatList.length,"openId":getApp().globalData.openid},
              method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
              header: { "content-type": "application/json" }, // 设置请求的 header
              success: function (res) {
                if (res.data.msg1 && res.data.msg2) {
                  console.log("删除后，修改已读留言成功")
                  //重新查找留言信息
                 // that.getChatMsg();
                } else {
                  console.log("删除后，修改已读留言失败")
                }
              }
            })
              
          }
          
        }
      }
    })
    
  },

  
})