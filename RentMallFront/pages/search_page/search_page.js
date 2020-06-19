//var network = require("../../utils/network.js");

Page({

  /**
   * 页面的初始数据
   */
  data: {
    list: [],  //存放搜索历史
    inputValue: null,
    resultList: null  //存放实时查询的结果
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var _this = this;
    //从本地存储中取出曾经的搜索历史条目
    wx.getStorage({
      key: 'historySearch',
      success(res) {
        _this.setData({
          list: res.data
        })
      }
    })
  },

  //输入框的实时监听函数
  blur: function (e) {
    this.setData({
      inputValue: e.detail.value
    })
    this.search();  //实时查找
  },

  //从数据库进行查找
  search: function () {
    var that = this
    var key = this.data.inputValue;
    console.log(key);
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/searchByKey.do',
      data: {"key":key},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        console.log("搜索")
        console.log(res)
        that.setData({
          resultList:res.data.searchList
        })
      }
    })
    /*
    network.requestLoading('projectAppList', { projectName: this.data.inputValue }, '', function (res) {
      if (res.respState == 'S') {
        _this.setData({
          resultList: res.dtos
        })
      }

    }, function () {
      wx.showToast({
        title: '搜索失败',
        icon: 'none'
      })
    })
    */
  },

  save: function () {
    var list = this.data.list;
    if (list.indexOf(this.data.inputValue) == -1 & this.data.inputValue != '') {
      list.push(this.data.inputValue);
    }
    this.setData({
      list: list
    })
    //存入新的搜索历史条目
    wx.setStorage({
      key: 'historySearch',
      data: list
    })
  },

  //点击搜索记录里面的搜索词调用的搜索函数
  searchName: function (e) {  
    this.setData({
      inputValue: e.currentTarget.dataset.value
    })
    this.search();
  },
  
  //清空搜索记录
  remove: function () {
    var _this = this;
    wx.showModal({
      title: '提示',
      content: '确认清空所有记录?',
      success(res) {
        if (res.confirm) {
          //清空历史记录的存储
          wx.removeStorage({
            key: 'historySearch',
            success() {
              _this.setData({
                list: []
              })
            }
          })
        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    })

  },

  //消除输入框内容
  //未收起键盘情况下，安卓清除input框内容失败，收起键盘再点清除则没问题
  //为了解决此问题，这里采用了延时清除的方式
  clean: function () {
    var _this = this
    setTimeout(function () {
      _this.setData({
        inputValue: '',

      })
    }, 100)
  },
  
  //跳转详情页面
  detail: function (e) {
    this.save();   //存下来搜索历史
    var goodid = e.currentTarget.dataset.goodid;
    wx.navigateTo({
      url: '../others/details/details?goodid=' + goodid,
    })

  }
})