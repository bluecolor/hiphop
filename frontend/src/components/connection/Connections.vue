<template lang="pug">
  .connections
    div(
      id="e3"
      style="max-width: 600px; margin: auto;"
      class="lighten-3"
    )
      v-container(fluid='', style='min-height: 0;', grid-list-lg='')
        v-layout(row='', wrap='')
          v-flex(xs12='' v-for='(con, i) in connections' v-bind:key="con.id")
            v-card(dense)
              v-card-title(dense class="blue white--text")
                .headline {{con.name}}
                v-spacer
                v-menu(bottom='', left='')
                  v-btn(icon='', slot='activator', dark='')
                    v-icon more_vert
                  v-list
                    v-list-tile(v-for='item in menu', :key='item.title', @click='onMenuItemClick(item.id, con.id)')
                      v-list-tile-title {{ item.title }}
              v-card-text
                div
                  span(:id="'url-'+con.id")
                    | {{con.url}}
              v-card-actions
                v-btn(@click="onTest(con.id)") Test
                v-progress-circular(v-show="testing === con.id" indeterminate color="primary")
                v-spacer
                v-btn(route :to="'/connection/' + con.id") Details
                v-btn(color="error", @click="onRemove(con.id)") Delete
      v-btn(fixed='', dark='', fab='', bottom='', right='', color='pink' route, to="/connection")
        v-icon add
</template>

<script>

import _ from 'lodash'
import {mapGetters, mapActions} from 'vuex'

export default {
  data () {
    return {
      testing: undefined,
      menu: [
        {
          title: 'Clone',
          id: 'CLONE'
        },
        {
          title: 'Copy url',
          id: 'COPY_URL'
        }
      ]
    }
  },
  computed: {
    ...mapGetters('connections', [
      'connections'
    ])
  },
  methods: {
    ...mapActions('connections', [
      'remove', 'test'
    ]),
    ...mapActions('notifications', [
      'snack', 'snackSuccess', 'snackError', 'snackInfo', 'snackWarning'
    ]),
    onMenuItemClick (m, c) {
      switch (m) {
        case 'CLONE': this.onClone(c); return
        case 'COPY_URL': this.onCopyUrl(c); return
      }
    },
    onClone (id) {
      this.$router.push({path: `/connection/${id}?clone=true`})
    },
    onRemove (id) {
      this.remove(id).then(() => {
        this.snackSuccess('Deleted connection')
      }).catch(() => {
        this.snackError('Failed to delete connection')
      })
    },
    onTest (id) {
      if (this.testing) {
        this.snackWarning('Wait for the active test to end...')
        return
      }
      this.testing = id
      this.test(this.testing).then((con) => {
        this.snackSuccess(`${con.name} works.`)
      }).catch(() => {
        const con = _.find(this.connections, {id})
        if (con) {
          this.snackError(`${con.name} not wroking!`)
        } else {
          this.snackError(`Connection is not wroking!`)
        }
      }).finally(() => {
        this.testing = undefined
      })
    },
    onCopyUrl (id) {
      const el = document.getElementById('url-' + id)
      let inp = document.createElement('input')
      document.body.appendChild(inp)
      inp.value = el.textContent
      inp.select()
      document.execCommand('copy', false)
      inp.remove()
      this.snackInfo('Copied connection url')
    }
  },
  mounted () {}

}
</script>

<style scoped>
  .hidden {
    display: none;
  }
  .url {
    font-size: 14px !important;
  }

  .connections {
    /* height: 100%; */
    display: flex;
    overflow-y: auto;
    width: 100%;
  }

</style>
