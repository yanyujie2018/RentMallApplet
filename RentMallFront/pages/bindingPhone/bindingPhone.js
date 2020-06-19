// pages/bindingPhone/bindingPhone.js
const app = getApp()

Page({
  data: {
    disabled: false,
    time: 0,
    timer: '',
    sendButtonText: '获取验证码',
    vCodeColor: '1',
    step: 1  //控制显示几步，如果是1就先显示之前已经注册过的号码，获取验证码之后才能绑定新手机号；如果是2表示原来就没有绑定手机号，那么直接绑定新手机号即可
  },

  onReady: function () {
    // 获取服务器端的sessionId，并且存储到本地
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getSessionId.do',
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      method: 'POST',
      success: function (res) {
        console.log("onReady");
        console.log(res);
        wx.setStorageSync("sessionId", "JSESSIONID=" + res.data);
      }
    })
  },

  //发送验证码
  sendMsg: function (e) {
    console.log("调用sendMsg")
    var sessionId = wx.getStorageSync('sessionId');
    var self = this
    var mobile = e.currentTarget.dataset.phone  //获取用户手机号
    if (!mobile) { //没有输入手机号
      wx.showToast({
        title: '请输入手机号',
        icon: 'none'
      })
      return
    }

    // 输入的手机号格式错误
    if (!/^1([34578])\d{9}$/.test(mobile)) {
      wx.showToast({
        title: '手机号码错误',
        icon: 'none'
      })
      return
    }

    if (!this.data.disabled) {
      this.data.disabled = true
      //clearTimeout()方法 可取消由 setTimeout() 方法设置的 timeout
      clearTimeout(this.data.timer)
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/sendSms.do',
        data: {
          "phoneNum": mobile  
        },
        header: {
          "Content-Type": "application/x-www-form-urlencoded",
          "Cookie": sessionId
        },
        method: 'POST',
        success: function (res) {
          console.log(res)
          if (res.statusCode==200){
            wx.showToast({
              title: '验证码发送成功',
              icon: 'success',
              success: function () {
                if (res.statusCode == 200) {
                  self.setData({
                    time: 60   //成功发射验证码之后，显示60s内不能够再次点击“获取验证码”按钮
                  })
                  self._getVerificationCode()  //调用函数_getVerificationCode实现按钮上的动态显示（还有多少多少秒可以重新发送）
                } else {
                  wx.showToast({  //显示错误信息
                    title: res.errMsg
                  })
                }
              }
            })
          }
        }
      })
    }
  },

  //获取用户输入的手机号码（新）
  _getMobileInput: function (e) {
    this.setData({
      mobile: e.detail.value
    })
  },

  //获取用户输入的验证码
  _getVcodeInput: function (e) {
    this.setData({
      vcode: e.detail.value
    })
  },

  //获取验证码
  sendCode: function (e) {
    console.log("发送验证码函数调用")
    var self = this
    var mobile = e.currentTarget.dataset.phone  //获取用户手机号
    if (!mobile) { //没有输入手机号
      wx.showToast({
        title: '请输入手机号',
        icon: 'none'
      })
      return
    }
    // 输入的手机号格式错误
    if (!/^1([34578])\d{9}$/.test(mobile)) { 
      wx.showToast({
        title: '手机号码错误',
        icon: 'none'
      })
      return
    }

    if (!this.data.disabled) {
      this.data.disabled = true
      //clearTimeout()方法 可取消由 setTimeout() 方法设置的 timeout
      clearTimeout(this.data.timer)
      user.sendCode({
        mobile: mobile
      },
        function (res) {
          wx.showToast({
            title: '验证码发送成功',
            icon: 'success',
            success: function () {
              if (res.statusCode== 200) {
                self.setData({
                  time: 60   //成功发射验证码之后，显示60s内不能够再次点击“获取验证码”按钮
                })
                self._getVerificationCode()  //调用函数_getVerificationCode实现按钮上的动态显示（还有多少多少秒可以重新发送）
              } else {
                wx.showToast({  //显示错误信息
                  title: res.errMsg
                })
              }
            }
          })
        }
      )
    }
  },

  _getVerificationCode: function () {
    if (this.data.time > 0) { //如果等待的时间还没有到时
      this.data.time--
      this.setData({
        sendButtonText: this.data.time+'秒后发送',
        timer: setTimeout(this._getVerificationCode, 1000),  //等候1秒之后再次执行这个函数显示秒数
        vCodeColor: '2'    //根据这个变量值，在页面显示时，会控制其所属class，进而控制其显示样式
      })
    } else {
      this.setData({
        disabled: false,
        sendButtonText: '获取验证码',  //时间到了，可以重新获取验证码
        vCodeColor: '1'
      })
      //clearTimeout()方法 可取消由 setTimeout() 方法设置的 timeout
      clearTimeout(this.data.timer)
    }
  },

  //点击下一步触发的函数
  _goNextStep: function (e) {
    var phone = e.currentTarget.dataset.phone;
    var sessionId = wx.getStorageSync('sessionId');
    var code = this.data.vcode;
    var that = this;
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/Verification.do',
      data: {
        phone: phone,
        code: code
      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded",
        "Cookie": sessionId
      },
      method: 'POST',
      success: function (res) {
        console.log(res)

        if (res.data=="success") {
          wx.showToast({
            title: '验证通过',
            icon: 'success'
          })
          that.setData({
            step: 2,
            disabled: false,
            sendButtonText: '获取验证码',
            vCodeColor: '1',
            time: 0,
            timer: '',
          })
        } else {
          wx.showToast({
            title: res.data,
            icon: 'none'
          })
        }
      },
      fail: function (res) {
        console.log(res)
      }
    })
  },

  
  _updateMobile: function (e) {
    var that = this;
    var phone = this.data.mobile;
    var sessionId = wx.getStorageSync('sessionId');
    var code = this.data.vcode;
    var openid = app.globalData.openid;
    //使用validform判断手机号是否格式错误或者为空
    //手机号码正常
    if (this.validForm()) {
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/modifyPhoneById.do',
        data: {
          phone: phone,
          code: code,
          userId: openid
        },
        header: {
          "Content-Type": "application/x-www-form-urlencoded",
          "Cookie": sessionId
        },
        method: 'POST',
        success: function (res) {
          console.log(res)

          if (res.data == "success") {
            wx.showToast({
              title: '修改成功',
              success: function () {
                wx.setStorage({
                  key: 'userTelInfo',
                  data: phone,
                })
                wx.navigateBack({
                  delta: 1
                })
              }
            })
          } else {
            wx.showToast({
              title: res.data,
              icon: 'none'
            })
          }
        },
        fail: function (res) {
          console.log(res)
        }
      })
      /*
      user.updateMobile({
        mobile: self.data.mobile,
        code: self.data.vcode
      },
        function (res) {
          if (res.data.code == 200) {
            wx.showToast({
              title: '绑定手机成功',
              success: function () {
                wx.setStorage({
                  key: 'userTelInfo',
                  data: self.data.mobile,
                })
                wx.navigateBack({
                  delta: 1
                })
              }
            })
          } else {  //手机号格式不对或者为空，显示错误信息
            wx.showToast({
              title: res.data.msg,
              icon: 'none'
            })
          }

        }
        
      )*/
    }else  //手机号码为空或格式不对
    {
      console.log(res)
      wx.showToast({
        title: res.data.msg,
        icon: 'none'
      })
    }
  },

  //手机号码、验证码的非空检测
  validForm: function () {
    if (!/^1([34578])\d{9}$/.test(this.data.mobile)) {
      wx.showToast({
        title: '手机号码错误',
        icon: 'none'
      })
      return false
    }

    //判断验证码是否为空
    if (!this.data.vcode) {
      wx.showToast({
        title: '验证码错误',
        icon: 'none'
      })
      return false
    }
    return true
  },

  //监听页面加载
  onLoad: function (options) {
    if (options.mobile!="未绑定") {
      //这两个是上一个页面传来的参数
      this.setData({
        mobile: options.mobile,
        phone: options.phone
      })
    } else {  //若上一个页面传回的phone为空，那么就说明还没有绑定手机，那么直接跳第二步即可
      this.setData({
        step: 2
      })
    }

  },

  onShow: function(){
    
  }
})