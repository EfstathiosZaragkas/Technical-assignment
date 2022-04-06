import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Filters from './filters';
import FiltersDetail from './filters-detail';
import FiltersUpdate from './filters-update';
import FiltersDeleteDialog from './filters-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FiltersUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FiltersUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FiltersDetail} />
      <ErrorBoundaryRoute path={match.url} component={Filters} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FiltersDeleteDialog} />
  </>
);

export default Routes;
