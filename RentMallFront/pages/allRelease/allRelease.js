const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentTab: 0,
    //winHeight: windowHeight(),
    orderStatus: ["全部", "待付款", "待发货", "待收货", "已收货"],
    pageIndex: 1,  //标记当前是第几页
    pageSize: 20,  //一页有20项显示
    urlTool:'',
    loading: true
  },
  //类目切换
  swichNav: function (e) {
    var cur = e.target.dataset.current
    if (this.data.currentTab == cur) {
      return false
    }
    this._getOrderList(cur)
    this.setData({
      currentTab: cur,
      loading: true,
      itemList: ''
    })
  },

  //value 改变时触发 change 事件，event.detail = {value: value}
  bindDateChange: function (e) {
    this.setData({
      date: e.detail.value
    })
  },
  _goGoodsDetails: function (e) {
    wx.navigateTo({
      url: '../orderdetails/index?orderId=' + e.currentTarget.dataset.orderid
    })
  },
  //支付订单
  _payOrder: function (e) {
    var self = this
    var orderId = e.currentTarget.dataset.order
    orderList.payOrder({
      unionOrderId: orderId,
      payType: 301
    }, function (res) {
      if (res.data.code == 200) {
        var data = res.data.data
        wxpay(data, function (req) {
          // console.log(req);
          if (req.errMsg.indexOf("requestPayment:ok") >= 0) {
            wx.showToast({
              title: '支付成功',
              icon: 'success',
              success: function () {
                self._getOrderList()
              }
            })
          } else if (req.errMsg.indexOf("requestPayment:fail") >= 0 || req.errMsg.indexOf("requestPayment:cancel") >= 0) {
            //requestPayment:fail:该订单已过期，请重新下单
            var msgs = req.errMsg.split(":");
            wx.showToast({
              title: msgs.length == 3 ? msgs[2] : '支付失败',
              icon: "none"
            })
          }
        })
      }
    })
  },
  //取消订单
  _cancelOrder: function (e) {
    var self = this
    var orderStatus = this.data.currentTab
    var orderId = e.currentTarget.dataset.order
    wx.showModal({
      content: '您确认要取消当前订单吗？',
      success: function (res) {
        if (res.confirm) {
          orderList.closeOrder({
            orderId: orderId
          }, function (req) {
            wx.showLoading({
              title: '取消中...',
              icon: 'loading',
              success: function () {
                if (req.data.code == 200) {
                  wx.showLoading({
                    title: '取消成功',
                    icon: 'success'
                  })
                  self._getOrderList(orderStatus)
                  wx.hideLoading()
                }
              }
            })
          })
        }
      }
    })
  },

  //删除订单
  _deleteOrder: function (e) {
    var self = this
    var orderStatus = this.data.currentTab
    var orderId = e.currentTarget.dataset.order
    wx.showModal({
      content: '您确认要删除当前订单吗？',
      success: function (res) {
        if (res.confirm) {
          orderList.deleteOrder({
            orderId: orderId
          }, function (req) {
            wx.showToast({
              title: '删除中...',
              icon: 'loading',
              success: function () {
                if (req.data.code == 200) {
                  wx.showToast({
                    title: '删除成功',
                    icon: 'success',
                    success: function () {
                      self._getOrderList(orderStatus)
                    }
                  })
                }
              }
            })
          })
        }
      }
    })
  },

  //确认收货
  _confirmOrder: function (e) {
    var self = this
    var orderStatus = this.data.currentTab
    var orderId = e.currentTarget.dataset.order[0].orderId
    wx.showModal({
      content: '您确认收货吗？',
      success: function (res) {
        self.setData({
          loading: true
        })
        if (res.confirm) {
          orderList.confirmOrder({
            orderId: orderId
          }, function (res) {
            if (res.data.code == 200) {
              var currentTab = 4
              self.setData({
                currentTab: currentTab
              })
              self._getOrderList(currentTab)
            }
          })
        }
      }
    })
  },

  //获取发布商品列表
  _getOrderList: function (customerId) {
    var that = this
    var data = {
      pageSize: this.data.pageSize,
      pageIndex: this.data.pageIndex,
      orderStatus: customerId,
    }
    var openid = app.globalData.openid;
    wx.request({
      url: 'http://192.168.56.1:8080/RentMall/showAllRelease.do',
      data: {"userId":openid},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        console.log("getList");
        console.log(res);
        that.setData({
          itemList:res.data.list,  //注入数据
          loading:false
        })
      }
    })
   
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var self = this
    var currenttab = options.currenttab || 0
    this.setData({
      currentTab: currenttab
    })
    this._getOrderList(currenttab)
  },

  onShow: function () { },

  onPullDownRefresh: function () {
    self.setData({
      loading: true
    })
    wx.stopPullDownRefresh()
    var orderStatus = this.data.currentTab
    this._getOrderList()
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    this.setData({
      loading: true
    })
    var that = this
    var page = this.data.pageIndex + 1
    var itemList = this.data.itemList
    var orderStatus = this.data.currentTab
    var data = {
      pageSize: this.data.pageSize,
      pageIndex: page,
      orderStatus: orderStatus,
    }
    orderList.getOrderList(data, function (res) {
      if (res.data.data.length > 0) {
        self.setData({
          itemList: itemList.concat(res.data.data),
          loading: false,
          pageIndex: page
        })
      } else {
        self.setData({
          loading: false,
        })
        wx.showToast({
          title: '没有更多记录...',
          icon: 'none'
        })
      }
    })
  }

})