<template lang="pug">

div.query(xs12 mt-1 style="height:100%")
  v-navigation-drawer(mt-3 fixed v-model="queryRightDrawer" right="" clipped app style="z-index:0;")
    v-container.drawer-container

      v-layout(v-show="e1==='connections'" column style="display:flex;")
        v-container(style="padding:0px; max-height: 140px;")
          v-list-tile-content.top-filters(style="padding:0px;")
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
        v-container.connection-list-container(style="flex-direction:column")
          v-list-tile(style="list-style-type:none;")
            v-list-tile-content
              v-list-tile-title Connections
            v-list-tile-action
              v-checkbox(color="green darken-3" v-model="connectionFilter.selectAll")
          v-divider
          v-list.css-scrollbar(dense)
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
                      v-list-tile(v-for='i in historyItemMenu', :key='i.title' @click='onHistoryMenuItemClick(i.id, item.id)')
                        v-list-tile-title {{ i.title }}
              v-divider(v-if="index + 1 < queries.length")
      v-layout(v-show="e1==='exports'" column style="display:flex;")
        v-container(style="padding:0px; max-height: 70px;")
          v-list-tile-content.top-filters(style="padding:0px;")
            v-text-field(clearable v-model="exportFilter.search" append-icon="search" style="padding:10px 10px 0px 10px;" )
        v-container.history-list-container
          v-list(two-line="" dense)
            template(v-for="(item, index) in _exports")
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
                      v-list-tile(v-for='i in exportItemMenu', :key='i.title' @click='onExportMenuItemClick(i.id, item.id)')
                        v-list-tile-title {{ i.title }}
              v-divider(v-if="index + 1 < queries.length")

    v-bottom-nav(absolute="" :value="true" :active.sync="e1" color="transparent", style="bottom:30px")
      v-btn(flat="" color="teal" value="connections")
        v-icon cloud
      v-btn(flat="" color="teal" value="history")
        v-icon history
      v-btn(flat="" color="teal" value="exports")
        v-icon file_download
      v-btn(flat="" color="teal" value="favorites")
        v-icon favorite
  codemirror(
    ref="cm",
    v-model="code",
    :options="cmOptions"
  )
  div
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
        v-icon(color="orange" :style="'opacity:' + opacity('logs')") bug_report
      v-btn(v-show="queryId" icon @click="download")
        v-icon(color="green") file_download
      v-spacer
      .play-container(@mouseleave="onPlayOut")
        v-btn.palay-and-export(@click="onExport" fab dark small color="primary" :style="`visibility:${playMenuVis};`")
          v-icon(dark="") file_download
        v-btn.play(icon @click="onPlay" @mouseenter="onPlayOver")
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
    div.logs(v-show="console.content==='logs'")
      div.welcome(v-if="logs.length === 0")
        div.welcome-message
          v-icon(x-large color="grey lighten-1") filter_vintage
          h3 Welcome to query editor
      v-list(v-else one-line="" dense)
        template(v-for="(item, index) in logs")
          v-list-tile.script-list-item(avatar ripple @click="")
            v-list-tile-action
              v-icon(:color="item.iconColor") {{item.iconName}}
            v-list-tile-content(@click="")
              v-list-tile-title {{ item.elapsed || dateFromNow(item.date) }}
              v-list-tile-sub-title {{ ellipsis(item.message, 100) }}
            v-list-tile-action
              v-btn(icon @click="onCopyLog(item)")
                v-icon(color="grey") content_copy
          v-divider(v-if="index + 1 < logs.length" )
    div.query-result(v-show="console.content==='grid'")
      div.welcome(v-if="resultConnections.length === 0")
        div.welcome-message
          v-icon(x-large color="grey lighten-1") filter_vintage
          h3 Welcome to query editor
      v-expansion-panel(v-else expand)
        v-expansion-panel-content.elevation-1(v-for="(r,i) in results" :key="r.id" :value="i === 1")
          div.query-result(slot="header") {{r.order.connection.name}}
          v-data-table(:headers="headers(r)" :items="tableData(r)" hide-actions="")
            template(slot="items" slot-scope="props")
              td.text-xs-left(v-for="(h, i) in headers(r)") {{ props.item[h.value] }}

</template>

<script>

import _ from 'lodash'
import moment from 'moment'
import {mapGetters, mapActions} from 'vuex'
import {codemirror, CodeMirror} from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/mode/sql/sql.js'
import 'codemirror/addon/selection/active-line.js'
import 'codemirror/addon/hint/show-hint.js'
import 'codemirror/addon/hint/sql-hint.js'
// import 'codemirror/addon/hint/anyword-hint.js'

export default {
  data () {
    return {
      historyItemMenu: [{
        title: 'Copy query to clipboard',
        id: 'COPY_CLIP'
      }, {
        title: 'Copy query to editor',
        id: 'COPY_EDITOR'
      }, {
        title: 'Export',
        id: 'EXPORT'
      }],
      exportItemMenu: [{
        title: 'Copy query to clipboard',
        id: 'COPY_CLIP'
      }, {
        title: 'Copy query to editor',
        id: 'COPY_EDITOR'
      }, {
        title: 'Download',
        id: 'DOWNLOAD'
      }],
      displayPlayMenu: false,
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
      exportFilter: {
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
        extraKeys: {'Ctrl-Space': 'autocomplete', 'Ctrl-Enter': 'play'}
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
      'allQueries',
      'queries',
      'exports',
      'result',
      'isRunning'
    ]),
    playMenuVis () {
      return this.displayPlayMenu ? 'visible' : 'hidden'
    },
    queryId () {
      if (this.result && this.result.query) {
        return this.result.query.id
      }
    },
    results () {
      if (!this.result) {
        return []
      }
      return _.orderBy(this.result.results, (r) => r.order.connection.name)
    },
    resultConnections () {
      if (!this.result) {
        return []
      }
      return this.result.query.connections
    },
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
    _exports () {
      if (_.isEmpty(this.exportFilter.search)) {
        return this.exports
      }
      return _.filter(this.exports, q =>
        q.query.toLowerCase().indexOf(this.exportFilter.search.toLowerCase()) > -1)
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
      'snackWarning', 'snackInfo', 'snackSuccess'
    ]),
    ...mapActions('queries', [
      'query',
      'log', 'logInfo', 'logError', 'logSuccess'
    ]),
    headers (result) {
      if (_.isEmpty(result)) { return [] }
      return _.map(result.columns, column => {
        return { text: column.columnLabel, value: column.columnLabel }
      })
    },
    tableData (result) {
      const columns = result.columns
      return _.map(result.data, record => {
        return _.reduce(record, (acc, item, index) => {
          acc[columns[index].columnLabel] = item
          return acc
        }, {})
      })
    },
    onPlayOver () {
      this.displayPlayMenu = true
    },
    onPlayOut () {
      this.displayPlayMenu = false
    },
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
      if (this.isRunning) {
        this.snackWarning('Wait for the current query to finish!')
      }

      const colors = ['primary', 'error', 'warning', 'success']
      const query = this.editor.getValue()
      if (_.isEmpty(query)) {
        this.snackWarning('Nothing to execute')
        return
      }
      const s = this.connectionFilter.selected
      const connections = _.chain(s).keys().filter(k => s[k]).map(c => parseInt(c)).value()

      if (_.isEmpty(connections)) {
        this.snackWarning('You must select at least one connection')
        return
      }
      const timer = setInterval(() => {
        this.progressColor = colors[_.random(4)]
      }, 500)
      this.logInfo(`Executing query: [ ${query} ] on ${connections.length} connection${connections.length > 1 ? 's' : ''}`)
      this.running = true
      this.query({connections, query}).then(q => {
        this.snackSuccess('Query finished')
        this.logSuccess(`Finished query: [ ${query} ] on ${connections.length} connection${connections.length > 1 ? 's' : ''}`)
      }).catch(e => {
        this.logError(e)
      }).finally(() => {
        clearInterval(timer)
        this.running = false
        this.console.content = 'grid'
      })
    },
    onExport (q) {
      const query = q || this.editor.getValue()
      if (_.isEmpty(query)) {
        this.snackWarning('Nothing to export')
        return
      }
      const s = this.connectionFilter.selected
      const connections = _.chain(s).keys().filter(k => s[k]).map(c => parseInt(c)).value()
      if (_.isEmpty(connections)) {
        this.snackWarning('You must select at least one connection')
        return
      }
      this.query({connections, query, export: true}).then(q => {
        this.logSuccess(`Export request sent for query: [ ${query} ] on ${connections.length} connection${connections.length > 1 ? 's' : ''}`)
      }).then(q => {
        this.snackSuccess('Export started')
        this.logSuccess(`Finished query: [ ${query} ] on ${connections.length} connection${connections.length > 1 ? 's' : ''}`)
      }).catch(e => {
        this.logError(e)
      })
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
      return _.truncate(s, {length: l || 30})
    },
    onHistoryMenuItemClick (m, i) {
      switch (m) {
        case 'COPY_CLIP': this.onCopyQueryClip(i); return
        case 'COPY_EDITOR': this.onCopyQueryEditor(i); return
      }
    },
    onExportMenuItemClick (m, id) {
      switch (m) {
        case 'COPY_CLIP': this.onCopyQueryClip(id); return
        case 'COPY_EDITOR': this.onCopyQueryEditor(id); return
        case 'DOWNLOAD': window.location = `/api/v1/queries/download/${id}`
      }
    },
    onCopyQueryClip (id) {
      const query = _.find(this.allQueries, {id}).query
      let inp = document.createElement('input')
      document.body.appendChild(inp)
      inp.value = query
      inp.select()
      document.execCommand('copy', false)
      inp.remove()
      this.snackInfo('Copied query')
    },
    onCopyQueryEditor (id) {
      const query = _.find(this.allQueries, {id}).query
      this.editor.setValue(query)
    },
    onCopyLog (log) {
      const message = log.message
      let inp = document.createElement('input')
      document.body.appendChild(inp)
      inp.value = message
      inp.select()
      document.execCommand('copy', false)
      inp.remove()
      this.snackInfo('Copied log message')
    },
    download () {
      if (this.result.query.query) {
        this.onExport(this.result.query.query)
      } else {
        this.snackWarning('Unable to get query')
      }
    }
  },
  components: {
    codemirror
  },
  mounted () {
    this.drag.e = document.getElementsByClassName('CodeMirror cm-s-default')[0]
    this.drag.style = window.getComputedStyle(this.drag.e)
    this.drag.h = parseInt(this.drag.style.height, 10)

    CodeMirror.commands.play = (cm) => {
      this.onPlay()
    }
  }
}
</script>

<style>
.drag-handle {
  cursor: ns-resize;
  height: 2px;
  background-color: transparent;
}

.drag-handle:hover {
  background-color:rgba(221, 183, 183, 0.514);
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

.query-result {
  font-weight: 900;
}

.query-result ul.expansion-panel,
.query-result .expansion-panel__body > div .table__overflow {
  overflow-y: auto;
  overflow-x: auto;
}

.query-result > div,
.drawer-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 0px;
}

.query-result,
.logs,
.history-list-container,
.fav-list-container,
.connection-list-container {
  bottom: 0px;
  display: flex;
  flex-direction: column;
  padding: 0px;
}

.connection-list-container ul,
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

.play-container {
  display: flex;
  flex-direction: column;
  padding-bottom: 41px;
}


.palay-and-export {
  visibility: hidden;
  width: 0px;
  height: 0px;
}

.play-container .btn--floating.btn--small {
  height: 30px;
  width: 30px;
}


</style>

