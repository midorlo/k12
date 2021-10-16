/* eslint-env node */
// noinspection JSUnusedGlobalSymbols

const ESLintPlugin = require('eslint-webpack-plugin');
const MergeJsonWebpackPlugin = require('merge-jsons-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const {configure} = require('quasar/wrappers');

/**
 * Quasar Main Configuration
 * Note: Not transpiled by Babel
 */
module.exports = configure(function (ctx) {
  return {
    supportTS: false,
    boot: ['i18n', 'axios'],
    css: ['app.scss'],
    animations: 'all',
    extras: ['roboto-font', 'material-icons'],
    build: {
      publicPath: '/spa',
      vueRouterMode: 'history',
      env: {
        APP_VERSION: process.env.APP_VERSION ? `v${process.env.APP_VERSION}` : 'DEV',
        BUILD_TIME: new Date().getTime(),
      },
      chainWebpack(chain) {
        chain.plugin('merge-jsons-webpack-plugin').use(MergeJsonWebpackPlugin, [
          {
            output: {
              groupBy: [
                {pattern: './i18n/de/*.json', fileName: '../i18n/de.json'},
                {pattern: './i18n/en/*.json', fileName: '../i18n/en.json'},
                {pattern: './i18n/fr/*.json', fileName: '../i18n/fr.json'},
              ],
            },
          },
        ]);
        chain.plugin('eslint-webpack-plugin').use(ESLintPlugin, [
          {
            extensions: ['js', 'vue'],
          },
        ]);
        chain.plugin('copy-webpack-plugin').use(CopyWebpackPlugin, [
          {
            patterns: [
              {
                context: './node_modules/swagger-ui-dist/',
                from: '*.{js,css,html,png}',
                to: '../swagger-ui/',
                globOptions: {ignore: ['**/index.html']},
              },
              {from: './node_modules/axios/dist/axios.min.js', to: '../swagger-ui/'},
              {from: './swagger-ui/', to: '../swagger-ui/'},
            ],
          },
        ]);
      },
    },
    devServer: {
      https: false,
      port: 8100,
      open: true,
      proxy: {
        target: 'http://localhost:8080',
        context: [
          '/api',
          '/i18n',
          '/management',
          '/swagger-ui',
          '/swagger-resources',
          '/v3/api-docs',
          '/oauth2',
          '/login',
          '/auth'
        ],
      },
    },
    framework: {
      config: {
        dark: 'false',
      },
      components: [],
      directives: [],
      plugins: ['Cookies', 'Dialog', 'LocalStorage', 'Notify', 'SessionStorage'],
    },

    /*
     * Only Junk from where on.
     */

    ssr: {
      pwa: false,
      prodPort: 3000,
      maxAge: 1000 * 60 * 60 * 24 * 30,
      chainWebpackWebserver(chain) {
        chain.plugin('eslint-webpack-plugin').use(ESLintPlugin, [{extensions: ['js']}]);
      },
      middlewares: [
        ctx.prod ? 'compression' : '',
        'render',
      ],
    },
    pwa: {
      workboxPluginMode: 'GenerateSW',
      workboxOptions: {},
      chainWebpackCustomSW(chain) {
        chain.plugin('eslint-webpack-plugin').use(ESLintPlugin, [{extensions: ['js']}]);
      },
      manifest: {
        name: `K12`,
        short_name: `k12`,
        description: `k12 webapp`,
        display: 'standalone',
        orientation: 'portrait',
        background_color: '#ffffff',
        theme_color: '#027be3',
        icons: [
          {
            src: 'icons/icon-128x128.png',
            sizes: '128x128',
            type: 'image/png',
          },
          {
            src: 'icons/icon-192x192.png',
            sizes: '192x192',
            type: 'image/png',
          },
          {
            src: 'icons/icon-256x256.png',
            sizes: '256x256',
            type: 'image/png',
          },
          {
            src: 'icons/icon-384x384.png',
            sizes: '384x384',
            type: 'image/png',
          },
          {
            src: 'icons/icon-512x512.png',
            sizes: '512x512',
            type: 'image/png',
          },
        ],
      },
    },
    cordova: {},
    capacitor: {
      hideSplashscreen: true,
    },
    electron: {
      bundler: 'packager',
      packager: {},
      builder: {
        appId: 'quasar',
      },
      chainWebpackMain(chain) {
        chain.plugin('eslint-webpack-plugin').use(ESLintPlugin, [{extensions: ['js']}]);
      },
    },
  };
});
