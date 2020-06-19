// pages/others/balance/balance.js
var app = getApp();
Page({
  data: {
    userInfo: {}, 
    mylist:[],
    totalmoney:null,
    totalDeposit:null,
    acountlist:[],
    usermes:null,
    consigneePhone:null,
    consigneeName:null,
    fromcart:null,
    remarks:null
  },
  onLoad: function (options){
    var that = this;
    var split= null;
    console.log("结算")
    console.log(options)
    var user = app.globalData.usermes;
    var fromcart = options.fromcart;
    that.setData({
        mylist: JSON.parse(options.model),  //记录计算商品的信息
        acountlist: JSON.parse(options.acount),  //记录每个商品的结算个数
        userInfo: app.globalData.userInfo,
        usermes:app.globalData.usermes,
        fromcart:fromcart
    })
    if(user.userName==null || user.userName==""){  //如果用户暂未填写用户姓名，那么就暂时使用微信的昵称代替
      that.setData({
        consigneeName: app.globalData.userInfo.nickName
      })
    } else {
      that.setData({
        consigneeName: user.userName
      })
    }

    if(user.phone==null || user.phone==""){
      that.setData({
        consigneePhone:"未绑定手机"
      })
    } else {
      that.setData({
        consigneePhone: user.phone
      })
    }

    //总价和押金汇总
    var totalmoney = 0;
    var totalDeposit=0;
    for (var i = 0; i < this.data.mylist.length ; i++) {
      totalmoney = totalmoney + this.data.mylist[i].price * this.data.acountlist[i] * this.data.mylist[i].timeLen;
      totalDeposit = totalDeposit + this.data.mylist[i].deposit * this.data.acountlist[i];
    }
    //更新数据
    that.setData({
      totalmoney: parseFloat(parseFloat(totalmoney).toFixed(2)),
      totalDeposit: parseFloat(parseFloat(totalDeposit).toFixed(2)),
    });
  },

  onshow(){
    console.log("再次显示")
    var user = app.globalData.usermes;
    if (user.userName == null) {  //如果用户暂未填写用户姓名，那么就暂时使用微信的昵称代替
      that.setData({
        consigneeName: app.globalData.userInfo.nickName
      })
    }else{
      that.setData({
        consigneeName: user.userName
      })
    }
    if (user.phone == null) {
      that.setData({
        consigneePhone: "暂未绑定手机"
      })
    }else{
      that.setData({
        consigneePhone:user.phone
      })
    }

  },


  //点击提交订单的监听函数
  //先判断用户是否已已经完善手机号码，如果未完善，则先提示完善并且跳转至我的设置页面
  topay:function(){
    var that = this;
    var len = that.data.mylist.length;
    var mylist=that.data.mylist;
    var acountlist = that.data.acountlist;
    var openid = app.globalData.openid;
    var user = app.globalData.usermes;
    console.log("mylist")
    console.log(mylist)
    if(getApp().globalData.idState==0) //账号正常，可以购买商品
    {
      if (user.phone != null) {  //当前用户已绑定手机号，可以进行支付

        //这里是从购物车跳转结算的方法
        //支付：1、将该条租赁信息填写入租赁表中，并设置状态为0，此时先不要吧到期时间填写上（增加了一个租户付款时间）
        //2、将商品表中的商品数量减去租用的数量
        //3、将用户购物车中的这几个结算了的商品删除
        //注意：这里需要使用循环处理每一个商品
        for (var i = 0; i < len; i++) {
          let cartId = mylist[i].cartId
          let cartGoodId = mylist[i].cartGoodId
          let cartNum = mylist[i].cartNum
          let fromcart = that.data.fromcart;
          let remarks = that.data.remarks;
          console.log("remarks=" + remarks)
          //写入租赁信息表
          wx.request({
            url: 'http://ZLofCampus.top/RentMall/addRentMsg.do',
            data: { "goodId": mylist[i].cartGoodId, "rentId": app.globalData.openid, "rentNum": mylist[i].cartNum, "unit": mylist[i].unit, "timeLen": mylist[i].timeLen, "releaseId": mylist[i].userId, "remarks": remarks },
            method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
            header: { "content-type": "application/json" }, // 设置请求的 header
            success: function (res) {
              if (res.data.msg == true) {
                if (fromcart == "true") {  //如果是来自购物车的
                  console.log("删除购物车")
                  console.log(cartId)
                  //删除购物车内容
                  wx.request({
                    url: 'http://ZLofCampus.top/RentMall/deleteCartById.do',
                    data: { "cartId": cartId },
                    method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                    header: { "content-type": "application/json" }, // 设置请求的 header
                    success: function (res) {
                      if (res.data.msg) {
                        //修改商品表
                        wx.request({
                          url: 'http://ZLofCampus.top/RentMall/modifyGoodMsg.do',
                          data: { "goodId": cartGoodId, "cartNum": cartNum },
                          method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                          header: { "content-type": "application/json" }, // 设置请求的 header
                          success: function (res) {
                            if (res.data.msg) {
                              wx.showToast({
                                title: '支付成功！',
                                icon: "success",
                                duration: 1500,
                                success: function () {
                                  setTimeout(function () {
                                    //要延时执行的代码
                                    wx.switchTab({
                                      url: '../../shopcar/shopcar',
                                    })
                                  }, 1000) //延迟时间
                                }
                              })
                            }
                          }
                        })
                      }
                    }
                  })
                } else {  //如果不是来自购物车的
                  wx.showToast({
                    title: '支付成功！',
                    icon: "success",
                    duration: 1500,
                    success: function () {
                      setTimeout(function () {
                        //要延时执行的代码
                        wx.switchTab({
                          url: '../../index/index',
                        })
                      }, 1000) //延迟时间
                    }
                  })
                }
              } else {
                wx.showToast({
                  title: '网络出错啦....',
                  icon: "none",
                  duration: 1000
                })
              }
            }
          })
        }

      } else {
        wx.showModal({
          title: '提示',
          content: '请先绑定手机!',
          success: function (res) {
            if (res.confirm) {  //点击了确定，跳转设置页面设置信息
              wx.redirectTo({  //这样跳转回导致当前页面销毁，会直接返回到购物车页面，需要重新选择商品结算，但是如果使用wx.navigateTo跳转时，有时候返回页面会调用onshow函数，有时候不会，不知道为什么，很不稳定，不能更新订单结算页面的相关信息，只能还是重新比较好
                url: '/pages/setting/setting',
              })
            }
          }
        })
      }
    }else{  //账号被封
      wx.showModal({
        title: '提示',
        content: '您的账号已被封锁，暂不可租赁商品！',
        success:function(res){
          wx.switchTab({
            url: '/pages/self/self',
          })
        }
      })
    }
  },

  //监听备注的输入
  remarksInput:function(e){
    console.log("备注")
    console.log(e);
    this.setData({
      remarks:e.detail.value
    })
  }



})