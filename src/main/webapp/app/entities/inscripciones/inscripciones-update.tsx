import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getVoluntariados } from 'app/entities/voluntariado/voluntariado.reducer';
import { createEntity, getEntity, reset, updateEntity } from './inscripciones.reducer';

export const InscripcionesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const voluntariados = useAppSelector(state => state.voluntariado.entities);
  const inscripcionesEntity = useAppSelector(state => state.inscripciones.entity);
  const loading = useAppSelector(state => state.inscripciones.loading);
  const updating = useAppSelector(state => state.inscripciones.updating);
  const updateSuccess = useAppSelector(state => state.inscripciones.updateSuccess);

  const handleClose = () => {
    navigate(`/inscripciones${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getVoluntariados({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...inscripcionesEntity,
      ...values,
      voluntariado: voluntariados.find(it => it.id.toString() === values.voluntariado?.toString()),
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
          ...inscripcionesEntity,
          voluntariado: inscripcionesEntity?.voluntariado?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="osdamApp.inscripciones.home.createOrEditLabel" data-cy="InscripcionesCreateUpdateHeading">
            <Translate contentKey="osdamApp.inscripciones.home.createOrEditLabel">Create or edit a Inscripciones</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="inscripciones-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('osdamApp.inscripciones.name')}
                id="inscripciones-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                id="inscripciones-voluntariado"
                name="voluntariado"
                data-cy="voluntariado"
                label={translate('osdamApp.inscripciones.voluntariado')}
                type="select"
              >
                <option value="" key="0" />
                {voluntariados
                  ? voluntariados.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/inscripciones" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default InscripcionesUpdate;
