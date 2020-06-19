Page({
  data: {
    resoutuijian:[], //热搜推荐
    jingpinhaoshu:[],  //精品好书
    xinpinremai:[],  //新品热卖
    dianzifengbao:[],  //电子风暴
    chuxinhaowu: [],  //出行好物
    zahuochuzu: [],  //杂货出租
  },
  search_page:function(){
    wx.navigateTo({
      url: '../search_page/search_page',
      success: function(res) {},
      fail: function(res) {},
      complete: function(res) {},
    })
  },
  onLoad(e) {
    var that = this;

    this.setData({
      msgList: [
        { url: "url", title: "    欢迎来到校园租赁商城  " },
        { url: "url", title: "     闲置租赁，实惠多多！   " },
        { url: "url", title: "    快来找找你的心仪物吧！   " },
        ] 
    });

    //热搜推荐前三个数据抓取（找浏览次数最多的三个商品）
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getHotGood.do',
      data: { },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log("hot")
        console.log(res)
        that.setData({
          resoutuijian:res.data.hotList
        });
      }
    });

    //精品好书前三个数据抓取(获取浏览最多的三条图书信息)
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getHot.do',
      data: {"type":1},      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log(res)
        that.setData({
          jingpinhaoshu: res.data.list
        });
      }
    });

    //新品热卖前三个数据抓取（最近发布的三个商品信息）
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getNewGood.do',   //http://ZLofCampus.top/RentMall/getNewGood.do
      data: {},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log("新品")
        console.log(res)
        that.setData({
          xinpinremai: res.data.newList
        });
      }
    });

    //电子风暴前三个数据抓取（最近发布的三个电子产品商品信息）
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getHot.do',
      data: {"type":3},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log("电子风暴")
        console.log(res)
        that.setData({
          dianzifengbao: res.data.list
        });
      }
    });

    //出行好物前三个数据抓取 （最近发布的三个出行工具商品信息）
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getHot.do',
      data: {"type":2},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log("出行")
        console.log(res)
        that.setData({
          chuxinhaowu: res.data.list
        });
      }
    });

    //杂货出租前三个数据抓取
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

    if(getApp().globalData.noRead>0){
      wx.setTabBarBadge({
        index: 3,
        text: String(getApp().globalData.noRead),
      })
    }else{
      wx.removeTabBarBadge({
        index: 3,
      })
    }

  }, 

  toDetail:function(e){
    var goodid = e.currentTarget.dataset.goodid;
    console.log("您刚点击的商品id为："+goodid);
    wx.navigateTo({
      url: '../others/details/details?goodid=' + goodid,
      success: function(res) {},
      fail: function(res) {},
      complete: function(res) {},
    })
  },

  //点击热搜推荐的查看更多
  resoutuijian:function(){
    //switchTab不能携带参数，所以只能采用全局的方式传递参数
    getApp().globalData.fenleiTabId=0;
    getApp().globalData.fromIndex = true;
    wx.switchTab({
      url: '../fenlei/fenlei',
    })
  },

   //点击精品好书的查看更多
  jingpinhaoshu:function(){
    getApp().globalData.fenleiTabId = 1;
    getApp().globalData.fromIndex = true;
    wx.switchTab({
      url: '../fenlei/fenlei',
    })
  },

  //新品热卖
  xinpinremai: function () {
    getApp().globalData.fenleiTabId = 2;
    getApp().globalData.fromIndex = true;
    wx.switchTab({
      url: '../fenlei/fenlei',
    })
  },

  //出行好物
  chuxinhaowu: function () {
    getApp().globalData.fenleiTabId = 3;
    getApp().globalData.fromIndex = true;
    wx.switchTab({
      url: '../fenlei/fenlei',
    })
  },

  //电子风暴
  dianzifengbao: function () {
    getApp().globalData.fenleiTabId = 4;
    getApp().globalData.fromIndex = true;
    wx.switchTab({
      url: '../fenlei/fenlei?id',
    })
  },

  //杂货出租
  zahuochuzu: function () {
    getApp().globalData.fenleiTabId = 5;
    getApp().globalData.fromIndex = true;
    wx.switchTab({
      url: '../fenlei/fenlei?id',
    })
  },

  
//分享功能
  onShareAppMessage: function (res) {    
    if(res.from === "menu"){
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
    this.onLoad();
  }


})