import { injectGlobalWebcomponentCss } from 'Frontend/generated/jar-resources/theme-util.js';

import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/app-layout/theme/lumo/vaadin-app-layout.js';
import '@vaadin/app-layout/theme/lumo/vaadin-drawer-toggle.js';
import '@vaadin/button/theme/lumo/vaadin-button.js';
import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import '@vaadin/horizontal-layout/theme/lumo/vaadin-horizontal-layout.js';
import '@vaadin/side-nav/theme/lumo/vaadin-side-nav.js';
import '@vaadin/side-nav/theme/lumo/vaadin-side-nav-item.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/icon/theme/lumo/vaadin-icon.js';
import '@vaadin/avatar/theme/lumo/vaadin-avatar.js';
import '@vaadin/context-menu/theme/lumo/vaadin-context-menu.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import 'Frontend/generated/jar-resources/contextMenuConnector.js';
import 'Frontend/generated/jar-resources/contextMenuTargetConnector.js';
import '@vaadin/text-field/theme/lumo/vaadin-text-field.js';
import '@vaadin/password-field/theme/lumo/vaadin-password-field.js';
import '@vaadin/notification/theme/lumo/vaadin-notification.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === 'b8090334c38146b0b8bc46ef33ecdbcbdffd5b46bd172dd9015ddd39c2497bf5') {
    pending.push(import('./chunks/chunk-285f9864ce9d4540a32d4c838917cd86f09714a4af06c53570a6fa67b4d7eb97.js'));
  }
  if (key === 'cc59621c5d2431a0883dd9844295b25ad74bf2ee42da80273ab7cea6148fb08b') {
    pending.push(import('./chunks/chunk-f54eca75d1498f225d8839d5b97f91494ff78c9e501fd306078a3c2ed4855092.js'));
  }
  if (key === 'c821bb951cc29e93653694506fae4c789f207b29dfbe1b96c8d0b7f57d8bae60') {
    pending.push(import('./chunks/chunk-f54eca75d1498f225d8839d5b97f91494ff78c9e501fd306078a3c2ed4855092.js'));
  }
  if (key === 'f68cc5759feaf39be7ecb21e5d866b6d9597c70b1f3b36570793189c51931b64') {
    pending.push(import('./chunks/chunk-7a892da6bdd8e8d7234715174de8fef8a9561d7a9b67db995c852460393ea995.js'));
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