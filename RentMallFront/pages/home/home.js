const app = getApp()
// pages/index/PerHomePage/PerHomePage.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    uid: '',
    userId: '',
    goodsList: [],
    isFollow: false,
    goodsSoldCount: '',
    goodRate: '',
    user:null,
    avat:null,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    this.getMsg(options.avat)
    that.setData({
      avat:options.avat
    })
    //this.freshGoods(),
     // this.getPersonalReview(),
     // this.getFollowBut()
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
    this.getMsg(this.data.avat)
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  getMsg(avat){
    console.log("主页")
    console.log(avat)
    var that = this
    //根据头像查找到对应的用户（因为微信在进行头像存储时，每个头像的地址一定是唯一的，所以可以找到唯一的用户）
    //找到这个用户后，返回这个用户的所有个人信息，和此用户发布的所有商品信息，再根据这些商品信息查找出所有评论，统计五星评论数和总评论数，得出好评率
    //最后返回前端的信息有：用户信息，所发布的所有商品信息，好评率
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/findHomeMsg.do',
      data: { "avat": avat },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log("进入主页2")
        console.log(res)
        var list = res.data.goods;
        for(var i=0;i<list.length;i++)
        {
          var imageList = []
          if(list[i].image1!=null)
            imageList.push(list[i].image1);
          if (list[i].image2 != null)
            imageList.push(list[i].image2);
          if (list[i].image3 != null)
            imageList.push(list[i].image3);
          list[i].imageList=imageList;
        }
        that.setData({
          user:res.data.user,
          goodsList:list,
          goodRate:res.data.rate,
        })

        //获取关注状态
        wx.request({
          url: 'http://ZLofCampus.top/RentMall/followState.do',
          data: { "followId": getApp().globalData.openid, "followToId": res.data.user.userId },
          method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
          header: { "content-type": "application/json" }, // 设置请求的 header
          success: function (res) {
            if (res.data.msg) {
              that.setData({
                isFollow: true
              })
            } else {
              that.setData({
                isFollow: false
              })
            }
          }
        })

      }
    })
  },
  


  // 查询商品细节信息
  goodsDetails: function (e) {
    console.log(e)
    wx.navigateTo({
      url: '../others/details/details?goodid=' + e.currentTarget.dataset.goodid
    })
  },

  // 改变关注的状态
  changeFollow: function (e) {
    var that = this
    var follow = e.currentTarget.dataset.follow;
    var userId = e.currentTarget.dataset.userid
    // 关注用户
    if (e.currentTarget.dataset.follow == false) {  //说明未关注，点击关注
      if (userId == getApp().globalData.openid) {  //不可关注自己
        wx.showToast({
          title: "不能关注自己哦",
          icon: "none",
          duration: 1500
        })
      }
      else {   //关注他人，条件符合
        wx.request({
          url: 'http://ZLofCampus.top/RentMall/addFollow.do',
          data: { "followId": getApp().globalData.openid,"followToId":userId},
          method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
          header: { "content-type": "application/json" }, // 设置请求的 header
          success:function(res){
            if(res.data.msg)
            {
              that.setData({
                isFollow:true
              })
            }else{
              wx.showToast({
                title: "网络出错啦...",
                icon: "none",
                duration: 1500
              })
            }
          }
        })
          
      }
    }
    else { //说明已关注，点击取消关注
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/cancelFollow.do',
        data: { "followId": getApp().globalData.openid, "followToId": userId },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success:function(res){
          if(res.data.msg)
          {
            that.setData({
              isFollow:false
            })
          }else{
            wx.showToast({
              title: "网络出错啦...",
              icon: "none",
              duration: 1500
            })
          }
        }
      })
    }
  }
})