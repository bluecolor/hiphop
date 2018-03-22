<template lang="pug">
  div
    //- v-navigation-drawer(mt-3 fixed :mini-variant='rd' right clipped app style="z-index:0;")
    //-   v-container
    //-     v-btn(icon @click="rd = !rd")
    //-       v-icon {{ rd ? 'chevron_left' : 'chevron_right'}}
    //-   v-divider
    log-shipping

</template>

<script>
import LogShipping from './LogShipping'

export default {
  data () {
    return {
      rd: true
    }
  },
  components: {
    'log-shipping': LogShipping
  }
}
</script>
