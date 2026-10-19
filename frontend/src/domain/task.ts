export type Priority = 'LOW' | 'MEDIUM' | 'HIGH'
export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE'
export type TaskAction = 'start' | 'complete' | 'reopen'
export type SortOrder = 'CREATION' | 'PRIORITY' | 'DUE_DATE'

export interface Task {
  id: string
  title: string
  description: string
  priority: Priority
  status: TaskStatus
  dueDate: string | null
  overdue: boolean
  createdAt: string
}

export interface TaskInput {
  title: string
  description: string
  priority: Priority
  dueDate: string | null
}

export interface TaskFilter {
  status?: TaskStatus
  priority?: Priority
  overdue: boolean
  sort: SortOrder
}

export const PRIORITY_LABELS: Record<Priority, string> = {
  LOW: 'Baixa',
  MEDIUM: 'Média',
  HIGH: 'Alta',
}

export const STATUS_LABELS: Record<TaskStatus, string> = {
  TODO: 'A fazer',
  IN_PROGRESS: 'Em andamento',
  DONE: 'Concluída',
}

export const ACTION_LABELS: Record<TaskAction, string> = {
  start: 'Iniciar',
  complete: 'Concluir',
  reopen: 'Reabrir',
}

export const SORT_LABELS: Record<SortOrder, string> = {
  CREATION: 'Criação',
  PRIORITY: 'Prioridade',
  DUE_DATE: 'Prazo',
}

const ACTIONS_BY_STATUS: Record<TaskStatus, TaskAction[]> = {
  TODO: ['start', 'complete'],
  IN_PROGRESS: ['complete', 'reopen'],
  DONE: ['reopen'],
}

/** Mirrors the backend state machine so the UI only offers valid transitions. */
export function availableActions(status: TaskStatus): TaskAction[] {
  return ACTIONS_BY_STATUS[status]
}

export function validateTaskInput(input: TaskInput): string | null {
  const title = input.title.trim()
  if (!title) return 'O título é obrigatório.'
  if (title.length > 120) return 'O título deve ter no máximo 120 caracteres.'
  return null
}

export const EMPTY_FILTER: TaskFilter = { overdue: false, sort: 'CREATION' }
