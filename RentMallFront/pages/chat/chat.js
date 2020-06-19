// pages/chat/chat.js
var app = getApp();
var message = '';
var text = '';
var user = {};
var length;
var zx_info_id;
var openid_talk;
Page({
  data: {
    news: '',
    scrollTop: 0,
    message: '',
    goodmsg:'',  //用来存放商品信息
    usermsg:'',  //用来存放发布商品的用户信息
    chatmsg:'',  //存放聊天信息
    content:'',  //用来存放要发送的消息
    avatarUrl_R:'',  //用来存放右边人物的头像，也就是登陆者的头像
    openId:'',
    text: text,
    centendata: '',
    nickName: '',
    avatarUrl_L: '',    //用来存放左边人物的头像，也就是对话方的头像
    news_input_val: '',
    tabdata: '',
    isImage:0,  //判断要发送的信息是否是图片，可以调用不同的后台，实现对文字和图片的分开处理
    chatName:null,   //显示与谁聊天的名字
  },

  bindChange: function (e) {
    message = e.detail.value
  },
   


  //根据options中的状态state判断是从哪个页面跳转过来的
  //如果state=0表示从商品详情页面传过来，从商品详情页面会传过来商品的id，发起者的id（其实就是登陆者的id）
  //如果state=1表示是从消息页面进来的，那么会传进来
  onLoad: function (options) {
    var that = this
    this.setData({
      openId:getApp().globalData.openid,
      avatarUrl_R: getApp().globalData.userInfo.avatarUrl
    })
    if(options.state==0){  //从商品详情页面传入的，那么一定是在与 商家 聊天
      let goodId = options.goodId;
      let chatAId = getApp().globalData.openid   //这里是聊天页面发送消息的人(其实就是登陆者本人)，即右边的消息
      //先根据商品id查出商品的发布信息
      //先查出发布该商品信息的商家信息，再查出该商品的发布信息
      //下面这个请求可以将商品信息和发布者信息都查询出来
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/queryGood.do',
        data: { "goodId": goodId },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("聊天页面商品信息查询1")
          console.log(res)
          console.log(res.data.usermsg.nickName)
          console.log(options.state)
          var chatName = null
          if (res.data.usermsg.userName == null || res.data.usermsg.userName == ""){
            chatName = res.data.usermsg.nickName
          }else
          {
            chatName = res.data.usermsg.userName
          }
          that.setData({
            goodmsg: res.data.goodmsg,
            usermsg: res.data.usermsg,
            avatarUrl_L: res.data.usermsg.avatarUrl,
            chatName:chatName
          })
          
          //因为查询聊天信息需要知道当前登陆者与商品发布者的id，以及商品的id，那么就只有等商品信息查找出来才可以
          //如果两个请求并列，就会由于异步执行可能导致再查聊天记录时，还没有查到商品发布者的信息
          that.queryChat(goodId, res.data.usermsg.userId, getApp().globalData.openid)
        }
      })
    }else{  //state=1表示是从消息页面进来的 (找到对话者双方的id，看那个id和当前登陆者id一样，那么就将另一个id对应的昵称或姓名赋值给chatName)
      console.log("来自消息页面的留言详情点击")
      console.log("Id="+options.Id)
      let goodId = options.chatGoodId
      let chatAId = getApp().globalData.openid
      let chatRightId = options.chatRightId
      let chatLeftId = options.chatLeftId
      let Id = options.Id;
      console.log("right="+chatRightId)
      console.log("left="+chatLeftId)
      //根据用户id找到该用户的昵称和姓名
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/queryNameAndNickAndAvat.do',
        data: { "userId": chatLeftId},
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success:function(res){
          if (res.data.user.userName == null || res.data.user.userName == ""){
            that.setData({
              chatName:res.data.user.nickName
            })
          }else{
            that.setData({
              chatName: res.data.user.userName
            })
          }
        }
      })
      //根据商品id查询商品信息
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/queryGood.do',
        data: { "goodId": goodId },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("聊天页面商品信息查询2")
          console.log(res)
          that.setData({
            goodmsg: res.data.goodmsg,
            usermsg: res.data.usermsg,
            avatarUrl_L: res.data.usermsg.avatarUrl
          })
          //查询双方对该商品的留言
          that.queryChat(goodId,chatLeftId, chatRightId)
        }
      })
    }
  },


  //查询聊天记录
  queryChat(goodId,userId,openid){
    var that = this
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/queryChat.do',
      data: { "chatGoodId": goodId, "chatA": openid, "chatB": userId },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log("聊天记录查询")
        console.log(res)
        var list = res.data.chatList;
        //如果当前这两个人对于这件商品没有聊天记录，那么发起聊天的一定是购买方，所以页面可以显示“您正在与。。。（卖家）进行沟通”，则在之前设置的name和头像均正确
        //如果当前这两个人对这件商品有两天记录，那么就需要看主聊天的两个id中那个id等于当前登陆者id，另一个找到其名字和，昵称头像，则此时就需要对name和头像进行修改
        if(list!=null)
        {
          var tmp=null
          if(app.globalData.openid==list.chatLeftId){
            tmp=list.chatRightId
          }else{
            tmp = list.chatLeftId
          }
          wx.request({
            url: 'http://ZLofCampus.top/RentMall/queryNameAndNickAndAvat.do',
            data: { "userId":tmp},
            method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
            header: { "content-type": "application/json" }, // 设置请求的 header
            success:function(res){
              console.log("user")
              console.log(res);
              that.setData({
                usermsg:res.data.user,
                avatarUrl_L: res.data.user.avatarUrl
              })
            }
          })
        }
        that.setData({
          chatmsg:res.data.chatList
        })
      }
    })
  },

  //点击发送消息
  sentChat(){
    var that = this;
    console.log("发送聊天信息")
    var userId = this.data.usermsg.userId
    var goodId = this.data.goodmsg.goodId
    var openid = getApp().globalData.openid
    var chatContent = this.data.content
    if(chatContent!=""){
      //根据goodid和openId（作为chatRightId），usermsg.userId（作为chatLeftId)添加聊天信息
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/addChat.do',
        data: { "chatGoodId": goodId, "chatRightId": openid, "chatLeftId": userId, "chatContent": chatContent},
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success:function(res){
          if(res.data.msg)  //添加成功后要重新查出来聊天信息
          {
            that.queryChat(goodId,userId,openid)   //重新查出来聊天信息显示在页面上
            //清空发送框中的内容，使用户可以再次填写放
            that.setData({
              content:''
            })
          }else{  //添加不成功，提示重新发送，并且将这次发送的内容显示在发送框中
            wx.showToast({
              title: '网络出错啦！请重新发送！',
              icon:'none',
              duration:1500
            })
            that.setData({
              content: chatContent
            })
          }
        }
      })
    }else{
      wx.showToast({
        title: '内容不可为空！',
        icon: 'none',
        duration: 1000
      })
    }
  },

  //监听信息发送框的输入,获取输入内容
  chatInput(e){
    this.setData({
      content:e.detail.value
    })
  },
 

  // 页面加载
  loaddata: function (a) {
    var that = this;
    var is_img = true;
    var data = {
      program_id: app.jtappid,
      openid: app._openid,
      zx_info_id: zx_info_id,
      openid_talk: openid_talk
    }
    util.request('/session_page', 'post', data, '', function (res) {
      if (res.data.k1) {
        res.data.k1.time_agree = util.js_date_time(res.data.k1.time_agree)
      }
      for (var i = 0; i < res.data.k2.length; i++) {
        res.data.k2[i].time = util.js_date_time(res.data.k2[i].time)
        var n = res.data.k2[i].content.charAt(res.data.k2[i].content.length - 1);
        switch (n) {
          case 'g':
            res.data.k2[i].is_img = is_img
            break;
          default:
        }
      }
      that.setData({
        tabdata: res.data.k1,
        centendata: res.data.k2.reverse()
      })
      wx.setNavigationBarTitle({ title: that.data.tabdata.nickname });
      if (a) {
        setTimeout(function () {
          that.bottom()
        }, 500);
      }
    })
    setTimeout(function () {
      if (that.data.centendata.length != length) {
        length = that.data.centendata.length
      }
      that.loaddata()
    }, 3000);

  },

  // 获取hei的id节点然后屏幕焦点调转到这个节点
  bottom: function () {
    var query = wx.createSelectorQuery()
    query.select('#hei').boundingClientRect()
    query.selectViewport().scrollOffset()
    query.exec(function (res) {
      wx.pageScrollTo({
        scrollTop: res[0].bottom  // #the-id节点的下边界坐标
      })
      res[1].scrollTop // 显示区域的竖直滚动位置
    })
  },


  // 选择图片并把图片保存  
  //选择完片之后就直接发送了
  upimg1: function () {  
    var that = this;
    var userId = this.data.usermsg.userId
    var goodId = this.data.goodmsg.goodId
    var openid = getApp().globalData.openid
    wx.chooseImage({  //选择图片
      count:1,
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'],  //选择图片的来源(从图库或相机选择)
      success: function (res) {
        var tempFilePaths = res.tempFilePaths  //tempFilePaths是图片的本地临时文件路径列表 (本地路径), tempFilePath可以作为img标签的src属性显示图片
        console.log("tempFilePaths[0]=" + tempFilePaths[0])
        wx.uploadFile({
          url: 'http://ZLofCampus.top/RentMall/addChatImage.do?', //提交信息至后台
          filePath: tempFilePaths[0],   //本地临时路径
          name: 'file', //文件对应的参数名字(key)
          formData:{   //这里是要传送的一些数据
            "chatGoodId": goodId,
            "chatRightId": openid,
            "chatLeftId": userId,
          },
          success: function (res) {
            console.log("发送图片留言")
            console.log(res)
            var data = JSON.parse(res.data)
            if (data.upload == "文件成功上传到指定目录下") {
              console.log("sucess")
              if (data.msg == true) {
                console.log("sucess")  //成功之后，重查聊天信息
                that.queryChat(goodId, userId, openid)   //重新查出来聊天信息显示在页面上
                //清空发送框中的内容，使用户可以再次填写放
                that.setData({
                  content: ''
                })
              }else{
                wx.showToast({
                  title: '网络出错啦!请重新发送哦~',
                  icon:'none',
                  duration:1000
                })
              }
            } else if (data.upload == "文件类型不符合,请按要求重新上传") {
              wx.showToast({
                title: '图片文件类型不符合,请按要求重新上传',
                icon: 'none',
                duration: 2000
              })
            } else if (data.upload == "文件类型为空") {
              wx.showToast({
                title: '图片文件类型为空,请按要求重新上传',
                icon: 'none',
                duration: 2000
              })
            } else if (data.upload == "没有找到相对应的文件") {
              wx.showToast({
                title: '没有找到相对应的图片文件',
                icon: 'none',
                duration: 2000
              })
            }
          }
        })
      }
    })
  },

  goHome(e){
    console.log("进入主页1")
    console.log(e)
    var avat = e.target.dataset.avat;
    //跳转主页
    wx.navigateTo({
      url: '../home/home?avat='+avat,
    })
  },

  onShareAppMessage: function () {

  }
})