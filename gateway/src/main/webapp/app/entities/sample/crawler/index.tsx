import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Crawler from './crawler';
import CrawlerDetail from './crawler-detail';
import CrawlerUpdate from './crawler-update';
import CrawlerDeleteDialog from './crawler-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CrawlerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CrawlerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CrawlerDetail} />
      <ErrorBoundaryRoute path={match.url} component={Crawler} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CrawlerDeleteDialog} />
  </>
);

export default Routes;
