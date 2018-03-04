<template lang="pug">

//- div(style="width:100%;")
v-flex(xs12 mt-1)
  v-navigation-drawer(mt-3 fixed v-model="queryRightDrawer" right="" clipped app style="z-index:0;")
    v-list(v-show="e1==='connections'" dense="")
      v-list-tile-content.top-filters
        v-text-field(clearable append-icon="search" style="padding:10px 10px 0px 10px;" )
        v-select(
          clearable
          style="padding:0px 10px 0px 10px;"
          :items="labels"
          item-text="name"
          item-value="id"
          multiple
          chips
          max-height="auto"
          autocomplete
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
          v-checkbox(color="green darken-3" v-model="selectAllConnections")
      v-divider
      v-list-tile(v-for="(c,i) in connections" @click="" ripple)
        v-list-tile-content
          v-list-tile-title {{c.name}}
        v-list-tile-action
          v-checkbox(color="success" v-model="sc[i]")

    v-bottom-nav(absolute="" :value="true" :active.sync="e1" color="transparent", style="bottom:30px")
      v-btn(flat="" color="teal" value="connections")
        v-icon cloud
      v-btn(flat="" color="teal" value="jobs")
        v-icon history
      v-btn(flat="" color="teal" value="favorites")
        v-icon favorite
  codemirror(
    ref="cm",
    v-model="code",
    :options="cmOptions"
  )
  v-divider#id-1.drag-handle(@drag="onDrag" @dragstart="onDragStart" @active="onActive")
  v-progress-linear(
    v-show="running"
    :indeterminate="true"
    height="2"
    :color="progressColor"
    style="margin:0;"
    background-color="white"
  )
  v-toolbar(dense draggable="false")
    v-btn(icon @click="console ='grid'")
      v-icon(color="light-blue darken-3" :style="'opacity:' + opacity('grid')") grid_on
    v-btn(icon @click="console ='logs'")
      v-icon(color="brown darken-1" :style="'opacity:' + opacity('logs')") bug_report
    v-spacer
    v-btn(icon @click="onPlay" draggable="false")
      v-icon(color="success") play_arrow
    v-btn(icon)
      v-icon(color="pink accent-1" draggable="false") favorite
    v-btn(icon @click="queryRightDrawer = !queryRightDrawer")
      v-icon(v-if="queryRightDrawer") keyboard_arrow_right
      v-icon(v-else) keyboard_arrow_left

</template>

<script>

import _ from 'lodash'
import {mapGetters} from 'vuex'
import {codemirror} from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/mode/sql/sql.js'
import 'codemirror/addon/selection/active-line.js'
import 'codemirror/addon/hint/show-hint.js'
import 'codemirror/addon/hint/sql-hint.js'

export default {
  data () {
    return {
      progressColor: 'primary',
      running: false,
      console: 'grid',
      selectAllConnections: undefined,
      sc: [],
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
          if (y === 0) {
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
    editor () {
      return this.$refs.cm.codemirror
    }
  },
  watch: {
    code () {
    }
  },
  methods: {
    preventDefault (e) {
      e.preventDefault()
    },
    opacity (item) {
      return this.console === item ? 1 : 0.4
    },
    onDrag (e) {
      this.drag.adjust(e.clientY)
    },
    onDragStart (e) {
      this.drag.ys = e.clientY
    },
    onActive (e) {
    },
    onPlay () {
      this.running = true
      const colors = ['primary', 'error', 'warning', 'success']

      const timer = setInterval(() => {
        this.progressColor = colors[_.random(4)]
      }, 500)

      setTimeout(() => {
        clearInterval(timer)
        this.running = false
      }, 10000)
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

<style scoped>
.drag-handle {
  cursor: ns-resize;
  height: 2px;
  background-color: transparent;
}

/* .top-filters input {
  padding-left: 10px;
} */

</style>

