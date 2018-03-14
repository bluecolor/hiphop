<template lang="pug">
  v-dialog(v-model="display" fullscreen="" transition="dialog-bottom-transition" :overlay="false")
    v-card
      v-toolbar(dark="" color="primary")
        v-btn(icon="" @click.native="display = false" dark="")
          v-icon close
        v-toolbar-title Settings
      div(
        v-show="screen==='password'"
        style="max-width: 700px; margin: auto;"
        class="lighten-3"
      )
        password(v-on:close="screen='settings'")
      div(
        v-show="screen==='mail'"
        style="max-width: 700px; margin: auto;"
        class="lighten-3"
      )
        mail(v-on:close="screen='settings'")
      div(
        v-show="screen==='settings'"
        style="max-width: 700px; margin: auto;"
        class="lighten-3"
      )
          v-list(one-line subheader)
            v-subheader General
            v-card.elevation-5
              v-list-tile(avatar ripple @click="screen = 'password'" )
                v-list-tile-content
                  v-list-tile-title Password
                  v-list-tile-sub-title Change your account's password
          v-list(one-line subheader)
            v-subheader User Controls
            v-card.elevation-5
              v-list-tile(avatar)
                v-list-tile-content
                  v-list-tile-title Footer
                  v-list-tile-sub-title Display footer
                v-list-tile-action
                  v-switch(v-model="m.footer" color="success")
              v-divider
              v-list-tile(avatar)
                v-list-tile-content
                  v-list-tile-title Mini variant
                  v-list-tile-sub-title Display left drawer as mini variant
                v-list-tile-action
                  v-switch(v-model="m.miniVariant" color="success")
              v-divider
              v-list-tile(avatar)
                v-list-tile-content
                  v-list-tile-title Desktop notifications
                  v-list-tile-sub-title Display desktop notifications for the events
                v-list-tile-action
                  v-switch(v-model="m.deno" color="success")
              v-divider
              v-list-tile(avatar)
                v-list-tile-content
                  v-list-tile-title Sound
                  v-list-tile-sub-title Play sound when the event occurs
                v-list-tile-action
                  v-switch(v-model="m.sound" color="success")
          v-list(one-line subheader)
            v-subheader Administration
            v-card.elevation-5
              v-list-tile(avatar ripple @click="screen = 'mail'")
                v-list-tile-content
                  v-list-tile-title Mail
                  v-list-tile-sub-title Mail settings for notifications
              v-divider
              v-list-tile(avatar ripple @click="screen = 'settings'")
                v-list-tile-content
                  v-list-tile-title Slack
                  v-list-tile-sub-title Configure slack hook for instant messages
              v-divider
              v-list-tile(avatar ripple @click="")
                v-list-tile-content
                  v-list-tile-title Updates
                  v-list-tile-sub-title Check for updates
</template>

<script>
  import _ from 'lodash'
  import {mapActions, mapGetters} from 'vuex'
  import Password from './Password'
  import Mail from './Mail'

  export default {
    props: ['show'],
    data () {
      return {
        m: {},
        display: JSON.parse(this.show),
        notifications: false,
        screen: 'settings'
      }
    },
    computed: {
      ...mapGetters('users', [
        'me'
      ])
    },
    watch: {
      'm.footer' (v) {
        this.setOption({ name: 'footer', value: v })
      },
      'm.miniVariant' (v) {
        this.setOption({ name: 'miniVariant', value: v })
      },
      'm.deno' (v) {
        this.setOption({ name: 'deno', value: v })
      },
      'm.sound' (v) {
        this.setOption({ name: 'sound', value: v })
      },
      options () {
        return JSON.parse(this.me.options)
      },
      show () {
        this.display = JSON.parse(this.show)
      },
      display () {
        if (!this.display) {
          this.$emit('hide')
        }
      },
      me () {
        this.m = _.cloneDeep(JSON.parse(this.me.options))
      }
    },
    mounted () {
      if (!this.me) { return }
      this.m = _.cloneDeep(JSON.parse(this.me.options))
    },
    methods: {
      ...mapActions('users', [
        'setOption'
      ]),
      onSet (name, value) {
      }
    },
    components: {
      Password,
      Mail
    }
  }
</script>
