import {
  PRIORITY_LABELS,
  SORT_LABELS,
  STATUS_LABELS,
  type Priority,
  type SortOrder,
  type TaskFilter,
  type TaskStatus,
} from '../../domain/task'

interface Props {
  filter: TaskFilter
  onChange: (filter: TaskFilter) => void
}

export function TaskFilters({ filter, onChange }: Props) {
  return (
    <div className="filters row">
      <select
        aria-label="Filtrar por status"
        value={filter.status ?? ''}
        onChange={(e) => onChange({ ...filter, status: (e.target.value || undefined) as TaskStatus | undefined })}
      >
        <option value="">Todos os status</option>
        {Object.entries(STATUS_LABELS).map(([value, label]) => (
          <option key={value} value={value}>
            {label}
          </option>
        ))}
      </select>
      <select
        aria-label="Filtrar por prioridade"
        value={filter.priority ?? ''}
        onChange={(e) => onChange({ ...filter, priority: (e.target.value || undefined) as Priority | undefined })}
      >
        <option value="">Todas as prioridades</option>
        {Object.entries(PRIORITY_LABELS).map(([value, label]) => (
          <option key={value} value={value}>
            {label}
          </option>
        ))}
      </select>
      <select
        aria-label="Ordenar"
        value={filter.sort}
        onChange={(e) => onChange({ ...filter, sort: e.target.value as SortOrder })}
      >
        {Object.entries(SORT_LABELS).map(([value, label]) => (
          <option key={value} value={value}>
            Ordenar por {label}
          </option>
        ))}
      </select>
      <label className="checkbox">
        <input
          type="checkbox"
          checked={filter.overdue}
          onChange={(e) => onChange({ ...filter, overdue: e.target.checked })}
        />
        Só atrasadas
      </label>
    </div>
  )
}
