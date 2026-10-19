import { useState } from 'react'
import {
  ACTION_LABELS,
  PRIORITY_LABELS,
  STATUS_LABELS,
  availableActions,
  type Task,
  type TaskAction,
  type TaskInput,
} from '../../domain/task'
import { TaskForm } from './TaskForm'

interface Props {
  task: Task
  onTransition: (id: string, action: TaskAction) => Promise<void>
  onUpdate: (id: string, input: TaskInput) => Promise<void>
  onRemove: (id: string) => Promise<void>
}

export function TaskItem({ task, onTransition, onUpdate, onRemove }: Props) {
  const [editing, setEditing] = useState(false)

  if (editing) {
    return (
      <li className="task">
        <TaskForm
          initial={{ title: task.title, description: task.description, priority: task.priority, dueDate: task.dueDate }}
          submitLabel="Salvar"
          onSubmit={async (input) => {
            await onUpdate(task.id, input)
            setEditing(false)
          }}
          onCancel={() => setEditing(false)}
        />
      </li>
    )
  }

  return (
    <li className={`task status-${task.status.toLowerCase()}`}>
      <div className="task-header">
        <strong>{task.title}</strong>
        <span className={`badge priority-${task.priority.toLowerCase()}`}>{PRIORITY_LABELS[task.priority]}</span>
        <span className="badge">{STATUS_LABELS[task.status]}</span>
        {task.overdue && <span className="badge overdue">Atrasada</span>}
      </div>
      {task.description && <p>{task.description}</p>}
      {task.dueDate && <small>Prazo: {formatDate(task.dueDate)}</small>}
      <div className="row actions">
        {availableActions(task.status).map((action) => (
          <button key={action} onClick={() => onTransition(task.id, action)}>
            {ACTION_LABELS[action]}
          </button>
        ))}
        <button className="secondary" onClick={() => setEditing(true)}>
          Editar
        </button>
        <button className="danger" onClick={() => onRemove(task.id)}>
          Excluir
        </button>
      </div>
    </li>
  )
}

function formatDate(isoDate: string): string {
  const [year, month, day] = isoDate.split('-')
  return `${day}/${month}/${year}`
}
