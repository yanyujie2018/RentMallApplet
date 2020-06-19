//获取应用实例
var app = getApp();
Page({
  data: {
    judgecontent:false,
    itemList: []  //用来接收从后台查询出的数据
  },
  onLoad: function () {
      // 刚进入页面或返回时对数据进行刷新
      var that = this;
      let openid = app.globalData.openid;
      var idState = app.globalData.idState
      //显示用户的全部发布
      wx.request({
      url: 'http://www.ZLofCampus.top/RentMall/showAllRelease.do',
      data: { "userId": openid },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log("我的发布")
        console.log(res);
        if(res.data.list.length==0)
        {
          that.setData({
            judgecontent: false
          })
        }else{
          that.setData({
            itemList: res.data.list,
            judgecontent: true
          })
        }
        
      }
    })
      
  },
  /**
  * 生命周期函数--监听页面初次渲染完成
  */
  onReady: function (){
  },

  //点击右下角发布按钮
  tonewsell: function () {
    wx.navigateTo({
      url: '../../release/release',
      success: function (res){},
      fail: function (res){},
      complete: function (res){},
    })
  },

  //点击删除按钮
  _deleteOrder: function(e){
    var that=this;
    var goodid = e.target.dataset.goodid;
    var openid=app.globalData.openid;
    //先提示是否确认删除，如果确认删除再进行删除
    wx.showModal({
      title: '提示',
      content: '该商品可能正在被人关注，确定删除吗？',
      success:function(res){
        if(res.confirm)  //点击确定
        {
          wx.request({
            url: 'http://www.ZLofCampus.top/RentMall/deleteGoodById.do',
            data: { "goodId": goodid },
            method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
            header: { "content-type": "application/json" }, // 设置请求的 header
            success: function (res) {
              console.log("delete");
              if (res.data.msg == true) {
                wx.showToast({
                  title: '删除成功',
                  icon: 'success',
                  duration: 2000,
                  success: function () {
                    setTimeout(function () {
                      wx.request({  //再次查出来
                        url: 'http://www.ZLofCampus.top/RentMall/showAllRelease.do',
                        data: { "userId": openid },
                        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                        header: { "content-type": "application/json" }, // 设置请求的 header
                        success: function (res) {
                          that.setData({
                            itemList: res.data.list,
                            judgecontent: true
                          })
                        }
                      })
                    }, 1000) //延迟时间
                  }
                })
              } else {
                wx.showToast({
                  title: '网络出错了...',
                  icon: 'none',
                  duration: 2000
                })
              }
            }
          })
        }
      }
    })
  },

  //点击商品进入商品发布详情页面，从此处点击进入商品发布页面，要自动填上值
  goDetail(e){
    console.log("进入商品发布详情页面")
    console.log(e)
    wx.navigateTo({
      url: '../../release/release?state=1&goodId='+e.currentTarget.dataset.goodid
    })
  },
  onUnload(){
    wx.switchTab({
      url: '../self/self',
    })
  }
});
