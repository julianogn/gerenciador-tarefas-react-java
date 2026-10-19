import { useCallback, useEffect, useState } from 'react'
import { EMPTY_FILTER, type Task, type TaskAction, type TaskFilter, type TaskInput } from '../domain/task'
import { useTaskRepository } from './useTaskRepository'

/** Controller (MVC): holds screen state and turns user intents into repository calls. */
export function useTasks() {
  const repository = useTaskRepository()
  const [tasks, setTasks] = useState<Task[]>([])
  const [filter, setFilter] = useState<TaskFilter>(EMPTY_FILTER)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [version, setVersion] = useState(0)

  useEffect(() => {
    let cancelled = false
    repository
      .list(filter)
      .then((result) => {
        if (cancelled) return
        setTasks(result)
        setError(null)
      })
      .catch((e: unknown) => !cancelled && setError(messageOf(e)))
      .finally(() => !cancelled && setLoading(false))
    return () => {
      cancelled = true
    }
  }, [repository, filter, version])

  const run = useCallback(async (operation: () => Promise<unknown>) => {
    try {
      await operation()
      setVersion((current) => current + 1)
    } catch (e) {
      setError(messageOf(e))
    }
  }, [])

  return {
    tasks,
    filter,
    loading,
    error,
    setFilter,
    create: (input: TaskInput) => run(() => repository.create(input)),
    update: (id: string, input: TaskInput) => run(() => repository.update(id, input)),
    transition: (id: string, action: TaskAction) => run(() => repository.transition(id, action)),
    remove: (id: string) => run(() => repository.remove(id)),
  }
}

function messageOf(error: unknown): string {
  return error instanceof Error ? error.message : 'Erro inesperado.'
}
