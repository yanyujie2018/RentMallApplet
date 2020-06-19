const app = getApp();
Page({
  data: {
    userInfo: {}, 
    hasUserInfo: false,
    // 判断小程序的API，回调，参数，组件等是否在当前版本可用
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    score:null,
    userName:null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    var that = this;
    console.log("我的页面测试onLoad")
    console.log(getApp().globalData.userIntegration)
    //根据openId多查一次积分
    //获取用户积分
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getUserIntegration.do',
      data: { "userId": getApp().globalData.openid },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        getApp().globalData.userIntegration = res.data.userIntegration
        console.log(getApp().globalData.userIntegration)
      }
    })

    //根据openid查看客户有没有完善设置姓名，如果有，那么就显示客户设置的姓名，如果没有就默认显示微信昵称
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getUserName.do',
      data: { "userId": getApp().globalData.openid },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        console.log("姓名")
        console.log(res);
        if(res.data.userName!=null){
          that.setData({
            userName: res.data.userName
          })
        }
      }
    })

    if (getApp().globalData.userIntegration!=null)
    {
      this.setData({
        score: getApp().globalData.userIntegration
      })
    }
    if (app.globalData.userInfo) { // 如果全局变量当中已经获取到userinfo的值，那么在这里就可以直接注入数据
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true    //已经获取到用户信息，控制页面不需要再显示按钮，而显示头像
      })
      var nickName = app.globalData.userInfo.nickName
      var avatarUrl = app.globalData.userInfo.avatarUrl;
      console.log("nickName=" + nickName);
      console.log("avatarUrl=" + avatarUrl)
      //将获取到的微信登录信息中的头像和昵称写入数据库
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/addNickAndUrl.do',
        data: { "userId": getApp().globalData.openid, "nickName": app.globalData.userInfo.nickName, "avatarUrl": app.globalData.userInfo.avatarUrl },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success:function(res){
          console.log("写入昵称")
          console.log(res);
        }
      })
    } else if (this.data.canIUse) {  //如果微信当前版本支持 open-type=getUserInfo
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      // userInfoReadyCallback 这个方法是定义在onload函数当中的。
      // 因为wx.getUserInfo是异步执行的，不知道异步先执行完毕还是page.onload先执行完毕
      // 1、方法如果定义了，则说明page.onload比当前方法运行的早（page已经初始化完成），但app的globalData中还没有数据，通过此回调可以及时刷新数据
      // 2、方法如果没有定义，那么说明page.onload比当前晚（page还没有初始化），app的globalData中是有数据，那么就可以直接在page.onload中取到值
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          score: app.globalData.userIntegration,
          hasUserInfo: true,
        })
        var nickName = app.globalData.userInfo.nickName
        var avatarUrl = app.globalData.userInfo.avatarUrl;
        console.log("nickName=" + nickName);
        console.log("avatarUrl=" + avatarUrl)
        //将获取到的微信登录信息中的头像和昵称写入数据库
        wx.request({
          url: 'http://ZLofCampus.top/RentMall/addNickAndUrl.do',
          data: { "userId": getApp().globalData.openid, "nickName":nickName, "avatarUrl": avatarUrl },
          method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
          header: { "content-type": "application/json" }, // 设置请求的 header
          success: function (res) {
            console.log("写入昵称")
            console.log(res);
          }
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理（版本不支持时的处理，此时只能使用getUserInformation去获取用户信息）
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true,
          })
          var nickName = app.globalData.userInfo.nickName
          var avatarUrl = app.globalData.userInfo.avatarUrl;
          console.log("nickName=" + nickName);
          console.log("avatarUrl=" + avatarUrl)
          //将获取到的微信登录信息中的头像和昵称写入数据库
          wx.request({
            url: 'http://ZLofCampus.top/RentMall/addNickAndUrl.do',
            data: { "userId": getApp().globalData.openid, "nickName": nickName, "avatarUrl": avatarUrl },
            method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
            header: { "content-type": "application/json" }, // 设置请求的 header
            success: function (res) {
              console.log("写入昵称")
              console.log(res);
            }
          }) 
        }
      })
    }
  },

  onGotUserInfo: function (e) {
    console.log("onGotUserInfo");
    console.log(e.detail.errMsg)
    if (e.detail.errMsg != "getUserInfo:fail auth deny"){
      app.globalData.userInfo = e.detail.userInfo
      this.setData({
        userInfo: e.detail.userInfo,
        hasUserInfo: true
      })
      var nickName = app.globalData.userInfo.nickName
      var avatarUrl = app.globalData.userInfo.avatarUrl;
      console.log("nickName="+nickName);
      console.log("avatarUrl="+avatarUrl)
      //将获取到的微信登录信息中的头像和昵称写入数据库
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/addNickAndUrl.do',
        data: { "userId": getApp().globalData.openid, "nickName": nickName, "avatarUrl": avatarUrl},
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("写入昵称")
          console.log(res);
        }
      })
    }else{
      wx.showModal({
        title: '提示',
        content: '请授权登录，否则部分功能将无法使用！',
      })
    }
    
  },


  //点击发布跳转发布页面
  Release: function(e){
    if (e.currentTarget.dataset.score<0)
    {
      wx.showToast({
        title: '您的账号已被封锁，暂不能发布信息!',
        icon: 'none',
        duration: 1000,
      })
    }else{
      wx.navigateTo({
        url: '../release/release?state=0',
      })
    }
    
  },


  //点击全部订单
  all_orders:function(){
    wx.navigateTo({
      url: '../others/all_orders/all_orders?currentTab=0',
      success: function(res) {},
      fail: function(res) {},
      complete: function(res) {},
    })
  },

  //点击租用中
  waitpay_list: function () {
    wx.navigateTo({
      url: '../others/all_orders/all_orders?currentTab=1',
      success: function (res) { },
      fail: function (res) { },
      complete: function (res) { },
    })
  },

  //点击待发货
  waitsend_list: function () {
    wx.navigateTo({
      url: '../others/all_orders/all_orders?currentTab=2',
      success: function (res) { },
      fail: function (res) { },
      complete: function (res) { },
    })
  },

//点击待收货
  waitReceive_list: function () {
    wx.navigateTo({
      url: '../others/all_orders/all_orders?currentTab=3',
      success: function (res) { },
      fail: function (res) { },
      complete: function (res) { },
    })
  },

// 点击待归还
  waitreturn_list: function () {
    wx.navigateTo({
      url: '../others/all_orders/all_orders?currentTab=4',
      success: function (res) { },
      fail: function (res) { },
      complete: function (res) { },
    })
  },

  //点击待收回
  waitRecovery_list: function () {
    wx.navigateTo({
      url: '../others/all_orders/all_orders?currentTab=5',
      success: function (res) { },
      fail: function (res) { },
      complete: function (res) { },
    })
  },


  //我的发布
  selfRelease: function () {
    wx.navigateTo({
      url: '../others/selfsell/selfsell',
      success: function (res) { },
      fail: function (res) { },
      complete: function (res) { },
    })
  },
  
  //我的收藏
  mycollections: function () {
    wx.navigateTo({
      url: '../others/mycollections/mycollections',
      success: function (res) { },
      fail: function (res) { },
      complete: function (res) { },
    })
  },

  //点击疑难解答调用函数
  question: function () {
    wx.navigateTo({
      url: '../others/question/question',
      success: function (res) { },
      fail: function (res) { },
      complete: function (res) { },
    })
  },

  //点击转发功能
  onShareAppMessage: function (res) {
    if (res.from === "menu") {
      console.log("来自右上角转发菜单")
    }
    return {
      title: "校园租赁商城",
      path: '/pages/index/index',
      imageUrl: "../imagas/nothing.jpg",
      success: (res) => {
        console.log("转发成功", res);
      },
      fail: (res) => {
        console.log("转发失败", res);
      }
    }
  },
  
  // 前往设置中心页面
  goSetting: function (e) {
    
    if(app.globalData.userInfo!=null)  // 如果已经授权，那么直接跳转页面即可
    {
      wx.navigateTo({
        url: '../setting/setting',
      })
    }else{   //如还未授权那么提示需要授权（由于在2.3版本之后就需要用户点击才能够拉起用户设置页面修改权限，所以体验感觉不好，在这里仅提示而已
      wx.showModal({
        title: '提示',
        content: '请授权登录！'
      })
    }
    
  },

  //跳转我的关注
  myFollow(){
    wx.navigateTo({
      url: '../myfollow/myfollow',
    })
  }

})