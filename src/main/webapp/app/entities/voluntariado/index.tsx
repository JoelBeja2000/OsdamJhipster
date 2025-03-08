import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Voluntariado from './voluntariado';
import VoluntariadoDetail from './voluntariado-detail';
import VoluntariadoUpdate from './voluntariado-update';
import VoluntariadoDeleteDialog from './voluntariado-delete-dialog';

const VoluntariadoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Voluntariado />} />
    <Route path="new" element={<VoluntariadoUpdate />} />
    <Route path=":id">
      <Route index element={<VoluntariadoDetail />} />
      <Route path="edit" element={<VoluntariadoUpdate />} />
      <Route path="delete" element={<VoluntariadoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VoluntariadoRoutes;
