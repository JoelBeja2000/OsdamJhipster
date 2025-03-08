import React from 'react';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Departamento from './departamento';
import Voluntariado from './voluntariado';
import Inscripciones from './inscripciones';
import { Route } from 'react-router';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="departamento/*" element={<Departamento />} />
        <Route path="voluntariado/*" element={<Voluntariado />} />
        <Route path="inscripciones/*" element={<Inscripciones />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
