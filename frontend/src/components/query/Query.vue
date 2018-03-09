<template lang="pug">

//- div(style="width:100%;")
div.query(xs12 mt-1 style="height:100%")
  v-navigation-drawer(mt-3 fixed v-model="queryRightDrawer" right="" clipped app style="z-index:0;")
    v-container.drawer-container
      v-list(v-show="e1==='connections'" dense)
        v-list-tile-content.top-filters
          v-text-field(clearable v-model="connectionFilter.search" append-icon="search" style="padding:10px 10px 0px 10px;" )
          v-select(
            clearable
            style="padding:0px 10px 0px 10px;"
            :items="labels"
            item-text="name"
            item-value="id"
            multiple
            chips
            max-height="auto"
            autocomplete,
            v-model="connectionFilter.labels"
          )
            template(slot="selection" slot-scope="data")
              v-chip.chip--select-multi(
                small
                text-color="white"
                :style="`border-color:${data.item.color};background-color:${data.item.color};`"
                close
                @input="data.parent.selectItem(data.item)"
                :selected="data.selected"
                :key="JSON.stringify(data.item)"
              )
                | {{ data.item.name }}
            template(slot="item" slot-scope="data")
              template(v-if="typeof data.item !== 'object'")
                v-list-tile-content(v-text="data.item")
              template(v-else="")
                v-icon(v-if="!data.item.labels" left :style="'margin-right:10px; color:' + data.item.color") label
                v-list-tile-content
                  v-list-tile-title(v-html="data.item.name" @click="")
        v-list-tile
          v-list-tile-content
            v-list-tile-title Connections
          v-list-tile-action
            v-checkbox(color="green darken-3" v-model="connectionFilter.selectAll")
        v-divider
        v-list-tile(v-for="(c,i) in cons" v-bind:key="c.id" @click="" ripple)
          v-list-tile-content
            v-list-tile-title {{c.name}}
          v-list-tile-action
            v-checkbox(color="success" v-model="connectionFilter.selected[c.id]")

      v-layout(v-show="e1==='favorites'" column style="display:flex;")
        v-container(style="padding:0px; max-height: 70px;")
          v-list-tile-content.top-filters(style="padding:0px;")
            v-text-field(clearable v-model="favFilter.search" append-icon="search" style="padding:10px 10px 0px 10px;" )
        v-container.fav-list-container
          v-list(two-line="" dense)
            template(v-for="(item, index) in favScripts")
              v-list-tile.script-list-item(avatar="" ripple="" @click="")
                v-list-tile-content(@click="onLoadFav(item)")
                  v-list-tile-title {{ unixFromNow(item.date) }}
                  v-list-tile-sub-title {{ ellipsis(item.content) }}
                v-list-tile-action
                  v-btn(icon @click="onDeleteFav(item.id)")
                    v-icon(color="red darken-3") delete
              v-divider(v-if="index + 1 < scripts.length" )

      v-layout(v-show="e1==='history'" column style="display:flex;")
        v-container(style="padding:0px; max-height: 70px;")
          v-list-tile-content.top-filters(style="padding:0px;")
            v-text-field(clearable v-model="histFilter.search" append-icon="search" style="padding:10px 10px 0px 10px;" )
        v-container.history-list-container
          v-list(two-line="" dense)
            template(v-for="(item, index) in histQueries")
              v-list-tile.history-list-item(avatar="" ripple="" @click="")
                v-list-tile-action
                  v-icon(:color="historyItemIconColor(item)") {{historyItemIcon(item)}}
                v-list-tile-content(@click="")
                  v-list-tile-title {{ unixFromNow(item.startDate) || 'Some time ago' }}
                  v-list-tile-sub-title {{ ellipsis(item.query) }}
                v-list-tile-action
                  v-menu(bottom='', left)
                    v-btn(icon, slot='activator', dark)
                      v-icon(color="grey darken-1") more_vert
                    v-list
                      v-list-tile(v-for='item in historyItemMenu', :key='item.title', @click='')
                        v-list-tile-title {{ item.title }}
              v-divider(v-if="index + 1 < queries.length" )

    v-bottom-nav(absolute="" :value="true" :active.sync="e1" color="transparent", style="bottom:30px")
      v-btn(flat="" color="teal" value="connections")
        v-icon cloud
      v-btn(flat="" color="teal" value="history")
        v-icon history
      v-btn(flat="" color="teal" value="favorites")
        v-icon favorite
  codemirror(
    ref="cm",
    v-model="code",
    :options="cmOptions"
  )
  //- @drag="onDrag" @dragstart="onDragStart"
  div#id-1.drag-handle(@mousedown="onDragStart")
  v-progress-linear(
    v-show="running"
    :indeterminate="true"
    height="2"
    :color="progressColor"
    style="margin:0;"
    background-color="white"
  )
  v-toolbar(dense draggable="false" style="z-index:1")
    v-btn(icon @click="console.content ='grid'")
      v-icon(color="light-blue darken-3" :style="'opacity:' + opacity('grid')") grid_on
    v-btn(icon @click="console.content ='logs'")
      v-icon(color="brown darken-1" :style="'opacity:' + opacity('logs')") bug_report
    v-btn(v-if="console.minified" icon @click="console.minified = false")
      v-icon keyboard_arrow_up
    v-btn(v-else icon @click="console.minified = true")
      v-icon keyboard_arrow_down
    v-spacer
    v-btn(icon @click="onPlay" draggable="false")
      v-icon(color="success") play_arrow
    v-btn(icon @click="onSaveFav" :disabled="!isSelected")
      v-icon(color="pink accent-1" draggable="false") favorite
    v-btn(icon @click="queryRightDrawer = !queryRightDrawer")
      v-icon(v-if="queryRightDrawer") keyboard_arrow_right
      v-icon(v-else) keyboard_arrow_left
  div.console
    div.welcome(v-show="!console.content")
      div.welcome-message
        v-icon(x-large color="grey lighten-1") filter_vintage
        h3 Welcome to query editor
    div.logs(v-show="console.content==='logs'" ref="consoleLogs")
      v-list(one-line="" dense)
        template(v-for="(item, index) in logs")
          v-list-tile.script-list-item(avatar="" ripple="" @click="")
            v-list-tile-action
              v-icon(:color="item.iconColor") {{item.iconName}}
            v-list-tile-content(@click="")
              v-list-tile-title {{ item.elapsed || dateFromNow(item.date) }}
              v-list-tile-sub-title {{ ellipsis(item.message, 100) }}
          v-divider(v-if="index + 1 < logs.length" )
</template>

<script>

import _ from 'lodash'
import moment from 'moment'
import {mapGetters, mapActions} from 'vuex'
import {codemirror} from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/mode/sql/sql.js'
import 'codemirror/addon/selection/active-line.js'
import 'codemirror/addon/hint/show-hint.js'
import 'codemirror/addon/hint/sql-hint.js'

export default {
  data () {
    return {
      historyItemMenu: [
        {
          title: 'Copy query to clipboard',
          id: 'COPY_QUERY_CLIP'
        }, {
          title: 'Copy query to editor',
          id: 'COPY_QUERY_EDITOR'
        }, {
          title: 'Download resultset',
          id: 'DOWNLOAD_RESULT_SET'
        }
      ],
      progressColor: 'primary',
      running: false,
      console: {
        minified: false,
        content: undefined
      },
      favFilter: {
        search: undefined
      },
      histFilter: {
        search: undefined
      },
      connectionFilter: {
        selectAll: undefined,
        search: undefined,
        labels: [],
        selected: {}
      },
      e1: 'connections',
      queryRightDrawer: false,
      drag: {
        ys: undefined, // y-start
        yc: undefined, // y-current
        ye: undefined, // y-end
        e: undefined,
        h: undefined,
        style: undefined,
        adjust (y) {
          if (y === 0 || y > 650) {
            return
          }
          this.yc = y
          this.h = this.h + (this.yc - this.ys)
          const h = this.h
          this.e.style.height = `${h}px`
          this.ys = this.yc
        }
      },
      code: undefined,
      cmOptions: {
        tabSize: 4,
        mode: 'text/x-mssql',
        lineNumbers: true,
        line: true,
        styleActiveLine: true,
        extraKeys: {'Ctrl-Space': 'autocomplete'}
      }
    }
  },
  computed: {
    ...mapGetters('labels', [
      'labels'
    ]),
    ...mapGetters('connections', [
      'connections'
    ]),
    ...mapGetters('scripts', [
      'scripts'
    ]),
    ...mapGetters('queries', [
      'logs',
      'queries'
    ]),
    favScripts () {
      if (_.isEmpty(this.favFilter.search)) {
        return this.scripts
      }
      return _.filter(this.scripts, s =>
        s.content.toLowerCase().indexOf(this.favFilter.search.toLowerCase()) > -1)
    },
    histQueries () {
      if (_.isEmpty(this.histFilter.search)) {
        return this.queries
      }
      return _.filter(this.queries, q =>
        q.query.toLowerCase().indexOf(this.histFilter.search.toLowerCase()) > -1)
    },
    editor () {
      return this.$refs.cm.codemirror
    },
    isSelected () {
      return true
    },
    cons () {
      const f = this.connectionFilter
      return _.filter(this.connections, connection => {
        if (!_.isEmpty(f.search) && connection.name.toLowerCase().indexOf(f.search.toLowerCase()) === -1) {
          return false
        }
        if (!_.isEmpty(f.labels) && _.chain(connection.labels).map('id').intersection(f.labels).isEmpty().value()) {
          return false
        }
        return true
      })
    }
  },
  watch: {
    code () {
    },
    'connectionFilter.selectAll' (v) {
      _.each(this.connections, c => { this.connectionFilter.selected[c.id] = v })
    }
  },
  methods: {
    ...mapActions('scripts', {
      saveToFav: 'create',
      removeFav: 'remove'
    }),
    ...mapActions('notifications', [
      'snackWarning'
    ]),
    ...mapActions('queries', [
      'log', 'logInfo', 'logError', 'logSuccess'
    ]),
    historyItemIcon (item) {
      const status = item.status || 'UNKNOWN'
      switch (status) {
        case 'UNKNOWN': return 'error_outline'
        case 'ERROR': return 'error'
        case 'SUCCESS': return 'done'
        case 'WARNING': return 'warning'
        case 'WAITING': return 'hourglass_empty'
        case 'RUNNING': return 'flash_on'
      }
    },
    historyItemIconColor (item) {
      const status = item.status || 'UNKNOWN'
      switch (status) {
        case 'UNKNOWN': return 'gray white--text'
        case 'ERROR': return 'red white--text'
        case 'SUCCESS': return 'green white--text'
        case 'WARNING': return 'amber darken-3 white--text'
        case 'WAITING': return 'amber white--text'
        case 'RUNNING': return 'blue white--text'
      }
    },
    opacity (item) {
      return this.console.content === item ? 1 : 0.4
    },
    onDragStart (e) {
      this.drag.ys = e.clientY
      const me = this
      function adjust (e) {
        me.drag.adjust(e.clientY)
      }
      document.addEventListener('mousemove', adjust)

      document.addEventListener('mouseup', e => {
        document.removeEventListener('mousemove', adjust)
      })
    },
    onActive (e) {
    },
    onPlay () {
      const query = this.editor.getValue()
      if (_.isEmpty(query)) {
        this.snackWarning('Nothing to execute')
        return
      }
      this.running = true
      const colors = ['primary', 'error', 'warning', 'success']
      const timer = setInterval(() => {
        this.progressColor = colors[_.random(4)]
      }, 500)
      this.logInfo('Started execution ...')
      setTimeout(() => {
        clearInterval(timer)
        this.running = false
      }, 10000)
    },
    onSaveFav () {
      const q = this.editor.getSelection()
      if (!_.isEmpty(q)) {
        this.saveToFav(q)
      } else {
        this.snackWarning('Select the script first')
      }
    },
    onLoadFav (s) {
      let v = this.editor.getValue()
      v = _.isEmpty(v) ? v : `${v}\n\n`
      this.editor.setValue(`${v}${s.content}`)
    },
    onDeleteFav (id) {
      this.removeFav(id)
    },
    dateFromNow (d) {
      return moment(d).fromNow()
    },
    unixFromNow (x) {
      if (x) {
        return moment.unix(x / 1000).fromNow()
      }
    },
    ellipsis (s, l) {
      return _.truncate(s, l || 15)
    }
  },
  components: {
    codemirror
  },
  mounted () {
    this.drag.e = document.getElementsByClassName('CodeMirror cm-s-default')[0]
    this.drag.style = window.getComputedStyle(this.drag.e)
    this.drag.h = parseInt(this.drag.style.height, 10)
  }
}
</script>

<style>
.drag-handle {
  cursor: ns-resize;
  height: 2px;
  background-color: transparent;
}

body {
  overflow-y: hidden !important;
}

.console {
  /* position: absolute; */
  height: 100%;
  width: 100%;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}

.query {
  flex-direction: column;
  height: 100%;
  width: 100%;
  margin-top: 2px;
  flex-grow: 1;
  display: flex;
}

.welcome {
  display: flex;
  flex-grow: 1;
}

.welcome-message {
  margin: auto;
  margin-top: 40px;
  display: flex;
  flex-direction: column;
}

.welcome-message > h3 {
  font-size: 20px;
  color: silver;
}

.history,
.scripts {
  /* height: 100%; */
  display: flex;
  overflow-y: auto;
  width: 100%;
}

.drawer-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 0px;
}

.logs,
.history-list-container,
.fav-list-container {
  bottom: 0px;
  display: flex;
  flex-direction: column;
  padding: 0px;
}

.logs ul,
.history-list-container ul,
.fav-list-container ul {
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.history-list-item .btn--icon,
.script-list-item .btn--icon {
  display: none;
}

.history-list-item:hover .btn--icon,
.script-list-item:hover .btn--icon {
  display:unset;
}

</style>

