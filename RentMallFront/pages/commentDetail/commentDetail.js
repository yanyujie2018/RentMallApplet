
const app = getApp()
Page({

  /**
   * 页面的初始数据
   * uid:用户id  goodsId: 商品id  goodsDetails：商品细节信息  goodsComments: 商品评论信息
   * content: 当前评论的内容  replyId：被评论的人的id   releaseName：被评论的人的名称
   * commentId：回复该评论的Id
   */
  data: {
    openid: '',  //用户id
    goodsId: '',  //商品id，这里表示被评价的商品id
    goodsDetails: '',  //商品的详细信息
    goodsComments: '',
    usermsg:'',
    content: '',
    replyId: '',
    commentId: '',
    releaseName: '',
    releaseFocus: false,  //用于将光标置于回复框中
    role: '',
    name:null,  //评论者的名字，存下来
    anonymous:null,  //查看该评论是否匿名
    replyToId:'',   //回复的对象id
   // replyList:[],

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // console.log(options)
    var that = this
    console.log("评论详情页")
    console.log("commentGoodId="+options.commentGoodId)
    that.setData({
      goodsId:options.commentGoodId,
      openid:app.globalData.openid,
      commentId:options.commentId
    })
    
   
    this.getGoodsDetails(options.commentGoodId)  //查找商品的详细信息
    this.getGoodsComment(options.commentId)  //根据评论id查找评论

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.getGoodsDetails(this.data.goodsId)  //查找商品的详细信息
    this.getGoodsComment(this.data.commentId)  //根据评论id查找评论
  },


  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  // 获取商品详细信息
  getGoodsDetails: function (id) {
    var that = this
    var goodid = id
    console.log("获取商品详情")
    console.log("goodid="+goodid);
    //先查出发布该商品信息的商家信息，再查出该商品的发布信息
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/queryGood.do',
      data: { "goodId": goodid },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        console.log(res)
        that.setData({
          goodsDetails: res.data.goodmsg,
          usermsg:res.data.usermsg
        })
      }
    })
  },

  // 获取商品的评论信息
  getGoodsComment: function (id) {
    var that = this
    var comment = []
    //先根据评论id查找评论的详细信息
    //然后再根据评论id查找所有的回复信息
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/findCommentById.do',
      data: { "commentId": id },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
          console.log("查找评论及回复")
          console.log(res);
          comment = res.data.commentmsg
          comment.list = res.data.replyList
          that.setData({
            goodsComments:comment,
          })
        console.log("添加属性之后")
        console.log(that.data.goodsComments)
      }
    })
    
  },

  // 提交回复
  replyCommentHandler: function (event) {
    var that = this;
    console.log("点击提交回复")
    console.log(event.detail.value)  //获取回复内容
    if (event.detail.value.content == "") {
      wx.showToast({
        title: "回复不能为空！",
        icon: "none",
        duration: 1500
      })
      return false;
    }
    var replyContent=event.detail.value.content
    var goodsId=this.data.goodsId
    var replyComment=this.data.commentId
    var replyToId = this.data.replyToId
    var replyId = this.data.replyId
    
    //添加回复
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/addReply.do',
      data: { "replyComment": replyComment, "replyId":replyId, "replyToId":replyToId , "replyContent":replyContent},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        console.log("添加回复")
        console.log(res)
        if(res.data.msg)
        {
          wx.showToast({
            title: '回复成功！',
            icon:'none',
            duration:1500,
          })
          // 将当前评论内容置空，下次继续评论
          that.setData({
            content: ''
          });
          // 评论后重新获取全部评论内容
          that.getGoodsComment(replyComment);
        }else{
          wx.showToast({
            title: '网络出错啦...',
            icon:'none',
            duration:1500
          })
        }
      }
    })

  },

  //点击之后设置回复对象的id和回复者的id（一级评论中的）
  bindReply: function (e) {
    console.log("点击回复")
    console.log(e)
    console.log(this.data.goodsComments)
    var replyToId = e.currentTarget.dataset.replytoid
    var anonymous = e.currentTarget.dataset.anonymous
    var nickName = e.currentTarget.dataset.nickname
    var userName = e.currentTarget.dataset.username
    var name = ''
    //因为这里是选择一级评论去回复的，而一级评论一定是其他人给登陆者的商品做评论，那么回复对象只可能是对方，所以不用再将回复对象的id与openId相比较了，只需要比较匿名即可
    if (anonymous==1)
    {
      name="匿名用户"
    }else{
      if(userName=='' || userName==null)
        name = nickName
      else
        name = userName
    }

    this.setData({
      releaseFocus: true,  //聚焦
      replyId: e.currentTarget.dataset.replyid,  //回复发起者
      replyToId: replyToId,    //回复对象的id
      commentId:e.currentTarget.dataset.commentid,   //回复所属评论id号
      releaseName:name
    })
  },

  //2级评论中的点击回复
  bindReply2: function (e) {
    console.log("点击回复2")
    console.log(e)
    var replyToId = e.currentTarget.dataset.replytoid
    var name='';
    var userName = e.currentTarget.dataset.username;
    var nickName = e.currentTarget.dataset.nickname;
    var anonymous = e.currentTarget.dataset.anonymous
    if(replyToId==getApp().globalData.openid)  //如果要回复的对象时自己，那么无论匿名与否，都直接写自己的昵称或者名字
    {
      if(userName=="" || userName==null)
        name = nickName;
      else
        nama = userName;
    }else{   //说明是在回复对方，那么就需要判断该条评论是否匿名了
      if (anonymous==1)  //匿名
      {
        name="匿名用户"
      }else{
        if (userName == "" || userName == null)
          name = nickName;
        else
          nama = userName;
      }
    }
    this.setData({
      releaseFocus: true,  //聚焦
      releaseName:name,
      replyId: e.currentTarget.dataset.replyid,  //回复发起者
      replyToId: replyToId
    })
  },

  // 长按删除 
  deleteReply: function (e) {
    console.log("长按删除")
    console.log(e)
    var that = this
    var id = e.currentTarget.dataset.id;
    var replycomment = e.currentTarget.dataset.replycomment
    // 只有自己才能删除自己的评论 加判断
    // 由于在进行回复时是对这条回复信息的对方回复，所以replytoID，replyID和这里需要的是反着的
    if (this.data.openid == e.currentTarget.dataset.replytoid) {
      wx.showModal({
        title: '提示',
        content: '是否确认删除该评论？',
        success: function (res) {
          if(res.confirm){
          wx.request({
            url: 'http://ZLofCampus.top/RentMall/deleteReply.do',
            data: { "Id":id},
            method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
            header: { "content-type": "application/json" }, // 设置请求的 header
            success:function(res){
              if(res.data.msg){
                wx.showToast({
                  title: '删除成功！',
                  icon:'success',
                  duration:1500
                })
                // 重新刷新商品评论信息
                that.getGoodsComment(replycomment);
              }else{
                wx.showToast({
                  title: '网络出错啦...',
                  icon:'none',
                  duration:1500
                })
              }
            }
          })
          /*
          //点击确定，根据回复id删除该条回复
          if (res.confirm) {
            post('/commentsDelete/' + e.currentTarget.dataset.commentid, null).then((res) => {
              if (res.statusCode == '200') {
                wx.showToast({
                  title: "删除成功",
                  icon: "success",
                  duration: 2000
                })
                // 重新刷新商品评论信息
                that.getGoodsComment(that.data.goodsId);
              }
              else {
                wx.showToast({
                  title: res.data.message,
                  icon: "none",
                  // image: '/pages/images/warning.png',
                  duration: 2000
                })
              }
            });
            */
          }
          else if (res.cancel) {
            console.log('用户已点击取消')
          }
        }
      })
    }else{
      wx.showModal({
        title: '提示',
        content: '您无权删除他人评论及回复！',
      })
    }
  },


  deleteReply2(){
    wx.showModal({
      title: '提示',
      content: '您不能删除他人评论！',
    })
  },

  // 操作
  moreOperation: function () {
    var that = this;
    wx.showActionSheet({
      itemList: ['生成交易二维码', '删除'],
      success: function (res) {
        if (res.tapIndex == 0) {
          console.log("res.tapIndex")
          wx.navigateTo({
            url: '../qr/qr?id=' + that.data.goodsDetails.id + '&goodsName=' + that.data.goodsDetails.goodsName
          })
        }
        else if (res.tapIndex == 1) {
          wx.showModal({
            title: '提示',
            content: '是否删除该商品？',
            success: function (res) {
              if (res.confirm) {
                post('/goodsDelete/' + that.data.goodsDetails.id, null).then((res) => {
                  if (res.statusCode == '200') {
                    wx.showToast({
                      title: "删除成功",
                      icon: "success",
                      duration: 2000
                    })
                    // 重新获取商品列表
                    var pages = getCurrentPages();//获取页面栈
                    if (pages.length > 1) {
                      var prePage = pages[pages.length - 2];
                      prePage.freshGoods()
                    }
                    wx.navigateBack({
                      delta: 1
                    })

                  }
                  else {
                    wx.showToast({
                      title: res.data.message,
                      icon: "none",
                      // image: '/pages/images/warning.png',
                      duration: 2000
                    })
                  }
                });

              }
              else if (res.cancel) {
                console.log('用户点击取消')
              }
            }
          })



        }


      },
      fail: function (res) {
        console.log(res.errMsg)
      }
    })

  }
})