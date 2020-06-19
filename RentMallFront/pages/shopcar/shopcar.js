 var Temp = require('../template/contract.js');
Page(Object.assign({}, Temp.Quantity, {
  data: {
    flagtimer:false,
    showModal:false,
    index:0,
    isAllSelect: false,  //判断是否全选
    totalMoney: 0,
    cartId:0, //删除时用于记录删除的ID号
    // 商品详情介绍
    carts: [],
    isSelect:[],
    istouchmove:[],
    booklist:null,
    startX: 0, //开始坐标
    startY: 0
  },
  onLoad(){
    var that = this;
    that.setData({
      flagtimer: false,
      showModal: false,
      index: 0,
      isAllSelect: false,
      totalMoney: 0, //购物车总金额
      // 商品详情介绍
      carts: [],
      isSelect: [],
      booklist: null,
    });

    //获取用户的购物车
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/fingCartByUser.do',
      data: { "cartUserId": getApp().globalData.openid },
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success:function(res){
        console.log("getCart")
        console.log(res)
        for (let i = 0; i < res.data.cartList.length; i++) {
          that.data.isSelect.push({ ischecked: false });
          that.data.istouchmove.push({ isTouchMove: false });
        }
        that.setData({
          carts: res.data.cartList,
          isSlect: that.data.isSelect
        });
      }
    })
    
  },

  //生命周期函数，监听页面显示
  onShow(){
    var that = this;  
    console.log("触发onshow函数")
    if(this.data.flagtimer==false)  //当这是第一次点击购物车页面时，那么一定会调用onload函数
    {
      console.log("第一次触发");
      this.data.flagtimer=!this.data.flagtimer
    }
    else //并不是第一次触发，这样就可能是有后台等载入的，那么要在这里手动执行onload函数，达到查出数据的目的。
    {
      console.log("不是第一次触发")
      that.onLoad();
    }
  },

  //勾选事件处理函数  
  switchSelect: function (e) {
    // 获取item项的id，和数组的下标值  
    var Allprice = 0, i = 0;
    var index = e.target.dataset.index;
    //若原来选中，则再次点击表示不选中；若原来没选中，则点击表示选中
    this.data.isSelect[index].ischecked = !this.data.isSelect[index].ischecked; 

    //价钱统计
    if (this.data.isSelect[index].ischecked) {  //如果选中则增加总金额
      this.data.totalMoney = this.data.totalMoney + this.data.carts[index].price * this.data.carts[index].cartNum*this.data.carts[index].timeLen;
    }
    else {  //如果取消选中则减少总金额
      this.data.totalMoney = this.data.totalMoney - this.data.carts[index].price * this.data.carts[index].cartNum * this.data.carts[index].timeLen;
    }

    //是否全选判断
    for (i = 0; i < this.data.carts.length; i++) {
      Allprice = Allprice + this.data.carts[i].price * this.data.carts[i].cartNum * this.data.carts[i].timeLen;
    }
    //如果所有的商品总价钱和现在选中的总价钱一致，那么就说明已经全部被选中，则将全选按钮变亮
    if (Allprice == this.data.totalMoney) {
      this.data.isAllSelect = true;
    }
    else {  //如果所有的商品总价钱和现在选中的总价钱不一致，那么就说明没有全部被选中，则将全选按钮不变
      this.data.isAllSelect = false;
    }
    //设置数据使得数据能动态的显示在页面上
    //这里需要实时改变页面数据必须使用setData设置，否则无效（但如果使用mpvue因为有vue的支持就可以直接实时改变）
    this.setData({
      carts: this.data.carts,
      //parseFloat() 函数可解析一个字符串，并返回一个浮点数。
      //该函数指定字符串中的首个字符是否是数字。如果是，则对字符串进行解析，直到到达数字的末端为止，然后以数字返回该数字，而不是作为字符串。
      //toFixed() 方法可把 Number 四舍五入为指定小数位数的数字。
      totalMoney: parseFloat(parseFloat(this.data.totalMoney).toFixed(2)),
      isAllSelect: this.data.isAllSelect,
      isSelect:this.data.isSelect,
    })
  },

  //全选(直接点击全选按钮)
  allSelect: function () {
    //处理全选逻辑
    let i = 0;
    if (!this.data.isAllSelect) {  //当前没有选中全选按钮
      this.data.totalMoney=0;
      for (i = 0; i < this.data.carts.length; i++) {
        this.data.isSelect[i].ischecked = true; //把每一个独立的按钮都选中
        //计算总金额
        this.data.totalMoney = this.data.totalMoney + this.data.carts[i].price * this.data.carts[i].cartNum*this.data.carts[i].timeLen;
      }   
    }
    else {  //取消全选按钮
      for (i = 0; i < this.data.carts.length; i++) {
        //错误修正
        this.data.isSelect[i].ischecked = false;  //将每一个独立的按钮都取消
      }
      this.data.totalMoney = 0;
    }
    //更新数据
    this.setData({
      carts: this.data.carts,
      isSelect: this.data.isSelect,
      booklist:this.data.booklist,
      isAllSelect: !this.data.isAllSelect,
      totalMoney: parseFloat(parseFloat(this.data.totalMoney).toFixed(2)),
    })
  },

  // 去结算
  toBuy() {
    //判断结算列表
    var booklist = [];//存放要结算的商品列表
    var acountlist = [];  //存放结算的商品数量。
    for (var i = 0; i < this.data.isSelect.length;i++)
    {
      if(this.data.isSelect[i].ischecked==true) //如果该商品选中，则说明该商品要结算
      {
        acountlist.push(this.data.carts[i].cartNum);
        booklist.push(this.data.carts[i]);  //用于存下来你选中的商品
      }
    }
    if (acountlist.length==0) //如果没有选中的商品
    {
      wx.showToast({
        title: '请选择结算商品',
        icon: 'none',
        duration: 2000
      });
      return 0;
    }
    //有选中的商品，则跳转页面
    //JSON.stringify将字符串转换为一个json数据
    var acount = JSON.stringify(acountlist);
    var model = JSON.stringify(booklist);
    console.log("去结算");
    console.log(acount);
    console.log(model); 
    //跳转订单结算页面
   wx.navigateTo({
     url: "/pages/others/balance/balance?model=" + model + "&acount=" + acount + "&fromcart=true",
   })
  },


  //减少租用数量
  decreaseCount:function(e){
    var that=this;
    var componentId = e.target.dataset.index;
    var cartId = e.target.dataset.cartid;
    var goodnum = e.target.dataset.goodnum;
    var cartnum = e.target.dataset.cartnum;
    if(cartnum>1)  //当前数量小于库存，否则不改变数值
    {
      //可以减1，同时修改后台数据库数量，购物车中的数量加1
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/modifyCartNumSub.do',
        data: { "cartId": cartId },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success:function(res)
        {
          console.log("subCartNum")
          console.log(res)
          that.data.carts[componentId].cartNum = that.data.carts[componentId].cartNum-1;
          if (that.data.isSelect[componentId].ischecked) {
            that.data.totalMoney = that.data.totalMoney - parseFloat(that.data.carts[componentId].price*that.data.carts[componentId].timeLen*1);
          }
            
          that.setData({
            booklist: that.data.booklist,
            carts: that.data.carts,
            totalMoney: parseFloat(parseFloat(that.data.totalMoney).toFixed(2)),
          });
        }
      })
    }else{
      wx.showToast({
        title: '数量不可小于1！',
        icon: 'none',
        duration: 1000
      })
    }


  }, 

  //增加租用的数量
  addCount:function(e){
    console.log(e.target.dataset.index);
   var that = this
    var componentId = e.target.dataset.index;
    var cartId = e.target.dataset.cartid;
    var goodnum = e.target.dataset.goodnum;
    var cartnum = e.target.dataset.cartnum;
    if (cartnum < goodnum)  //当前数量小于库存，否则不改变数值
    {
      //可以加1，同时修改后台数据库数量，购物车中的数量加1
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/modifyCartNumAdd.do',
        data: { "cartId": cartId },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("addCartNum")
          console.log(res)
          that.data.carts[componentId].cartNum = that.data.carts[componentId].cartNum+1;
          //更新总金额
          if (that.data.isSelect[componentId].ischecked) {//如果当前是被选中的，那么每次点击加1都需要在总金额上再加一个价钱
            that.data.totalMoney = that.data.totalMoney + parseFloat(that.data.carts[componentId].price*that.data.carts[componentId].timeLen*1);
          }
          that.setData({
            booklist: that.data.booklist,
            carts: that.data.carts,
            totalMoney: parseFloat(parseFloat(that.data.totalMoney).toFixed(2)),
          });
        }
      })
    }else{
      wx.showToast({
        title: '数量不可大于当前库存！',
        icon:'none',
        duration:1000
      })
    }

    //this.data.booklist[componentId].boughtnumber = this.data.booklist[componentId].boughtnumber + 1;

  },


  //减少租用时长
  decreaseLen: function (e) {
    var that = this;
    var componentId = e.target.dataset.index;
    var cartId = e.target.dataset.cartid;
    var goodnum = e.target.dataset.goodnum;
    var timeLen = e.target.dataset.timelen;
    console.log("减少租赁时长")
    console.log(timeLen)
    if(timeLen>1){
      //时长减1
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/modifyCartTimeSub.do',
        data: { "cartId": cartId },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("subCartNum")
          console.log(res)
          that.data.carts[componentId].timeLen = that.data.carts[componentId].timeLen - 1;
          if (that.data.isSelect[componentId].ischecked) {
            that.data.totalMoney = that.data.totalMoney - parseFloat(that.data.carts[componentId].price * that.data.carts[componentId].cartNum*1);
          }
          that.setData({
            booklist: that.data.booklist,
            carts: that.data.carts,
            totalMoney: parseFloat(parseFloat(that.data.totalMoney).toFixed(2)),
          });
        }
      })
    }else{
      wx.showToast({
        title: '租赁时长不可小于1天（月）！',
        icon: 'none',
        duration: 1000
      })
    }
  }, 


  //增加租用的时长
  addLen: function (e) {
    console.log(e.target.dataset.index);
    var that = this
    var componentId = e.target.dataset.index;
    var cartId = e.target.dataset.cartid;
    var timeLen = e.target.dataset.timelen;
      //时长加1
      wx.request({
        url: 'http://ZLofCampus.top/RentMall/modifyCartTimeAdd.do',
        data: { "cartId": cartId },
        method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
        header: { "content-type": "application/json" }, // 设置请求的 header
        success: function (res) {
          console.log("addCartNum")
          console.log(res)
          that.data.carts[componentId].timeLen = that.data.carts[componentId].timeLen+1
          //更新总金额
          if (that.data.isSelect[componentId].ischecked) {//如果当前是被选中的，那么每次点击加1都需要在总金额上再加一个价钱
          that.data.totalMoney = that.data.totalMoney + parseFloat(that.data.carts[componentId].price * that.data.carts[componentId].cartNum*1);
          }
          that.setData({
            booklist: that.data.booklist,
            carts: that.data.carts,
            totalMoney: parseFloat(parseFloat(that.data.totalMoney).toFixed(2)),
          });

        }
      })
   
    //this.data.booklist[componentId].boughtnumber = this.data.booklist[componentId].boughtnumber + 1;

  },



  //跳转详情页面
  todetail:function(e){
    var index = e.currentTarget.dataset.index;
    var cartGoodId = this.data.carts[index].cartGoodId;
 
    wx.navigateTo({
      url: '/pages/others/details/details?goodid=' + cartGoodId,
      success: function (res) {
        console.log("详细跳转成功");
      },
      fail: function (res) { },
      complete: function (res) { },
    })
  },

  //删除购物车中的商品（点击小垃圾桶）
  into_garbage:function(){
    var that = this;
    var componentId = this.data.index;
    var cartId = this.data.cartId;
    wx.request({
      url: 'http://ZLofCampus.top/RentMall/deleteCartById.do',
      data: {"cartId":cartId},
      method: 'GET', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: { "content-type": "application/json" }, // 设置请求的 header
      success: function (res) {
        console.log(res);  
        if(res.data.msg)
        {
          wx.showToast({
            title: '删除成功!',
            icon: 'success',
            duration: 1500
          });
          that.setData({
            carts: [],
            isSelect: [],
            booklist: null,
            isAllSelect: false,
            totalMoney: 0,
          });
          that.onLoad();
        }else{
          wx.showToast({
            title: '网络出错啦....',
            icon: 'none',
            duration: 1000
          });
        }
      }
    }); 
  },

  //分享功能
  onShareAppMessage: function (res) {
    if (res.from === "menu") {
      console.log("来自右上角转发菜单")
    }
    return {
      title: "校园租赁商城",
      path: '/pages/index/index',
      imageUrl: "../../images/nothing.jpg",
      success: (res) => {
        console.log("转发成功", res);
      },
      fail: (res) => {
        console.log("转发失败", res);
      }
    }
  },

  //点击删除之后，弹出是否确认删除的提示框
  showModal: function (e) {
    this.setData({
      showModal: true,
      index: e.target.dataset.index,
      cartId:e.target.dataset.cartid
    });
  },

  //关闭提示框
  hideModal: function () {
    this.setData({
      showModal: false,
    });
  },

  //点击提示框的取消之后
  onCancel: function () {
    this.hideModal();
  },

  /**
   * 对话框确认按钮点击事件
   */
  onConfirm: function () {
    this.into_garbage();
    wx.showToast({
      title: '已删除',
    })
    this.hideModal();  //关闭提示框
  },


  // 左滑删除动画开始
  //手指触摸动作开始 记录起点X坐标
  touchstart: function (e) {
    console.log("滑动")
    console.log(e);
    //开始触摸时 重置所有删除
    console.log(this.data.istouchmove)
    this.data.istouchmove.forEach(function (v, i) {
      if (v.isTouchMove)//只操作为true的
        v.isTouchMove = false;
    });
    this.setData({
      //changedTouches: 涉及当前(引发)事件的触摸点的列表
      startX: e.changedTouches[0].clientX,  //获取触点坐标
      startY: e.changedTouches[0].clientY,
      istouchmove: this.data.istouchmove
    });

  },


  //滑动事件处理
  touchmove: function (e) {
    var that = this;
    var index = e.currentTarget.dataset.index;//当前索引
    var startX = that.data.startX;//开始X坐标
    var startY = that.data.startY;//开始Y坐标
    var touchMoveX = e.changedTouches[0].clientX;//滑动变化坐标
    var touchMoveY = e.changedTouches[0].clientY;//滑动变化坐标
      //获取滑动角度，调用计算角度的函数
    var angle = that.angle({ X: startX, Y: startY }, { X: touchMoveX, Y: touchMoveY });
    //forEach实现数组的遍历，其中v就是从数组中取出来的每一组
    that.data.istouchmove.forEach(function (v, i) {
      
      v.isTouchMove = false
      //滑动超过30度角 return
      if (Math.abs(angle) > 30)
        return;
      if (i == index) {
        if (touchMoveX > startX) //右滑，不会滑到删除
        {
          v.isTouchMove = false
          console.log("右滑")
        }
        else //左滑
        {
          v.isTouchMove = true
          console.log("左滑")
        }
          
      }
    })

    //更新数据
    that.setData({
      istouchmove: that.data.istouchmove
    })
  },

  /**
  
  * 计算滑动角度
  * @param {Object} start 起点坐标
  * @param {Object} end 终点坐标
  */

  angle: function (start, end) {
    var _X = end.X - start.X;
    var _Y = end.Y - start.Y;
    //返回角度 /Math.atan()返回数字的反正切值
    return 360 * Math.atan(_Y / _X) / (2 * Math.PI);
  },
// 左滑删除动画结束
}));
