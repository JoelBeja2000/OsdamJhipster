import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Inscripciones from './inscripciones';
import InscripcionesDetail from './inscripciones-detail';
import InscripcionesUpdate from './inscripciones-update';
import InscripcionesDeleteDialog from './inscripciones-delete-dialog';

const InscripcionesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Inscripciones />} />
    <Route path="new" element={<InscripcionesUpdate />} />
    <Route path=":id">
      <Route index element={<InscripcionesDetail />} />
      <Route path="edit" element={<InscripcionesUpdate />} />
      <Route path="delete" element={<InscripcionesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InscripcionesRoutes;
