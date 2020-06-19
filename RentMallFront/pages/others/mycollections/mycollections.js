Page({
  data: {
    iscollections:true,
    showModal:false,
    collectList:[],
    deleteid:null,
  },

  onLoad(){
    var that = this;
    //获取收藏数据
    wx.request({
      url: 'http://www.ZLofCampus.top/RentMall/collectQuery.do',
      data: { "collectUserId":getApp().globalData.openid},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log(res)
        that.setData({
          collectList: res.data.list
        });
        if(res.data.list.length==0)
          {
            that.setData({
            iscollections:false
          });
        }
        else 
        {
          that.setData({
             iscollections: true
          });
        }
      }
    });
  },
 

  showModal:function(e){
    console.log(e);
    var delid=e.target.dataset.id;
    this.setData({
      showModal:true,
      deleteid:delid,
    });
  },
  hideModal: function () {
    this.setData({
      showModal: false,
    });
  },
  onCancel: function () {
    this.hideModal();
  },
  /**
   * 对话框确认按钮点击事件，执行删除操作
   */
  onConfirm: function () {
    var that = this;
    console.log(this.data.deleteid)
    wx.request({
      url: 'http://www.ZLofCampus.top/RentMall/deleteCollect.do',
      data: {"id":this.data.deleteid},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) 
      {
        if(res.data.msg==true)
        {
          wx.showToast({
            title: '已删除',
            duration:1500,
            icon:'success'
          })
          that.setData({
            booklist: null,
            carts: [],
            deletebookid: null,
          });
          that.onLoad();
        }else{
          wx.showToast({
            title: '网络出错了.....',
            icon: 'none',
            duration:1500
          })
        }
      }
    });
    this.hideModal();
    
  },

  //跳转主页
  gotoshop: function () {
    wx.switchTab({
      url: '../../../pages/index/index',
      success: function (res) {
        console.log("跳转主页成功");
      },
      fail: function (res) {
        console.log("跳转主页失败");
      },
      complete: function (res) { },
    })
  },

  //跳转商品详情页面
  gotodetail:function(e){
    var goodid = e.currentTarget.dataset.goodid;
    console.log("你点击的书本bookid为："+goodid);
    wx.navigateTo({
      url: '/pages/others/details/details?goodid=' + goodid,
      success: function (res) { },
      fail: function (res) { },
      complete: function (res) { },
    })
  }
})