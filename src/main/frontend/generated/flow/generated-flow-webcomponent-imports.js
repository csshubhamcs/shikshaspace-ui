import { injectGlobalWebcomponentCss } from 'Frontend/generated/jar-resources/theme-util.js';

import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/text-field/theme/lumo/vaadin-text-field.js';
import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import '@vaadin/password-field/theme/lumo/vaadin-password-field.js';
import '@vaadin/button/theme/lumo/vaadin-button.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/icon/theme/lumo/vaadin-icon.js';
import '@vaadin/notification/theme/lumo/vaadin-notification.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === 'c821bb951cc29e93653694506fae4c789f207b29dfbe1b96c8d0b7f57d8bae60') {
    pending.push(import('./chunks/chunk-6f5d53304429630134f1264ef749ee3dddcf1d76baba2e8c9219e95fdf4a19f1.js'));
  }
  if (key === 'f68cc5759feaf39be7ecb21e5d866b6d9597c70b1f3b36570793189c51931b64') {
    pending.push(import('./chunks/chunk-c4ab35363bafc50f5a2ba3e1d9483b4b2707b3ece87da33db34247718be3b621.js'));
  }
  if (key === 'cc59621c5d2431a0883dd9844295b25ad74bf2ee42da80273ab7cea6148fb08b') {
    pending.push(import('./chunks/chunk-6f5d53304429630134f1264ef749ee3dddcf1d76baba2e8c9219e95fdf4a19f1.js'));
  }
  if (key === '94d14820ea6f1274610bbd5ce6422cbb7cffd689303133414ff95e00c4163737') {
    pending.push(import('./chunks/chunk-6f5d53304429630134f1264ef749ee3dddcf1d76baba2e8c9219e95fdf4a19f1.js'));
  }
  if (key === 'b8090334c38146b0b8bc46ef33ecdbcbdffd5b46bd172dd9015ddd39c2497bf5') {
    pending.push(import('./chunks/chunk-4a6c73c28087746822ff59ec4d0cdc0ce8472c8439f53bd430c23b23239ce5ce.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}