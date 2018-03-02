<template lang="pug">
  v-dialog(v-model="display" fullscreen="" transition="dialog-bottom-transition" :overlay="false")
    v-card
      v-toolbar(dark="" color="primary")
        v-btn(icon="" @click.native="display = false" dark="")
          v-icon close
        v-toolbar-title Settings
        v-spacer
        v-toolbar-items
          v-btn(dark="" flat="" @click.native="dialog = false") Save
      v-list(three-line="" subheader="")
        v-subheader User Controls
        v-list-tile(avatar="")
          v-list-tile-content
            v-list-tile-title Content filtering
            v-list-tile-sub-title Set the content filtering level to restrict apps that can be downloaded
        v-list-tile(avatar="")
          v-list-tile-content
            v-list-tile-title Password
            v-list-tile-sub-title Require password for purchase or use password to restrict purchase
      v-divider
      v-list(three-line="" subheader="")
        v-subheader General
        v-list-tile(avatar="")
          v-list-tile-action
            v-checkbox(v-model="notifications")
          v-list-tile-content
            v-list-tile-title Notifications
            v-list-tile-sub-title Notify me about updates to apps or games that I downloaded
        v-list-tile(avatar="")
          v-list-tile-action
            v-checkbox(v-model="sound")
          v-list-tile-content
            v-list-tile-title Sound
            v-list-tile-sub-title Auto-update apps at any time. Data charges may apply
        v-list-tile(avatar="")
          v-list-tile-action
            v-checkbox(v-model="widgets")
          v-list-tile-content
            v-list-tile-title Auto-add widgets
            v-list-tile-sub-title Automatically add home screen widgets

</template>

<script>
  export default {
    props: ['show'],
    data () {
      return {
        display: JSON.parse(this.show),
        notifications: false,
        sound: true,
        widgets: false
      }
    },
    computed: {
    },
    watch: {
      show () {
        this.display = JSON.parse(this.show)
      },
      display () {
        if (!this.display) {
          this.$emit('hide')
        }
      }
    },
    mounted () {
    }
  }
</script>
