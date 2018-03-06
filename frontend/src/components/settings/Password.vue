<template lang="pug">
    v-flex(mt-5)
      v-card.elevation-5
        v-toolbar(color="white")
          v-btn(icon="" @click="onBack")
            v-icon arrow_back
          v-toolbar-title Change password
        v-card-text
          v-form
            v-text-field(
              label='Current password',
              v-model='p.current',
              :rules="[rules.required]"
              hint="At least 8 characters"
              min="8"
              :append-icon="e1 ? 'visibility' : 'visibility_off'"
              :append-icon-cb="() => (e1 = !e1)"
              :type="e1 ? 'password' : 'text'"
              counter
            )
            v-text-field(
              label='New password',
              v-model='p.new',
              :rules="[rules.required]"
              hint="At least 8 characters"
              min="8"
              :append-icon="e1 ? 'visibility' : 'visibility_off'"
              :append-icon-cb="() => (e1 = !e1)"
              :type="e1 ? 'password' : 'text'"
              counter
            )
            v-text-field(
              label='Re new password',
              v-model='p.reNew',
              :rules="[rules.required]"
              hint="At least 8 characters"
              min="8"
              :append-icon="e1 ? 'visibility' : 'visibility_off'"
              :append-icon-cb="() => (e1 = !e1)"
              :type="e1 ? 'password' : 'text'"
              counter
            )
        v-card-actions
          v-spacer
          v-btn(flat @click="onSave") Save
</template>

<script>
import {mapActions} from 'vuex'

export default {
  data () {
    return {
      e1: false,
      rules: {
        required: (value) => !!value || ''
      },
      p: {
        current: undefined,
        new: undefined,
        reNew: undefined
      }
    }
  },
  methods: {
    ...mapActions('users', [
      'changePassword'
    ]),
    onBack () {
      this.$emit('close')
    },
    onSave () {
      const payload = {
        currentPassword: this.p.current,
        newPassword: this.p.new
      }
      this.changePassword(payload)
    }
  }
}
</script>

