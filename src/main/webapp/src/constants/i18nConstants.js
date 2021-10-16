/**
 * THIS FILE IS HIGHLY MODIFIED BY THE BLUEPRINT
 * DO NOT EDIT.
 *
 **/
export const languages = { en: 'English', fr: 'Français' };

export const langObjects = Object.entries(languages).map(entry => {
  return { key: entry[0], value: entry[1] };
});

export const datefnsMapping = { en: 'enUS' };

export const quasarLangMapping = { en: 'en-US' };

export const importLocale = () => {
  switch (window.__localeId__) {
    case 'enUS':
      return require('date-fns/locale/en-US');
    case 'fr':
      return require('date-fns/locale/fr');
    default:
      return require('date-fns/locale/en-US');
  }
};
