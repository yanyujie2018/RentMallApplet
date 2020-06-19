var app = getApp()
Page({
  /**
   * 页面的初始数据
   */
  data: {
    //导航栏的数据
    postBook: true,
    postThing: false,
    postJob: false,

    banner: [],
    chooseViewShowBanner: true,

    //发布书本的data值
    introduce: '', //商品简介         原来是：用户电话号码  bookPhoneNumber
    bookFound: false, //
    bookId: '', //书本id 
    goodImg: '', //图片链接
    goodName: '', //商品名字 1 
    bookAuthor: '', //书本作者2
    deposit: '', //商品押金 3
    currentPrice: '', //售价7 物品价格
    num: '',
    goodId:null,
    settlementUnit: ["天", "月"], //结算单位
    goodType: ["服装饰品", "书籍", "出行工具", "电子产品", "日用品", "其他"], //商品类别
    returnPeriod: ["3天", "5天", "7天"],  //归还期限

    conditions: ["全新", "几乎全新", "五成新", "不影响使用"], //5

    typeIndex: 0, //类别选择框初始显示0号位的选项
    unitIndex: 0,    //结算单位选择框的初始值
    returnIndex: 0,  //归还期限选择框的index

    postRemark: '', // 备注8
    buttonLoading: false,

    //textarea能填写的最大字数
    max: 500,
    state:0

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    let openid = app.globalData.openid;
    var idState = app.globalData.idState;
    var state = options.state
    this.setData({
      state:state
    })
    if(state==0){   //表示从我的页面点击发布进入该商品发布页面
      //先根据用户的openId查询该用户是否完善个人信息（至少是电话等），若未完善则提示完善后才可以发布信息
      wx.request({
        url: 'http://www.ZLofCampus.top/RentMall/checkPhone.do',
        data: { "userId": openid },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("电话")
          console.log(res)
          if (res.data.check == false) {  //提示先完善信息
            wx.showModal({
              title: '提示',
              content: '请先完善个人信息',
              success: function (res) {
                if (res.confirm) {  //点击了确定，跳转设置页面设置信息
                  wx.navigateTo({
                    url: '../setting/setting',
                  })
                } else {  //点击取消，跳转我的页面
                  wx.switchTab({
                    url: '../self/self',
                  })
                }
              }
            })
          }
        }
      })
      //判断账号是否被封
      if(idState==1){
        wx.showToast({
          title: '您的账号已被封锁，暂不能发布信息!',
          icon:'none',
          duration:1500,
          success:function(res){
            setTimeout(function(){
              wx.switchTab({
                url: '/pages/self/self',
              })
            },2000)
          }
        })
      }
    }else{  //否则是从我的发布中点击商品进入
      var goodId = options.goodId
      //根据goodId查找到该商品全部信息
      wx.request({
        url: 'http://www.ZLofCampus.top/RentMall/queryGood.do',
        data: { "goodId": goodId },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success:function(res){
          console.log("根据商品id获取商品信息")
          console.log(res)
          var image=[]
          if(res.data.goodmsg.image1!=null)
          {
            image.push(res.data.goodmsg.image1)
          }
          if (res.data.goodmsg.image2 != null) {
            image.push(res.data.goodmsg.image2)
          }
          if (res.data.goodmsg.image3 != null) {
            image.push(res.data.goodmsg.image3)
          }
          that.setData({
            goodName:res.data.goodmsg.goodName,
            currentPrice:res.data.goodmsg.price,
            unitIndex:res.data.goodmsg.unit,
            deposit: res.data.goodmsg.deposit,
            introduce: res.data.goodmsg.introduction,
            typeIndex:res.data.goodmsg.type,
            num:res.data.goodmsg.num,
            returnIndex:res.data.goodmsg.deadline,
            thingDescribe: res.data.goodmsg.goodDetail,
            banner:image,
            goodId:res.data.goodmsg.goodId
          })
        }
      })
    }

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var that = this;
    var nickName = that.data.nickName;
    wx.getStorage({
      key: 'nickName',
      success: function (res) {
        that.setData({
          nickName: res.data
        })
      },
    })

  },


  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    wx.setNavigationBarTitle({
      title: '校园二手交易'
    });
    wx.showNavigationBarLoading(); //在标题栏中显示加载图标
    setTimeout(function () {
      wx.stopPullDownRefresh(); //停止加载
      wx.hideNavigationBarLoading(); //隐藏加载icon
    }, 2000)
  },




  //响应事件

  //填写商品详细参数的监听函数
  bindThingDescribeInput: function (e) { //商品描述
    let value = e.detail.value;  //获取
    let len = parseInt(value.length);  //求当前输入文本的长度
    if (len > this.data.max)
      return;  //如果当前字数大于最大字数，那么就直接返回，不再进行setData 
    this.setData({
      thingDescribe: e.detail.value,
      currentWordNumber: len
    })
  },

  //介绍输入框的监听函数
  bindIntroduceInput: function (e) {
    this.setData({
      introduce: e.detail.value
    })
  },

  //数量输入框的监听函数
  bindNumInput: function (e) {
    this.setData({
      num: e.detail.value
    })
  },

  //商品名称输入的监听事件
  bindNameInput: function (e) {
    this.setData({
      goodName: e.detail.value
    })
  },

  //押金输入事件监听
  bindDepositInput: function (e) {
    this.setData({
      deposit: e.detail.value
    })
  },


  //结算单位选择框改变时调用的函数
  bindSettlementUnitChange: function (e) {
    this.setData({
      unitIndex: e.detail.value  //value中存储了当前所选择项的下标
    })
  },

  //类别选择框改变时调用的函数
  bindTypeChange: function (e) {
    this.setData({
      typeIndex: e.detail.value  //value中存储了当前所选择项的下标
    })
  },

  //归还期限的选择框监听事件
  bindReturnChange: function (e) {
    this.setData({
      returnIndex: e.detail.value
    })
  },

  //价格输入的监听函数
  bindCurrentPriceInput: function (e) {
    this.setData({
      currentPrice: e.detail.value
    })
  },


  //添加图片
  bindImageInput: function () {
    var that = this;
    var banner = this.data.banner;
    //wx.chooseImage 可以用来 从本地相册选择图片或使用相机拍照。
    //这里使用 wx.chooseImage 获取到的其实是本地资源的一个临时文件路径，在本次访问小程序的过程中不会有问题，但是下次访问时可能就会有问题了，如果需要持久保存，需在主动调用 wx.saveFile，这样在小程序下次启动时才可以访问的到
    wx.chooseImage({
      count: 3,  //最多可以选择的图片张数
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'],  //选择图片的来源(从图库或相机选择)
      success: function (res) {
        // console.log()
        var goodImg1 = res.tempFilePaths;  //tempFilePaths是图片的本地临时文件路径列表 (本地路径),tempFilePath可以作为img标签的src属性显示图片
        console.log(goodImg1);
        banner.push(res.tempFilePaths)
        console.log(banner);
        that.setData({
          //goodImg: goodImg1,
          banner: banner
        })
      },

    })
  },

  //删除选中的图片
  /** 删除图片Banner */
  deleteImvBanner: function (e) {
    var that = this
    var banner = that.data.banner;
    var itemIndex = e.currentTarget.dataset.id;
    banner.splice(itemIndex, 1);
    that.setData({
      banner: banner,
      checkUp: false
    })
  },

  //发布物品的响应事件
  bindSubmitThing: function () {
    var that = this;
    var goodImage = that.data.goodImage; //图片
    var goodName = that.data.goodName; //名字
    var unitIndex = that.data.unitIndex; //结算单位索引值
    var unit = that.data.settlementUnit[unitIndex]; //结算单位
    var typeIndex = that.data.typeIndex; //类别索引值
    var type = that.data.goodType[typeIndex]; //类别
    var thingDescribe = that.data.thingDescribe || '无详细参数'; //商品详细参数
    var returnIndex = that.data.returnIndex; //归还期限的索引值
    var returnPeriod = that.data.returnPeriod[returnIndex]; //归还期限
    var currentPrice = that.data.currentPrice; //商品价格
    var deposit = that.data.deposit; //押金
    var introduce = that.data.introduce; //介绍
    var number = that.data.num;

    var openid = app.globalData.openid;
    var banner = that.data.banner;


    //先提交了除图片意外的商品信息
    wx.request({
      url: "http://ZLofCampus.top/RentMall/addGoodMsg.do",
      data: {
        "goodName": goodName,
        "unit": unitIndex,
        "type": typeIndex,
        "goodDetail": thingDescribe,
        "deadline": returnIndex,
        "price": currentPrice,
        "deposit": deposit,
        "userId": openid,
        "introduction": introduce,
        "num": number
      },
      method: "POST",
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      success: function (res) {   //添加基本商品信息成功后，把商品图片也上传至服务器，全部完成之后才算是真正的发布成功
        console.log(res);
        var goodId = res.data.goodId;
        console.log(goodId);

        if (res.data.msg == true) {
          //除图片外的信息添加成功，并获取了最新的goodId
          //这里将图片也存储到服务器上，并将服务器上的地址存储到数据库中(通过循环将多张图片加入数据库)
          console.log("添加图片")
          console.log(banner.length);
          for(var i=0;i<banner.length;i++){
            var img = banner[i];
            //var index= i;
            wx.uploadFile({
              url: 'http://ZLofCampus.top/RentMall/addGoodImage.do?',
              filePath: img[0],
              name: 'file',
              formData: {
                "goodId": goodId,
                "index": i
              },
              success: function (res) {
                console.log(res);
                // 与wx.request不同，wx.uploadFile返回的是[字符串]，需要自己转为JSON格式，否则里面的数据都获取不到
                var data = JSON.parse(res.data)
                if (data.upload == "文件成功上传到指定目录下") {
                  console.log("sucess")
                  if (data.msg == true) {
                    console.log("sucess")
                    wx.showToast({
                      title: '发布成功',
                      icon: 'succes',
                      duration: 1500,
                      mask: true,  //表示显示透明蒙层，防止触摸穿透
                      success: function () {
                        //这里必须使用setTimeout将回调要进行的操作延时，否则，该弹框只会显示一下，就会立马跳转，duration是无效的
                        setTimeout(function () {
                          //要延时执行的代码
                          wx.switchTab({  //由于是要跳转到tabbar页面，所以不能用navigateTo
                            url: '../self/self'
                          });
                        }, 1000) //延迟时间
                      }
                    })
                  }
                } else if (data.upload == "文件类型不符合,请按要求重新上传") {
                  wx.showToast({
                    title: '图片文件类型不符合,请按要求重新上传',
                    icon: 'none',
                    duration: 2000
                  })
                } else if (data.upload == "文件类型为空") {
                  wx.showToast({
                    title: '图片文件类型为空,请按要求重新上传',
                    icon: 'none',
                    duration: 2000
                  })
                } else if (data.upload == "没有找到相对应的文件") {
                  wx.showToast({
                    title: '没有找到相对应的图片文件',
                    icon: 'none',
                    duration: 2000
                  })
                }
              }
            })
          }
        } else {
          wx.showToast({
            title: '网络出错了...',
            icon: 'none',
            duration: 2000,
          })
        }
     
                  
      },
      fail: function (res) {
        console.log(JSON.stringify(res));
        wx.showToast({
          title: '发布失败',
          icon: 'loading',
          duration: 2000
        })
        that.setData({
          buttonLoadingThing: false
        })
      },
    })

  },

  //修改商品
  modifyGood(e){
    console.log("修改商品")
    console.log(e)
    var goodId = e.currentTarget.dataset.goodid
    var that = this;
    var goodName = that.data.goodName; //名字
    var unitIndex = that.data.unitIndex; //结算单位索引值
    var unit = that.data.settlementUnit[unitIndex]; //结算单位
    var typeIndex = that.data.typeIndex; //类别索引值
    var type = that.data.goodType[typeIndex]; //类别
    var thingDescribe = that.data.thingDescribe || '无详细参数'; //商品详细参数
    var returnIndex = that.data.returnIndex; //归还期限的索引值
    var returnPeriod = that.data.returnPeriod[returnIndex]; //归还期限
    var currentPrice = that.data.currentPrice; //商品价格
    var deposit = that.data.deposit; //押金
    var introduce = that.data.introduce; //介绍
    var number = that.data.num;

    //先对基本信息进行修改
    //可以设置一个banner_tmp 用来存放从数据库中读到的商品图片
    //如果banner_tmp 和 banner 有不同的那么，一定是修改过或者添加，删除过的
    //可以分为 banner_tmp >banner 删除了
    wx.request({
      url: 'http://www.ZLofCampus.top/RentMall/modifyGood.do',
      data: { "goodId": goodId, "goodName": goodName, "unit": unitIndex, "type": typeIndex, "goodDetail": thingDescribe, "deadline": returnIndex, "price": currentPrice, "deposit": deposit, "introduction": introduce, "num": number},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        if(res.data.msg){
          wx.showToast({
            title: '修改成功!',
            icon: 'success',
            duration: 1000,
          })
          wx.navigateTo({
            url: '../others/selfsell/selfsell',
          })
        }else{
          wx.showToast({
            title: '网络出错了...',
            icon: 'none',
            duration: 1500,
          })
        }
      }
    })
  }

})