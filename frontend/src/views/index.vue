<template>
  <div class="dashboard-editor-container">

    <panel-group @handleSetLineChartData="handleSetLineChartData" />
     <el-row :gutter="10">
      <el-col :xs="24" :sm="24">
        <div
          class="app-container"
          style="height: 340px; background-color: #fff"
        >
          <div class="user-wrapper">
            <div class="user-header">
              <img :src="avatar" class="avatar" />
            </div>
            <div class="user-info">
              <div class="random-message text">{{ welcomeMessage }}</div>
              <div class="user-dept">
                <span>角色：{{ user.roles[0] ? user.roles[0] : "暂无角色" }}</span>
              </div>
              <div class="user-dept">
                <span>手机号：{{ user.phone ? user.phone : "暂无手机号" }}</span>
                <span>邮 箱：{{ user.email ? user.email : "暂无邮箱" }}</span>

              </div>
              <div class="user-dept">
              </div>
              <div class="user-login-info">
                {{ "最后登录IP" }}：
                <span id="last-login-time">{{
                  user.lastLoginIp ? user.lastLoginIp : "第一次登录系统"
                }}</span>
              </div>
              
            </div>
          </div>
        </div>
      </el-col>
      <div>
      </div>
    </el-row>


  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import LineChart from './dashboard/LineChart'
import RaddarChart from './dashboard/RaddarChart'
import PieChart from './dashboard/PieChart'
import BarChart from './dashboard/BarChart'
import { mapGetters } from "vuex";

const lineChartData = {
  '2020-08': 200,
  '2020-07': 20,
  '2020-06': 150,
  '2020-05': 20,
  '2020-04': 120,
  '2020-03': 20,
  '2020-02': 20,
  '2020-01': 20,
  '2019-12': 20,
  '2019-11': 90,
  '2019-10': 20,
  '2019-09': 209,
}

export default {
  name: 'Index',
  components: {
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart
  },
  computed: {
    ...mapGetters(["sidebar", "avatar", "device"]),
    user() {
      return this.$store.state.user;
    },
  },
  data() {
    return {
        welcomeMessage: "",
      lineChartData: lineChartData,
      monthStrList: this.getNear12Month(),
      //[30, 52, 200, 334, 390, 330, 220, 200, 334, 390, 330, 220]
      monthData: []
    }
  },
  created() {
    this.getMonthCountList(this.getNear12Month())
        this.welcomeMessage = this.welcome();

  },
  methods: {
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    },
    getMonthCountList(monthStrList) {

    },
    welcome() {
      const date = new Date();
      const hour = date.getHours();
      const time =
        hour < 6
          ? "早上好"
          : hour <= 11
          ? "早上好"
          : hour <= 13
          ? "下午好"
          : hour <= 18
          ? "下午好"
          : "晚上好";
      const welcomeArr = ["欢迎访问问卷系统!"];
      const index = Math.floor(Math.random() * welcomeArr.length);
      return `${time}, ${this.user.name}, ${welcomeArr[index]}`;
    //   return `${time}, ${this.user.name}`;
    },

    // 返回前12个月的yyyy-mm格式数据，倒序
    getNear12Month() {
      let dataArr = [];
      let data = new Date();
      let year = data.getFullYear();
      data.setMonth(data.getMonth()+1, 1)           //获取到当前月份,设置月份
      for (let i = 0; i < 12; i++) {
        data.setMonth(data.getMonth() - 1);     //每次循环一次 月份值减1
        let m = data.getMonth() + 1;
        m = m < 10 ? "0" + m : m;
        dataArr.push(data.getFullYear() + "-" + (m));
      }
      return dataArr.reverse();
    },
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}

.app-container {
    margin: 0 0 10px 0;
  }
  .user-container {
    padding: 15px;
  }
  .user-wrapper {
    width: 100%;
    display: inline-block;
    .user-header {
      display: inline-block;
      vertical-align: middle;
      img {
        width: 10rem;
        margin: 0.5rem 1rem;
        border-radius: 50%;
      }
    }
    .user-info {
      display: inline-block;
      vertical-align: middle;
      .random-message {
        font-size: 2rem;
        margin-bottom: 2rem;
        color: #333;
      }
      .user-dept,
      .user-login-info {
        color: rgba(0, 0, 0, 0.45);
        margin-bottom: 2rem;
        font-size: 1.2rem;
        line-height: 3rem;
      }
    }
  }
  .user-visits {
    text-align: center;
    padding-right: 2rem;
    margin-top: 1rem;
    vertical-align: middle;
    .num {
      font-weight: 600;
    }
  }
  .text {
    letter-spacing: 0.2rem;
    font-size: 1.5rem;
    background-image: -webkit-linear-gradient(left, #147B96, #E6D205 25%, #147B96 50%, #E6D205 75%, #147B96);
    -webkit-text-fill-color: transparent;
    -webkit-background-clip: text;
    -webkit-background-size: 200% 100%;
    -webkit-animation: maskedAnimation 4s infinite linear;

}
.text::before{
    content: attr(data-text);
    position: absolute;
    left: -2px;
    width: 100%;
    background: black;
    text-shadow:2px 0 red;
    animation: animation-before 3s infinite linear alternate-reverse;
}

.text::after{
    content: attr(data-text);
    position: absolute;
    left: 2px;
    width: 100%;
    background: black;
    text-shadow: -2px 0 blue;
    animation: animation-after 3s infinite linear alternate-reverse;
}
@keyframes maskedAnimation {
    0% {
        background-position: 0 0;
    }

    100% {
        background-position: -100% 0;
    }
}
</style>
