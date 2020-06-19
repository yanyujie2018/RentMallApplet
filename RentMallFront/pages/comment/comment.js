// pages/evaluation/evaluation.js
Page({
  data: {
    flag: 1,  //默认最低一颗星
    noteMaxLen: 300, // 最多放多少字
    info: "", //存放输入的评价内容
    noteNowLen: 0,//备注当前字数
    isAnonymous:true, //是否匿名，默认匿名
    goodId:null,  //记录要评论的商品id
    releaseId:null,  //商品发布者的id号
    id:null,  //该条评价的id
    rentState:null  //表明处在状态4还是5，若是5那么评论之后需要将状态由5改至8，表示评论完成押金扣留
  },

  onLoad:function(option){
    console.log("评论")
    console.log(option)
    this.setData({
      goodId:option.goodId,
      releaseId:option.releaseId,
      id:option.Id,
      rentState:option.rentState
    })
  },

  // 监听字数，看字数是否超过限制
  bindTextAreaChange: function (e) {
    var that = this
    var value = e.detail.value,
      len = parseInt(value.length);
    if (len > that.data.noteMaxLen)
      return;
    that.setData({ 
      info: value, 
      noteNowLen: len 
    })

  },

  // 提交当前所填写的评论，并填入数据库
  bindSubmit: function () {
    var that = this;
    var openid = getApp().globalData.openid;
    var anonymous=1;
    var flag=this.data.flag;
    var id= this.data.id;
    var info = this.data.info;
    var rentState = this.data.rentState;
    if(this.data.isAnonymous)
    {
      anonymous=1;
    }else{
      anonymous=0;
    }
    //1、先添加评论表
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/addComment.do',
      data: { "commentGoodId": that.data.goodId, "commentUserId": openid, "anonymous": anonymous,"content":info,"degree":flag},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        if(res.data.msg)
        {
          //2、对商品发布者的积分根据所得分数进行修改
          wx.request({
            url: 'http://ZLofCampus.top/RentMall/modifyReleaseScore.do',
            data: {"userId":that.data.releaseId,"degree":flag},
            method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
            header: { "content-type": "application/json" }, // 设置请求的 header
            success:function(res){
              if(res.data.msg)
              {
                if(rentState==4){
                  //3、修改租赁表的状态4到6（已评价且退换押金）
                  wx.request({
                    url: 'http://ZLofCampus.top/RentMall/modifyRentState4To6.do',
                    data: { "Id": id },
                    method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                    header: { "content-type": "application/json" }, // 设置请求的 header
                    success: function () {
                      if (res.data.msg) {
                        wx.showToast({
                          title: '评价成功!',
                          icon: 'success',
                          duration: 1500,
                          mask: false,
                          success: function () {
                            setTimeout(function () {
                              //要延时执行的代码
                              wx.navigateTo({
                                url: '../others/all_orders/all_orders?currentTab=0',
                              })
                            }, 1000)
                          }
                        })
                      }
                    }
                  })
                }else if(rentState==5){
                  //3、修改租赁表的状态5到8（已评价但押金扣留）
                  wx.request({
                    url: 'http://ZLofCampus.top/RentMall/modifyRentState5To8.do',
                    data: { "Id": id },
                    method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
                    header: { "content-type": "application/json" }, // 设置请求的 header
                    success: function () {
                      if (res.data.msg) {
                        wx.showToast({
                          title: '评价成功!',
                          icon: 'success',
                          duration: 1500,
                          mask: false,
                          success: function () {
                            setTimeout(function () {
                              //要延时执行的代码
                              wx.navigateTo({
                                url: '../others/all_orders/all_orders?currentTab=0',
                              })
                            }, 1000)
                          }
                        })
                      }
                    }
                  })
                }
                
              }
            }
          })
        }else{
          wx.showToast({
            title: '网络出错啦....',
            icon: 'none',
            duration: 1000,
          })
        }
      }
    })
  },

  //改变一颗星
  changeColor1: function () {
    var that = this;
    console.log("点击一颗星")
    that.setData({
      flag: 1
    });
  },

  changeColor2: function () {
    var that = this;
    that.setData({
      flag: 2
    });
  },

  changeColor3: function () {
    var that = this;
    that.setData({
      flag: 3
    });
  },

  changeColor4: function () {
    var that = this;
    that.setData({
      flag: 4
    });
  },

  changeColor5: function () {
    var that = this;
    that.setData({
      flag: 5
    });
  },

  //是否匿名
  bindNeedCourse: function (e) {
    console.log("开关")
    console.log(e.detail.value)
    this.setData({
      isTextbook: e.detail.value
    })
  }

})

