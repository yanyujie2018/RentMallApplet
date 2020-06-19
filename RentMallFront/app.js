//app.js
import request from './utils/request.js'
App({
  onLaunch: function () {
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    var that = this
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)
    // 登录，回调函数中的code是当前用户登录小程序的凭证，需要将这个凭证传回后台服务器，通过后台服务器向微信服务器去请求用户的唯一标识openId和会话秘钥session_key
    wx.login({
      success: res => {
        console.log("res.code=");
        console.log(res.code);
        //getApp().globalData.code = res.code;  //将用户的登录凭证放入全局变量，但是只要五分钟有效
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        wx.request({
          url: 'http://ZLofCampus.top/RentMall/getOpenid.do',
          data: {code:res.code},
          method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
          header: { "content-type": "application/json" }, // 设置请求的 header
          success: function (res) {
            console.log("getOpenId")
            console.log(res.data.openId);
            var openid = res.data.openId;
            getApp().globalData.openid=openid;
            //判断是否第一次注册
            wx.request({
              url: 'http://ZLofCampus.top/RentMall/findUserMsgById.do',
              data: {"userId": openid },
              method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
              header: { "content-type": "application/json" }, // 设置请求的 header
              success: function (res) {
                console.log("注册")
                console.log(res);
                //将用户的信息存储在全局变量中
                getApp().globalData.usermes=res.data.usermsg;
                // console.log(res.data[0]);
                if (res.data.querymsg) {  //判断是否第一次注册结束
                  console.log("账户已注册");
                  //获取用户积分
                  wx.request({
                    url: 'http://ZLofCampus.top/RentMall/getUserIntegration.do',
                    data: { "userId": openid },
                    method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                    header: { "content-type": "application/json" }, // 设置请求的 header
                    success: function (res) {
                      getApp().globalData.userIntegration = res.data.userIntegration
                      console.log(getApp().globalData.userIntegration)
                      if (res.data.userIntegration<0)//如果积分小于0,设置id状态为封号状态
                      {
                        wx.request({
                          url: 'http://ZLofCampus.top/RentMall/updateIdState.do',
                          data: { "userId": openid },
                          method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                          header: { "content-type": "application/json" }, // 设置请求的 header
                          success:function(res){
                            if(res.data.msg){
                              getApp().globalData.idState = 1;  //表示封锁账号
                              wx.showModal({
                                title: '提示',
                                content: '您的账号已被封锁，解封请联系客服！',
                                success:function(res){
                                  if(res.confirm){
                                    wx.switchTab({
                                      url: '/pages/self/self',
                                    })
                                  }
                                }
                              })
                            }
                          }
                        })
                      }
                    }
                  })

                  var list1=[];
                  var list2=[];
                  var list3=[];
                  var req = new request();
                  //先以当前用户ID为rentID查询订单状态为2并且超过租赁时间但是还在归还期限内的订单，修改状态为3
                  //不在期限内的修改为状态5
                  req.getRequest('http://ZLofCampus.top/RentMall/modifyRentState.do',{ "rentId": openid },{ "content-type": "application/json" }).then((res)=>{
                    console.log("测试promise")
                    console.log(res);
                    list1 = res.data.list1;
                    list2 = res.data.list2;
                    console.log("list1="+list1.length);
                    console.log("list2="+list2.length);
                    if(list1.length>0)
                    {
                      wx.showModal({
                        title: '提示',
                        content: '您有商品需要归还！逾期押金将不再退还！',
                        success:function(res){
                          if(list2.length>0)
                          {
                            wx.showModal({
                              title: '提示',
                              content: '部分物品由于未按期归还，押金已扣除，如有特殊情况请主动与物主协商！详见订单页面',
                            })
                            //将积分减少20,注意，这里的扣分是每个商品均要扣分，所以需要循环
                            for(var i=0;i<list2.length;i++)
                            {
                              req.getRequest('http://ZLofCampus.top/RentMall/subScoreById.do', { "userId": openid }, { "content-type": "application/json" }).then((res) => {
                                if (res.data.msg) {
                                  console.log("修改积分成功1");
                                  getApp().globalData.userIntegration = res.data.score
                                }
                                console.log("积分=" + getApp().globalData.userIntegration)
                              }).catch((err) => {
                                console.log("修改积分捕捉错误")
                              })
                            }
                          }
                        }
                      })
                    }else{
                      if (list2.length > 0) {
                        console.log("进入list2 提示及修改积分")
                        wx.showModal({
                          title: '提示',
                          content: '部分物品由于未按期归还，押金已扣除，如有特殊情况请主动与物主协商！详见订单页面',
                        })
                        //将积分减少20,注意，这里的扣分是每个商品均要扣分，所以需要循环
                        for(var i=0;i<list2.length;i++){
                          req.getRequest('http://ZLofCampus.top/RentMall/subScoreById.do', { "userId": openid }, { "content-type": "application/json" }).then((res) => {
                            if (res.data.msg) {
                              console.log("修改积分成功1");
                              getApp().globalData.userIntegration = res.data.score
                            }
                            console.log("积分=" + getApp().globalData.userIntegration)
                          }).catch((err) => {
                            console.log("修改积分捕捉错误")
                          })
                        }
                      }
                    }
                  }).catch((err)=>{
                    console.log("测试错误捕捉")
                    console.log(res);
                  })
                  

                  setTimeout(function(){
                   //查找租户的订单状态为3的，看有没有超过归还期限的
                  req.getRequest('http://ZLofCampus.top/RentMall/modifyRentState2.do', { "rentId": openid },{ "content-type": "application/json" }).then((res)=>{
                    console.log("测试promise2")
                    console.log(res)
                    list3 = res.data.list1
                    console.log("@@@@@@@@@@@@@@")
                    console.log(list2.length);
                    console.log(list3.length);
                    if(list2.length<=0 && list3.length>0)
                    {
                      wx.showModal({
                        title: '提示',
                        content: '部分物品由于未按期归还，押金已扣除，如有特殊情况请主动与物主协商！详见订单页面',
                      })
                    }
                    if(list3.length>0){
                      console.log("进入list3的积分修改")
                      //将积分减少20,注意，这里的扣分是每个商品均要扣分，所以需要循环
                      for (var i=0; i < list3.length; i++) {
                        req.getRequest('http://ZLofCampus.top/RentMall/subScoreById.do', { "userId": openid }, { "content-type": "application/json" }).then((res) => {
                          if (res.data.msg) {
                            console.log("修改积分成功2");
                            getApp().globalData.userIntegration = res.data.score
                          }
                        }).catch((err) => {
                          console.log("修改积分捕捉错误")
                        })
                      }
                    }
                  }).catch((err)=>{
                    console.log("测试错误捕捉2")
                    console.log(err);
                  })
                  },1000)

                  //这里看看经过上面的两种订单的查找和修改后，积分会不会小于0，如果小于0，那么就封号(如果当前已经是封号状态就不用再执行了)
                  setTimeout(function () {
                    if (getApp().globalData.userIntegration < 0 && getApp().globalData.idState==0) {
                      wx.request({
                        url: 'http://ZLofCampus.top/RentMall/updateIdState.do',
                        data: { "userId": openid },
                        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                        header: { "content-type": "application/json" }, // 设置请求的 header
                        success: function (res) {
                          if (res.data.msg) {
                            getApp().globalData.idState = 1;  //表示封锁账号
                            wx.showModal({
                              title: '提示',
                              content: '您的账号已被封锁，解封请联系客服！',
                              success: function (res) {
                                if (res.confirm) {
                                  wx.switchTab({
                                    url: '/pages/self/self',
                                  })
                                }
                              }
                            })
                          }
                        }
                      })
                    }
                  }, 2500)
                }
                else{  //该用户未注册
                  console.log("not find");
                  console.log("openid="+openid);
                  console.log("userInfo="+this.globalData.userInfo);
                  //进行注册操作
                  wx.request({
                    url: 'http://ZLofCampus.top/RentMall/addUserMsg.do',
                    data: {"userId": openid},
                    method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                    header: { "content-type": "application/json" }, // 设置请求的 header
                    success: function (res) {
                      // console.log("注册时的nickname为：" + getApp().globalData.userInfo.nickName);
                      console.log("账号注册成功！");
                      //获取用户积分
                      wx.request({
                        url: 'http://ZLofCampus.top/RentMall/getUserIntegration.do',
                        data: { "userId": openid },
                        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                        header: { "content-type": "application/json" }, // 设置请求的 header
                        success: function (res) {
                          getApp().globalData.userIntegration = res.data.userIntegration
                          console.log(getApp().globalData.userIntegration)
                          if (res.data.userIntegration < 0)//如果积分小于0,设置id状态为封号状态
                          {
                            wx.request({
                              url: 'http://ZLofCampus.top/RentMall/updateIdState.do',
                              data: { "userId": openid },
                              method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                              header: { "content-type": "application/json" }, // 设置请求的 header
                              success: function (res) {
                                if (res.data.msg) {
                                  getApp().globalData.idState = 1;  //表示封锁账号
                                  wx.showModal({
                                    title: '提示',
                                    content: '您的账号已被封锁，解封请联系客服！',
                                    success: function (res) {
                                      if (res.confirm) {
                                        wx.switchTab({
                                          url: '/pages/self/self',
                                        })
                                      }
                                    }
                                  })
                                }
                              }
                            })
                          }
                        }
                      })

                      var list1 = [];
                      var list2 = [];
                      var list3 = [];
                      var req = new request();
                      //先以当前用户ID为rentID查询订单状态为2并且超过租赁时间但是还在归还期限内的订单，修改状态为3
                      req.getRequest('http://ZLofCampus.top/RentMall/modifyRentState.do', { "rentId": openid }, { "content-type": "application/json" }).then((res) => {
                        console.log("测试promise")
                        console.log(res);
                        list1 = res.data.list1;
                        list2 = res.data.list2;
                        if (list1.length > 0) {
                          wx.showModal({
                            title: '提示',
                            content: '您有商品需要归还！逾期押金将不再退还！',
                            success: function (res) {
                              if (list2.length > 0) {
                                wx.showModal({
                                  title: '提示',
                                  content: '部分物品由于未按期归还，押金已扣除，如有特殊情况请主动与物主协商！详见订单页面',
                                })
                                //将积分减少20,注意，这里的扣分是每个商品均要扣分，所以需要循环
                                for (var i=0; i < list2.length; i++) {

                                  req.getRequest('http://ZLofCampus.top/RentMall/subScoreById.do', { "userId": openid }, { "content-type": "application/json" }).then((res) => {
                                    if (res.data.msg) {
                                      console.log("修改积分成功3");
                                      getApp().globalData.userIntegration = res.data.score
                                    }
                                  }).catch((err) => {
                                    console.log("修改积分捕捉错误")
                                  })
                                }
                              }
                            }
                          })
                        } else {
                          if (list2.length > 0) {
                            wx.showModal({
                              title: '提示',
                              content: '部分物品由于未按期归还，押金已扣除，如有特殊情况请主动与物主协商！详见订单页面',
                            })
                            //将积分减少20,注意，这里的扣分是每个商品均要扣分，所以需要循环
                            for (var i=0; i < list2.length; i++) {
                              //将积分减少20
                              req.getRequest('http://ZLofCampus.top/RentMall/subScoreById.do', { "userId": openid }, { "content-type": "application/json" }).then((res) => {
                                if (res.data.msg) {
                                  console.log("修改积分成功3");
                                  getApp().globalData.userIntegration = res.data.score
                                }
                              }).catch((err) => {
                                console.log("修改积分捕捉错误")
                              })
                            }
                          }
                        }
                        
                      }).catch((err) => {
                        console.log("测试错误捕捉")
                        console.log(res);
                      })


                      setTimeout(function(){
                      //查找租户的订单状态为3的，看有没有超过归还期限的
                      req.getRequest('http://ZLofCampus.top/RentMall/modifyRentState2.do', { "rentId": openid }, { "content-type": "application/json" }).then((res) => {
                        console.log("测试promise2")
                        console.log(res)
                        list3 = res.data.list3
                        if (list2.length <= 0 && list3.length > 0) {
                          wx.showModal({
                            title: '提示',
                            content: '部分物品由于未按期归还，押金已扣除，如有特殊情况请主动与物主协商！详见订单页面',
                          })
                        }
                        if(list3.length>0){
                          //将积分减少20,注意，这里的扣分是每个商品均要扣分，所以需要循环
                          for (var i=0; i < list3.length; i++) {
                            req.getRequest('http://ZLofCampus.top/RentMall/subScoreById.do', { "userId": openid }, { "content-type": "application/json" }).then((res) => {
                              if (res.data.msg) {
                                console.log("修改积分成功4");
                                getApp().globalData.userIntegration = res.data.score
                              }
                            }).catch((err) => {
                              console.log("修改积分捕捉错误")
                            })
                          }
                        }
                      }).catch((err) => {
                        console.log("测试错误捕捉2")
                        console.log(err);
                      })
                      },1000)

                      //这里看看经过上面的两种订单的查找和修改后，积分会不会小于0，如果小于0，那么就封号
                      setTimeout(function(){
                        if (getApp().globalData.userIntegration < 0 && getApp().globalData.idState == 0)
                        {
                          wx.request({
                            url: 'http://ZLofCampus.top/RentMall/updateIdState.do',
                            data: { "userId": openid },
                            method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                            header: { "content-type": "application/json" }, // 设置请求的 header
                            success: function (res) {
                              if (res.data.msg) {
                                getApp().globalData.idState = 1;  //表示封锁账号
                                wx.showModal({
                                  title: '提示',
                                  content: '您的账号已被封锁，解封请联系客服！',
                                  success: function (res) {
                                    if (res.confirm) {
                                      wx.switchTab({
                                        url: '/pages/self/self',
                                      })
                                    }
                                  }
                                })
                              }
                            }
                          })
                        }
                      },2500)
                    }
                  });
                }
              }
            });
             
          }
        });
      }
    }),

    // 获取用户信息
    // 获取用户当前的设置，返回值中只会出现小程序已经向用户请求过的权限。
    wx.getSetting({
      success: res => {
        // authSetting获取用户的权限结果
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              this.globalData.userInfo = res.userInfo
              console.log("userInfo_app.js=");
              console.log(res.userInfo);
              

              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
               
            }
          })
        }
      }
    })

    setTimeout(function (){
      that.getUnreadComment()   //查找未读评论
      that.getChatMsg()   //查找聊天记录
      setTimeout(function(){
        if(getApp().globalData.noRead>0){
          //设置未读消息红圈的显示
          wx.setTabBarBadge({
            index: 3,
            text: String(getApp().globalData.noRead),
          })
        }
      },500)
      
    },2000)
    

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
      success: function (res) {
        console.log("app.js，获取未读评论")
        console.log(res);
        let sum=0;
        for(var i=0;i<res.data.list.length;i++)
        {
          if (res.data.list[i].isRead==0)  //本身未读，也要算上
            sum = sum+1
          sum = sum+res.data.list[i].replyNoRead
        }
        getApp().globalData.noRead = getApp().globalData.noRead+sum;
        console.log("未读数1=" + getApp().globalData.noRead)
      }
    })

  },

  //获取留言记录
  getChatMsg() {
    var that = this;
    var list = []
    var openid = getApp().globalData.openid
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
      success: function (res) {
        console.log("app.js，获取所有留言信息");
        console.log(res);
        list = res.data.allChat;
        let sum=0;
        for (var i = 0; i < list.length; i++) {
          sum = sum+list[i].noRead
        }
        getApp().globalData.noRead = getApp().globalData.noRead+sum
        console.log("未读数2="+getApp().globalData.noRead)
      }
    })
  },

  // 全局变量
  globalData: {
    userInfo: null,
    openid:null,
    usermes:null,
    code:null,
    userIntegration:null,
    fenleiTabId:0,
    fromIndex:false, //判断是否是从首页点击查看更多跳转至分类页面
    idState:0,  //账号状态
    noRead:0   //未读评论和未读留言的总数
  },
  myRequest(){
    return new request();
  }
})