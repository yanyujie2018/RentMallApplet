// pages/others/question/question.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  getAnswer:function(e){
    wx.navigateTo({
      url: '../answer/answer?status='+e.currentTarget.dataset.status,
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },
})