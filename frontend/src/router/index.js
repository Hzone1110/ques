import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: 路由配置项
 *
 * hidden: true                   // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true               // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect           // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * meta : {
    roles: ['admin','editor']    // 设置该路由进入的权限，支持多个权限叠加
    title: 'title'               // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'             // 设置该路由的图标，对应路径src/icons/svg
    breadcrumb: false            // 如果设置为false，则不会在breadcrumb面包屑中显示
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: (resolve) => require(['@/views/redirect'], resolve)
      }
    ]
  },
  {
    path: '/login',
    component: (resolve) => require(['@/views/login'], resolve),
    hidden: true
  },
  {
    path: '/rePassword',
    component: (resolve) => require(['@/views/rePassword'], resolve),
    hidden: true
  },
  {
    path: '/registerCompany',
    component: (resolve) => require(['@/views/registerCompany'], resolve),
    hidden: true
  },
  {
    path: '/registerAnswer',
    component: (resolve) => require(['@/views/registerAnswer'], resolve),
    hidden: true
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/error/404'], resolve),
    hidden: true
  },
  {
    path: '/401',
    component: (resolve) => require(['@/views/error/401'], resolve),
    hidden: true
  },
  {
    path: '/ques/preview',
    component: () => import('@/views/ques/preview'),
    name: '问卷',
    hidden: true,
    meta: { title: '问卷', icon: 'dashboard' }
  },
  {
    path: '/ques/view',
    component: () => import('@/views/ques/view'),
    name: '预览回答',
    hidden: true,
    meta: { title: '预览回答', icon: 'dashboard' }
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: (resolve) => require(['@/views/index'], resolve),
        name: '首页',
        meta: { title: '首页', icon: 'dashboard', noCache: true, affix: true }
      },
      {
        path: '/ques/qu-detail',
        component: () => import('@/views/ques/qu-add'),
        name: '题目详情',
        hidden: true,
        meta: { title: '题目详情', icon: 'dashboard' }
      },
      {
        path: '/ques/stat',
        component: () => import('@/views/ques/stat'),
        name: '统计分析',
        hidden: true,
        meta: { title: '统计分析', icon: 'dashboard' }
      },
      {
        path: '/ques/setting',
        component: () => import('@/views/ques/setting/index'),
        name: '问卷风格设置',
        hidden: true,
        meta: { title: '问卷风格设置', icon: 'dashboard' }
      },
      {
        path: '/ques/exam-detail',
        component: () => import('@/views/ques/exam-add'),
        name: '问卷配置详情',
        hidden: true,
        meta: { title: '问卷配置详情', icon: 'dashboard' }
      },
      
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: (resolve) => require(['@/views/selfcom/user/profile/index'], resolve),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  {
    path: '/dict',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'type/data/:dictId(\\d+)',
        component: (resolve) => require(['@/views/system/dict/data'], resolve),
        name: 'Data',
        meta: { title: '字典数据', icon: '' }
      }
    ]
  }
]

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
