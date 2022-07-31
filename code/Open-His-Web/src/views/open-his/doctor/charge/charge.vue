<template>
  <div
    v-loading="loading"
    class="app-container"
    :element-loading-text="loadingText"
  >
    <!-- 挂号编号的查询条件开始 -->
    <el-card style="margin-bottom: 5px">
      <el-form ref="queryForm" label-width="88px">
        <el-form-item label="挂号单ID" prop="regId">
          <el-input
            v-model="regId"
            clearable
            size="small"
            style="width:300px"
          />
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">读取IC卡</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <!-- 挂号编号的查询条件结束 -->
    <!-- 挂号输入框下面的所有内容开始 -->
    <div>
      <!-- 病例信息开始 -->
      <el-card style="margin-bottom: 5px">
        <el-form label-position="right" label-width="120px" inline class="demo-table-expand">
          <el-form-item label="挂号单号:">
            <span v-text="careHistory.regId" />
          </el-form-item>
          <el-form-item label="医生姓名:">
            <span v-text="careHistory.userName" />
          </el-form-item>
          <el-form-item label="科室名称:">
            <span v-text="careHistory.deptName" />
          </el-form-item>
          <el-form-item label=" 患者姓名:">
            <span v-text="careHistory.patientName" />
          </el-form-item>
          <el-form-item label="就诊时间:">
            <span v-text="careHistory.careDate" />
          </el-form-item>
          <el-form-item label="主诉:">
            <span v-text="careHistory.caseTitle" />
          </el-form-item>
          <el-form-item label="诊断信息:">
            <span v-text="careHistory.caseResult" />
          </el-form-item>
          <el-form-item label="医生建议:">
            <span v-text="careHistory.doctorTips" />
          </el-form-item>
        </el-form>
      </el-card>
      <!-- 病例信息结束 -->
      <!-- 工具按钮开始 -->
      <el-card style="margin-bottom: 5px">
        <el-button type="success" icon="el-icon-finished" @click="handleSelectAll">全选</el-button>
        <el-button type="success" icon="el-icon-finished" @click="handleUnSelectAll">取消全选</el-button>
        <el-button type="danger" icon="el-icon-bank-card" @click="handlePayWithCash">现金支付</el-button>
        <el-button type="danger" icon="el-icon-bank-card" @click="handlePayWithZfb">支付宝支付</el-button>
        <span style="margin-left:20px">订单总额:<span style="color:red;margin-left:20px">￥:{{ allAmount }}</span></span>
      </el-card>
      <!-- 工具结束开始 -->
      <!-- 未支付的处方信息开始 -->
      <el-card style="margin-bottom: 5px">
        <el-collapse v-if="careOrders.length>0" v-model="activeNames">
          <el-collapse-item v-for="(item,index) in careOrders" :key="index" :name="index">
            <template slot="title">
              【{{ item.coType==='0'?'药用处方':'检查处方' }}】【{{ index+1 }}】【处方总额】:￥{{ item.allAmount }}
            </template>
            <el-table
              ref="refTable"
              v-loading="loading"
              border
              :data="item.careOrderItems"
              :row-class-name="tableRowClassName"
              @selection-change="handleCareOrderItemSelectionChange($event,item.coId)"
            >
              <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="序号" align="center" type="index" width="50" />
              <el-table-column :label="item.coType==='0'?'药品名称':'项目名称'" align="center" prop="itemName" />
              <el-table-column label="数量" align="center" prop="num" />
              <el-table-column label="单价(元)" align="center" prop="price" />
              <el-table-column label="金额(元)" align="center" prop="amount" />
              <el-table-column label="备注" align="center" prop="remark" />
              <el-table-column label="状态" align="center" prop="status" :formatter="statusFormatter" />
            </el-table>
          </el-collapse-item>
        </el-collapse>
      </el-card>
      <!-- 未支付的处方信息结束 -->
      <!-- 挂号输入框下面的所有内容结束 -->
    </div>

    <el-dialog
      title="请使用支付宝支付"
      :visible.sync="openPay"
      center
      :close-on-click-modal="false"
      :before-close="handleClose"
      append-to-body
    >
      <el-form label-position="left" label-width="120px" inline class="demo-table-expand">
        <el-card>
          <el-form-item label="订单号:">
            <span>{{ payObj.orderId }}</span>
          </el-form-item>
          <el-form-item label="总金额:">
            <span>{{ payObj.allAmount }}</span>
          </el-form-item>
        </el-card>
      </el-form>
      <div style="text-align:center">
        <vue-qr :text="payObj.payUrl" :size="200" />
        <div>请使用支付宝扫码</div>
      </div>
    </el-dialog>

  </div>
</template>
<script>
// 引入生成二维码的组件
import vueQr from 'vue-qr'
import { getNoChargeCareHistoryByRegId, createOrderChargeWithCash, createOrderChargeWithZfb, queryOrderChargeOrderId } from '@/api/doctor/charge'
export default {
  comments: {
    vueQr
  },
  data() {
    return {
      // 整体遮罩
      loading: false,
      // 设置遮罩层的文字
      loadingText: '',
      // 用户输入的挂号单号
      regId: undefined,
      // 病例对象
      careHistory: {},
      // 处方信息
      careOrders: [],
      // 处方详情的状态字典
      statusOptions: [],
      // 当前选中的订单总额
      allAmount: 0.00,
      // 当前选中的所有项目集合
      itemObjs: [],
      // 展开的折叠面板的名字
      activeNames: [],
      // 支付对象
      payObj: {},
      // 是否打开支付宝二维码支付层
      openPay: false,
      // 定时轮询对象
      intervalObj: undefined
    }
  },
  created() {
    // 加载状态的字典数据
    this.getDataByType('his_order_details_status').then(res => {
      this.statusOptions = res.data
    })
  },
  methods: {
    // 查询
    handleQuery() {
      if (this.regId === '') {
        this.msgError('请输入挂号单号')
        this.resetCurrentParams()
        return
      }
      this.careHistory = {}
      this.careOrders = []
      this.loading = true
      this.loadingText = '数据查询中，请稍后...'
      getNoChargeCareHistoryByRegId(this.regId).then(res => {
        this.careHistory = res.data.careHistory
        this.careOrders = res.data.careOrders
        this.loading = false
        this.loadingText = ''
        this.careOrders.filter((c, index) => {
          this.activeNames.push(index)
        })
      }).catch(() => {
        this.msgError('查询失败')
        this.loading = false
        this.loadingText = ''
      })
    },
    // 翻译处方详情状态
    statusFormatter(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 监听多个表格的checkbox的选中事件
    handleCareOrderItemSelectionChange(selection, coId) {
      if (this.itemObjs.length === 0) {
        this.itemObjs = selection
      } else {
        for (let i = 0; i < this.itemObjs.length; i++) {
          const obj = this.itemObjs[i]
          if (obj.coId === coId) {
            this.itemObjs.splice(i, 1)
            i--
          }
        }
        // 最后再来添加
        selection.filter(newItem => {
          this.itemObjs.push(newItem)
        })
      }
      // 调用计算选中的总额
      this.computeAllAmount()
    },

    // //计算选中的总额
    computeAllAmount() {
      this.allAmount = 0
      this.itemObjs.filter(item => {
        this.allAmount += item.amount
      })
    },
    tableRowClassName({ row, rowIndex }) {
      row.index = rowIndex
    },
    // 全选择
    handleSelectAll() { // 先取消再反选择
      const tables = this.$refs.refTable
      tables.filter(t => {
        t.clearSelection()
        t.toggleAllSelection() // 注意不要用错方法了
      })
    },
    // 取消全选
    handleUnSelectAll() {
      const tables = this.$refs.refTable
      tables.filter(t => {
        t.clearSelection()
      })
    },
    // 现金支付
    handlePayWithCash() {
      if (!this.careHistory.regId) {
        this.msgError('请输入挂号单ID查询')
        return
      } else if (this.itemObjs.length === 0) {
        this.msgError('请选择要支付的项目')
        return
      } else {
        // 组装数据
        const postObj = {
          orderChargeDto: {
            orderAmount: this.allAmount,
            chId: this.careHistory.chId,
            regId: this.careHistory.regId,
            patientName: this.careHistory.patientName
          },
          orderChargeItemDtoList: []
        }
        this.itemObjs.filter(item => {
          const obj = {
            itemId: item.itemId,
            coId: item.coId,
            itemName: item.itemName,
            itemPrice: item.price,
            itemNum: item.num,
            itemType: item.itemType,
            itemAmount: item.amount
          }
          postObj.orderChargeItemDtoList.push(obj)
        })
        // 发送请求
        this.loading = true
        this.loadingText = '订单创建并现金支付中'
        this.$confirm('就否确定创建订单并使用现金支付?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          createOrderChargeWithCash(postObj).then(res => {
            this.msgSuccess('订单创建并现金支付成功')
            this.resetCurrentParams()
            this.loading = false
          }).catch(() => {
            this.msgError('创建订单失败')
            this.loading = false
          })
        }).catch(() => {
          this.msgError('创建已取消')
          this.loading = false
        })
      }
    },
    // 支付宝支付
    handlePayWithZfb() {
      if (!this.careHistory.regId) {
        this.msgError('请输入挂号单ID查询')
        return
      } else if (this.itemObjs.length === 0) {
        this.msgError('请选择要支付的项目')
        return
      } else {
        // 组装数据
        const postObj = {
          orderChargeDto: {
            orderAmount: this.allAmount,
            chId: this.careHistory.chId,
            regId: this.careHistory.regId,
            patientName: this.careHistory.patientName
          },
          orderChargeItemDtoList: []
        }
        this.itemObjs.filter(item => {
          const obj = {
            itemId: item.itemId,
            coId: item.coId,
            itemName: item.itemName,
            itemPrice: item.price,
            itemNum: item.num,
            itemType: item.itemType,
            itemAmount: item.amount
          }
          postObj.orderChargeItemDtoList.push(obj)
        })
        // 发送请求
        this.loading = true
        this.loadingText = '订单创建并支付宝支付中'
        this.$confirm('是否确定创建订单并使用支付宝支付?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          createOrderChargeWithZfb(postObj).then(res => {
            this.payObj = res.data
            this.msgSuccess('订单创建成功，请扫码支付')
            const tx = this
            tx.openPay = true// 打开支付的弹出层
            // 定时轮询
            tx.intervalObj = setInterval(function() {
              // 根据ID查询订单信息
              queryOrderChargeOrderId(tx.payObj.orderId).then(r => {
                if (r.data.orderStatus === '1') { // 说明订单状态为支付成功
                  // 清空定时器
                  clearInterval(tx.intervalObj)
                  tx.$notify({
                    title: '支付成功',
                    message: '【' + tx.payObj.orderId + '】的订单编写支付成功',
                    type: 'success'
                  })
                  tx.openPay = false
                  tx.resetCurrentParams()
                }
              }).catch(() => {
                // 清空定时器
                clearInterval(tx.intervalObj)
              })
            }, 2000)
            this.loading = false
          }).catch(() => {
            this.msgError('创建订单失败')
            this.loading = false
          })
        }).catch(() => {
          this.msgError('创建已取消')
          this.loading = false
        })
      }
    },
    // 清空careHistory和careOrders
    resetCurrentParams() {
      this.careHistory = {}
      this.careOrders = []
    },
    // 如果用户没有支付，而弹出层被关闭了
    handleClose() {
      this.$confirm('您确定放弃支付吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.msgError('您已放弃支付，可以回到收费查询列表里面再次支付')
        this.resetCurrentParams()
        this.openPay = false
        // 关闭轮询
        clearInterval(this.intervalObj)
      }).catch(() => {
        this.msgSuccess('欢迎继续支付')
      })
    }
  }
}
</script>
<style scoped>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
