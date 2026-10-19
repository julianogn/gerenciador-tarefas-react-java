import type { Task, TaskAction, TaskFilter, TaskInput } from '../domain/task'
import type { TaskRepository } from '../domain/taskRepository'
import { request } from './httpClient'

/** Adapter: implements the repository port against the Spring Boot REST API. */
export class HttpTaskRepository implements TaskRepository {
  private readonly baseUrl: string

  constructor(baseUrl = '/api/tasks') {
    this.baseUrl = baseUrl
  }

  list(filter: TaskFilter): Promise<Task[]> {
    return request<Task[]>(`${this.baseUrl}?${toQueryString(filter)}`)
  }

  create(input: TaskInput): Promise<Task> {
    return request<Task>(this.baseUrl, { method: 'POST', body: JSON.stringify(input) })
  }

  update(id: string, input: TaskInput): Promise<Task> {
    return request<Task>(`${this.baseUrl}/${id}`, { method: 'PUT', body: JSON.stringify(input) })
  }

  transition(id: string, action: TaskAction): Promise<Task> {
    return request<Task>(`${this.baseUrl}/${id}/${action}`, { method: 'POST' })
  }

  remove(id: string): Promise<void> {
    return request<void>(`${this.baseUrl}/${id}`, { method: 'DELETE' })
  }
}

export function toQueryString(filter: TaskFilter): string {
  const params = new URLSearchParams({ sort: filter.sort })
  if (filter.status) params.set('status', filter.status)
  if (filter.priority) params.set('priority', filter.priority)
  if (filter.overdue) params.set('overdue', 'true')
  return params.toString()
}
