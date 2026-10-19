import { useContext } from 'react'
import type { TaskRepository } from '../domain/taskRepository'
import { RepositoryContext } from './RepositoryContext'

export function useTaskRepository(): TaskRepository {
  const repository = useContext(RepositoryContext)
  if (!repository) throw new Error('useTaskRepository deve ser usado dentro de RepositoryProvider')
  return repository
}
