import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Connection from '@/components/connection/Connection'
import Connections from '@/components/connection/Connections'
import Deployment from '@/components/deployment/Deployment'
import Deployments from '@/components/deployment/Deployments'
import DeploymentOrders from '@/components/deployment/DeploymentOrders'
import DeploymentOrderSteps from '@/components/deployment/DeploymentOrderSteps'
import User from '@/components/user/User'
import Users from '@/components/user/Users'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Hello',
      component: Home
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
      path: '/deployment',
      component: Deployment
    }, {
      path: '/deployments',
      component: Deployments
    }, {
      path: '/deployments/:id/orders',
      component: DeploymentOrders
    }, {
      path: '/deployments/:deploymentId/orders/:orderId/steps',
      component: DeploymentOrderSteps
    }
  ]
})
