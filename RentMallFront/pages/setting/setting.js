const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    RealName: "去完善",
    UserGender: ['男', '女'],
    UserBirthday: '',
    UserCardNo: '',
    UserMobile: '',
    UserWxNo: '',
    school: "",
    checked: '',
    showModal: false
  },
  // 获取姓名那一栏
  _prefect: function (e) {
    wx.navigateTo({
      url: '../updateUserInfo/updateUserInfo?type=1&content=' + e.currentTarget.dataset.content,
    })
  },
  // 获取身份证号那一栏
  _idCard: function (e) {
    // 通过e.currentTarget.dataset.content获取触发事件时传递的参数
    wx.navigateTo({
      url: '../updateUserInfo/updateUserInfo?type=4&content=' + e.currentTarget.dataset.content,
    })
  },
  //获取微信号那一栏
  _wxNumber: function (e) {
    console.log("@@@@@");
    console.log(e.currentTarget.dataset.content);
    wx.navigateTo({
      url: '../updateUserInfo/updateUserInfo?type=5&content=' + e.currentTarget.dataset.content,
    })
  },
  // 获取手机号那一栏
  _mobilePhone: function (e) {
    console.log("#####");
    console.log(e.target.dataset.phone);
    console.log(this.data.phone);
    wx.navigateTo({
      //mobile是加过密的手机号或者“未绑定”，phone是明码的手机号
      url: '../bindingPhone/bindingPhone?mobile=' + e.target.dataset.phone + '&phone=' + this.data.phone
    })
  },
  //获取学校那一栏
  _wxSchool: function (e) {
    wx.navigateTo({
      url: '../updateUserInfo/updateUserInfo?type=6&content=' + e.currentTarget.dataset.content,
    })
  },

  // 修改生日日期触发的事件
  bindDateChange: function (e) {
    let that = this;
    console.log(e);
    this.setData({
      UserBirthday: e.detail.value  //获取用户填写的生日
    })
    let openid = app.globalData.openid;
    let birthday = e.detail.value;
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/modiyfBirthById.do',
      data: { "userId": openid, "birthday": birthday },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        if(res.data.success)
        {
          wx.showToast({
            title: '修改成功',
            icon: 'success'
          })
        }else{
          wx.showToast({
            title: '网络出错了...',
            icon: 'none'
          })
        }
      }
    })

    
  },

  /**
   * 修改性别
   */
  bindPickerChange: function (e) {
    this.setData({
      index: e.detail.value
    })
    var gender = this.data.UserGender[e.detail.value]
    let openid = app.globalData.openid
    console.log(gender);
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/modiyfSexById.do',
      data: {"userId":openid,"gender": gender},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function(res) {
        if(res.data.success)
        {
          wx.showToast({
            title: '修改成功',
            icon: 'success'
          })
        }else
        {
          wx.showToast({
            title: '网络出错了...',
            icon: 'none'
          })
        }
      }
    })
  },

  /**
   * 添加/修改密码
   */
  _goPayPassword: function (e) {
    var self = this
    if (this.data.phone) {
      wx.navigateTo({
        url: '../payPassword/index?mobile=' + e.currentTarget.dataset.mobile + '&phone=' + this.data.phone,
      })
    } else {
      wx.showModal({
        content: '请先绑定手机号码',
        success: function (res) {
          if (res.confirm) {
            wx.navigateTo({
              url: '../bindingPhone/index?phone=' + self.data.phone
            })
          }
        }
      })
    }
  },
  //是否开启支付密码
  passwordStatus: function (e) {
    var self = this
    var status = e.currentTarget.dataset.passwordstatus

    if (status == 1) {
      user.updatePayPasswordStatus({
        status: 0
      }, function (res) {
        if (res.data.code == 200) {
          self.setData({
            PayPasswordStatus: 0
          })
          wx.showToast({
            title: '关闭成功',
            icon: 'success'
          })
        }
      })
    }
    if (status == 0) {
      user.updatePayPasswordStatus({
        status: 1
      }, function (res) {
        if (res.data.code == 200) {
          self.setData({
            PayPasswordStatus: 1
          })
          wx.showToast({
            title: '开启成功',
            icon: 'success'
          })
        }
      })
    }
  },

  //获取用户设置界面信息，页面显示出来的时候就会调用
  //如果用户已经设置过详细的用户信息那么直接显示用户的设置，如果用户没有设置，那么就显示"去完善"等的提示
  //因为这里没有user.js（自己设的）这个文件，所以不能使用user
  //这里可以使用wx.request的方式请求后台，看能否从后台获取信息，然后这里经过判断后显示
  _getSetting: function () {
    var self = this
    let openid = getApp().globalData.openid;
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/findUserMsgById.do',
      data: {"userId": openid},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function(res) {
        console.log("findmsg");
        console.log(res);
        //每次修改后都直接修改全局信息里面的用户信息
        getApp().globalData.usermes = res.data.usermsg;
        var tel = res.data.usermsg.phone
        var reg = /^(\d{3})\d{4}(\d{4})$/
        console.log(tel);
        if(tel!=null){
          //JavaScript中 replace() 方法用于在字符串中用一些字符替换另一些字符，或替换一个与正则表达式匹配的子串。
          tel = tel.replace(reg, "$1****$2")
          console.log(tel);
        }
        self.setData ({
          RealName: res.data.usermsg.userName || '去完善',
          UserBirthday: res.data.usermsg.birthday || '请选择生日',
          UserSex: res.data.sex || '未知',
          UserCardNo: res.data.usermsg.idCard || '请完善身份证信息',
          UserMobile: tel || '未绑定',
          UserWxNo: res.data.usermsg.wxNum || '请绑定微信号',
          school: res.data.usermsg.school || '请填写学校',
          //PayPassworded: res.settingItem.PayPassworded,
          //PayPasswordStatus: res.settingItem.PayPasswordStatus,
          phone: res.data.usermsg.phone
        })
      }
    })
  },
  
  //清楚缓存
  clearStorge: function (e) {
    wx.showToast({
      title: '清除成功',
      icon: 'success',
      success: function () {
        wx.clearStorage()  // 清除本地数据缓存
      }
    })
  },
  onLoad: function (options) { 

  },

  //页面一显示出来就调用函数_getSetting()，查看数据库中用户的信息是否已经完善，如果已经完善，那么就将信息显示出来
  onShow: function (e) {
    this._getSetting()
  },
})