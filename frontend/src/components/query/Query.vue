<template lang="pug">

//- div(style="width:100%;")
v-flex(xs12 mt-1)
  codemirror(
    ref="cm",
    v-model="code",
    :options="cmOptions"
  )
  v-divider.drag-handle(@drag="onDrag" @dragstart="onDragStart")
  //- v-container.divider.drag-handle(@drag="onDrag" @dragstart="onDragStart")
  v-toolbar(dense)
    v-spacer
    v-btn(icon)
      v-icon(color="success") play_arrow
    v-btn(icon)
      v-icon(color="pink accent-1") favorite
    v-btn(icon @click="toggleQueryRightDrawer")
      v-icon(v-if="queryRightDrawer") keyboard_arrow_right
      v-icon(v-else) keyboard_arrow_left

</template>

<script>

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
          console.log(this.ys, this.yc, this.h)
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
    ...mapGetters('ui', [
      'queryRightDrawer'
    ]),
    editor () {
      return this.$refs.cm.editor
    }
  },
  watch: {
    code () {
      console.log(this.code)
    }
  },
  methods: {
    ...mapActions('ui', [
      'toggleQueryRightDrawer'
    ]),
    onDrag (e) {
      this.drag.adjust(e.clientY)
    },
    onDragStart (e) {
      this.drag.ys = e.clientY
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
}
</style>

