import MenuItem from 'app/shared/layout/menus/menu-item';
import React from 'react';
import { Translate } from 'react-jhipster';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/departamento">
        <Translate contentKey="global.menu.entities.departamento" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/voluntariado">
        <Translate contentKey="global.menu.entities.voluntariado" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/inscripciones">
        <Translate contentKey="global.menu.entities.inscripciones" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
