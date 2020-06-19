// pages/myfollow/myfollow.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    followList:null,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    //查找我的关注
    this.findFollow()
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
    //查找我的关注
    this.findFollow()
  },

  //查找所有关注
  findFollow(){
    var that = this;
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/findFollow.do',
      data: { "followId": getApp().globalData.openid },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log("我的关注")
        console.log(res)
        that.setData({
          followList: res.data.list
        })
      }
    })
  },

  //取消关注
  changeFollow(e){
    console.log("取消关注")
    console.log(e)
    var followId = e.currentTarget.dataset.followid
    var followToId = e.currentTarget.dataset.followtoid
    var that = this
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/cancelFollow.do',
      data: { "followId": followId, "followToId":  followToId},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        if (res.data.msg) {
          that.findFollow();
        } else {
          wx.showToast({
            title: "网络出错啦...",
            icon: "none",
            duration: 1500
          })
        }
      }
    })
    
  },

  toHome(e){
    console.log("去主页")
    console.log(e)
    var avat = e.currentTarget.dataset.avat
    //跳转主页
    wx.navigateTo({
      url: '../home/home?avat=' + avat,
    })
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

  }
})