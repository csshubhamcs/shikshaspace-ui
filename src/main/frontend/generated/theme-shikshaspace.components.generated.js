import { unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin/register-styles';

import topicCardCss from 'themes/shikshaspace/components/topic-card.css?inline';
import socialButtonsCss from 'themes/shikshaspace/components/social-buttons.css?inline';
import navbarCss from 'themes/shikshaspace/components/navbar.css?inline';
import homePageCss from 'themes/shikshaspace/components/home-page.css?inline';
import formInputsCss from 'themes/shikshaspace/components/form-inputs.css?inline';
import authLayoutCss from 'themes/shikshaspace/components/auth-layout.css?inline';
import _variablesCss from 'themes/shikshaspace/components/_variables.css?inline';
import _baseCss from 'themes/shikshaspace/components/_base.css?inline';


if (!document['_vaadintheme_shikshaspace_componentCss']) {
  registerStyles(
        'topic-card',
        unsafeCSS(topicCardCss.toString())
      );
      registerStyles(
        'social-buttons',
        unsafeCSS(socialButtonsCss.toString())
      );
      registerStyles(
        'navbar',
        unsafeCSS(navbarCss.toString())
      );
      registerStyles(
        'home-page',
        unsafeCSS(homePageCss.toString())
      );
      registerStyles(
        'form-inputs',
        unsafeCSS(formInputsCss.toString())
      );
      registerStyles(
        'auth-layout',
        unsafeCSS(authLayoutCss.toString())
      );
      registerStyles(
        '_variables',
        unsafeCSS(_variablesCss.toString())
      );
      registerStyles(
        '_base',
        unsafeCSS(_baseCss.toString())
      );
      
  document['_vaadintheme_shikshaspace_componentCss'] = true;
}

if (import.meta.hot) {
  import.meta.hot.accept((module) => {
    window.location.reload();
  });
}

