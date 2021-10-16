## Localization

In `src/main/webapp/quasar.conf:`


    chainWebpack(chain) { chain.plugin('merge-jsons-webpack-plugin').use(MergeJsonWebpackPlugin, [{ output: { groupBy: [
                {pattern: './i18n/de/*.json', fileName: '../i18n/de.json'},
                {pattern: './i18n/en/*.json', fileName: '../i18n/en.json'},
                {pattern: './i18n/fr/*.json', fileName: '../i18n/fr.json'}]}}]);
