import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './voluntariado.reducer';

export const VoluntariadoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const voluntariadoEntity = useAppSelector(state => state.voluntariado.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="voluntariadoDetailsHeading">
          <Translate contentKey="osdamApp.voluntariado.detail.title">Voluntariado</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{voluntariadoEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="osdamApp.voluntariado.name">Name</Translate>
            </span>
          </dt>
          <dd>{voluntariadoEntity.name}</dd>
          <dt>
            <span id="fechaInicio">
              <Translate contentKey="osdamApp.voluntariado.fechaInicio">Fecha Inicio</Translate>
            </span>
          </dt>
          <dd>
            {voluntariadoEntity.fechaInicio ? (
              <TextFormat value={voluntariadoEntity.fechaInicio} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="fechaFin">
              <Translate contentKey="osdamApp.voluntariado.fechaFin">Fecha Fin</Translate>
            </span>
          </dt>
          <dd>
            {voluntariadoEntity.fechaFin ? <TextFormat value={voluntariadoEntity.fechaFin} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="osdamApp.voluntariado.departamento">Departamento</Translate>
          </dt>
          <dd>{voluntariadoEntity.departamento ? voluntariadoEntity.departamento.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/voluntariado" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/voluntariado/${voluntariadoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VoluntariadoDetail;
