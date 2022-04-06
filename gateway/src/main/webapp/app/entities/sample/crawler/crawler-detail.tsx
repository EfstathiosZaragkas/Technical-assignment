import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './crawler.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CrawlerDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const crawlerEntity = useAppSelector(state => state.crawler.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="crawlerDetailsHeading">Crawler</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{crawlerEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{crawlerEntity.name}</dd>
          <dt>
            <span id="fetch">Fetch</span>
          </dt>
          <dd>{crawlerEntity.fetch}</dd>
          <dt>
            <span id="source">Source</span>
          </dt>
          <dd>{crawlerEntity.source}</dd>
        </dl>
        <Button tag={Link} to="/crawler" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/crawler/${crawlerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CrawlerDetail;
