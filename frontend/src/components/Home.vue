<template lang="pug">
v-container(grid-list-md text-xs-center style="margin-top:10px;")
  v-layout(row='' wrap='' mt-3='')
</template>

<script>

import {mapGetters} from 'vuex'

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
  },
  computed: {
    ...mapGetters('connections', [
      'connections'
    ]),
    ...mapGetters('labels', [
      'labels'
    ])
  }
}
</script>

