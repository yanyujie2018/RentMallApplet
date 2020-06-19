import request from '../../../utils/request.js'
Page({
  data: {
    navbar: ['已完成', '租用中', '待发货','待收货','待归还','待收回'],
    text:'您暂无相关订单',
    currentTab:0,
    waitSend: [], //待发货
    waitReceive: [], //待发货
    waitReturn: [], //待归还
    waitRecovery: [], //待收回
    finish1: [],  //已完成1
    finish2: [],  //已完成2
    waitPay: [], //待付款
    userName:null,
    infoimage:null,
    idState:0,
    openid:null,
    allOrder:null,
    yiwancheng_mes: [],
  },

  onLoad:function(option){
    var that = this;
    this.setData({
      currentTab: option.currentTab,
      infoimage: getApp().globalData.userInfo.avatarUrl,  //获取用户的头像
      userName: getApp().globalData.userInfo.nickName,  //获取用户的昵称
      idState:getApp().globalData.idState,
      openid:getApp().globalData.openid
    })
    var openid = getApp().globalData.openid;
    var req = new request();

    //这里修改过期订单的信息不再使用这种写法，而使用app.js中的写法，这里再去删选一次，主要是为了防止在app.js中没有执行成功的现象
    /*
    //先以当前用户ID为rentID查询订单状态为2并且超过租赁时间的订单，修改状态为3
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/modifyRentState.do',
      data: { "rentId": openid},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log("修改状态");
        console.log(res);
      }
    })
    */


    //查找过期rentmsg，修改状态，并扣除积分
    var list1 = [];
    var list2 = [];
    var list3 = [];
    var req = new request();
    //先以当前用户ID为rentID查询订单状态为2并且超过租赁时间但是还在归还期限内的订单，修改状态为3
    //不在期限内的修改为状态5
    req.getRequest('http://ZLofCampus.top/RentMall/modifyRentState.do', { "rentId": openid }, { "content-type": "application/json" }).then((res) => {
      console.log("测试promise")
      console.log(res);
      list1 = res.data.list1;
      list2 = res.data.list2;
      if (list1.length > 0) {
        wx.showModal({
          title: '提示',
          content: '您有商品需要归还！逾期押金将不再退还！',
          success: function (res) {
            if (list2.length > 0) {
              wx.showModal({
                title: '提示',
                content: '部分物品由于未按期归还，押金已扣除，如有特殊情况请主动与物主协商！详见订单页面',
              })
              //将积分减少20,注意，这里的扣分是每个商品均要扣分，所以需要循环
              for (var i = 0; i < list2.length; i++) {
                req.getRequest('http://ZLofCampus.top/RentMall/subScoreById.do', { "userId": openid }, { "content-type": "application/json" }).then((res) => {
                  if (res.data.msg) {
                    console.log("修改积分成功1");
                    getApp().globalData.userIntegration = res.data.score
                  }
                  console.log("积分=" + getApp().globalData.userIntegration)
                }).catch((err) => {
                  console.log("修改积分捕捉错误")
                })
              }
            }
          }
        })
      } else {
        if (list2.length > 0) {
          wx.showModal({
            title: '提示',
            content: '部分物品由于未按期归还，押金已扣除，如有特殊情况请主动与物主协商！详见订单页面',
          })
          //将积分减少20,注意，这里的扣分是每个商品均要扣分，所以需要循环
          for (var i=0; i < list2.length; i++) {
            req.getRequest('http://ZLofCampus.top/RentMall/subScoreById.do', { "userId": openid }, { "content-type": "application/json" }).then((res) => {
              if (res.data.msg) {
                console.log("修改积分成功1");
                getApp().globalData.userIntegration = res.data.score
              }
              console.log("积分=" + getApp().globalData.userIntegration)
            }).catch((err) => {
              console.log("修改积分捕捉错误")
            })
          }
        }
      }
    }).catch((err) => {
      console.log("测试错误捕捉")
      console.log(res);
    })


    setTimeout(function () {
      //查找租户的订单状态为3的，看有没有超过归还期限的
      req.getRequest('http://ZLofCampus.top/RentMall/modifyRentState2.do', { "rentId": openid }, { "content-type": "application/json" }).then((res) => {
        console.log("测试promise2")
        console.log(res)
        list3 = res.data.list1
        console.log("@@@@@@@@@@@@@@")
        console.log(list2.length);
        console.log(list3.length)
        if (list2.length <= 0 && list3.length > 0) {
          wx.showModal({
            title: '提示',
            content: '部分物品由于未按期归还，押金已扣除，如有特殊情况请主动与物主协商！详见订单页面',
          })
        }
        if (list3.length > 0) {
          console.log("进入list3的积分修改")
          //将积分减少20,注意，这里的扣分是每个商品均要扣分，所以需要循环
          for (var i = 0; i < list3.length; i++) {
            req.getRequest('http://ZLofCampus.top/RentMall/subScoreById.do', { "userId": openid }, { "content-type": "application/json" }).then((res) => {
              if (res.data.msg) {
                console.log("修改积分成功2");
                getApp().globalData.userIntegration = res.data.score
              }
            }).catch((err) => {
              console.log("修改积分捕捉错误")
            })
          }
        }
      }).catch((err) => {
        console.log("测试错误捕捉2")
        console.log(err);
      })
    }, 1000)

    //这里看看经过上面的两种订单的查找和修改后，积分会不会小于0，如果小于0，那么就封号(如果当前已经是封号状态就不用再执行了)
    setTimeout(function () {
      if (getApp().globalData.userIntegration < 0 && getApp().globalData.idState == 0) {
        wx.request({
          url: 'http://ZLofCampus.top/RentMall/updateIdState.do',
          data: { "userId": openid },
          method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
          header: { "content-type": "application/json" }, // 设置请求的 header
          success: function (res) {
            if (res.data.msg) {
              getApp().globalData.idState = 1;  //表示封锁账号
              wx.showModal({
                title: '提示',
                content: '您的账号已被封锁，解封请联系客服！',
              })
            }
          }
        })
      }
    }, 2500)
    //以上非法订单状态修改完毕



    if(option.currentTab==0){  //已完成
      //用户已完成订单的检索
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/findFinish.do',
        data: { "userId": openid },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("已完成")
          console.log(res);
          that.setData({
            finish1: res.data.list1,
            finish2: res.data.list2
          })
        }
      })
    } else if (option.currentTab == 1){  //租用中
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/findRent.do',
        data: { "rentId": openid, "rentState": 2 },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("租用中")
          console.log(res);
          that.setData({
            waitPay: res.data.list
          })
        }
      })
    } else if (option.currentTab == 2){  //待发货
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/findWaitSend.do',
        data: { "releaseId": openid, "rentState": 0 },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log(res);
          that.setData({
            waitSend: res.data.list
          })
        }
      })
    } else if (option.currentTab == 3) //待收货
    {
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/findWaitReceive.do',
        data: { "rentId": openid },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log(res);
          that.setData({
            waitReceive: res.data.list
          })
        }
      })
    } else if (option.currentTab == 4) //待归还
    {
      //以当前用户id为rentid，查询状态为3的订单
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/findWaitReturn.do',
        data: { "rentId": openid,"rentState":3 },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log(res);
          that.setData({
            waitReturn: res.data.list
          })
        }
      })

    }else if(option.currentTab==5){ //待收回
      //以当前用户id作为releaseId查询状态为7或3的订单（7表示租户已经归还，等物主收回；3表示租户正在归还）
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/findWaitSend2.do',
        data: { "releaseId": openid},
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("待收回")
          console.log(res);
          that.setData({
            waitRecovery: res.data.list
          })
        }
      })
    }
  },


  //点击订单进入订单详情页面
  toOrderDetail: function (e) {
    console.log("点击订单进入详情")
    var index=e.currentTarget.dataset.index; 
    var status = e.currentTarget.dataset.status; 
    var orderid = e.currentTarget.dataset.orderid;
    var rentLen=e.currentTarget.dataset.rentlen;
    var unit=e.currentTarget.dataset.unit;
    var status = e.currentTarget.dataset.status;
    wx.navigateTo({
      url: '../order_detail/order_details?index=' + index + "&orderid=" + orderid+"&rentLen="+rentLen+"&unit="+unit+"&status="+status,
    })
  },


  // 头部导航栏滑动
  navbarTap: function (e) {
    var that = this;
    console.log("导航栏") //1-待付款 2-代发货 3-待收货 4-待归还 5-待收回 6-退款中
    console.log(e.currentTarget.dataset.idx)
    this.setData({
      currentTab: e.currentTarget.dataset.idx
    })
    var openid = getApp().globalData.openid;

    //已进入页面，先以当前用户的id作为rentid查询其订单状态为2且超过租赁期限的订单，将这些订单修改为状态3

    if (e.currentTarget.dataset.idx==2)  //当时点击待发货时，以当前用户id为发货者id，查找状态为0的租赁信息
    {
        wx.request({
          url: 'http://ZLofCampus.top/RentMall/findWaitSend.do',
          data: {"releaseId":openid,"rentState":0},
          method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
          header: { "content-type": "application/json" }, // 设置请求的 header
          success:function(res){
            console.log(res);
            that.setData({
              waitSend:res.data.list
            })
          }
        })
    } else if (e.currentTarget.dataset.idx == 3){ //待收货
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/findWaitReceive.do',
        data: { "rentId": openid },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log(res);
          that.setData({
            waitReceive: res.data.list
          })
        }
      })
    } else if (e.currentTarget.dataset.idx == 4)  //待归还
    {
      //以当前用户id为rentid，查询状态为3的订单
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/findRent.do',
        data: { "rentId": openid, "rentState": 3 },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("待归还列表")
          console.log(res);
          that.setData({
            waitReturn: res.data.list
          })
        }
      })
    } else if (e.currentTarget.dataset.idx == 5) //待收回
    {
      //以当前用户id作为releaseId查询状态为7或3的订单
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/findWaitSend2.do',
        data: { "releaseId": openid},
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("待收回")
          console.log(res);
          if(res.statusCode==200){
            that.setData({
              waitRecovery: res.data.list
            })
          }
        }
      })
    } else if (e.currentTarget.dataset.idx == 6)  //退款中
    {

    } else if (e.currentTarget.dataset.idx == 1) //租用中
    {
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/findRent.do',
        data: { "rentId": openid ,"rentState":2},
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log(res);
          that.setData({
            waitPay: res.data.list
          })
        }
      })
    } else if (e.currentTarget.dataset.idx == 0)  //已完成
    {
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/findFinish.do',
        data: { "userId": openid },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("已完成")
          console.log(res);
          //作为租用者查出来的交易成功是list2
          //作为出租者查出来的交易成功是list1
          that.setData({
            finish1: res.data.list1,
            finish2: res.data.list2
          })
        }
      })
    }
  },

  //点击收货
  goReceive: function (e) {
    console.log("点击收货")
    console.log(e);
    var id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '提示',
      content: '请确认是否已收货',
      success: function (res) {
        if (res.confirm) {
          //修改状态
          wx.request({
            url: 'http://ZLofCampus.top/RentMall/modifyRentState1To2.do',
            data: { "Id": id },
            method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
            header: { "content-type": "application/json" }, // 设置请求的 header
            success: function (res) {
              console.log("点击收货")
              console.log(res);
              if (res.data.msg == true) {
                wx.showToast({
                  title: '收货成功！',
                  icon: 'success',
                  duration: 1500,
                  success(res) {
                    setTimeout(function () {
                      //要延时执行的代码
                      wx.switchTab({
                        url: '../../self/self?',
                      })
                    }, 1000)
                  }
                })
              }else{
                wx.showToast({
                  title: '网络出错了.....',
                  icon: 'none',
                  duration: 1000
                })
              }
            }
          })
        }
      }
    })

  },

  
  //点击收回，修改状态7为4交易完成，并且将商品表中的数目加上租赁表中数目，表示商品的现有数目恢复原样
  goRecovery: function (e) {
    console.log("点击收回")
    var id = e.currentTarget.dataset.id;
    var goodid = e.currentTarget.dataset.goodid;
    var rentNum = e.currentTarget.dataset.rentnum;
    console.log(goodid)
    wx.showModal({
      title: '提示',
      content: '请确认是否已收回商品',
      success: function (res) {
        if (res.confirm) {
          //1、修改状态
          wx.request({
            url: 'http://ZLofCampus.top/RentMall/modifyRentState7To4.do',
            data: { "Id": id },
            method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
            header: { "content-type": "application/json" }, // 设置请求的 header
            success: function (res) {
              console.log(res);
              if (res.data.msg == true) {
                //2、恢复数量
                wx.request({
                  url: 'http://ZLofCampus.top/RentMall/addNumAfterTrade.do',
                  data: { "goodId": goodid,"rentNum":rentNum },
                  method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                  header: { "content-type": "application/json" }, // 设置请求的 header
                  success:function(res){
                    if(res.data.msg)
                    {
                      wx.showToast({
                        title: '交易完成！',
                        icon: 'success',
                        duration: 1500,
                        success(res) {
                          setTimeout(function () {
                            //要延时执行的代码
                            wx.switchTab({
                              url: '../../self/self?',
                            })
                          }, 1000)
                        }
                      })
                    }
                  }
                })
              }else{
                wx.showToast({
                  title: '网络出错啦....',
                  icon:'none',
                  duration:1000
                })
              }
            }
          })
        }
      }
    })
  },
  
  //点击归还，查询到该物品发布者的手机号，调起拨号，同时修改状态3为7，主人拿到货品点击收回后状态变为4交易完成
  goReturn: function (e) {
    console.log("点击归还")
    let openid = getApp().globalData.openid;
    var id = e.currentTarget.dataset.id;
    var that = this;
    //先根据租赁信息的id获取发布该商品信息的用户电话号码
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getReleasePhone.do',
      data: { "Id": id },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log(res)
        if (res.data.msg)  //成功获取到发布者电话
        {
          console.log(res.data.releasePhone)
          let releasePhone = res.data.releasePhone
          //调用微信的调用电话的接口
          wx.makePhoneCall({
            phoneNumber: releasePhone,
            success: function (res) { //调用成功的回调函数
              //调起电话后先谈个框，看是不是确认与主人已取得联系
              wx.showModal({
                title: '提示',
                content: '确认已与物主取得联系？',
                success(res) {
                  if (res.confirm) {  //点击确认修改状态
                    //拨打电话后将租赁状态由待归还3改为7
                    wx.request({
                      url: 'http://ZLofCampus.top/RentMall/modifyRentState3To7.do',
                      data: { "Id": id },
                      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                      header: { "content-type": "application/json" }, // 设置请求的 header
                      success: function (res) {
                        console.log(res);
                        if (res.data.msg == true) {
                          wx.showToast({
                            title: '请及时归还！',
                            icon: 'success',
                            duration: 1500,
                            success(res) {
                              setTimeout(function () {
                                //要延时执行的代码
                                wx.switchTab({
                                  url: '../../self/self?',
                                })
                              }, 1000)
                            }
                          })
                        }
                      }
                    })
                  }
                }
              })
            }
          })
        } else {  //未获取到发布者电话
          wx.showToast({
            title: '网络出错了.....',
            icon:'none',
            duration:1000
          })
        }
      }
    })
  },

  //点击发货，查询到该物品租用者的手机号，调起拨号，同时改变状态0为1，用户拿到东西点击收货后再次改变
  //物主发货之后，rentDate填入当前时间，并且天数从目前开始计算，填入dueTime到租赁表的相应条目中
  //如果当前用户账号已经被封，那么将不再允许发货
  goSend: function (e) {
    console.log("点击发货")
    let openid = getApp().globalData.openid;
    var id = e.currentTarget.dataset.id;
    var goodid=e.currentTarget.dataset.goodid;
    var rentlen=e.currentTarget.dataset.rentlen;
    var unit = e.currentTarget.dataset.unit;
    var that = this;
    if(getApp().globalData.idState==0){
      //先根据租赁信息的id获取租用该商品的用户电话号码
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/getRentPhone.do',
        data: { "Id": id },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          if (res.data.msg)  //成功获取到发布者电话
          {
            let rentPhone = res.data.rentPhone
            //调用微信的调用电话的接口
            wx.makePhoneCall({
              phoneNumber: rentPhone,
              success: function (res) { //调用成功的回调函数
                //调起电话后先谈个框，看是不是确认与客户已取得联系
                wx.showModal({
                  title: '提示',
                  content: '确认已与用户取得联系？',
                  success(res) {
                    if (res.confirm) {  //点击确认修改状态
                      //拨打电话后将租赁状态由待发货0改为1
                      wx.request({
                        url: 'http://ZLofCampus.top/RentMall/modifyRentState0To1.do',
                        data: { "Id": id },
                        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                        header: { "content-type": "application/json" }, // 设置请求的 header
                        success: function (res) {
                          console.log(res);
                          if (res.data.msg == true) {
                            //修改租赁表中的时间，开始计时
                            wx.request({
                              url: 'http://ZLofCampus.top/RentMall/modifyRentTime.do',
                              data: { "Id": id, "timeLen": rentlen, "unit": unit },
                              method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                              header: { "content-type": "application/json" }, // 设置请求的 header
                              success: function (res) {
                                if (res.data.msg) {
                                  wx.showToast({
                                    title: '请及时发货！',
                                    icon: 'success',
                                    duration: 1500,
                                    success(res) {
                                      setTimeout(function () {
                                        //要延时执行的代码
                                        wx.switchTab({
                                          url: '../../self/self?',
                                        })
                                      }, 1000)
                                    }
                                  })
                                }
                              }
                            })
                          } else {
                            wx.showToast({
                              title: '网络出错了.....',
                              icon: 'none',
                              duration: 1000
                            })
                          }
                        }
                      })
                    }
                  }
                })
              }
            })
          } else {  //未获取到租用者电话
            wx.showToast({
              title: '网络出错了.....',
              icon: 'none',
              duration: 1000
            })
          }
        }
      })
    }else{  //当前账号已被封锁
      wx.showModal({
        title: '提示',
        content: '您的账号已被封锁，未发货商品交易已取消！',
      })
    }
    
  },

  //点击催促归还
  goUrge(e){
    console.log("点击催促归还")
    var id = e.currentTarget.dataset.id;
    //先根据租赁信息的id获取租用该商品的用户电话号码
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getRentPhone.do',
      data: { "Id": id },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        if (res.data.msg)  //成功获取到发布者电话
        {
          let rentPhone = res.data.rentPhone
          console.log(rentPhone)
          wx.makePhoneCall({
            phoneNumber: rentPhone,
            success:function(res){
              console.log("拨打电话成功");
            }
          })
        }else{
          wx.showToast({
            title: '网络出错了.....',
            icon: 'none',
            duration: 1000
          })
        }
      }
    })
  },

  //跳转首页
  gotoshop:function(){
    wx.switchTab({
      url: '../../index/index',
      success: function (res) {
        console.log("跳转主页成功");
       },
      fail: function (res) {
        console.log("跳转主页失败");
      },
      complete: function (res) { },
    })
  },

  //跳转评价页面
  goRemark:function(e){
    var goodid = e.currentTarget.dataset.goodid;
    var releaseid = e.currentTarget.dataset.releaseid;
    var id = e.currentTarget.dataset.id;
    var rentstate = e.currentTarget.dataset.rentstate;
    console.log("去评论页面")
    console.log(rentstate)
    wx.navigateTo({
      url: '../../comment/comment?goodId='+goodid+"&releaseId="+releaseid+"&Id="+id+"&rentState="+rentstate,
    })
  },

  //点击协商触发函数
  goConsult(e){
    console.log("点击协商")
    //找到物主的电话，进行协商
    var releaseId = e.currentTarget.dataset.releaseid;
    var id = e.currentTarget.dataset.id;
    console.log("id="+id);
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getReleasePhone.do',
      data: { "Id": id },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        if (res.data.msg)  //成功获取到发布者电话
        {
          console.log(res.data.releasePhone)
          let releasePhone = res.data.releasePhone
          //向发布者发起电话
          wx.makePhoneCall({
            phoneNumber: releasePhone,
            success:function(res){
              console.log("拨打电话成功！")
            }
          })
        } else {
          wx.showToast({
            title: '网络出错了.....',
            icon: 'none',
            duration: 1000
          })
        }
      }
    })
  },


  //点击协商触发函数
  goConsult2(e) {
    console.log("点击协商")
    //找到租户的电话，进行协商
    var id = e.currentTarget.dataset.id;
    console.log("id=" + id);
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getRentPhone.do',
      data: { "Id": id },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        if (res.data.msg)  //成功获取到发布者电话
        {
          console.log(res.data.rentPhone);
          let rentPhone = res.data.rentPhone
          //向租用者发起电话
          wx.makePhoneCall({
            phoneNumber: rentPhone,
            success: function (res) {
              console.log("拨打电话成功！")
            }
          })
        } else {
          wx.showToast({
            title: '网络出错了.....',
            icon: 'none',
            duration: 1000
          })
        }
      }
    })
  },

  //点击催促发货，则查询出物主号码，调起拨打电话
  goUrgeDelivery(e){
    console.log("点击催促发货")
    console.log(e);
    var id = e.currentTarget.dataset.id;
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getReleasePhone.do',
      data: { "Id": id },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        if (res.data.msg)  //成功获取到发布者电话
        {
          console.log(res.data.releasePhone)
          let releasePhone = res.data.releasePhone
          //向发布者发起电话
          wx.makePhoneCall({
            phoneNumber: releasePhone,
            success: function (res) {
              console.log("拨打电话成功！")
            }
          })
        } else {
          wx.showToast({
            title: '网络出错了.....',
            icon: 'none',
            duration: 1000
          })
        }
      }
    })
  },

  goReturn2(e){
    var id = e.currentTarget.dataset.id;
    console.log("催促归还")
    console.log(id)
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getRentPhone.do',
      data: { "Id": id },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        if (res.data.msg)  //成功获取到租用者电话
        {
          console.log(res.data.rentPhone);
          let rentPhone = res.data.rentPhone
          //向租用者发起电话
          wx.makePhoneCall({
            phoneNumber: rentPhone,
            success: function (res) {
              console.log("拨打电话成功！")
            }
          })
        } else {
          wx.showToast({
            title: '网络出错了.....',
            icon: 'none',
            duration: 1000
          })
        }
      }
    })
  },

  onShow:function(){

  },

  onUnload:function(){
    wx.switchTab({
      url: '../self/self',
    })
  }

});