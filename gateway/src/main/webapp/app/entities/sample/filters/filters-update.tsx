import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICrawler } from 'app/shared/model/sample/crawler.model';
import { getEntities as getCrawlers } from 'app/entities/sample/crawler/crawler.reducer';
import { getEntity, updateEntity, createEntity, reset } from './filters.reducer';
import { IFilters } from 'app/shared/model/sample/filters.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FiltersUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const crawlers = useAppSelector(state => state.crawler.entities);
  const filtersEntity = useAppSelector(state => state.filters.entity);
  const loading = useAppSelector(state => state.filters.loading);
  const updating = useAppSelector(state => state.filters.updating);
  const updateSuccess = useAppSelector(state => state.filters.updateSuccess);
  const handleClose = () => {
    props.history.push('/filters');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCrawlers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...filtersEntity,
      ...values,
      crawler: crawlers.find(it => it.id.toString() === values.crawler.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...filtersEntity,
          crawler: filtersEntity?.crawler?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.sampleFilters.home.createOrEditLabel" data-cy="FiltersCreateUpdateHeading">
            Create or edit a Filters
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="filters-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField id="filters-crawler" name="crawler" data-cy="crawler" label="Crawler" type="select">
                <option value="" key="0" />
                {crawlers
                  ? crawlers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/filters" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FiltersUpdate;
