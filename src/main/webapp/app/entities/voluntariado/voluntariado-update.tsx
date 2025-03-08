import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getDepartamentos } from 'app/entities/departamento/departamento.reducer';
import { createEntity, getEntity, reset, updateEntity } from './voluntariado.reducer';

export const VoluntariadoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const departamentos = useAppSelector(state => state.departamento.entities);
  const voluntariadoEntity = useAppSelector(state => state.voluntariado.entity);
  const loading = useAppSelector(state => state.voluntariado.loading);
  const updating = useAppSelector(state => state.voluntariado.updating);
  const updateSuccess = useAppSelector(state => state.voluntariado.updateSuccess);

  const handleClose = () => {
    navigate(`/voluntariado${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDepartamentos({}));
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
    values.fechaInicio = convertDateTimeToServer(values.fechaInicio);
    values.fechaFin = convertDateTimeToServer(values.fechaFin);

    const entity = {
      ...voluntariadoEntity,
      ...values,
      departamento: departamentos.find(it => it.id.toString() === values.departamento?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          fechaInicio: displayDefaultDateTime(),
          fechaFin: displayDefaultDateTime(),
        }
      : {
          ...voluntariadoEntity,
          fechaInicio: convertDateTimeFromServer(voluntariadoEntity.fechaInicio),
          fechaFin: convertDateTimeFromServer(voluntariadoEntity.fechaFin),
          departamento: voluntariadoEntity?.departamento?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="osdamApp.voluntariado.home.createOrEditLabel" data-cy="VoluntariadoCreateUpdateHeading">
            <Translate contentKey="osdamApp.voluntariado.home.createOrEditLabel">Create or edit a Voluntariado</Translate>
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
                  id="voluntariado-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('osdamApp.voluntariado.name')}
                id="voluntariado-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('osdamApp.voluntariado.fechaInicio')}
                id="voluntariado-fechaInicio"
                name="fechaInicio"
                data-cy="fechaInicio"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('osdamApp.voluntariado.fechaFin')}
                id="voluntariado-fechaFin"
                name="fechaFin"
                data-cy="fechaFin"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="voluntariado-departamento"
                name="departamento"
                data-cy="departamento"
                label={translate('osdamApp.voluntariado.departamento')}
                type="select"
              >
                <option value="" key="0" />
                {departamentos
                  ? departamentos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/voluntariado" replace color="info">
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

export default VoluntariadoUpdate;
