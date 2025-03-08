import dayjs from 'dayjs';
import { IDepartamento } from 'app/shared/model/departamento.model';

export interface IVoluntariado {
  id?: number;
  name?: string | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  departamento?: IDepartamento | null;
}

export const defaultValue: Readonly<IVoluntariado> = {};
