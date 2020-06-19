Page({
  data: {
    activeIndex: 0,
    scrolltop:0,
    tabs: [
      {
        id: 1,
        tabName: '热搜推荐'
      }, {
        id: 2,
        tabName: '精品好书'
      }, {
        id: 3,
        tabName: '新品热卖'
      }, {
        id: 4,
        tabName: '出行好物'
      }, {
        id: 5,
        tabName: '电子风暴'
      }, {
        id: 6,
        tabName: '杂货出租'
      }
    ],
    resoutuijian:null,
    jingpinhaoshu:null,
    xinpinremai:null,
    chuxinhaowu:null,
    dianzifengbao:null,
    zahuochuzu:null,
  },

  //点击搜索框，跳转搜索页面
  search_page: function () {
    wx.navigateTo({
      url: '../search_page/search_page',
      success: function (res) { },
      fail: function (res) { },
      complete: function (res) { },
    })
  },

  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    var that =this;
    console.log("分类")
    console.log(getApp().globalData.fromIndex)
    console.log(getApp().globalData.fenleiTabId)
    if (getApp().globalData.fromIndex)
    {
      this.setData({
        activeIndex: getApp().globalData.fenleiTabId
      })
    }
    //获取系统信息
    wx.getSystemInfo({
      success: (res) => {
        that.setData({
          deviceHeight: res.windowHeight,  //获取可使用窗口高度
        });
      }
    }); 

    // 获取热搜推荐数据
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getHotGood.do',
      data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
       that.setData({
         resoutuijian: res.data.hotList
       });
      }
    });

    // 获取精品好书数据
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getHot.do',
      data: { "type": 1 },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        that.setData({
          jingpinhaoshu: res.data.list
        });
      }
    });

    // 获取新品热卖数据
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getNewGood.do',
      data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        that.setData({
          xinpinremai: res.data.newList
        });
      }
    });

    // 获取出行好物数据
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getHot.do',
      data: { "type": 2 },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        that.setData({
          chuxinhaowu: res.data.list
        });
      }
    });

    // 获取电子风暴数据
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getHot.do',
      data: { "type": 3 },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        that.setData({
          dianzifengbao: res.data.list
        });
      }
    });

    // 获取杂货出租数据
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getHotGroceries.do',
      data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        that.setData({
          zahuochuzu: res.data.groceriesList
        });
      }
    });
  },

  //切换种类的事件函数
  changeTab: function (e) {
    this.setData({
      activeIndex: e.currentTarget.dataset.index,
      scrolltop:0,
    })
  },

//跳转商品详情页面
  gotodetail:function(res){ 
    var goodid = res.currentTarget.dataset.goodid;
    wx.navigateTo({
      url: '../others/details/details?goodid='+goodid,
    })
  },

  //分享功能
  onShareAppMessage: function (res) {
    if (res.from === "menu") {
      console.log("来自右上角转发菜单")
    }
    return {
      title: "校园租赁商城",
      path: '/pages/index/index',
      imageUrl: "../images/nothing.jpg",
      success: (res) => {
        console.log("转发成功", res);
      },
      fail: (res) => {
        console.log("转发失败", res);
      }
    }
  },

  onShow:function(){
    if (getApp().globalData.fromIndex) {
      this.setData({
        activeIndex: getApp().globalData.fenleiTabId
      })
    }
  },

  getMore: function () {
    // this.setData({
    //   contentList: this.data.contentList.concat([
    //     { text: ' ' },
    //     { text: ' ' },
    //     { text: ' ' },
    //     { text: ' ' },
    //     { text: ' ' }
    //   ])
    // });
  },
})