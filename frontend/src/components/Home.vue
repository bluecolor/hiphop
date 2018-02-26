<template lang="pug">
v-container(grid-list-md text-xs-center)
  v-layout(row='' wrap='' mt-3='')
    v-flex(xs4='' pa-3)
      v-card(:class="c1" v-on:mouseover="onMouseOver(1)" v-on:mouseout="onMouseOut(1)")
        v-toolbar(class="red accent-2")
          v-icon(mr-3='' dark='') cloud
          v-card-title(class="white--text")
            h4 Connections
        v-divider
        v-list(dense="")
          v-list-tile
            v-list-tile-content Calories:
            v-list-tile-content.align-end Some dummy text
          v-list-tile
            v-list-tile-content Fat:
            v-list-tile-content.align-end Another text
        v-card-actions
          v-btn(block='' color="") Details
    v-flex(xs4='' pa-3)
      v-card(:class="c2" v-on:mouseover="onMouseOver(2)" v-on:mouseout="onMouseOut(2)")
        v-toolbar(class="blue darken-1")
          v-icon(mr-3='' dark='') storage
          v-card-title(class="white--text" )
            h4 Deoployments
        v-divider
        v-list(dense="")
          v-list-tile
            v-list-tile-content Calories:
            v-list-tile-content.align-end Some dummy text
          v-list-tile
            v-list-tile-content Fat:
            v-list-tile-content.align-end Another text
        v-card-actions
          v-btn(block='' color="") Details
    v-flex(xs4='' pa-3)
      v-card(:class="c3" v-on:mouseover="onMouseOver(3)" v-on:mouseout="onMouseOut(3)")
        v-toolbar(class="yellow accent-4")
          v-icon(mr-3='') perm_identity
          v-card-title
            h4 Users
        v-divider
        v-list(dense="")
          v-list-tile
            v-list-tile-content Calories:
            v-list-tile-content.align-end Some dummy text
          v-list-tile
            v-list-tile-content Fat:
            v-list-tile-content.align-end Another text
        v-card-actions
          v-btn(block='' color="") Details
</template>

<script>
export default {
  data () {
    return {
      c1: 'elevation-6',
      c2: 'elevation-6',
      c3: 'elevation-6'
    }
  },
  methods: {
    onMouseOver (n) {
      switch (n) {
        case 1: this.c1 = 'elevation-12'; break
        case 2: this.c2 = 'elevation-12'; break
        case 3: this.c3 = 'elevation-12'; break
      }
    },
    onMouseOut (n) {
      switch (n) {
        case 1: this.c1 = 'elevation-6'; break
        case 2: this.c2 = 'elevation-6'; break
        case 3: this.c3 = 'elevation-6'; break
      }
    }
  }
}
</script>

