Page({
  data: {
    iscomments:true,
    comments:'',
    remarks:'',
    itemdata:null,
    itemdetail: null,
    releasePhone:null, //该商品的发布者电话
    rentLen:null,
    unit:null,
    openid:getApp().globalData.openid,
    status:null,
  },

  onLoad: function (options) {
    var that = this;
    console.log("订单详情")
    console.log(options);
    var rentId = options.orderid;
    this.setData({
      rentLen: options.rentLen,
      unit: options.unit,
      status:options.status,
      openid:getApp().globalData.openid
    })
    
  //查询订单详细信息
    wx.request({
      url: 'http://www.ZLofCampus.top/RentMall/QueryRentDetail.do',
      data: { "Id":rentId },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log(res);
        that.setData({
          itemdetail:res.data.msg
        })
        if (res.data.msg.remarks == "" || res.data.msg.remarks == 'null' || res.data.msg.remarks == null)
        {
          that.setData({
            remarks:'无备注信息'
          })
        }else{
          that.setData({
            remarks: res.data.msg.remarks
          })
        }
        if(res.data.msg.rentState==4) //已完成
        {
          that.setData({  //可以评论
            iscomments:true
          })
        }else
        {
          that.setData({  
            iscomments: false
          })

        }
      }
    });
  },

  //点击收货
  goReceive: function(e){
    console.log(e);
    var id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '提示',
      content: '请确认是否已收货',
      success:function(res){
        if(res.confirm){
          //修改状态
          wx.request({
            url: 'http://www.ZLofCampus.top/RentMall/modifyRentState1To2.do',
            data: { "Id": id },
            method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
            header: { "content-type": "application/json" }, // 设置请求的 header
            success: function (res) {
              console.log(res);
              if(res.data.msg==true)
              {
                wx.showToast({
                  title: '收货成功！',
                  icon:'success',
                  duration:1500,
                  success(res){
                    setTimeout(function () {
                      //要延时执行的代码
                      wx.switchTab({
                        url: '../../self/self?',
                      })
                    }, 1000) 
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
    
  },

 //点击归还，查询到该物品发布者的手机号，调起拨号，同时修改状态3为7，主人拿到货品点击收回后状态变为4交易完成
  goReturn:function(e){
    let openid = getApp().globalData.openid;
    var id = e.currentTarget.dataset.id;
    var that = this;
    //先根据租赁信息的id获取发布该商品信息的用户电话号码
    wx.request({
      url: 'http://www.ZLofCampus.top/RentMall/getReleasePhone.do',
      data: { "Id": id },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res){
        if(res.data.msg)  //成功获取到发布者电话
        {
          let releasePhone = res.data.releasePhone
          //调用微信的调用电话的接口
          wx.makePhoneCall({ 
            phoneNumber: releasePhone,
            success:function(res){ //调用成功的回调函数
              //调起电话后先谈个框，看是不是确认与主人已取得联系
              wx.showModal({
                title: '提示',
                content: '确认已与物主取得联系？',
                success(res){
                  if(res.confirm){  //点击确认修改状态
                    //拨打电话后将租赁状态由待归还3改为7
                    wx.request({
                      url: 'http://www.ZLofCampus.top/RentMall/modifyRentState3To7.do',
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
        }else{  //未获取到发布者电话
          wx.showToast({
            title: '网络出错了.....',
            icon: 'none',
            duration: 1000
          })
        }
      }
    })
  },
  
  //点击发货，查询到该物品租用者的手机号，调起拨号，同时改变状态0为1，用户拿到东西点击收货后再次改变
  goSend: function (e) {
    let openid = getApp().globalData.openid;
    var id = e.currentTarget.dataset.id;
    var rentLen=this.data.rentLen;
    var unit=this.data.unit;
    var that = this;
    //先根据租赁信息的id获取租用该商品的用户电话号码
    wx.request({
      url: 'http://www.ZLofCampus.top/RentMall/getRentPhone.do',
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
                content: '确认已与客户取得联系？',
                success(res) {
                  if (res.confirm) {  //点击确认修改状态
                    //拨打电话后将租赁状态由待发货0改为1
                    wx.request({
                      url: 'http://www.ZLofCampus.top/RentMall/modifyRentState0To1.do',
                      data: { "Id": id },
                      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                      header: { "content-type": "application/json" }, // 设置请求的 header
                      success: function (res) {
                        console.log(res);
                        if (res.data.msg == true) {

                          //修改租赁表中的时间，开始计时
                          wx.request({
                            url: 'http://www.ZLofCampus.top/RentMall/modifyRentTime.do',
                            data: { "Id": id, "timeLen": rentLen, "unit": unit },
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
        } else {  //未获取到发布者电话
          wx.showToast({
            title: '网络出错了.....',
            icon: 'none',
            duration: 1000
          })
        }
      }
    })
  },

  //点击收回，修改状态7为4交易完成
  goRecovery:function(e){
    var id = e.currentTarget.dataset.id;
    var goodid = e.currentTarget.dataset.goodid
    var rentNum = e.currentTarget.dataset.rentnum
    wx.showModal({
      title: '提示',
      content: '请确认是否已收回商品',
      success: function (res) {
        if (res.confirm) {
          //修改状态
          wx.request({
            url: 'http://www.ZLofCampus.top/RentMall/modifyRentState7To4.do',
            data: { "Id": id },
            method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
            header: { "content-type": "application/json" }, // 设置请求的 header
            success: function (res) {
              console.log(res);
              if (res.data.msg == true) {
                //2、恢复数量
                wx.request({
                  url: 'http://www.ZLofCampus.top/RentMall/addNumAfterTrade.do',
                  data: { "goodId": goodid, "rentNum": rentNum },
                  method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                  header: { "content-type": "application/json" }, // 设置请求的 header
                  success: function (res) {
                    if (res.data.msg) {
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
              } else {
                wx.showToast({
                  title: '网络出错啦....',
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


  //点击协商触发函数
  goConsult(e) {
    console.log("点击协商")
    //找到物主的电话，进行协商
    var releaseId = e.currentTarget.dataset.releaseid;
    var id = e.currentTarget.dataset.id;
    console.log("id=" + id);
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
  goUrgeDelivery(e) {
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


  //点击催促发货，则查询出物主号码，调起拨打电话
  goUrgeDelivery(e) {
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


  //点击催促归还
  goUrge(e) {
    console.log("点击催促归还")
    var id = e.currentTarget.dataset.id;
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
          console.log(rentPhone)
          wx.makePhoneCall({
            phoneNumber: rentPhone,
            success: function (res) {
              console.log("拨打电话成功");
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

  //跳转评价页面
  goRemark: function (e) {
    var goodid = e.currentTarget.dataset.goodid;
    var releaseid = e.currentTarget.dataset.releaseid;
    var id = e.currentTarget.dataset.id;
    var rentstate = e.currentTarget.dataset.rentstate;
    console.log("去评论页面")
    console.log(rentstate)
    wx.navigateTo({
      url: '../../comment/comment?goodId=' + goodid + "&releaseId=" + releaseid + "&Id=" + id + "&rentState=" + rentstate,
    })
  },

})