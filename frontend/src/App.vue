<template lang="pug">
v-app(light='')
  v-snackbar(
    :timeout="snack.timeout"
    :top="true"
    :color="snack.color"
    v-model="snack.display"
  ) {{snack.text}}
  settings(:show="settings" v-on:hide="settings=false")
  v-navigation-drawer(fixed="" v-model="queryRightDrawer" right="" clipped="" app="")
      v-list(dense="")
        v-list-tile()
          v-list-tile-action
            v-icon exit_to_app
          v-list-tile-content
            v-list-tile-title Open Temporary Drawer
  v-navigation-drawer(fixed='', :mini-variant='miniVariant', :clipped='clipped', v-model='drawer', app='')
    v-list.pt-0
      v-list-tile(value='true', v-for='(item, i) in items', :key='i', exact='', router :to='item.to')
        v-list-tile-action
          v-icon(light='', v-html='item.icon')
        v-list-tile-content
          v-list-tile-title(v-text='item.title')
  v-toolbar(fixed='', app='', :clipped-left='clipped')
    v-toolbar-side-icon(@click.stop='drawer = !drawer', light='')
    v-btn(icon='', light='', @click.stop='miniVariant = !miniVariant')
      v-icon(v-html="miniVariant ? 'chevron_right' : 'chevron_left'")
    v-toolbar-title(v-text='title')
    v-spacer
    v-btn(icon='', light='', @click="settings = true")
      v-icon settings
    v-btn(icon='', light='' @click="logut")
      v-icon(color="red") exit_to_app
  v-content.content
    router-view
  v-footer(:fixed='fixed', app='')
    .hidden-xs
      span Version:  
      span {{v.major}}.{{v.minor}}.{{v.code}}
      span &nbsp; Build date:  
      span {{version.date}}
    span
      | &nbsp; Copyright © {{year}} &nbsp;
      a(target="_blank" href='http://primeit.com.tr' style="text-decoration: none !important;") prime.
    |  All rights reserved.
</template>

<script>
  import {mapGetters} from 'vuex'
  import Settings from '@/components/settings/Settings'

  export default {
    data () {
      return {
        settings: false,
        clipped: true,
        drawer: true,
        fixed: true,
        connections: [
          {
            name: 'Development'
          }, {
            name: 'Production'
          }
        ],
        items: [{
          icon: 'home',
          title: 'Home',
          to: '/'
        }, {
          icon: 'cloud',
          title: 'Connections',
          to: '/connections'
        }, {
          icon: 'storage',
          title: 'Deployments',
          to: '/deployments'
        }, {
          icon: 'code',
          title: 'Query',
          to: '/query'
        }, {
          icon: 'perm_identity',
          title: 'Users',
          to: '/users'
        }],
        miniVariant: true,
        right: true,
        rightDrawer: false,
        title: ''
      }
    },
    computed: {
      ...mapGetters('ui', [
        'queryRightDrawer'
      ]),
      ...mapGetters('notifications', [
        'snack'
      ]),
      ...mapGetters('app', [
        'version'
      ]),
      v () {
        let version = {}
        if (this.version.major) {
          version = {
            major: this.version.major.split('=')[1],
            minor: this.version.minor.split('=')[1],
            code: this.version.versionCode.split('=')[1]
          }
        }
        return version
      },
      year () {
        if (this.version.date) {
          const x = this.version.date.split(' ')
          return x[x.length - 1]
        } else {
          return '2018'
        }
      }
    },
    methods: {
      logut () {
        window.location = '/logout'
      }
    },
    components: {
      Settings
    }
  }
</script>


<style>
</style>
