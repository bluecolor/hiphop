import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Connection from '@/components/connection/Connection'
import Connections from '@/components/connection/Connections'
import Deployment from '@/components/deployment/Deployment'
import User from '@/components/user/User'
import Users from '@/components/user/Users'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Hello',
      component: Hello
    }, {
      path: '/user',
      component: User
    }, {
      path: '/users',
      component: Users
    }, {
      path: '/user/:id',
      props: true,
      component: User
    }, {
      path: '/connection',
      component: Connection
    }, {
      path: '/connection/:id',
      props: true,
      component: Connection
    }, {
      path: '/connections',
      component: Connections
    }, {
      path: '/deployments',
      name: 'Deployments',
      component: Deployment
    }
  ]
})
