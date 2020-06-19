var time = require('../../../utils/util.js');
Page({
  data: {
    iscollection:false,
    rentNumber:1,
    rentTime:1,
    isShowSelectInfo:true,
    hasComment:false,
    //商品信息如下：
    goodmsg:[],
    comments:[],
    //库存
    stock:null,
    // banner
    imgUrls: [],
    indicatorDots: true, //是否显示面板指示点
    autoplay: true, //是否自动切换
    interval: 3000, //自动切换时间间隔,3s 
    duration: 1000, //  滑动动画时长1s
    infoimage:null,
    userName:null,
    userId:null,
    school:null,
  },

  //一进来详情页面就需要在商品的views上加1
  onLoad:function(options){
    var that = this;
    var banner=[];
    this.setData({
      infoimage: getApp().globalData.userInfo.avatarUrl,  //获取用户的头像
      userName: getApp().globalData.userInfo.nickName,  //获取用户的昵称
      userId:getApp().globalData.openid
    })
    //查看商品是否收藏过
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/findCollect.do',
      data: { "collectUserId": getApp().globalData.openid,"collectGoodId": options.goodid },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        if(res.data.msg)
        {
          console.log("收藏过")
            that.setData({
              iscollection:true
            })
        }else
        {
          console.log("没收藏过")
          that.setData({
            iscollection: false
          })
        }
      }
    })
    //获取商品详情
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/getGoodDetail.do',
      data: { "goodId" : options.goodid},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log("detail")
        console.log(res);
        if (res.data.detailList.image1!=null) {
          banner.push(res.data.detailList.image1)
        }
        if (res.data.detailList.image2 != null) {
          banner.push(res.data.detailList.image2)
        }
        if (res.data.detailList.image3 != null) {
          banner.push(res.data.detailList.image3)
        }
        console.log(banner);
        that.setData({
          goodmsg: res.data.detailList,
          stock: res.data.detailList.num,
          imgUrls:banner,
        });
        console.log("获取数据成功")
      },
      fail: function (res) {
        console.log("获取数据失败！");
      },
    });

   // 获取评论（根据商品id获取）
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/findComment.do',
      data: { "commentGoodId": options.goodid },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log("评论")
        console.log(res)
        if (res.data.commentList.length==0){ //没有评论，在页面上显示，暂无评论
          that.setData({
            hasComment:false
          })
        }else{  //显示评论
          that.setData({
            comments: res.data.commentList,
            hasComment:true
          });
        }
      }
    });

    //根据商品id获取发布者所在学校
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/findSchool.do',
      data: { "goodId": options.goodid },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        console.log(res);
        console.log(res.data.school)
        that.setData({
          school:res.data.school
        })
      }
    })

    //将商品的浏览次数加1
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/addViews.do',
      data: { "goodId": options.goodid },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        if(res.data.msg){
          console.log("浏览次数增加")
        }
      }
    })
  },

  //点击联系卖家，就可以进入聊天框与商家聊天
  toContent(e){
    var goodId = e.currentTarget.dataset.goodid;
    wx.navigateTo({
      url: '../../chat/chat?goodId='+goodId+'&state=0',
    })

  },

  //点击收藏事件函数
  collection:function(e){
    var goodId=e.currentTarget.dataset.goodid
    var that = this;
    if(!this.data.iscollection)  //当前没有收藏过
    {
      //添加收藏
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/collectGood.do',
        data: {"collectUserId":getApp().globalData.openid,"collectGoodId":goodId},
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          if(res.data.msg){
            //将商品的收藏数加1
            wx.request({
              url: 'http://ZLofCampus.top/RentMall/addCollect.do',
              data: { "goodId": goodId},
              method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
              header: { "content-type": "application/json" }, // 设置请求的 header
              success:function(res){
                if(res.data.msg){
                  that.setData({
                    iscollection: true,
                  });
                  wx.showToast({
                    title: '收藏成功',
                    icon: 'success',
                    duration: 1500
                  });
                }else{
                  wx.showToast({
                    title: '网络出错啦....',
                    icon: 'none',
                    duration: 1000
                  });
                }
              }
            })
          }else{
            wx.showToast({
              title: '网络出错啦....',
              icon: 'none',
              duration: 1000
            });
          }
        }
      });
    }
    else{  //已经收藏过，点击则为取消收藏
      //取消收藏
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/cancelcollect.do',
        data: { "collectUserId": getApp().globalData.openid, "collectGoodId": goodId},
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          if (res.data.msg) {
            //将商品的收藏数减1
            wx.request({
              url: 'http://ZLofCampus.top/RentMall/subCollect.do',
              data: { "goodId": goodId },
              method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
              header: { "content-type": "application/json" }, // 设置请求的 header
              success: function (res) {
                if (res.data.msg) {
                  that.setData({
                    iscollection: false,
                  });
                  wx.showToast({
                    title: '移除收藏成功！',
                    icon: 'success',
                    duration: 1500
                  });
                } else {
                  wx.showToast({
                    title: '网络出错啦....',
                    icon: 'none',
                    duration: 1000
                  });
                }
              }
            })
          } else {
            wx.showToast({
              title: '网络出错啦....',
              icon: 'none',
              duration: 1000
            });
          }
        }
      });
    }
    
    console.log("触发了点击事件")
  },

  //点击转发分享
  onShareAppMessage:function(res){
    console.log(res.from);
    if (res.from === 'button') {
      console.log("来自页面内转发按钮");
      console.log(res.target);
    }
    else {
      console.log("来自右上角转发菜单")
    }
    return {
      title: '校园租赁小程序',  //自定义，一般写小程序的名字
      path: '/pages/others/details/details?goodid='+this.data.goodmsg.goodid, //写这个页面的路径
      imageUrl: '',  //这个是显示的图片，不写就默认当前页面的截图
      success: (res) => {
        console.log("转发成功", res);
      },
      fail: (res) => {
        console.log("转发失败", res);
      }
    }
  },

  show3d:function(){
    wx.navigateTo({
      url: '/pages/others/bookshow/bookshow',
      success: function(res) {},
      fail: function(res) {},
      complete: function(res) {},
    })
  },

  //预览图片(将图片放大)
  previewImage: function (e) {
    var current = e.target.dataset.src;
    console.log("放大图片")
    console.log(current);
    wx.previewImage({
      current: current, // 当前显示图片的http链接  
      urls: this.data.imgUrls // 需要预览的图片http链接列表  
    })
  },

  // 返回首页事件函数
  toHome:function(){
    wx.switchTab({
      url: '/pages/index/index'
    })
  },
  // 跳到购物车时间函数
  toCar() {
    wx.switchTab({
      url: '/pages/shopcar/shopcar'
    })
  },
  
  //点击详细参数的事件函数
  detailinfomation:function(){
    this.showSelectInfo();
  },

  //添加购物车
  addCar: function () {
    if (this.data.stock>0){
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/addCart.do',
        data: { "cartUserId": getApp().globalData.openid, "cartGoodId": this.data.goodmsg.goodId, "cartNum": this.data.rentNumber,"timeLen":this.data.rentTime},
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log(res);
          if(res.data.msg)
          {
            wx.showToast({
              title: '加入购物车成功',
              icon: 'success',
              duration: 1500
            });
          }else{
            wx.showToast({
              title: '网络出错啦.....',
              icon: 'none',
              duration: 1000
            });
          }
        }
      })
    }else{
      wx.showToast({
        title: '暂时无货',
        icon: 'none',
        duration: 1000
      });
    }
  },

  //显示弹窗
  showSelectInfo: function (e) {
    this.setData({
      isShowSelectInfo: false,
    });
  },

  //隐藏弹窗(点击弹窗的×)
  hiddenSelectInfo: function () {
    this.setData({
      isShowSelectInfo: true,
    });
  },

  //点击弹窗的商品属性
  clickAttr: function (e) {
    selectIndex = e.currentTarget.dataset.selectIndex;
    attrIndex = e.currentTarget.dataset.attrIndex;
    var count = shopDialogInfo.selectInfoAttributeList[selectIndex].AttributeNameList.length;
    for (var i = 0; i < count; i++) {
      shopDialogInfo.selectInfoAttributeList[selectIndex].AttributeNameList[i].IsSelect = false;
    }
    shopDialogInfo.selectInfoAttributeList[selectIndex].AttributeNameList[attrIndex].IsSelect = true;

    var name = shopDialogInfo.selectInfoAttributeList[selectIndex].AttributeNameList[attrIndex].Name;//点击属性的名称


    var shopSelectInfoHaveSelectName = "";
    //点击过，修改属性
    selectIndexArray[selectIndex].value = name;
    var selectIndexArraySize = selectIndexArray.length;
    //将数组的所有属性名拼接起来
    for (var i = 0; i < selectIndexArraySize; i++) {
      shopSelectInfoHaveSelectName += ' "' + selectIndexArray[i].value + '" ';
    }

    shopDialogInfo.shopSelectInfoHaveSelect = "已选:" + shopSelectInfoHaveSelectName;
    this.setData({
      shopSelectInfo: shopDialogInfo,
    });

  },

  //减少租赁数量
  minusNum:function(){
    var that = this;
    if (this.data.rentNumber>1)
      this.data.rentNumber--;
    else {
      wx.showToast({
        title: '租赁数量不可小于1！',
        icon: 'none',
      })
    }
    that.setData({
      rentNumber: this.data.rentNumber
    });
  },

  //添加租赁数量
  addNum: function () {
    var that = this;
    if (this.data.rentNumber < this.data.stock){
      this.data.rentNumber++;
      that.setData({
        rentNumber: this.data.rentNumber
      });
    }else{
      wx.showToast({
        title: '不能大于当前库存！',
        icon:'none',
      })
    }
    
  },

  //减少租赁时长
  minusTime: function () {
    var that = this;
    if (this.data.rentTime > 1)
      this.data.rentTime--;
    else{
      wx.showToast({
        title: '租赁时间不可小于1！',
        icon: 'none',
      })
    }
    that.setData({
      rentTime: this.data.rentTime
    });
  },

  //添加租赁时长
  addTime: function () {
    var that = this;
    this.data.rentTime++;
    that.setData({
      rentTime: this.data.rentTime
    });
  },

  // 立即租赁
  immeBuy() {
    if (this.data.stock>0){
      //跳转结算页面（）
      var booklist = [];
      var acountlist = [];
      //在goodmsg里面增加一个人rentLen属性，值为rentTime
      this.data.goodmsg.timeLen = this.data.rentTime;
      this.data.goodmsg.cartGoodId = this.data.goodmsg.goodId;
      this.data.goodmsg.cartNum = this.data.rentNumber;
      booklist.push(this.data.goodmsg);  //存放要结算的商品列表
      acountlist.push(this.data.rentNumber);  //存放结算数目
      var acount = JSON.stringify(acountlist);  //转换为json格式
      var model = JSON.stringify(booklist);
      console.log("单个结算")
      console.log("model="+model);
      console.log("acount"+acount);
      wx.navigateTo({
        url: "/pages/others/balance/balance?model=" + model + "&acount=" + acount+"&fromcart=false",
      })
    }else{
      wx.showToast({
        title: '暂时无货',
        icon: 'none',
        duration:500
      })
    }
  },

  //点击删除评论
  deleteComment(e){
    var that = this;
    var commentId = e.currentTarget.dataset.commentid;
    var index= e.currentTarget.dataset.index;
    var comments = this.data.comments;
    if(getApp().globalData.idState==0){  //账号正常才可以删除
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/deleteCommentById.do',
        data: { "commentId": commentId },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success:function(res){
          if(res.data.msg){
            //评论更新
            console.log("删除")
            console.log(comments)
            comments.splice(index,1);
            console.log(comments);
            wx.showToast({
              title: '评论删除成功！',
              icon:'success',
              duration:1000,
              success:function(res){
                setTimeout(function(){
                  that.setData({
                    comments: comments
                  })
                },1000)
              }
            })
          }
        }
      })
    }else{  //账号被封
      wx.showModal({
        title: '提示',
        content: '您的账号已被封锁，暂不能删除评论！',
      })
    }
    
  }

})