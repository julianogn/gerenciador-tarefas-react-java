import { useState, type FormEvent } from 'react'
import { PRIORITY_LABELS, validateTaskInput, type Priority, type TaskInput } from '../../domain/task'

const EMPTY_INPUT: TaskInput = { title: '', description: '', priority: 'MEDIUM', dueDate: null }

interface Props {
  initial?: TaskInput
  submitLabel: string
  onSubmit: (input: TaskInput) => Promise<void>
  onCancel?: () => void
}

export function TaskForm({ initial = EMPTY_INPUT, submitLabel, onSubmit, onCancel }: Props) {
  const [input, setInput] = useState<TaskInput>(initial)
  const [error, setError] = useState<string | null>(null)

  const change = <K extends keyof TaskInput>(field: K, value: TaskInput[K]) =>
    setInput((current) => ({ ...current, [field]: value }))

  async function handleSubmit(event: FormEvent) {
    event.preventDefault()
    const validationError = validateTaskInput(input)
    setError(validationError)
    if (validationError) return
    await onSubmit({ ...input, title: input.title.trim() })
    if (!onCancel) setInput(EMPTY_INPUT)
  }

  return (
    <form className="task-form" onSubmit={handleSubmit}>
      <input
        aria-label="Título"
        placeholder="O que precisa ser feito?"
        value={input.title}
        onChange={(e) => change('title', e.target.value)}
      />
      <textarea
        aria-label="Descrição"
        placeholder="Descrição (opcional)"
        value={input.description}
        onChange={(e) => change('description', e.target.value)}
      />
      <div className="row">
        <select
          aria-label="Prioridade"
          value={input.priority}
          onChange={(e) => change('priority', e.target.value as Priority)}
        >
          {Object.entries(PRIORITY_LABELS).map(([value, label]) => (
            <option key={value} value={value}>
              Prioridade {label}
            </option>
          ))}
        </select>
        <input
          aria-label="Prazo"
          type="date"
          value={input.dueDate ?? ''}
          onChange={(e) => change('dueDate', e.target.value || null)}
        />
        <button type="submit">{submitLabel}</button>
        {onCancel && (
          <button type="button" className="secondary" onClick={onCancel}>
            Cancelar
          </button>
        )}
      </div>
      {error && <p className="error">{error}</p>}
    </form>
  )
}
