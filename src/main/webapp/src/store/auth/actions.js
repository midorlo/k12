import { LocalStorage, SessionStorage } from 'quasar';

export function login(context, data) {
  context.commit('setAccount', data);
}

export function logout(context) {
  context.commit('setAccount', null);
  SessionStorage.remove('app-authenticationToken');
  LocalStorage.remove('app-authenticationToken');
}
