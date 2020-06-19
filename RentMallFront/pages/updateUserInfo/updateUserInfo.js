const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {

  },
  getContent: function (e) {
    this.setData({
      content: e.detail.value  //这里可以根据输入实时的更新变量的值
    })
  },

  //点击提交时触发该函数
  updateUserInfo: function () {
    var dType = this.data.type   //获取传递参数中的type，以判断当前修改的是哪一个，是身份证还是其他
    var content = this.data.content  //获取用户在文本框中输入的内容
    var placeholder = this.data.placeholder
    console.log("updateUserInfo")

    var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/  // 判断身份证是否符合要求的正则式
    if(content!=""){
      if (dType == 4) {  //如果是输入身份证，就判断输入的数据是否符合格式，如果不符合则弹框提醒
        if (!reg.test(content)) {
          wx.showToast({
            title: '身份证输入有误',
            icon: 'none'
          })
          return
        }
      }
      if (!content) {  //若检测到输入框中信息没有输入，那么就提示输入信息
        wx.showToast({
          title: placeholder,
          icon: 'none'
        })
        return
      }
      let openid = app.globalData.openid;
      if (dType == 6) //修改学校信息
      {
        let school = content;
        wx.request({
          url: 'http://ZLofCampus.top/RentMall/modifySchoolById.do',
          data: { "userId": openid, "school": school },
          method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
          header: { "content-type": "application/json" }, // 设置请求的 header
          success: function (res) {
            if (res.data.success) {
              wx.showToast({
                title: '修改成功',
                icon: 'success',
                duration: 2000,
                success: function () {
                  wx.navigateBack({  //返回上一级页面或多级页面
                    delta: 1
                  })
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
      } else if(dType == 5) //修改微信号
      {
        let wxNum = content;
        wx.request({
          url: 'http://ZLofCampus.top/RentMall/modifyWxNumById.do',
          data: { "userId": openid, "wxNum": wxNum },
          method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
          header: { "content-type": "application/json" }, // 设置请求的 header
          success: function (res) {
            if (res.data.success) {
              wx.showToast({
                title: '修改成功',
                icon: 'success',
                duration: 5000,  //感觉这好像没有用
                success: function () {
                  wx.navigateBack({  //返回上一级页面或多级页面
                    delta: 1
                  })
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
      }else if(dType == 4) //修改身份证
      {
        let idCard = content;
        wx.request({
          url: 'http://ZLofCampus.top/RentMall/modifyIdCardById.do',
          data: { "userId": openid, "idCard": idCard },
          method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
          header: { "content-type": "application/json" }, // 设置请求的 header
          success: function (res) {
            if (res.data.success) {
              wx.showToast({
                title: '修改成功',
                icon: 'success',
                duration: 2000,
                success: function () {
                  wx.navigateBack({  //返回上一级页面或多级页面
                    delta: 1
                  })
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
      }else if(dType == 1) //修改姓名
      {
        let userName = content;
        wx.request({
          url: 'http://ZLofCampus.top/RentMall/modifyNameById.do',
          data: { "userId": openid, "userName": userName},
          method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
          header: { "content-type": "application/json" }, // 设置请求的 header
          success: function (res) {
            if(res.data.success)
            {
              wx.showToast({
                title: '修改成功',
                icon: 'success',
                duration: 2000,
                success: function () {
                  wx.navigateBack({  //返回上一级页面或多级页面
                    delta: 1
                  })
                }
              })
            }else
            {
              wx.showToast({
                title: '网络出错了...',
                icon: 'none',
                duration: 2000
              })
            }
          }
        })
      }
    }else{
      wx.showToast({
        title: '内容不可为空！',
        icon: 'none',
        duration: 1000
      })
    }
  },

  /**
   * 生命周期函数--监听页面加载，option是上一个页面传过来的参数
   */
  onLoad: function (options) {
    if (options.content == '去完善') {  //如果传入的content显示“去完善”，说明用户当前并没有填写相关信息，那么就显示默认的提示信息“请输入姓名”
      this.setData({
        type: options.type,
        content: '',
        placeholder: '请输入姓名'
      })
    } else if (options.content == '请完善身份证信息') {
      this.setData({
        type: options.type,
        content: '',
        placeholder: '请完善身份证信息'
      })
    } else if (options.content == '绑定微信号') {
      this.setData({
        type: options.type,
        content: '',
        placeholder: '绑定微信号'
      })
    } else if (options.content == '请填写学校') {
      this.setData({
        type: options.type,
        content: '',
        placeholder: '请填写学校'
      })
    }else {  //否则说明用户已经填写过信息，那么将获取到的content显示出来即可
      this.setData({
        type: options.type,
        content: options.content
      })
    }
  },
})